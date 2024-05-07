package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import managers.ConfigManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class AuthorizeTest {

    private LoginPage loginPage;
    private String expectedUrl;

    @Before
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.timeout = 2000;

        expectedUrl = ConfigManager.getProperty("documentsPageUrl");

        loginPage = new LoginPage();
    }

    @Test
    public void testSuccessfulLogin() {
        String email = ConfigManager.getProperty("email");
        String password = ConfigManager.getProperty("password");

        loginPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(webdriver().driver().getWebDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(expectedUrl));

        Assert.assertEquals(expectedUrl, Selenide.webdriver().driver().url());
    }

    @After
    public void tearDown() {
        closeWebDriver();
    }
}
