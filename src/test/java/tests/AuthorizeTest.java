package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;

public class AuthorizeTest {

    private LoginPage loginPage;
    private Properties properties;
    private String expectedUrl;


    @Before
    public void setUp() {
        properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("src/test/java/resources/config.properties");
            properties.load(input);
            input.close();

            expectedUrl = properties.getProperty("documentsPageUrl");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Configuration.browser = "chrome";
        Configuration.timeout = 2000;

        open(properties.getProperty("loginPageUrl"));
        loginPage = new LoginPage();
    }

    @Test
    public void testSuccessfulLogin() {
        loginPage.login(properties.getProperty("email"), properties.getProperty("password"));

        WebDriverWait wait = new WebDriverWait(webdriver().driver().getWebDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(expectedUrl));

        Assert.assertEquals("URL не изменился после входа", expectedUrl, Selenide.webdriver().driver().url());

    }

    @After
    public void tearDown() {
        closeWebDriver();
    }
}
