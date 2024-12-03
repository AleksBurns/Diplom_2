import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Ingredient;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static constants.Routes.BASE_URI;

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

    Order newOrderWithInvalidIng = new Order(List.of(
            new Ingredient(RandomGenerator.randomIngredient()),
            new Ingredient(RandomGenerator.randomIngredient())));

    Order newOrderWithoutIngredients = new Order(List.of());

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
        Order orderWithAll = deserializedIngredients();
        Response createdOrderResponse = createOrderWithAuth(orderWithAll,newUser);
        ensureStatusCode200(createdOrderResponse);
        ensureResponseBodyNewOrder(createdOrderResponse);
    }

    @Test
    @DisplayName("Создание заказа с валидными данными без авторизации")
    @Description("Можно создать заказ с валидными данными без авторизации, код ответа 200 и в теле отображается заказ")
    public void checkCreateOrderWithCorrectIngAndWithoutAuth(){
        Order orderWithAll = deserializedIngredients();
        Response createdOrderResponse = createOrderWithoutAuth(orderWithAll);
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