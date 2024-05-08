package tests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import managers.ConfigManager;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.DocumentsPage;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.driver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Tests {

    private LoginPage loginPage = new LoginPage();
    private DocumentsPage documentsPage = new DocumentsPage();

    private String loginPageUrl = ConfigManager.getProperty("loginPageUrl");
    private String documentsPageUrl = ConfigManager.getProperty("documentsPageUrl");

    private String email = ConfigManager.getProperty("email");
    private String password = ConfigManager.getProperty("password");

    @Before
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.timeout = 10_000;
    }

    @Test
    @DisplayName("Проверка недоступности юрлица, на которое у кадровика нет прав")
    public void test() {
        testSuccessfulLogin();
        testSearchEmployee();
    }

    @Step("Выполнить вход в ЛК кадровика")
    public void testSuccessfulLogin() {
        loginPage.login(email, password);
        documentsPage.openPage();
        assertTrue(driver().getWebDriver().getCurrentUrl().contains(documentsPageUrl));
    }

    @Step("Поиск по фильтру 'Сотрудник' в реестре документов")
    public void testSearchEmployee() {
        documentsPage.searchEmployee("Орлов Д.а");
        if (documentsPage.getNotFoundEmployee().isDisplayed()) {
            String actualText = documentsPage.notFoundEmployeeString();
            assertTrue(actualText.contains("Ненайдено"));
        } else {

        }

    }


    @After
    public void tearDown() {
        closeWebDriver();
    }
}