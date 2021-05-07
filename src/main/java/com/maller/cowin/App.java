package com.maller.cowin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.maller.cowin.model.Session;
import com.maller.cowin.services.VaccineService;

public final class App {
	private boolean isContinuosSearch = false;
	private long pollIntervalInSeconds = 15;
	
	private String searchMethod = "BY_PINCODE";
	private JSONObject methodData = new JSONObject("{ \"SearchData\": { \"pin_code\" : \"560049\" } }");
	private boolean isWeeklySearch = false;
	
	private int ageGroup = 18;
	private LocalDate date = LocalDate.now(); 
	
    private App() {
    }
    
    protected JSONObject getSearchParametersFromFile(String filePath) {
    	try {
			String parametersJSONString = Files.readString(Path.of(filePath));
			return new JSONObject(parametersJSONString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    protected void initializeSearchParameters(JSONObject parametersJSON) {
    	try {
    		if(parametersJSON.has("ContinousSearch"))
    			isContinuosSearch = parametersJSON.getBoolean("ContinousSearch");
    		
    		if(parametersJSON.has("PollIntervalInSeconds"))
    			pollIntervalInSeconds = parametersJSON.getLong("PollIntervalInSeconds");
    		
    		if(parametersJSON.has("Method"))
    			searchMethod = parametersJSON.getString("Method").toUpperCase();
    			methodData = parametersJSON.getJSONObject("SearchData");
    		
    		if(parametersJSON.has("Weekly"))
    			isWeeklySearch = parametersJSON.getBoolean("Weekly");
    		
    		if(parametersJSON.has("AgeGroup"))
    			ageGroup = parametersJSON.getInt("AgeGroup");
    		
    		if(parametersJSON.has("Date"))
    			date = LocalDate.parse(parametersJSON.getString("Date"), DateTimeFormatter.ofPattern("ddMMyyyy"));
    	} catch(Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    protected void displayAvailableSessions(List<Session> sessions) {
    	try {
    		long totalAvailableDoses = 0;
    		totalAvailableDoses = sessions.stream()
    				.map(session -> Integer.parseInt(session.getAvailable_capacity()))
    				.reduce(0, Integer::sum);
    		String totalSessionsStr = "Total Sessions: " + sessions.size();
    		String totalDosesStr = "Total Doses Available: " + totalAvailableDoses;
    		
    		System.out.println();
    		System.out.println(totalSessionsStr);
    		System.out.println(totalDosesStr);
    		sessions.stream().forEach(session -> System.out.print(session.getMinDetails()));
    	} catch(Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    protected void exportSessionsToFile(List<Session> sessions) {
    	try {
    		StringBuilder builder = new StringBuilder();
    		long totalAvailableDoses = 0;
    		totalAvailableDoses = sessions.stream()
    				.map(session -> Integer.parseInt(session.getAvailable_capacity()))
    				.reduce(0, Integer::sum);
    		builder.append("\nTotal Sessions: ").append(sessions.size());
    		builder.append("\nTotal Doses Available: ").append(totalAvailableDoses);
    		sessions.stream().forEach(session -> builder.append(session.getMinDetails()));
    		System.out.println(builder.toString());
    		Path fileName = Path.of(".\\Sessions_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_hhmmss")) + ".txt");
    		Files.writeString(fileName, builder.toString());
    	} catch(Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    protected List<Session> searchAvailability() {
    	List<Session> sessions = null;
    	try {
    		Map<String, String> searchData = new HashMap<>();
    		methodData.keys().forEachRemaining(key -> searchData.put(key.toUpperCase(), methodData.getString(key)));
    		do {
    			System.out.println("Polling for session at " + LocalDateTime.now().toLocalTime());
    			sessions = VaccineService.getSessionsByMethod(searchMethod, searchData, date, isWeeklySearch);
    			sessions = VaccineService.filterAvailableSessionsByAgeGroup(sessions, ageGroup);
    			if(!sessions.isEmpty())
    				break;
    			TimeUnit.SECONDS.sleep(pollIntervalInSeconds);
    		} while(isContinuosSearch);
    	} catch(Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
		return sessions;
    }

    public static void main(String[] args) {
    	App app = new App();
    	String filePath = "search_parameters.txt";
    	
    	JSONObject parametersJSON = app.getSearchParametersFromFile(filePath);
    	if(parametersJSON == null)
    		return;
    	app.initializeSearchParameters(parametersJSON);
    	List<Session> sessions = app.searchAvailability();
    	if(sessions.isEmpty()) {
    		System.out.println("No Vaccination sessions found");
    		return;
    	}
    	app.exportSessionsToFile(sessions);
    }
}