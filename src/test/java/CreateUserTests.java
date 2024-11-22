import POJO.User;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Создание пользователя:
 * создать уникального пользователя;
 * создать пользователя, который уже зарегистрирован;
 * создать пользователя и не заполнить одно из обязательных полей.
 */

public class CreateUserTests {

    User newUser = RandomGenerator.randomUser();
    User token;

    User userWithoutEmail = RandomGenerator.randomUserWithoutEmail();
    User userWithoutPassword = RandomGenerator.randomUserWithoutPassword();
    User userWithoutName = RandomGenerator.randomUserWithoutName();

@Before
public void setUp(){
    RestAssured.baseURI = URI.BASE_URI;
}

    @Step("Создание пользователя")
    public Response createUser(User newUser) {
    Response response;
        response = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(newUser)
                    .when()
                    .post(URI.USER_REGISTER);
        return response;
    }

    @Step("Присваивание токена")
    public void setToken(Response response){
     token = response.as(User.class);
    System.out.println("Access Token: \n" + token.getAccessToken());
    }

    @Step("Проверка кода ответа при успешном создании пользователя")
    public void statusCode200(Response response){
    response.then().statusCode(200);
    }

    @Step("Проверка тела ответа при успешном создании пользователя")
    public void responseBodyUserCreate(Response response, User newUser, User token){
    response.then().assertThat().body("success", equalTo(true))
            .and().body("accessToken", equalTo(token.getAccessToken()))
            .and().body("user.email", equalTo (newUser.getEmail()))
            .and().body("user.name", equalTo(newUser.getName()));
    }
    @Step("Статус код 403")
    public void statusCode403(Response response){
    response.then().assertThat().statusCode(403);
    }

    @Step("Тело ответа при попытке создания зарегистрированного пользователя")
    public void userAlreadyExist(Response response){
    response.then().assertThat().body("success", equalTo(false))
            .and().body("message", equalTo("User already exists"));
    }

    @Step("Тело ответа при незаполнении обязательного поля")
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
        responseBodyUserCreate(originalUser, newUser, token);
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
    public void deleteUser(){
    if(token != null) {
        given()
                .auth()
                .oauth2(token
                        .getAccessToken()
                        .replace("Bearer ", ""))
                .delete(URI.USER)
                .then()
                .statusCode(202);
        System.out.println("Тестовый пользователь успешно удалён.");
    }
    else return;
    }
}