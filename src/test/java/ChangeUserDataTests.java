import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import models.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static constants.IApiRoutes.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


/**
 * Изменение данных пользователя:
 * с авторизацией,
 * без авторизации,
 * Для обеих ситуаций нужно проверить, что любое поле можно изменить. Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.
 */

public class ChangeUserDataTests extends UserSteps {
    User originalUser = RandomGenerator.randomUser();
    User newUserData = RandomGenerator.randomUserData();
    private User CreatedUser;


    @Before
    public void setUp(){
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Передача новых данных о пользователе с авторизацией")
    public Response setNewUserDataWithAuth(User user){
        Response response;
        response = given()
                .header("Content-type", "application/json")
                .auth()
                .oauth2(user.getAccessToken().replace("Bearer ", ""))
                .body(user)
                .when()
                .patch(USER_ROUTE);
        return response;
    }

    @Step("Передача новых данных о пользователе без авторизации")
    public Response setNewUserDataWithoutAuth(User user){
        Response response;
        response = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .patch(USER_ROUTE);
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
        Response createdUserResponse = createUser(originalUser);
        CreatedUser = createdUserResponse.as(User.class);
        ensureAttributes(createdUserResponse, originalUser);
        newUserData.setAccessToken(CreatedUser.getAccessToken());
        Response modifiedUserResponse = setNewUserDataWithAuth(newUserData);
        ensureStatusCode200(modifiedUserResponse);
        ensureAttributes(modifiedUserResponse, newUserData);
    }
    @Test
    @DisplayName("Тест: Изменение данных пользователя без авторизации")
    public void checkChangeUserDataWithoutAuth(){
        Response createdUserResponse = createUser(originalUser);
        CreatedUser = createdUserResponse.as(User.class);
        ensureAttributes(createdUserResponse, originalUser);
        newUserData.setAccessToken(CreatedUser.getAccessToken());
        Response modifiedUser = setNewUserDataWithoutAuth(newUserData);
        ensureStatusCode401(modifiedUser);
        responseBodySetNewUserDataWithoutAuth(modifiedUser);
    }

    @Test
    public void checkGetUserData(){
        Response createdUserResponse = createUser(originalUser);
        CreatedUser = createdUserResponse.as(User.class);
    }

    @After
    public void deleteUserAfterTest(){
    deleteUser(CreatedUser);
    }
}