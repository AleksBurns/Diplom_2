import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static constants.IApiRoutes.*;

/**
 * Логин пользователя:
 * логин под существующим пользователем,
 * логин с неверным логином и паролем.
 */

public class LoginUserTests extends Steps{
    User newUser = RandomGenerator.randomUser();


    @Before
    public void setUp(){
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Тест: Логин под существующим пользователем")
    @Description("Можно залогиниться с данными созданного пользователя, код ответа 200 и в теле корректные данные")
    public void checkLoginWithCreatedUser(){
        Response createdUserResponse  = createUser(newUser);
        setTokenToCreatedUser(createdUserResponse, newUser);
        Response loginUserResponse = userLogin(newUser);
        ensureStatusCode200(loginUserResponse);
        ensureUserAttributesInResponseBody(loginUserResponse, newUser);
    }

    @Test
    @DisplayName("Тест: Логин с неверным логином и паролем")
    @Description("Нельзя залогиниться с неверными данными, код ответа 401 и в теле ошибка: email or password are incorrect")
    public void checkLoginWithUnregisteredUser(){
        Response loginUserResponse = userLogin(newUser);
        ensureStatusCode401(loginUserResponse);
        incorrectLoginDataResponse(loginUserResponse);
    }


    @After
    @DisplayName("Удаление тестового пользователя")
    @Description("Тестовый пользователь должен быть удалён после теста")
    public void deleteUserAfterTest(){
        deleteUser(newUser);
    }
}