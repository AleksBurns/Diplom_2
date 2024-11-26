import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static constants.IApiRoutes.BASE_URI;
/**
 * Создание заказа:
 * с авторизацией,
 * без авторизации,
 * с ингредиентами,
 * без ингредиентов,
 * с неверным хешем ингредиентов.
 */

public class CreateOrderTests extends Steps {
    User newUser = RandomGenerator.randomUser();
    private final List<String> validIngredients = List.of("61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa6e");
    private final List<String> invalidIngredients = List.of(RandomGenerator.randomString(24), RandomGenerator.randomString(24));
    private final List<String> emptyIngredients = List.of();

    Order newOrderWithValidIng = new Order(validIngredients);
    Order newOrderWithInvalidIng = new Order(invalidIngredients);
    Order newOrderWithoutIngredients = new Order(emptyIngredients);

    @Before
    public void setUp(){
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Создание заказа с валидными данными и авторизацией")
    @Description("Можно создать заказ с валидными данными и авторизацией, код ответа 200 и в теле отображается заказ")
    public void checkCreateOrderWithCorrectIngAndWithAuth() {
        Response createdUserResponse = createUser(newUser);
        setTokenToCreatedUser(createdUserResponse, newUser);
        Response createdOrderResponse = createOrderWithAuth(newOrderWithValidIng,newUser);
        ensureStatusCode200(createdOrderResponse);
        ensureResponseBodyNewOrder(createdOrderResponse);
    }

    @Test
    @DisplayName("Создание заказа с валидными данными без авторизации")
    @Description("Можно создать заказ с валидными данными без авторизации, код ответа 200 и в теле отображается заказ")
    public void checkCreateOrderWithCorrectIngAndWithoutAuth(){
        Response createdOrderResponse = createOrderWithoutAuth(newOrderWithValidIng);
        ensureStatusCode200(createdOrderResponse);
        ensureResponseBodyNewOrder(createdOrderResponse);
    }

    @Test
    @DisplayName("Создание заказа с невалидными данными и авторизацией")
    @Description("Нельзя создать заказ с невалидными данными и авторизацией, код ответа 500")
    public void checkCreateOrderWithoutCorrectIngAndWithAuth(){
        Response createdUserResponse = createUser(newUser);
        setTokenToCreatedUser(createdUserResponse, newUser);
        Response createdOrderResponse = createOrderWithAuth(newOrderWithInvalidIng, newUser);
        ensureStatusCode500(createdOrderResponse);
    }

    @Test
    @DisplayName("Создание заказа с невалидными данными без авторизации")
    @Description("Нельзя создать заказ с невалидными данными и  без авторизации, код ответа 500")
    public void checkCreateOrderWithoutCorrectIngAndWithoutAuth(){
        Response createdOrderResponse = createOrderWithoutAuth(newOrderWithInvalidIng);
        ensureStatusCode500(createdOrderResponse);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов и с вторизацией")
    @Description("Нельзя создать заказ с авторизацией, но без ингредиентов, код ответа 400, в теле ответа ошибка: Ingredient ids must be provided")
    public void checkCreateOrderWithoutIngAndWithAuth(){
        Response createdUserResponse = createUser(newUser);
        setTokenToCreatedUser(createdUserResponse, newUser);
        Response createdOrderResponse = createOrderWithAuth(newOrderWithoutIngredients, newUser);
        ensureStatusCode400(createdOrderResponse);
        emptyIngredientsResponse(createdOrderResponse);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов и без авторизации")
    @Description("Нельзя создать заказ без авторизации и ингредиентов, код ответа 400, в теле ответа ошибка: Ingredient ids must be provided")
    public void checkCreateOrderWithoutIngAndWithoutAuth(){
        Response createdOrderResponse = createOrderWithoutAuth(newOrderWithoutIngredients);
        ensureStatusCode400(createdOrderResponse);
        emptyIngredientsResponse(createdOrderResponse);
    }

    @After
    @DisplayName("Удаление тестового пользователя")
    @Description("Тестовый пользователь должен быть удалён после теста")
    public void deleteUserAfterTest(){
        deleteUser(newUser);
    }
}