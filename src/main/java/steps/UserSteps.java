package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;

import static constants.IApiRoutes.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class UserSteps {

    @Step("Создание тестового пользователя")
    public Response createUser(User newUser) {
        Response response;
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .when()
                .post(USER_REGISTER_ROUTE);
        return response;
    }

    @Step("Удаление тестового пользователя")
    public void deleteUser(User createdUser) {
        if (createdUser == null) return;
        if (createdUser.getAccessToken() != null) {
            given()
                    .auth()
                    .oauth2(createdUser.getAccessToken()
                            .replace("Bearer ", ""))
                    .delete(USER_ROUTE)
                    .then()
                    .statusCode(202);
            System.out.println("Тестовый пользователь успешно удалён.");
        }
    }

    @Step("Проверка кода ответа 200")
    public void ensureStatusCode200(Response response) {
        response.then().assertThat().statusCode(200);
    }

    @Step("Проверка статус кода 401")
    public void ensureStatusCode401(Response response) {
        response.then().assertThat().statusCode(401);
    }

    @Step("Проверка статус кода 403")
    public void ensureStatusCode403(Response response) {
        response.then().assertThat().statusCode(403);
    }

    @Step("Проверка данных о пользователе в теле ответа")
    public void ensureAttributes(Response response, User user) {
        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(user.getEmail()))
                .and().body("user.name", equalTo(user.getName()));
    }
}
