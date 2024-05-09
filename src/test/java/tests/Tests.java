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

    private String loginPageUrl = ConfigManager.getProperty("loginPageUrl");
    private String documentsPageUrl = ConfigManager.getProperty("documentsPageUrl");
    private String email = ConfigManager.getProperty("email");
    private String password = ConfigManager.getProperty("password");

    ApiManager apiManager = new ApiManager();

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
        testSearchLegalEntity();
    }

    @Step("Выполнить вход в ЛК кадровика")
    public void testSuccessfulLogin() {
        loginPage.login(email, password);
        documentsPage.openPage();
        assertTrue(driver().getWebDriver().getCurrentUrl().contains(documentsPageUrl));
    }

    @Step("Поиск по фильтру 'Сотрудник' в реестре документов")
    public void testSearchEmployee() {
        // Проверка наличия сотрудника в фильтре "Сотрудник"
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
            Assertions.assertNotEquals("ООО \"Коот\"", legalEntityName);
            String legalEntityShortName = (String) legalEntity.get("shortName");
            Assertions.assertNotEquals("ООО \"Коот\"", legalEntityShortName);
        }

        // Проверка наличия документа в реестре по искомому сотруднику
        String employeeId = "962f94c2-ac7c-45c2-864a-9e3bc484be5d";
        Response hrRegistryResponse = apiManager.searchEmployeeInHRRegistry(employeeId);
        Assertions.assertEquals(200, hrRegistryResponse.getStatusCode());
        JsonPath hrJsonPath = hrRegistryResponse.jsonPath();
        List<Object> documents = hrJsonPath.getList("documents");
        Assertions.assertTrue(documents.isEmpty());
    }

    @Step("В верхнем фильтре нажать на фильтр \"Юрлицо\"")
    public void testSearchLegalEntity() {
        documentsPage.searchLegalEntity("ООО \"Коот\"");
        documentsPage.notFoundLegalEntity();
        String actualText = documentsPage.notFoundLegalEntityString();
        assertTrue(actualText.contains("Ненайдено"));
    }

    @After
    public void tearDown() {
        closeWebDriver();
    }
}