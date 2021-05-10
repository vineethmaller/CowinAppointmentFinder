package com.maller.cowin.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.maller.cowin.model.Session;

public class SessionFinder {
	
	private boolean isContinuosSearch = false;
	private long pollIntervalInSeconds = 15;
	
	private String searchMethod = "BY_PINCODE";
	private JSONObject methodData = new JSONObject("{ \"SearchData\": { \"pin_code\" : \"560049\" } }");
	private boolean isWeeklySearch = false;
	
	private int ageGroup = 18;
	private LocalDate date = LocalDate.now(); 
	
	public SessionFinder(JSONObject searchParameters) {
    	try {
    		if(searchParameters.has("ContinousSearch"))
    			isContinuosSearch = searchParameters.getBoolean("ContinousSearch");
    		
    		if(searchParameters.has("PollIntervalInSeconds"))
    			pollIntervalInSeconds = searchParameters.getLong("PollIntervalInSeconds");
    		
    		if(searchParameters.has("Method"))
    			searchMethod = searchParameters.getString("Method").toUpperCase();
    			methodData = searchParameters.getJSONObject("SearchData");
    		
    		if(searchParameters.has("Weekly"))
    			isWeeklySearch = searchParameters.getBoolean("Weekly");
    		
    		if(searchParameters.has("AgeGroup"))
    			ageGroup = searchParameters.getInt("AgeGroup");
    		
    		if(searchParameters.has("Date"))
    			date = LocalDate.parse(searchParameters.getString("Date"), DateTimeFormatter.ofPattern("ddMMyyyy"));
    	} catch(Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
	
	public List<Session> find() {
    	List<Session> sessions = null;
    	try {
    		Map<String, String> searchData = new HashMap<>();
    		methodData.keys().forEachRemaining(key -> searchData.put(key.toUpperCase(), methodData.getString(key)));
    		System.out.println("Polling for vaccine sessions from " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
    		do {
    			sessions = SessionMiddleware.getSessionsByMethod(searchMethod, searchData, date, isWeeklySearch);
    			sessions = SessionMiddleware.filterAvailableSessionsByAgeGroup(sessions, ageGroup);
    			if(!sessions.isEmpty()) {
    				long totalAvailableDose = getTotalAvailableDoses(sessions);
    				if(totalAvailableDose != 0)
    					break;
    			}
    			TimeUnit.SECONDS.sleep(pollIntervalInSeconds);
    		} while(isContinuosSearch);
    	} catch(Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
		return sessions;
    }
	
	public static long getTotalAvailableDoses(List<Session> sessions) {
		return sessions.stream()
		.map(session -> Integer.parseInt(session.getAvailable_capacity()))
		.reduce(0, Integer::sum);
	}
	
	public static List<Session> filterSessionsByMinimumDose(List<Session> sessions, int minimumDoseAvailable) {
		return sessions.stream().filter(session -> Integer.parseInt(session.getAvailable_capacity())>=minimumDoseAvailable).collect(Collectors.toList());
	}
}
