package br.ce.wcaquino.tasks.functional;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TasksTest {
	
	

    @SuppressWarnings("deprecation")
	public WebDriver accessApplication() throws MalformedURLException {
    	
    	String ip = System.getenv("APP_IP");
	    if (ip == null || ip.isEmpty()) {
	        throw new IllegalStateException("The APP_IP environment variable has not been set!");
	    }
	    
	    System.out.println("http://" + ip + ":8001/tasks/");
    	
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new RemoteWebDriver(new URL("http://" + ip + ":4444"), options);
        driver.navigate().to("http://" + ip + ":8001/tasks/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        return driver;
    }

    @Test
    public void shouldDontSaveTaskWithoutDescription() throws MalformedURLException {
        WebDriver driver = accessApplication();
        try {
			driver.findElement(By.id("addTodo")).click();
			driver.findElement(By.id("dueDate")).sendKeys("10/10/2040");
			driver.findElement(By.id("saveButton")).click();
			String message = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Fill the task description", message);
		} finally {
			driver.quit();
		}
    }

    @Test
    public void shouldDontSaveTaskWithoutDate() throws MalformedURLException {
        WebDriver driver = accessApplication();
        try {
			driver.findElement(By.id("addTodo")).click();
			driver.findElement(By.id("task")).sendKeys("Teste via selenium");
			driver.findElement(By.id("saveButton")).click();
			String message = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Fill the due date", message);
		} finally {
			driver.quit();
		}
    }
    
    @Test
    public void shouldSaveTaskWithPastDate() throws MalformedURLException {
        WebDriver driver = accessApplication();
		try {
			driver.findElement(By.id("addTodo")).click();
			driver.findElement(By.id("task")).sendKeys("Teste via selenium");
			driver.findElement(By.id("dueDate")).sendKeys("10/10/1999");
			driver.findElement(By.id("saveButton")).click();
			String message = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Due date must not be in past", message);
		} finally {
			driver.quit();
		}
    }
	@Test
	public void shouldSaveTaskSuccessfuly() throws MalformedURLException {
		WebDriver driver = accessApplication();
		try {
			driver.findElement(By.id("addTodo")).click();
			driver.findElement(By.id("task")).sendKeys("Teste Selenium/Nathalia");
			driver.findElement(By.id("dueDate")).sendKeys("10/10/2040");
			driver.findElement(By.id("saveButton")).click();
			String message = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Success!", message);
		} finally {
			driver.quit();
		}
	}
}
