import io.qameta.allure.Description;
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
 * без авторизации.
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
    @Description("Можно изменить данные пользователя с авторизацией, код ответа 200 и в теле корректные данные")
    public void checkChangeUserDataWithAuth(){
        Response createdUserResponse = createUser(newUser);
        setTokenToCreatedUser(createdUserResponse, newUser);
        ensureUserAttributesInResponseBody(createdUserResponse, newUser);
        Response modifiedUserResponse = setNewUserDataWithAuth(newUserData, newUser);
        ensureStatusCode200(modifiedUserResponse);
        ensureUserAttributesInResponseBody(modifiedUserResponse, newUserData);
    }

    @Test
    @DisplayName("Тест: Изменение данных пользователя без авторизации")
    @Description("Нельзя изменить данные пользователя без авторизации, код ответа 401 и в теле ошибка авторизации")
    public void checkChangeUserDataWithoutAuth(){
        Response createdUserResponse = createUser(newUser);
        setTokenToCreatedUser(createdUserResponse, newUser);
        ensureUserAttributesInResponseBody(createdUserResponse, newUser);
        Response modifiedUser = setNewUserDataWithoutAuth(newUserData);
        ensureStatusCode401(modifiedUser);
        responseBodyAuthError(modifiedUser);
    }

    @After
    @DisplayName("Удаление тестового пользователя")
    @Description("Тестовый пользователь должен быть удалён после теста")
    public void deleteUserAfterTest(){
    deleteUser(newUser);
    }
}