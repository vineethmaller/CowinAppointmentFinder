package com.maller.cowin.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.maller.cowin.model.District;
import com.maller.cowin.model.Session;
import com.maller.cowin.model.State;
import com.maller.cowin.services.api.CowinAPIService;

public class SessionMiddleware {
	
	private SessionMiddleware() {}
	
	public static List<Session> filterAvailableSessionsByAgeGroup(List<Session> sessions, int minAgeGroup) {
		return sessions.parallelStream()
				.filter(session -> session.getMin_age_limit().equals(String.valueOf(minAgeGroup)))
				.collect(Collectors.toList());
	}
	
	public static List<Session> filterAvailableSessionsByPINCode(List<Session> sessions, long PINCode) {
		return sessions.parallelStream()
				.filter(session -> session.getPincode().equals(String.valueOf(PINCode)))
				.collect(Collectors.toList());
	}
	
	public static State getStateWithName(String stateName) {
		List<State> states = CowinAPIService.getStates();
		final String formattedStateName = stateName.toLowerCase().trim();
		return states.parallelStream()
				.filter(state -> state.getState_name().equalsIgnoreCase(formattedStateName))
				.findFirst().orElse(null);
	}
	
	public static District getDistrictInStateWithName(String districtName, int stateId) {
		List<District> districts = CowinAPIService.getDistrictsInState(stateId);
		final String formattedDistrictName = districtName.toLowerCase().trim();
		return districts.parallelStream()
				.filter(district -> district.getDistrict_name().equalsIgnoreCase(formattedDistrictName))
				.findFirst().orElse(null);
	}
	
	public static List<Session> getSessionsByMethod(String method, Map<String, String> searchData, LocalDate date, boolean isWeeklySearch) throws Exception {
		if(method.equals("BY_PINCODE")) {
			long pinCode = Long.parseLong(searchData.get("PIN_CODE"));
			return CowinAPIService.getSessionsByPINCodeAndDate(pinCode, date, isWeeklySearch);
		} else if(method.equals("BY_DISTRICT")) {
			String stateName = searchData.get("STATE");
			String districtName = searchData.get("DISTRICT");
			
			State state = getStateWithName(stateName);
			if(state == null)
				throw new Exception("No state found with given name");
		
			District district = getDistrictInStateWithName(districtName, state.getState_id());
			if(district == null)
				throw new Exception("No district found with given name");
		
			return CowinAPIService.getSessionsByDistrictAndDate(district.getDistrict_id(), date, isWeeklySearch);
		} else
			throw new Exception("Invalid Method");
	}
}
