package managers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiManager {

    private static final String BASE_URL = ConfigManager.getProperty("baseUrlForApi");
    private static final String USER_API_TOKEN = ConfigManager.getProperty("userApiToken");
    private static final String CLIENT_ID = ConfigManager.getProperty("clientId");

    static {
        RestAssured.baseURI = BASE_URL;
    }

    public Response searchEmployees(String search) {
        return given()
                .header("User-Api-Token", USER_API_TOKEN)
                .pathParam("id", CLIENT_ID)
                .queryParam("type", "HR")
                .queryParam("search", search)
                .when()
                .get("/clients/{id}/documentRegistryFilters/employees");
    }

    public Response searchEmployeeInHRRegistry(String employeeId) {
        return given()
                .header("User-Api-Token", USER_API_TOKEN)
                .contentType(ContentType.JSON)
                .pathParam("id", CLIENT_ID)
                .body("{\"employeeIds\":[\"" + employeeId + "\"]}")
                .when()
                .post("/clients/{id}/documents/hrRegistry");
    }

    public Response searchEmployeeInHRRegistryApplications(String employeeId) {
        return given()
                .baseUri("https://app-regress-dgudov.myhrlink.ru/api/v2")
                .header("User-Api-Token", USER_API_TOKEN)
                .contentType(ContentType.JSON)
                .pathParam("id", CLIENT_ID)
                .body("{\"employeeIds\":[\"" + employeeId + "\"]}")
                .when()
                .post("/clients/{id}/applicationGroups/getHrRegistry");
    }

}
