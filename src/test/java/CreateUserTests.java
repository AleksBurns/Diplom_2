import models.User;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;
import static constants.URI.*;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Создание пользователя:
 * создать уникального пользователя;
 * создать пользователя, который уже зарегистрирован;
 * создать пользователя и не заполнить одно из обязательных полей.
 */ 

public class CreateUserTests extends UserSteps{

    User newUser = RandomGenerator.randomUser();

    User userWithoutEmail = RandomGenerator.randomUserWithoutEmail();
    User userWithoutPassword = RandomGenerator.randomUserWithoutPassword();
    User userWithoutName = RandomGenerator.randomUserWithoutName();

@Before
public void setUp(){
    RestAssured.baseURI = BASE_URI;
}

    @Step("Проверка тела ответа при попытке создания зарегистрированного пользователя")
    public void userAlreadyExist(Response response){
    response.then().assertThat().body("success", equalTo(false))
            .and().body("message", equalTo("User already exists"));
    }

    @Step("Проверка тела ответа при незаполнении обязательного поля")
    public void requiredFields(Response response){
    response.then().assertThat().body("success", equalTo(false))
            .and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Тест: Создание уникального пользователя")
    @Description("Можно создать пользователя, ранее не зарегистрированного в системе")
    public void checkCreateUser(){
        Response originalUser = createUser(newUser);
        setToken(originalUser);
        statusCode200(originalUser);
        responseBodyUserData(originalUser, newUser);
    }

    @Test
    @DisplayName("Тест: Создание пользователя без заполнения обязательного поля")
    @Description("Нельзя создать пользователя, не заполнив все обязательные поля")
    public void checkRequiredFields(){
        Response requestWithoutEmail = createUser(userWithoutEmail);
        statusCode403(requestWithoutEmail);
        requiredFields(requestWithoutEmail);
        Response requestWithoutPassword = createUser(userWithoutPassword);
        statusCode403(requestWithoutPassword);
        requiredFields(requestWithoutPassword);
        Response requestWithoutName = createUser(userWithoutName);
        statusCode403(requestWithoutName);
        requiredFields(requestWithoutName);
    }

    @Test
    @DisplayName("Тест: Создание пользователя, ранее зарегистрированного в системе ")
    @Description("Нельзя повторно создать уже зарегистрированного пользователя")
    public void checkDuplicateUser(){
    Response originalUser = createUser(newUser);
    setToken(originalUser);
    Response duplicateUser = createUser(newUser);
    statusCode403(duplicateUser);
    userAlreadyExist(duplicateUser);
    }

    @After
    @DisplayName("Удаление тестового пользователя")
    @Description("Тестовый пользователь должен быть удалён после теста")
    public void deleteUserAfterTest(){
       deleteUser();
    }
}