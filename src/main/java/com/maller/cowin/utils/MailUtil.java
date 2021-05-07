package com.maller.cowin.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

class Recipient {
	private InternetAddress address;
	private RecipientType type;
	
	public Recipient(InternetAddress address, RecipientType type) {
		this.address = address;
		this.type = type;
	}
	
	public InternetAddress getAddress() {
		return this.address;
	}
	
	public RecipientType getType() {
		return this.type;
	}
}

public class MailUtil {
	
	private Properties props;
	private Session session;
	
	private InternetAddress fromAddress;
	private List<Recipient> recipientList;
	
	private String subject = "";
	private String body = "";
	private String attachment = null;
	
	private boolean isAuthenticated = false;
	
	public MailUtil(String host, String port, boolean isAuth, boolean isEnableStartTLS) {
		try {
			final String IS_AUTH = isAuth? "true": "false";
			final String IS_ENABLE_TLS = isEnableStartTLS? "true": "false";
			
			props = new Properties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", IS_AUTH);
			props.put("mail.smtp.starttls.enable", IS_ENABLE_TLS);
			
			this.recipientList = new ArrayList<>();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void authenticate(String username, String password, String fromAddress) {
		try {
			this.fromAddress = new InternetAddress(fromAddress);
			
			session = Session.getDefaultInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication( ) {
					return new PasswordAuthentication(username, password);
				}
			});
			
			isAuthenticated = true;
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setRecepient(String toAddressStr, String typeStr) {
		try {
			RecipientType type;
			InternetAddress toAddress = new InternetAddress(toAddressStr);
			
			switch(typeStr.toUpperCase()) {
				case "BCC": type = RecipientType.BCC; break;
				case "CC": type = RecipientType.CC; break;
				default: type = RecipientType.TO;
			}
			
			recipientList.add(new Recipient(toAddress, type));
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void composeMail(String subject, String body) {
		this.subject = subject;
		this.body = body;
	}
	
	public void addAttachment(String filePath) {
		this.attachment = filePath;
	}
	
	public void sendMail() {
		try {
			BodyPart messageBodyPart;
			
			if(!isAuthenticated)
				throw new Exception("User must be authenticated before this method is called");
			Message message = new MimeMessage(session);
		
			message.setFrom(this.fromAddress);
			for(Recipient recipient : this.recipientList) {
				message.setRecipient(recipient.getType(), recipient.getAddress());
			}
			message.setSubject(this.subject);
			message.setText(this.body);
			
			if(this.attachment != null) {
				Multipart multipart = new MimeMultipart();
				
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(body);
				multipart.addBodyPart(messageBodyPart);
				
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachment);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(attachment.split("\\\\")[1]);
				multipart.addBodyPart(messageBodyPart);

				message.setContent(multipart);
			} else
				message.setText(body);
			
			Transport.send(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to send mail due to error: " + e.getMessage());
		}
		
	}
}