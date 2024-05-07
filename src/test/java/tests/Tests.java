package tests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import managers.ConfigManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pages.LoginPage;
import pages.DocumentsPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.assertEquals;

public class Tests {

    private LoginPage loginPage = new LoginPage();
    private DocumentsPage documentsPage = new DocumentsPage();

    private String documentsPageUrl = ConfigManager.getProperty("documentsPageUrl");

    String email = ConfigManager.getProperty("email");
    String password = ConfigManager.getProperty("password");

    @Before
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.timeout = 10_000;
    }

    @Test
    @Step("Выполнить вход в ЛК кадровика.")
    public void testSuccessfulLogin() {
        loginPage.login(email, password);
        documentsPage.openPage();
        assertEquals(documentsPageUrl, webdriver().driver().getWebDriver().getCurrentUrl());
        documentsPage.searchEmployee("Орлов Д.");
        documentsPage.notFoundEmployee();
    }

    @After
    public void tearDown() {
        closeWebDriver();
    }
}
