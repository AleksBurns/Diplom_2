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

import static constants.Routes.*;

/**
 * Получение заказов конкретного пользователя:
 * авторизованный пользователь,
 * неавторизованный пользователь.
 */

public class GetUserOrderTests extends Steps {
    private List<Ingredient> validIngredients;
    User newUser = RandomGenerator.randomUser();
    Order newOrder = new Order(validIngredients);

    @Before
    public void setUp(){
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Тест: Получение заказов авторизованного пользователя")
    @Description("Можно получить заказы авторизованного пользователя, код ответа 200 и в теле есть список заказов")
    public void checkGetUserOrdersWithAuth(){
        Response createdUserResponse = createUser(newUser);
        setTokenToCreatedUser(createdUserResponse, newUser);
        createOrderWithAuth(newOrder, newUser);
        Response userOrdersResponse = getUserOrdersWithAuth(newUser);
        ensureStatusCode200(userOrdersResponse);
        ensureResponseBodyUserOrders(userOrdersResponse);
    }

    @Test
    @DisplayName("Тест: Получение заказа пользователя без авторизации")
    @Description("Нельзя получить заказы пользователя без авторизации, код ответа 401 и в теле ответа ошибка авторизации")
    public void checkGetUserOrdersWithoutAuth(){
        Response userOrdersResponse = getUserOrdersWithoutAuth();
        ensureStatusCode401(userOrdersResponse);
        responseBodyAuthError(userOrdersResponse);
    }

    @After
    @DisplayName("Удаление тестового пользователя")
    @Description("Тестовый пользователь должен быть удалён после теста")
    public void deleteUserAfterTest(){
        deleteUser(newUser);
    }
}