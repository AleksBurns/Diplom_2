import io.qameta.allure.junit4.DisplayName;
import models.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static constants.IApiRoutes.*;


/**
 * Изменение данных пользователя:
 * с авторизацией,
 * без авторизации,
 * Для обеих ситуаций нужно проверить, что любое поле можно изменить. Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.
 */

public class ChangeUserDataTests extends Steps{
    User newUser = RandomGenerator.randomUser();
    User newUserData = RandomGenerator.randomUserData();

    @Before
    public void setUp(){
        RestAssured.baseURI = BASE_URI;
    }


    @Test
    @DisplayName("Тест: Изменение данных пользователя с авторизацией")
    public void checkChangeUserDataWithAuth(){
        Response createdUserResponse = createUser(newUser);
        setTokenToCreatedUser(createdUserResponse, newUser);
        ensureAttributes(createdUserResponse, newUser);
        Response modifiedUserResponse = setNewUserDataWithAuth(newUserData, newUser);
        ensureStatusCode200(modifiedUserResponse);
        ensureAttributes(modifiedUserResponse, newUserData);
    }
    @Test
    @DisplayName("Тест: Изменение данных пользователя без авторизации")
    public void checkChangeUserDataWithoutAuth(){
        Response createdUserResponse = createUser(newUser);
        setTokenToCreatedUser(createdUserResponse, newUser);
        ensureAttributes(createdUserResponse, newUser);
        Response modifiedUser = setNewUserDataWithoutAuth(newUserData);
        ensureStatusCode401(modifiedUser);
        responseBodySetNewUserDataWithoutAuth(modifiedUser);
    }

    @After
    public void deleteUserAfterTest(){
    deleteUser(newUser);
    }
}