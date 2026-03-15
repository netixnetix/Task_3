package api;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import api.models.User;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;

public final class StepCrudUser {

    private static final String CREATE_USER_PATH = "/api/auth/register";
    private static final String GET_UPDATE_DELETE_USER_PATH = "/api/auth/user";

    private StepCrudUser() {
    }

    @Step("Создать пользователя")
    public static Response create(User user) {
        Map<String, String> body = new HashMap<>();
        if (user.getEmail() != null) {
            body.put("email", user.getEmail());
        }
        if (user.getName() != null) {
            body.put("name", user.getName());
        }
        if (user.getPassword() != null) {
            body.put("password", user.getPassword());
        }
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(CREATE_USER_PATH);
    }

    @Step("Удалить пользователя (под авторизацией)")
    public static Response deleteUser(Response authResponse) {
        return given()
                .log().ifValidationFails()
                .header("Authorization", authResponse.path("accessToken"))
                .when()
                .delete(GET_UPDATE_DELETE_USER_PATH);
    }

}
