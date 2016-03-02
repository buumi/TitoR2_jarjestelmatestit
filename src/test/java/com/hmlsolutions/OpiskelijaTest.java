package com.hmlsolutions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jkankaanpaa on 3/2/16.
 */
public class OpiskelijaTest {

    private final String USERNAME = "buhmen";
    private final String ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");
    private final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";
    private final String TESTATTAVAN_URL = "http://hmlsolutions.com/ryhma2/vesa/proto3";

    private WebDriver driver;

    @BeforeClass
    public void setup() {
        if (ACCESS_KEY != null) {
            DesiredCapabilities caps = DesiredCapabilities.chrome();
            caps.setCapability("platform", "Windows 8.1");
            caps.setCapability("version", "43.0");

            try {
                driver = new RemoteWebDriver(new URL(URL), caps);
            } catch (MalformedURLException e) {
                System.err.println("SAUCE URL ON VIRHEELLINEN");
            }
        }
        else {
            driver = new FirefoxDriver();
        }

        driver.get(TESTATTAVAN_URL + "/logout.php");
        driver.get(TESTATTAVAN_URL);

        driver.findElement(By.name("tunnus")).sendKeys("55555");
        driver.findElement(By.name("salasana")).sendKeys("12345");

        driver.findElement(By.name("salasana")).submit();
    }

    @Test
    public void testClickEvent() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("fc-widget-content")));
        driver.findElement(By.className("fc-widget-content")).click();
    }

    @AfterMethod
    public void resetPage() {
        //driver.get(TESTATTAVAN_URL);
    }

    @AfterClass
    public void tearDown() {
        //driver.close();
    }


}
