package com.hmlsolutions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;

public class AutentikointiTest {

    private final String USERNAME = "buhmen";
    private final String ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");
    private final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";

    private final String TESTATTAVAN_URL = "http://hmlsolutions.com/ryhma2/tatu";

    private final String OPISKELIJA_TUNNUS = "33333";
    private final String OPETTAJA_TUNNUS = "44444";
    private final String VAARA_TUNNUS = "VAARA_TUNNUS";

    private final String OPISKELIJA_SALASANA = "12345";
    private final String OPETTAJA_SALASANA = "12345";
    private final String VAARA_SALASANA = "VAARA_SALASANA";

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
    }

    @Test
    public void kirjauduOpettajana_paadyOpettajanSivulle() throws Exception {
        driver.get(TESTATTAVAN_URL);

        kirjauduSisaan(OPETTAJA_TUNNUS, OPETTAJA_SALASANA);

        Assert.assertNotNull(driver.findElement(By.id("omaKalenteriLinkki")), "Päädytyllä sivulla ei ole linkkiä" +
                " omaan kalenteriin");
        Assert.assertEquals(driver.findElements(By.id("hallintaSivunLinkki")).size(), 1,
                "Päädytyllä sivulla ei ole linkkiä aikojen hallintaan vaikka pitäisi");
    }

    @Test
    public void kirjauduOpiskelijana_paadyOpiskelijanSivulle() {
        driver.get(TESTATTAVAN_URL);

        kirjauduSisaan(OPISKELIJA_TUNNUS, OPISKELIJA_SALASANA);

        Assert.assertNotNull(driver.findElement(By.id("omaKalenteriLinkki")), "Päädytyllä sivulla ei ole linkkiä" +
                " omaan kalenteriin");
        Assert.assertEquals(driver.findElements(By.id("hallintaSivunLinkki")).size(), 0,
                "Päädytyllä sivulla on linkki aikojen hallintaan vaikka ei pitäisi");
    }

    @Test
    public void kirjauduVaarallaTunnuksella_paadyKirjautumissivulle() {
        driver.get(TESTATTAVAN_URL);

        kirjauduSisaan(VAARA_TUNNUS, VAARA_SALASANA);

        driver.findElement(By.name("salasana")).submit();

        Assert.assertTrue(driver.getTitle().equals("Ajanvaraus kirjautuminen"),
                "Päädytyllä sivulla ei ole otsikkoa 'Ajanvaraus kirjautuminen'");
    }

    @Test
    public void kirjauduUlos_pitäisiMennaKirjautumissivulle() {
        driver.get(TESTATTAVAN_URL);

        kirjauduSisaan(OPISKELIJA_TUNNUS, OPISKELIJA_SALASANA);

        Assert.assertNotNull(driver.findElement(By.id("omaKalenteriLinkki")), "Päädytyllä sivulla ei ole linkkiä" +
                " omaan kalenteriin");

        driver.get(TESTATTAVAN_URL + "/logout.php");
        driver.get(TESTATTAVAN_URL);

        Assert.assertTrue(driver.getTitle().equals("Ajanvaraus kirjautuminen"),
                "Päädytyllä sivulla ei ole otsikkoa 'Ajanvaraus kirjautuminen'");
    }

    private void kirjauduSisaan(String kayttajaTunnus, String salasana) {
        driver.findElement(By.name("tunnus")).sendKeys(kayttajaTunnus);
        driver.findElement(By.name("salasana")).sendKeys(salasana);

        driver.findElement(By.name("salasana")).submit();
    }

    @AfterMethod
    public void logout() {
        driver.get(TESTATTAVAN_URL + "/logout.php");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
