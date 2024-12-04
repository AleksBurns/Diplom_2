import models.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static constants.Routes.*;

/**
 * Создание пользователя:
 * создать уникального пользователя;
 * создать пользователя, который уже зарегистрирован;
 * создать пользователя и не заполнить одно из обязательных полей.
 */

public class CreateUserTests extends Steps {

    User newUser = RandomGenerator.randomUser();
    User userWithoutEmail = RandomGenerator.randomUserWithoutEmail();
    User userWithoutPassword = RandomGenerator.randomUserWithoutPassword();
    User userWithoutName = RandomGenerator.randomUserWithoutName();

    @Before
    public void setUp(){
    RestAssured.baseURI = BASE_URI;
}

    @Test
    @DisplayName("Тест: Создание уникального пользователя")
    @Description("Можно создать пользователя, ранее не зарегистрированного в системе, код ответа 200, в теле ответа корректные данные")
    public void checkCreateUser(){
        Response createdUserResponse = createUser(newUser);
        setTokenToCreatedUser(createdUserResponse,newUser);
        ensureStatusCode200(createdUserResponse);
        ensureUserAttributesInResponseBody(createdUserResponse, newUser);
    }

    @Test
    @DisplayName("Тест: Создание пользователя, ранее зарегистрированного в системе ")
    @Description("Нельзя повторно создать уже зарегистрированного пользователя, код ответа 403 и в теле ошибка: User already exists")
    public void checkDuplicateUser(){
        Response createdUserResponse = createUser(newUser);
        setTokenToCreatedUser(createdUserResponse, newUser);
        Response duplicateUser = createUser(newUser);
        ensureStatusCode403(duplicateUser);
        userAlreadyExistResponse(duplicateUser);
    }

    @Test
    @DisplayName("Тест: Создание пользователя без заполнения обязательного поля")
    @Description("Нельзя создать пользователя, не заполнив все обязательные поля, код ответа 403, в теле ошибка: Email, password and name are required fields")
    public void checkRequiredFields(){
        Response createdUserResponse = createUser(userWithoutEmail);
        ensureStatusCode403(createdUserResponse);
        requiredFieldEmptyResponse(createdUserResponse);
        Response requestWithoutPassword = createUser(userWithoutPassword);
        ensureStatusCode403(requestWithoutPassword);
        requiredFieldEmptyResponse(requestWithoutPassword);
        Response requestWithoutName = createUser(userWithoutName);
        ensureStatusCode403(requestWithoutName);
        requiredFieldEmptyResponse(requestWithoutName);
    }

    @After
    @DisplayName("Удаление тестового пользователя")
    @Description("Тестовый пользователь должен быть удалён после теста")
    public void deleteUserAfterTest(){
       deleteUser(newUser);
    }
}