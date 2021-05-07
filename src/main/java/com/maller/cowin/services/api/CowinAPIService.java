package com.maller.cowin.services.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.json.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maller.cowin.model.Center;
import com.maller.cowin.model.District;
import com.maller.cowin.model.Session;
import com.maller.cowin.model.State;

public class CowinAPIService {

	@SuppressWarnings("unused")
	private static String TOKEN = null;
	private static String HOST = "https://cdn-api.co-vin.in/api/";
	private static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	private CowinAPIService() {
	}

	public static String generateOTP(long mobileNumber) {
		try {
			final URI GENERATE_OTP = new URI(HOST + "v2/auth/public/generateOTP");

			JSONObject body = new JSONObject();
			body.append("mobile", String.valueOf(mobileNumber));

			HttpRequest request = (HttpRequest) HttpRequest.newBuilder().POST(BodyPublishers.ofString(body.toString()))
					.setHeader("User-Agent", USER_AGENT)
					.uri(GENERATE_OTP).build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				body = new JSONObject(response.body());
				return body.getString("txnId");
			}

			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static void confirmOTP(String txnID, String otp) {
		try {
			final String HASHING_ALGORITHM = "SHA-256";
			final URI CONFIRM_OTP = new URI(HOST + "v2/auth/public/confirmOTP");

			MessageDigest digest = MessageDigest.getInstance(HASHING_ALGORITHM);
			String OTP_hash = digest.digest(otp.getBytes(StandardCharsets.UTF_8)).toString();

			JSONObject body = new JSONObject();
			body.append("otp", OTP_hash);
			body.append("txnID", txnID);

			HttpRequest request = (HttpRequest) HttpRequest.newBuilder().POST(BodyPublishers.ofString(body.toString()))
					.setHeader("User-Agent", USER_AGENT)
					.uri(CONFIRM_OTP).build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				body = new JSONObject(response.body());
				TOKEN = body.getString("token");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<State> getStates() {
		try {
			final URI GET_STATES = new URI(HOST + "v2/admin/location/states");

			HttpRequest request = (HttpRequest) HttpRequest.newBuilder().GET().uri(GET_STATES)
					.setHeader("User-Agent", USER_AGENT)
					.setHeader("Accept-Language", "hi_IN").build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				JSONObject body = new JSONObject(response.body());
				String statesStr = body.getJSONArray("states").toString();
				return mapper.readValue(statesStr, new TypeReference<List<State>>() {});
			} else 
				throw new Exception("Unable to fetch states. Response Status: " + response.statusCode() + " Message: " + response.body());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<State>();
	}

	public static List<District> getDistrictsInState(int stateID) {
		try {
			final URI GET_DISTRICTS = new URI(HOST + "v2/admin/location/districts/" + stateID);

			HttpRequest request = (HttpRequest) HttpRequest.newBuilder().GET().uri(GET_DISTRICTS)
					.setHeader("User-Agent", USER_AGENT)
					.setHeader("Accept-Language", "hi_IN").build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				JSONObject body = new JSONObject(response.body());
				String districtsStr = body.getJSONArray("districts").toString();
				return mapper.readValue(districtsStr, new TypeReference<List<District>>() {});
			} else
				throw new Exception("Unable to fetch states. Response Status: " + response.statusCode() + " Message: " + response.body());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<District>();
	}

	public static List<Session> getSessionsByPINCodeAndDate(long PINCode, LocalDate date, boolean isWeeklySearch) {
		try {
			final String query = "?pincode=" + PINCode + "&date=" + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			final URI FIND_BY_PIN = new URI(HOST + "v2/appointment/sessions/public/findByPin" + query);
			final URI CALENDAR_BY_PIN = new URI(HOST + "v2/appointment/sessions/public/calendarByPin" + query);
			
			HttpRequest request = (HttpRequest) HttpRequest.newBuilder().GET()
					.uri(isWeeklySearch? CALENDAR_BY_PIN: FIND_BY_PIN)
					.setHeader("User-Agent", USER_AGENT)
					.setHeader("Accept-Language", "hi_IN")
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if(response.statusCode() == 200) {
				JSONObject body = new JSONObject(response.body());
				String key = isWeeklySearch? "centers": "sessions";
				String sessionsStr = body.getJSONArray(key).toString();
				if(isWeeklySearch) {
					List<Center> centers = mapper.readValue(sessionsStr, new TypeReference<List<Center>>() {});
					List<Session> sessions = new ArrayList<>();
					for(Center center : centers) {
						List<Session> sessionsInCenter = center.mapToSessions();
						sessions.addAll(sessionsInCenter);
					}
					return sessions;
				}
				return mapper.readValue(sessionsStr, new TypeReference<List<Session>>() {});
			} else
				throw new Exception("Unable to fetch states. Response Status: " + response.statusCode() + " Message: " + response.body());
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<Session>();
	}

	public static List<Session> getSessionsByDistrictAndDate(int districtID, LocalDate date, boolean isWeeklySearch) {
		try {
			final String query = "?district_id=" + districtID + "&date=" + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			
			final URI FIND_BY_DISTRICT = new URI(HOST + "v2/appointment/sessions/public/findByDistrict" + query);
			final URI CALENDAR_BY_DISTRICT = new URI(HOST + "v2/appointment/sessions/public/calendarByDistrict" + query);
			
			HttpRequest request = (HttpRequest) HttpRequest.newBuilder().GET()
					.uri(isWeeklySearch? CALENDAR_BY_DISTRICT: FIND_BY_DISTRICT)
					.setHeader("User-Agent", USER_AGENT)
					.setHeader("Accept-Language", "hi_IN")
					.build();
			
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if(response.statusCode() == 200) {
				JSONObject body = new JSONObject(response.body());
				String key = isWeeklySearch? "centers": "sessions";
				String sessionsStr = body.getJSONArray(key).toString();
				if(isWeeklySearch) {
					List<Center> centers = mapper.readValue(sessionsStr, new TypeReference<List<Center>>() {});
					List<Session> sessions = new ArrayList<>();
					for(Center center : centers) {
						List<Session> sessionsInCenter = center.mapToSessions();
						sessions.addAll(sessionsInCenter);
					}
					return sessions;
				}
				return mapper.readValue(sessionsStr, new TypeReference<List<Session>>() {});
			} else
				throw new Exception("Unable to fetch states. Response Status: " + response.statusCode() + " Message: " + response.body());	
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
}
