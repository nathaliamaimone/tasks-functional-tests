package br.ce.wcaquino.tasks.prod;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class HealthCheckIT {
	
	   @SuppressWarnings("deprecation")
		public WebDriver accessApplication() throws MalformedURLException {
	    	
	    	String ip = System.getenv("APP_IP");
		    if (ip == null || ip.isEmpty()) {
		        throw new IllegalStateException("The APP_IP environment variable has not been set!");
		    }
		    
		    System.out.println("http://" + ip + ":8001/tasks/");
	    	
	        ChromeOptions options = new ChromeOptions();
	        WebDriver driver = new RemoteWebDriver(new URL("http://" + ip + ":4444"), options);
	        driver.navigate().to("http://" + ip + ":9999/tasks/");
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	        return driver;
	    }

		@Test
		public void validateApplication() throws MalformedURLException {
			WebDriver driver = accessApplication();
			try {
			    String version = driver.findElement(By.id("version")).getText();
			    Assert.assertTrue(version.startsWith("build"));
			} finally {
				driver.quit();
			}
		}
	
}
