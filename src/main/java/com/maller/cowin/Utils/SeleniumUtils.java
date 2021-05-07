package com.maller.cowin.Utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumUtils {
	private static WebDriver driver = null;
	
	private static WebDriver initializeDriver() {
		String chromeDriverPath = System.getenv("CHROME_WEB_DRIVER");
		if(chromeDriverPath == null)
			chromeDriverPath = "chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--window-size=1920,1080");
		return new ChromeDriver(options);
	}
	
	public static WebDriver getDriver() {
		driver = (driver==null)? initializeDriver(): driver;
		return driver;
	}
}
