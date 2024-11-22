import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import models.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static constants.URI.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


/**
 * Изменение данных пользователя:
 * с авторизацией,
 * без авторизации,
 * Для обеих ситуаций нужно проверить, что любое поле можно изменить. Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.
 */

public class ChangeUserDataTests extends UserSteps {
    User newUser = RandomGenerator.randomUser();
    User newUserData = RandomGenerator.randomUserData();
    User token;


    @Before
    public void setUp(){
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Передача новых данных о пользователе с авторизацией")
    public Response setNewUserDataWithAuth(User userData, User token){
        Response response;
        response = given()
                .header("Content-type", "application/json")
                .auth()
                .oauth2(token.getAccessToken().replace("Bearer ", ""))
                .body(userData)
                .when()
                .patch(USER);
        return response;
    }

    @Step("Присвоение токена и вывод его в консоль")
    public void setToken2(Response response){
        token = response.as(User.class);
        System.out.println("\nAccess Token: \n" + token.getAccessToken());
    }

    @Step("Передача новых данных о пользователе без авторизации")
    public Response setNewUserDataWithoutAuth(User user){
        Response response;
        response = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .patch(USER);
        return response;
    }

    @Step("Проверка тела ответа при изменении данных без авторизации")
    public void responseBodySetNewUserDataWithoutAuth(Response response){
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Тест: Изменение данных пользователя с авторизацией")
    public void checkChangeUserDataWithAuth(){
        Response originalUser = createUser(newUser);
        setToken2(originalUser);
        responseBodyUserData(originalUser, newUser);
        Response modifiedUser = setNewUserDataWithAuth(newUserData, token);
        statusCode200(modifiedUser);
        responseBodyUserData(modifiedUser, newUserData);
    }
    @Test
    @DisplayName("Тест: Изменение данных пользователя без авторизации")
    public void checkChangeUserDataWithoutAuth(){
        Response originalUser = createUser(newUser);
        setToken(originalUser);
        responseBodyUserData(originalUser, newUser);
        Response modifiedUser = setNewUserDataWithoutAuth(newUserData);
        statusCode401(modifiedUser);
        responseBodySetNewUserDataWithoutAuth(modifiedUser);
    }

    @Test
    public void checkGetUserData(){
        Response originalUser = createUser(newUser);
        setToken(originalUser);
    }

    @After
    public void deleteUserAfterTest(){
    deleteUser();
    }
}