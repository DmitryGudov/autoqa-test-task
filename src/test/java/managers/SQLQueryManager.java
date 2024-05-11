package managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLQueryManager {

    public static String getLegalEntityId(String legalEntityName) {
        try {
            String query = "SELECT id " +
                    "FROM ekd_ekd.legal_entity " +
                    "WHERE name = '" + legalEntityName + "';";
            ResultSet resultSet = DBConnectionManager.databaseConnection(query);
            if (resultSet.next()) {
                String legalEntityId = resultSet.getString("id");
                resultSet.close();
                DBConnectionManager.disconnect();
                return legalEntityId;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getHrId(String legalEntityId, String hrRoleId) {
        try {
            String query = "SELECT DISTINCT ekd_ekd.employee.id " +
                    "FROM ekd_ekd.employee " +
                    "JOIN ekd_ekd.client_user_employee_role ON employee.client_user_id = client_user_employee_role.client_user_id " +
                    "WHERE ekd_ekd.employee.legal_entity_id = '" + legalEntityId + "' " +
                    "AND ekd_ekd.client_user_employee_role.role_id = '" + hrRoleId + "';";
            ResultSet resultSet = DBConnectionManager.databaseConnection(query);
            if (resultSet.next()) {
                String hrId = resultSet.getString("id");
                resultSet.close();
                DBConnectionManager.disconnect();
                return hrId;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getEmployeeId(String legalEntityId, String employeeName) {
        try {
            String query = "SELECT ekd_ekd.employee.id " +
                    "FROM ekd_ekd.employee " +
                    "WHERE legal_entity_id = '" + legalEntityId + "' " +
                    "AND client_user_id IN (SELECT ekd_ekd.client_user.id " +
                    "FROM ekd_ekd.client_user " +
                    "WHERE user_id IN (SELECT user_id " +
                    "FROM ekd_id.person " +
                    "WHERE CONCAT(last_name, ' ', first_name) = '" + employeeName + "'));";
            ResultSet resultSet = DBConnectionManager.databaseConnection(query);
            if (resultSet.next()) {
                String employeeId = resultSet.getString("id");
                resultSet.close();
                DBConnectionManager.disconnect();
                return employeeId;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static int getCountApplications(String legalEntityId, String employeeName) {
        try {
            String query = "SELECT COUNT(application_group_id) " +
                    "FROM ekd_ekd.application_group " +
                    "JOIN ekd_ekd.application ON application_group.id = application.application_group_id " +
                    "WHERE legal_entity_id = '" + legalEntityId + "' " +
                    "AND creator_id IN (SELECT ekd_ekd.client_user.id " +
                    "FROM ekd_ekd.client_user " +
                    "WHERE user_id IN (SELECT user_id " +
                    "FROM ekd_id.person " +
                    "WHERE CONCAT(last_name, ' ', first_name) = '" + employeeName + "')) " +
                    "GROUP BY creator_id;";
            ResultSet resultSet = DBConnectionManager.databaseConnection(query);
            if (resultSet.next()) {
                int count = Integer.parseInt(resultSet.getString("count"));
                resultSet.close();
                DBConnectionManager.disconnect();
                return count;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public static int getCountDocuments(String legalEntityId, String employeeId) {
        try {
            String query = "SELECT COUNT(document.id) " +
                    "FROM ekd_ekd.document " +
                    "JOIN ekd_ekd.document_signer ON document.id = document_signer.document_id " +
                    "WHERE legal_entity_id = '" + legalEntityId + "' " +
                    "AND employee_id = '" + employeeId + "' " +
                    "GROUP BY employee_id;";
            ResultSet resultSet = DBConnectionManager.databaseConnection(query);
            if (resultSet.next()) {
                int count = Integer.parseInt(resultSet.getString("count"));
                resultSet.close();
                DBConnectionManager.disconnect();
                return count;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public static List<String> getLegalEntitiesList(String employeeName) {
        try {
            String query = "SELECT legal_entity_id " +
                    "FROM ekd_ekd.client_user_employee_role " +
                    "WHERE client_user_id IN (SELECT ekd_ekd.client_user.id " +
                    "FROM ekd_ekd.client_user " +
                    "WHERE user_id IN (SELECT user_id " +
                    "FROM ekd_id.person " +
                    "WHERE CONCAT(last_name, ' ', first_name, ' ', patronymic) = '" +
                    employeeName + "'));";
            ResultSet resultSet = DBConnectionManager.databaseConnection(query);
            List<String> legalEntityList = new ArrayList<>();
            while (resultSet.next()) {
                legalEntityList.add(resultSet.getString("legal_entity_id"));
            }
            resultSet.close();
            DBConnectionManager.disconnect();
            return legalEntityList;
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
