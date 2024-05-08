package tests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import managers.ApiManager;
import managers.ConfigManager;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.DocumentsPage;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.driver;
import static org.junit.Assert.assertTrue;

public class Tests {

    private LoginPage loginPage = new LoginPage();
    private DocumentsPage documentsPage = new DocumentsPage();

    private String loginPageUrl;
    private String documentsPageUrl;
    private String email;
    private String password;

    ApiManager apiManager = new ApiManager();

    @Before
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.timeout = 10_000;

        loadConfigProperties();
    }

    private void loadConfigProperties() {
        loginPageUrl = ConfigManager.getProperty("loginPageUrl");
        documentsPageUrl = ConfigManager.getProperty("documentsPageUrl");
        email = ConfigManager.getProperty("email");
        password = ConfigManager.getProperty("password");
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
        String search = "Орлов Д.";
        Response response = apiManager.searchEmployees(search);
        Assertions.assertEquals(200, response.getStatusCode());
        JsonPath jsonPath = response.jsonPath();
        boolean result = jsonPath.getBoolean("result");
        Assertions.assertTrue(result);

        List<Map<String, Object>> employees = jsonPath.getList("employees");
        if (employees.isEmpty()) {
            Assertions.assertTrue(employees.isEmpty());
        } else {
            Map<String, Object> firstEmployee = employees.get(0);
            Map<String, Object> legalEntity = (Map<String, Object>) firstEmployee.get("legalEntity");
            Assertions.assertNotNull(legalEntity);
            String legalEntityName = (String) legalEntity.get("name");
            Assertions.assertNotEquals("ООО \"Кот\"", legalEntityName);
            String legalEntityShortName = (String) legalEntity.get("shortName");
            Assertions.assertNotEquals("ООО \"Кот\"", legalEntityShortName);
        }
    }

    @After
    public void tearDown() {
        closeWebDriver();
    }
}