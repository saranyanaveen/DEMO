package demoweb;

import java.io.File;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utility.Dataproviders;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class DemoLog1 {
	private ExtentReports extent;
	private ExtentTest test;
	private WebDriver driver;

	@BeforeClass
	public void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		extent = new ExtentReports();
		ExtentSparkReporter sparkReporter = new ExtentSparkReporter("extent-report.html");
		extent.attachReporter(sparkReporter);
	}

	@Test(dataProvider = "LoginData", dataProviderClass = Dataproviders.class)
	public void login(String scenario, String username, String password) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			driver.manage().window().maximize();
			driver.get("https://demowebshop.tricentis.com/");
			test = extent.createTest("Login Test - " + scenario);

			driver.findElement(By.xpath("//a[@href='/login']")).click();
			driver.findElement(By.id("Email")).sendKeys(username);
			driver.findElement(By.id("Password")).sendKeys(password);
			driver.findElement(By.xpath("//input[@class='button-1 login-button']")).click();

			Reporter.log("Testing scenario: " + scenario, true);

			if (scenario.equals("bothcorrect")) {
				WebElement account = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("account")));
				Assert.assertTrue(account.isDisplayed(), "Login successful: Account element is not displayed.");
				test.pass("Login successful: Account element is displayed.");
			} else {
				WebElement errorMessageElement = wait.until(
						ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='message-error']")));
				String errorMessage = errorMessageElement.getText();
				Reporter.log("Actual error message: " + errorMessage, true);
				// Take a screenshot for failure
				TakesScreenshot ts = (TakesScreenshot) driver;
				File file = ts.getScreenshotAs(OutputType.FILE);
				String screenshotPath = "./Screenshots/" + scenario + "image1.png"; 


				FileUtils.copyFile(file, new File(screenshotPath));
				 test.fail("Login failed: " + errorMessage)
                 .addScreenCaptureFromPath(screenshotPath);

				Assert.assertTrue(errorMessage.contains("The credentials provided are incorrect"),
						"Error message does not match expected text.");

			}
		} catch (Exception e) {
			Reporter.log("Test failed due to exception: " + e.getMessage(), true);
			test.fail("Test failed due to exception: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Test failed due to exception: " + e.getMessage());
		}
	}

	@AfterClass
	public void tearDown() {
		extent.flush();
		if (driver != null) {
			driver.quit();
		}
	}
}
