package com.hmlsolutions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by jkankaanpaa on 3/2/16.
 */
public class OpiskelijaTest {

    private final String USERNAME = "buhmen";
    private final String ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");
    private final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";
    private final String TESTATTAVAN_URL = "http://hmlsolutions.com/ryhma2/tatu";
    private final String RESET_URL = "http://hmlsolutions.com/ryhma2/sivu/tests/selenium_reset.php";

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

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver.get(RESET_URL);

        driver.get(TESTATTAVAN_URL + "/logout.php");
        driver.get(TESTATTAVAN_URL);

        driver.findElement(By.name("tunnus")).sendKeys("33333");
        driver.findElement(By.name("salasana")).sendKeys("12345");

        driver.findElement(By.name("salasana")).submit();
    }

    @Test
    public void testSiirryOpettajanKalenteriin_opettajanKalenteriAukeaa() {
        driver.findElement(By.id("kalenterinHakuKentta")).sendKeys("selenium");
        driver.findElement(By.linkText("SELENIUM_TESTI_OPETTAJA")).click();

        Assert.assertEquals(driver.findElement(By.id("kalenterinOmistajaTeksti")).getText(), "SELENIUM_TESTI_OPETTAJA",
                "Ei päädytty SELENIUM_TESTI_OPETTAJAN kalenteriin.");
    }

    @Test
    public void testKlikkaaOmaKalenteriLinkkia_omaKalenteriTuleeNakyviin() {
        driver.get(TESTATTAVAN_URL + "/index.php?id=44444");
        driver.findElement(By.id("omaKalenteriLinkki")).click();

        Assert.assertEquals(driver.findElement(By.id("kalenterinOmistajaTeksti")).getText(), "Oma kalenterisi",
                "Ei päädytty omaan kalenteriin.");
    }

    @Test
    public void testVaraaValidiAika_aikaIlmestyySivulle() {
        driver.findElement(By.className("fc-widget-content")).click();

        driver.findElement(By.id("kuvaus")).sendKeys("SELENIUM TESTITAPAAMINEN");
        driver.findElement(By.id("muokkausTallenna")).click();

        List<WebElement> elements = driver.findElements(By.className("fc-title"));

        boolean found = false;

        for (WebElement element : elements) {
            if (element.getText().equals("SELENIUM TESTITAPAAMINEN")) {
                found = true;
            }
        }

        Assert.assertTrue(found, "Tehtyä varausta ei löydy kalenterista!");

        //wait = new WebDriverWait(driver, 10);
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.text));

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
