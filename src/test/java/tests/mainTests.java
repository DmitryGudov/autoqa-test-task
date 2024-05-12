package tests;

import com.codeborne.selenide.Configuration;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import managers.ApiManager;
import managers.ConfigManager;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import pages.*;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.driver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    private String employee = ConfigManager.getProperty("employeeName");
    private String legalEntity = ConfigManager.getProperty("legalEntityNameSecond");
    private String employeeId = ConfigManager.getProperty("employeeId");

    @Before
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.timeout = 10_000;
    }

    @Test
    @Order(1)
    @DisplayName("1. Выполнить вход в ЛК кадровика")
    public void testSuccessfulLogin() {
        // Входим в аккаунт кадровика из "ОАО "Тепло""
        loginPage.login(email, password);

        // Проверяем, что открыт реестр документов
        assertTrue(driver().getWebDriver().getCurrentUrl().contains(documentsPageUrl));
    }

    @Test
    @Order(2)
    @DisplayName("1.1. Поиск по фильтру 'Сотрудник' в реестре документов")
    public void testSearchEmployeeInDocumentsRegistry() {
        // В реестре документов открываем боковое меню
        documentsPage.clickSideFilter();

        // Задаем фильтр по сотруднику "Орлов Д."
        documentsPage.searchEmployee(employee);

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
            boolean actual = documentsPage.isTextInEmployeeList(legalEntity);
            assertEquals(false, actual);
        }
    }

    @Test
    @Order(3)
    @DisplayName("1.2. Проверка отсутствия документа в реестре по искомому сотруднику")
    public void testSearchDocumentInDocumentsRegistry() {
        // Проверяем, что в реестре документов отсутствуют документы, где "Орлов Д." фигурирует в качестве сотрудника
        Response response = apiManager.searchEmployeeInDocumentsRegistry(employeeId);

        // Проверяем, что ответ на запрос возвращается со статусом 200
        Assertions.assertEquals(200, response.getStatusCode());
        JsonPath hrJsonPath = response.jsonPath();

        // Проверяем, что "result":"true"
        Assertions.assertTrue(hrJsonPath.getBoolean("result"));

        // Получаем массив документов из JSON-ответа
        List<Map<String, Object>> documents = hrJsonPath.getList("documents");

        // Если массив документов не пустой
        if (!documents.isEmpty()) {
            // Проверяем, что в теле ответа не содержится текст "ООО "Кот""
            String responseAsString = response.asString();
            Assertions.assertFalse(responseAsString.contains(legalEntity));
        } else {
            // Если массив документов пустой, то проверяем, что он действительно пустой
            Assertions.assertTrue(documents.isEmpty());
        }
    }

    @Test
    @Order(4)
    @DisplayName("2. В верхнем фильтре нажать на фильтр \"Юрлицо\"")
    public void testSearchLegalEntityInDocumentsRegistry() {
        // В реестре документов в поле "Юрлицо" вводим "ООО "Кот""
        documentsPage.searchLegalEntity(legalEntity);

        // Проверяем, что выпадающий список пуст и отображается "Не найдено"
        documentsPage.notFoundLegalEntity();
        String actualText = documentsPage.notFoundLegalEntityText();
        assertTrue(actualText.contains("Ненайдено"));
    }

    @Test
    @Order(5)
    @DisplayName("3. Открыть реестр заявлений")
    public void testSearchEmployeeInApplicationsRegistry() {
        // Переходим в реестр заявлений
        applicationsPage.clickRegistryOfApplicationsIcon();

        /* Если реестр заявлений не пустой, то проверяем, что в нем нет заявлений, где "Орлов Д." фигурирует
           в качестве сотрудника, а также то, что в нем нет заявлений, которые относятся к юрлицу "ООО "Кот"" */
        if (applicationsPage.rowsIsVisible() == true) {
            boolean actual = applicationsPage.verifyNoTextInRows(legalEntity, employee);
            assertEquals(true, actual);
        } else {
            // Если реестр заявлений пустой, проверяем, что отображается текст "Нет заявлений"
            String actual = applicationsPage.noApplicationText();
            assertEquals("Нет заявлений", actual);
        }
    }

    @Test
    @Order(6)
    @DisplayName("3.1. Проверка отсутствия заявления в реестре по искомому сотруднику")
    public void testSearchApplicationInApplicationsRegistry() {
        // Проверяем, что в реестре заявлений отсутствуют заявления, где "Орлов Д." фигурирует в качестве сотрудника
        Response response = apiManager.searchEmployeeInApplicationsRegistry(employeeId);

        // Проверяем, что ответ на запрос возвращается со статусом 200
        Assertions.assertEquals(200, response.getStatusCode());
        JsonPath jsonPath = response.jsonPath();

        // Проверяем, что "result":"true"
        Assertions.assertTrue(jsonPath.getBoolean("result"));

        // Получаем массив групп заявлений из JSON-ответа
        List<Map<String, Object>> applicationGroups = jsonPath.getList("applicationGroups");

        // Если массив групп заявлений не пустой
        if (!applicationGroups.isEmpty()) {
            // Проверяем, что в теле ответа не содержится текст "ООО "Кот""
            String responseAsString = response.asString();
            Assertions.assertFalse(responseAsString.contains(legalEntity));
        } else {
            // Если массив документов пустой, то проверяем, что он действительно пустой
            Assertions.assertTrue(applicationGroups.isEmpty());
        }

    }

    @Test
    @Order(7)
    @DisplayName("4. Поиск по фильтру 'ФИО' в реестре Сотрудников")
    public void testSearchEmployeeInEmployeesRegistry() {
        // Переходим в реестр сотрудников и задаем в фильтре по ФИО "Орлов Д."
        employeesPage.searchEmployee(employee);

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
            boolean actual = employeesPage.isTextEmployeesRegistryRows(legalEntity);
            assertEquals(false, actual);
        }

    }

    @Test
    @Order(8)
    @DisplayName("5. Открыть раздел \"Справочники\"")
    public void testSearchLegalEntityInLegalEntitiesRegistry() {
        // Переходим в раздел "Справочники"
        handbooksPage.clickHandBooksIcon();

        // Проверяем, что в перечне юрлиц отсутствует "ООО "Кот""
        boolean actual = handbooksPage.isTextLegalEntityRegistryRows(legalEntity);
        assertEquals(false, actual);
    }

    @After
    public void tearDown() {
        closeWebDriver();
    }

}
