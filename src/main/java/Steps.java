import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;
import models.User;


import static constants.Routes.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class Steps {

    @Step("Создание тестового пользователя")
    public Response createUser(User user) {
        Response response;
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(USER_REGISTER_ROUTE);
        return response;
    }

    @Step("Получение токена тестового пользоватея")
    public void setTokenToCreatedUser(Response response, User user){
        User createdUser = response.as(User.class);
        user.setAccessToken(createdUser.getAccessToken());
    }

    @Step("Удаление тестового пользователя")
    public void deleteUser(User createdUser) {
        if (createdUser == null) return;
        if (createdUser.getAccessToken() != null) {
            given()
                    .auth()
                    .oauth2(createdUser.getAccessToken()
                            .replace("Bearer ", ""))
                    .delete(USER_ROUTE)
                    .then()
                    .statusCode(202);
            System.out.println("Тестовый пользователь успешно удалён.\n");
        }
    }

    @Step("Проверка тела ответа при попытке создания зарегистрированного пользователя")
    public void userAlreadyExistResponse(Response response){
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("User already exists"));
    }

    @Step("Проверка тела ответа при незаполнении обязательного поля")
    public void requiredFieldEmptyResponse(Response response){
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Проверка данных о пользователе в теле ответа с авторизацией")
    public void ensureUserAttributesInResponseBody(Response response, User createdUser){
        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(createdUser.getEmail()))
                .and().body("user.name", equalTo(createdUser.getName()));
    }

    @Step("Логин пользователя")
    public Response userLogin(User createdUser){
        return given()
                .header("Content-type", "application/json")
                .body(createdUser)
                .post(USER_LOGIN_ROUTE);
    }

    @Step("Проверка ошибки в теле ответа при логине с некорректными данными")
    public void incorrectLoginDataResponse(Response response){
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"));
    }

    @Step("Передача новых данных о пользователе с авторизацией")
    public Response setNewUserDataWithAuth(User user, User createdUser){
        Response response;
        response = given()
                .header("Content-type", "application/json")
                .auth()
                .oauth2(createdUser.getAccessToken().replace("Bearer ", ""))
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

    @Step("Проверка тела ответа с ошибкой авторизации")
    public void responseBodyAuthError(Response response){
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }

    @Step("Десериализация списка ингредиентов из базы данных в заказ")
    public Order deserializedIngredients(){
        return given()
                .header("content-type","application/json")
                .get(INGREDIENTS_ROUTE)
                .as(Order.class);
    }

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(Order order, User createdUser){
        return given()
                .header("Content-type", "Application/json")
                .auth().oauth2(createdUser.getAccessToken().replace("Bearer ", ""))
                .body(order.serialize())
                .post(ORDERS_ROUTE);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order){
        return given()
                .header("Content-type", "Application/json")
                .body(order.serialize())
                .post(ORDERS_ROUTE);
    }

    @Step("Проверка тела ответа нового заказа")
    public void ensureResponseBodyNewOrder(Response response) {
        response.then().assertThat()
                .body("success", equalTo(true))
                .and().body("order", notNullValue());
    }

    @Step("Проверка ошибки тела ответа при создании заказа без ингредиентов")
    public void  emptyIngredientsResponse(Response response){
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Запрос заказов пользователя с авторизацией")
    public Response getUserOrdersWithAuth(User createdUser){
        return given()
                .auth().oauth2(createdUser.getAccessToken().replace("Bearer ", ""))
                .get(ORDERS_ROUTE);
    }

    @Step("Проверка тела ответа заказов пользователя")
    public void ensureResponseBodyUserOrders(Response response) {
        response.then().assertThat()
                .body("success", equalTo(true))
                .and().body("orders", notNullValue());
    }

    @Step("Запрос заказов пользователя без авторизации")
    public Response getUserOrdersWithoutAuth(){
        return given()
                .get(ORDERS_ROUTE);
    }

    @Step("Проверка кода ответа 200")
    public void ensureStatusCode200(Response response) {
        response.then().assertThat().statusCode(200);
    }
    @Step("Проверка статус кода 400")
    public void ensureStatusCode400(Response response) {
        response.then().assertThat().statusCode(400);
    }
    @Step("Проверка статус кода 401")
    public void ensureStatusCode401(Response response) {
        response.then().assertThat().statusCode(401);
    }
    @Step("Проверка статус кода 403")
    public void ensureStatusCode403(Response response) {
        response.then().assertThat().statusCode(403);
    }
    @Step("Проверка статус кода 500")
    public void ensureStatusCode500(Response response) {
        response.then().assertThat().statusCode(500);
    }
}
