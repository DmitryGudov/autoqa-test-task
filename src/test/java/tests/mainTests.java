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
import pages.*;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.driver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class mainTests {

    private LoginPage loginPage = new LoginPage();
    private DocumentsPage documentsPage = new DocumentsPage();
    private ApplicationsPage applicationsPage = new ApplicationsPage();
    private EmployeesPage employeesPage = new EmployeesPage();
    private HandbooksPage handbooksPage = new HandbooksPage();

    private ApiManager apiManager = new ApiManager();

    private String documentsPageUrl = ConfigManager.getProperty("documentsPageUrl");
    private String email = ConfigManager.getProperty("email");
    private String password = ConfigManager.getProperty("password");
    private String employeeName = ConfigManager.getProperty("employeeName");
    private String legalEntityName = ConfigManager.getProperty("legalEntityName");
    private String employeeId = ConfigManager.getProperty("employeeId");

    @Before
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.timeout = 10_000;
    }

    @Test
    @DisplayName("Проверка недоступности юрлица, на которое у кадровика нет прав")
    public void mainTests() {
        testSuccessfulLogin();
        testSearchEmployeeInDocumentsRegistry();
        testSearchDocumentInDocumentsRegistry();
        testSearchLegalEntityInDocumentsRegistry();
        testSearchEmployeeInApplicationsRegistry();
        testSearchApplicationInApplicationsRegistry();
        testSearchEmployeeInEmployeesRegistry();
        testSearchLegalEntityInLegalEntitiesRegistry();
    }

    @Step("1. Выполнить вход в ЛК кадровика")
    public void testSuccessfulLogin() {
        // Входим в аккаунт кадровика из "ОАО "Тепло""
        loginPage.login(email, password);

        // Проверяем, что открыт реестр документов
        assertTrue(driver().getWebDriver().getCurrentUrl().contains(documentsPageUrl));
    }

    @Step("1.1. Поиск по фильтру 'Сотрудник' в реестре документов")
    public void testSearchEmployeeInDocumentsRegistry() {
        // В реестре документов открываем боковое меню
        documentsPage.clickSideFilter();

        // Задаем фильтр по сотруднику "Орлов Д."
        documentsPage.searchEmployee(employeeName);

        /* Если у сотрудника "Орлов Д." нет совместителей в других юрлицах и нет других сотрудников,
           удовлетворящих условиям поиска, то ожидаем в выпадающем списке "Не найдено" */
        if (documentsPage.notFoundEmployeeIsVisible() == true) {
            documentsPage.notFoundEmployee();
            String actualText = documentsPage.notFoundEmployeeText();
            assertTrue(actualText.contains("Ненайдено"));
        } else {
            /* Если у сотрудника "Орлов Д." есть совместитель в другом юрлице или есть сотрудники,
               которые удовлетворяют условиям поиска, тогда проверяем, что среди этих сотрудников
               нет тех, которые относятся к юрлицу "ООО "Кот"" */
            boolean actual = documentsPage.isTextInEmployeeList(legalEntityName);
            assertEquals(false, actual);
        }
    }

    @Step("1.2. Проверка отсутствия документа в реестре по искомому сотруднику")
    public void testSearchDocumentInDocumentsRegistry() {
        // Проверяем, что в реестре документов отсутствуют документы, где "Орлов Д." фигурирует в качестве сотрудника
        Response hrRegistryResponse = apiManager.searchEmployeeInDocumentsRegistry(employeeId);

        // Проверяем, что ответ на запрос возвращается со статусом 200
        Assertions.assertEquals(200, hrRegistryResponse.getStatusCode());
        JsonPath hrJsonPath = hrRegistryResponse.jsonPath();

        // Проверяем, что "result":"true"
        Assertions.assertTrue(hrJsonPath.getBoolean("result"));

        // Проверяем, что в ответе возвращается пустой массив документов
        List<Object> documents = hrJsonPath.getList("documents");
        Assertions.assertNotNull(documents);
        Assertions.assertTrue(documents.isEmpty());
    }

    @Step("2. В верхнем фильтре нажать на фильтр \"Юрлицо\"")
    public void testSearchLegalEntityInDocumentsRegistry() {
        // В реестре документов в поле "Юрлицо" вводим "ООО "Кот""
        documentsPage.searchLegalEntity(legalEntityName);

        // Проверяем, что выпадающий список пуст и отображается "Не найдено"
        documentsPage.notFoundLegalEntity();
        String actualText = documentsPage.notFoundLegalEntityText();
        assertTrue(actualText.contains("Ненайдено"));
    }

    @Step("3. Открыть реестр заявлений")
    public void testSearchEmployeeInApplicationsRegistry() {
        // Переходим в реестр заявлений и задаем фильтр по сотруднику "Орлов Д."
        applicationsPage.searchEmployee(employeeName);

        // Проверяем, что выпадающий список пуст и отображается "Не найдено"
        applicationsPage.notFoundEmployee();
        String actualText = applicationsPage.notFoundEmployeeText();
        assertTrue(actualText.contains("Ненайдено"));
    }

    @Step("3.1. Проверка отсутствия заявления в реестре по искомому сотруднику")
    public void testSearchApplicationInApplicationsRegistry() {
        // Проверяем, что в реестре заявлений отсутствуют заявления, где "Орлов Д." фигурирует в качестве сотрудника
        Response response = apiManager.searchEmployeeInApplicationsRegistry(employeeId);

        // Проверяем, что ответ на запрос возвращается со статусом 200
        Assertions.assertEquals(200, response.getStatusCode());
        JsonPath jsonPath = response.jsonPath();

        // Проверяем, что "result":"true"
        Assertions.assertTrue(jsonPath.getBoolean("result"));

        // Проверяем, что в ответе возвращается пустой массив групп заявлений
        List<Object> applicationGroups = jsonPath.getList("applicationGroups");
        Assertions.assertNotNull(applicationGroups);
        Assertions.assertTrue(applicationGroups.isEmpty());
    }

    @Step("4. Поиск по фильтру 'ФИО' в реестре Сотрудников")
    public void testSearchEmployeeInEmployeesRegistry() {
        // Переходим в реестр сотрудников и задаем в фильтре по ФИО "Орлов Д."
        employeesPage.searchEmployee(employeeName);

        /* Если у сотрудника "Орлов Д." нет совместителей в других юрлицах и нет других сотрудников,
           удовлетворящих условиям поиска, то в реестре сотрудников ожидаем сообщение о том, что
           нет сотрудников для отображения */
        if (employeesPage.noEmployeesToDisplayIsVisible() == true) {
            employeesPage.noEmployeesToDisplay();
            String actualText = employeesPage.noEmployeesToDisplayText();
            assertTrue(actualText.contains("Нет сотрудников для отображения"));
        } else {
            /* Если у сотрудника "Орлов Д." есть совместитель в другом юрлице или есть сотрудники,
               которые удовлетворяют условиям поиска, тогда проверяем, что среди этих сотрудников
               нет тех, которые относятся к юрлицу "ООО "Кот"" */
            boolean actual = employeesPage.isTextEmployeesRegistryRows(legalEntityName);
            assertEquals(false, actual);
        }

    }

    @Step("5. Открыть раздел \"Справочники\"")
    public void testSearchLegalEntityInLegalEntitiesRegistry() {
        // Переходим в раздел "Справочники"
        handbooksPage.clickHandBooksIcon();

        // Проверяем, что в перечне юрлиц отсутствует "ООО "Кот""
        boolean actual = handbooksPage.isTextLegalEntityRegistryRows(legalEntityName);
        assertEquals(false, actual);
    }

    @After
    public void tearDown() {
        closeWebDriver();
    }

}