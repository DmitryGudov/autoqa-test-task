package tests;

import managers.ConfigManager;
import managers.SQLQueryManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class preconditionsTests {

    SQLQueryManager sqlQueryManager = new SQLQueryManager();

    String legalEntityNameFirst = ConfigManager.getProperty("legalEntityNameFirst");
    String legalEntityNameSecond = ConfigManager.getProperty("legalEntityNameSecond");
    String legalEntityIdFirst = ConfigManager.getProperty("legalEntityIdFirst");
    String legalEntityIdSecond = ConfigManager.getProperty("legalEntityIdSecond");
    String hrRoleId = ConfigManager.getProperty("hrRoleId");
    String hrId = ConfigManager.getProperty("hrId");
    String employeeName = ConfigManager.getProperty("employeeName");
    String employeeHrName = ConfigManager.getProperty("employeeHrName");
    String employeeId = ConfigManager.getProperty("employeeId");


    @Test
    @DisplayName("Проверка существования ОАО \"Тепло\" на пространстве")
    public void testVerificationOfThExistenceOfTheFirstLegalEntity() {
        String legalEntityIdActual = sqlQueryManager.getLegalEntityId(legalEntityNameFirst);
        assertEquals(legalEntityIdFirst, legalEntityIdActual);
    }

    @Test
    @DisplayName("Проверка существования ООО \"Кот\" на пространстве")
    public void testVerificationOfThExistenceOfTheSecondLegalEntity() {
        String legalEntityIdActual = sqlQueryManager.getLegalEntityId(legalEntityNameSecond);
        assertEquals(legalEntityIdSecond, legalEntityIdActual);
    }

    @Test
    @DisplayName("Проверка существования сотрудника с ролью \"Кадровик\" в ОАО \"Тепло\"")
    public void testVerificationOfTheExistenceOfAHrInALegalEntity() {
        String hrIdActual = sqlQueryManager.getHrId(legalEntityIdFirst, hrRoleId);
        assertEquals(hrId, hrIdActual);
    }

    @Test
    @DisplayName("Проверка существования сотрудника Орлов Д. в ООО \"Кот\"")
    public void testVerificationOfTheExistenceOfEmployeeInALegalEntity() {
        String employeeIdActual = sqlQueryManager.getEmployeeId(legalEntityIdSecond, employeeName);
        assertEquals(employeeId, employeeIdActual);
    }

    @Test
    @DisplayName("Проверка существования заявления Орлова Д.")
    public void testVerificationOfTheExistenceOfApplications() {
        int countActual = sqlQueryManager.getCountApplications(legalEntityIdSecond, employeeName);
        boolean actual = countActual >= 1;
        assertEquals(true, actual);
    }

    @Test
    @DisplayName("Проверка существования документа Орлова Д.")
    public void testVerificationOfTheExistenceOfDocuments() {
        int countActual = sqlQueryManager.getCountDocuments(legalEntityIdSecond, employeeId);
        boolean actual = countActual >= 1;
        assertEquals(true, actual);
    }

    @Test
    @DisplayName("Проверка отсутствия у кадровика прав на юрлицо ООО \"Кот\"")
    public void testVerificationOfTheAbsenceOfRightsToAaLegalEntity() {
        List<String> actualLegalEntityList = sqlQueryManager.getLegalEntitiesList(employeeHrName);
        if (actualLegalEntityList != null) {
            assertFalse(actualLegalEntityList.contains(legalEntityIdSecond));
        } else {
            assertTrue(true);
        }
    }

}
