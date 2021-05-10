package com.maller.cowin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.maller.cowin.model.Session;
import com.maller.cowin.services.SessionFinder;
import com.maller.cowin.utils.MailUtil;
import com.maller.cowin.web.CowinPortal;

public final class App {
	
	private static final String DEFAULT_SEARCH_PARAM_FILEPATH = "src\\main\\resources\\parameters.txt";
	
    private App() {
    }
    
    protected static JSONObject getParametersFromFile(String filePath) {
    	try {
			String parametersJSONString = Files.readString(Path.of(filePath));
			return new JSONObject(parametersJSONString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
      
    protected static String exportVaccineSessionListToFile(List<Session> sessions) {
    	try {
    		StringBuilder builder = new StringBuilder();
    		long totalAvailableDoses = SessionFinder.getTotalAvailableDoses(sessions);
    		builder.append("\nTotal Sessions: ").append(sessions.size());
    		builder.append("\nTotal Doses Available: ").append(totalAvailableDoses);
    		System.out.println(builder.toString());
    		
    		sessions.stream().forEach(session -> builder.append(session.getMinDetails()));
    		Path fileName = Path.of(".\\Sessions_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_hhmmss")) + ".txt");
    		Files.writeString(fileName, builder.toString());
    		return fileName.toString();
    	} catch(Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    		return null;
    	}
    }
    
    
    
    protected static void bookAppointmentOnCowinPortal(String mobileNumber) {
    	try {
    		CowinPortal portal = new CowinPortal();
    		portal.open();
    		portal.insertMobileNumberAndGetOTP(mobileNumber);
    		
    	} catch(Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    private static int getSearchParameterIndexInArgs(String[] args) {
    	return Arrays.asList(args).indexOf("--search_params") + 1;
    }

    public static void main(String[] args) {
    	String filePath;
    	int searchParamIndex = getSearchParameterIndexInArgs(args);
    	if(searchParamIndex == 0)
    		filePath = DEFAULT_SEARCH_PARAM_FILEPATH;
    	else
    		filePath = args[searchParamIndex];
    		
    	JSONObject parameters = getParametersFromFile(filePath);
    	if(parameters == null)
    		return;
    	
    	SessionFinder finder = new SessionFinder(parameters);
    	List<Session> sessions = finder.find();
    	boolean isDosesAvailable = SessionFinder.getTotalAvailableDoses(sessions) > 0? true: false;
    	if(isDosesAvailable) {
    		sessions = SessionFinder.filterSessionsByMinimumDose(sessions, 1);
    		String exportedFile = exportVaccineSessionListToFile(sessions);
    		parameters.put("ExportedFile", exportedFile);
    		
    		boolean hasMailDetails = parameters.has("Mail");
    		if(hasMailDetails)
    			sendMailToUser(parameters);
    		
    		boolean hasMobileNumber = parameters.has("MobileNumber");
    		if(hasMobileNumber) {
    			String mobileNumber = parameters.getString("MobileNumber");
    			bookAppointmentOnCowinPortal(mobileNumber);
    		}
    	} else
    		System.out.println("No vaccine doses found");
    }

	private static void sendMailToUser(JSONObject parameters) {
		try {
			final String SUBJECT = "Cowin vaccine session found";
			StringBuilder bodyBuilder = new StringBuilder();
			
			JSONObject mailDetails = parameters.getJSONObject("Mail");
			String host = mailDetails.getString("Host");
			String port = mailDetails.getString("Port");
			boolean isAuth = mailDetails.getBoolean("IsAuth");
			boolean isEnableStartTLS = mailDetails.getBoolean("EnableStartTLS");
			MailUtil mailer = new MailUtil(host, port, isAuth, isEnableStartTLS);
			
			
			String username = mailDetails.getString("username");
			String password = mailDetails.getString("password");
			String fromAddress = mailDetails.getString("fromAddress");
			mailer.authenticate(username, password, fromAddress);
			
			JSONArray toAddressList = mailDetails.getJSONArray("toAddress");
			toAddressList.forEach(toAddress -> mailer.setRecepient((String) toAddress, "to"));
			
			bodyBuilder.append("Hi, \n");
			bodyBuilder.append("Cowin sessions are detected as per your preference.\n\n");
			bodyBuilder.append("Please login immediately to book an appointment.\n\n");
			bodyBuilder.append("Thanks & Regards,\n");
			bodyBuilder.append("Cowin appointment finder");
			
			mailer.composeMail(SUBJECT, bodyBuilder.toString());
			boolean hasExportedFile = parameters.has("ExportedFile");
			if(hasExportedFile) {
				String exportedFile = parameters.getString("ExportedFile");
				mailer.addAttachment(exportedFile);
			}
			mailer.sendMail();
		} catch(Exception e) {
			// TODO Auto-generated catch block
    		e.printStackTrace();
		}
	}
}
