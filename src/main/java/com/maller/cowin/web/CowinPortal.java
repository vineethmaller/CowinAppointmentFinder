package com.maller.cowin.web;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.maller.cowin.Utils.SeleniumUtils;

public class CowinPortal{
	private static final String COWIN_PORTAL_URL = "https://selfregistration.cowin.gov.in/";
	private static WebDriver driver;
	
	public CowinPortal() {
		 driver = SeleniumUtils.getDriver();
	}
	
	public void open() {
		driver.get(COWIN_PORTAL_URL);
	}
	
	public void insertMobileNumberAndGetOTP(String mobileNumber) {
		try {
			WebElement mobileNumberField = driver.findElement(By.id("mat-input-0"));
			mobileNumberField.sendKeys(mobileNumber);
			TimeUnit.SECONDS.sleep(1);
			WebElement otpButton = driver.findElement(By.cssSelector(".next-btn"));
			otpButton.click();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
