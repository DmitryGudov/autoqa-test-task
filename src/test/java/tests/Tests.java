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
import pages.ApplicationsPage;
import pages.LoginPage;
import pages.DocumentsPage;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.driver;
import static org.junit.Assert.assertTrue;

public class Tests {

    private LoginPage loginPage = new LoginPage();
    private DocumentsPage documentsPage = new DocumentsPage();
    private ApplicationsPage applicationsPage = new ApplicationsPage();

    private ApiManager apiManager = new ApiManager();

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
        testSearchEmployeeInDocumentsRegistry();
        testSearchDocumentInDocumentsRegistry();
        testSearchLegalEntityInDocumentsRegistry();
        testSearchEmployeeInApplicationsRegistry();
        testSearchDocumentInApplicationsRegistry();
    }

    @Step("1. Выполнить вход в ЛК кадровика")
    public void testSuccessfulLogin() {
        loginPage.login(email, password);
        assertTrue(driver().getWebDriver().getCurrentUrl().contains(documentsPageUrl));
    }

    @Step("1.1. Поиск по фильтру 'Сотрудник' в реестре документов")
    public void testSearchEmployeeInDocumentsRegistry() {
        documentsPage.clickSideFilter();
        documentsPage.searchEmployee("Орлов Д!");
        documentsPage.notFoundEmployee();
        String actualText = documentsPage.notFoundEmployeeString();
        assertTrue(actualText.contains("Ненайдено"));
    }

    @Step("1.2. Проверка отсутствия документа в реестре по искомому сотруднику")
    public void testSearchDocumentInDocumentsRegistry() {
        String employeeId = ConfigManager.getProperty("employeeId");
        Response hrRegistryResponse = apiManager.searchEmployeeInDocumentsRegistry(employeeId);
        Assertions.assertEquals(200, hrRegistryResponse.getStatusCode());
        JsonPath hrJsonPath = hrRegistryResponse.jsonPath();
        List<Object> documents = hrJsonPath.getList("documents");
        Assertions.assertTrue(documents.isEmpty());
    }

    @Step("2. В верхнем фильтре нажать на фильтр \"Юрлицо\"")
    public void testSearchLegalEntityInDocumentsRegistry() {
        documentsPage.searchLegalEntity("ООО \"Коот\"");
        documentsPage.notFoundLegalEntity();
        String actualText = documentsPage.notFoundLegalEntityString();
        assertTrue(actualText.contains("Ненайдено"));
    }

    @Step("3. Открыть реестр заявлений")
    public void testSearchEmployeeInApplicationsRegistry() {
        applicationsPage.searchEmployee("Орлов Д!");
        applicationsPage.notFoundEmployee();
        String actualText = applicationsPage.notFoundEmployeeString();
        assertTrue(actualText.contains("Ненайдено"));
    }

    @Step("3.1. Проверка отсутствия заявления в реестре по искомому сотруднику")
    public void testSearchDocumentInApplicationsRegistry() {
        String employeeId = ConfigManager.getProperty("employeeId");
        Response response = apiManager.searchEmployeeInApplicationsRegistry(employeeId);
        Assertions.assertEquals(200, response.getStatusCode(), "Статус ответа должен быть 200");
        JsonPath jsonPath = response.jsonPath();
        Assertions.assertTrue(jsonPath.getBoolean("result"), "Поле 'result' должно быть true");
        List<Object> applicationGroups = jsonPath.getList("applicationGroups");
        Assertions.assertNotNull(applicationGroups, "Поле 'applicationGroups' должно быть в ответе");
        Assertions.assertTrue(applicationGroups.isEmpty(), "Поле 'applicationGroups' должно быть пустым");
    }

    @After
    public void tearDown() {
        closeWebDriver();
    }

}
