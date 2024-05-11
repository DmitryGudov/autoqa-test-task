package managers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiManager {

    private static final String BASE_URI = ConfigManager.getProperty("baseUriForApi");
    private static final String USER_API_TOKEN = ConfigManager.getProperty("userApiToken");
    private static final String CLIENT_ID = ConfigManager.getProperty("clientId");

    static {
        RestAssured.baseURI = BASE_URI;
    }

    public Response searchEmployeeInDocumentsRegistry(String employeeId) {
        return given()
                .baseUri(BASE_URI + "1")
                .header("User-Api-Token", USER_API_TOKEN)
                .contentType(ContentType.JSON)
                .pathParam("id", CLIENT_ID)
                .body("{\"employeeIds\":[\"" + employeeId + "\"]}")
                .when()
                .post("/clients/{id}/documents/hrRegistry");
    }

    public Response searchEmployeeInApplicationsRegistry(String employeeId) {
        return given()
                .baseUri(BASE_URI + "2")
                .header("User-Api-Token", USER_API_TOKEN)
                .contentType(ContentType.JSON)
                .pathParam("id", CLIENT_ID)
                .body("{\"employeeIds\":[\"" + employeeId + "\"]}")
                .when()
                .post("/clients/{id}/applicationGroups/getHrRegistry");
    }

}
