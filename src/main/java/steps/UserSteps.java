package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;

import static constants.URI.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserSteps {
    User token;

    @Step("Создание тестового пользователя")
    public Response createUser(User newUser) {
        Response response;
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .when()
                .post(USER_REGISTER);
        return response;
    }

    @Step("Удаление тестового пользователя")
    public void deleteUser(){
        if(token != null) {
            given()
                    .auth()
                    .oauth2(token
                            .getAccessToken()
                            .replace("Bearer ", ""))
                    .delete(USER)
                    .then()
                    .statusCode(202);
            System.out.println("Тестовый пользователь успешно удалён.");
        }
        else return;
    }

    @Step("Присвоение токена и вывод его в консоль")
    public void setToken(Response response){
        token = response.as(User.class);
        System.out.println("\nAccess Token: \n" + token.getAccessToken());
    }

    @Step("Проверка кода ответа 200")
    public void statusCode200(Response response){
        response.then().assertThat().statusCode(200);
    }

    @Step("Проверка статус кода 401")
    public void statusCode401(Response response){
        response.then().assertThat().statusCode(401);
    }

    @Step("Проверка статус кода 403")
    public void statusCode403(Response response){
        response.then().assertThat().statusCode(403);
    }

    @Step("Получение данных о пользователе")
    public Response getUserData(){
        Response response;
        response = given()
                .auth()
                .oauth2(token.getAccessToken().replace("Bearer ", ""))
                .get(USER);
        return response;
    }

    @Step("Проверка данных о пользователе в теле ответа")
    public void responseBodyUserData(Response response, User user){
        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo (user.getEmail()))
                .and().body("user.name", equalTo(user.getName()));
    }
}
