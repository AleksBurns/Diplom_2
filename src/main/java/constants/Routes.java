package constants;

public interface Routes {
    String BASE_URI = "https://stellarburgers.nomoreparties.site";
    String USER_REGISTER_ROUTE = "/api/auth/register";
    String USER_LOGIN_ROUTE = "/api/auth/login";
    String USER_ROUTE = "/api/auth/user";
    String INGREDIENTS_ROUTE = "/api/ingredients";
    /*
     * GET USER - Получение данных о зарегистрированном пользователе (Нужна авторизация)
     * PATCH USER - Изменение имейла и имени пользователя (Нужна авторизация)
     * DELETE USER - Удаление пользователя (Нужна авторизация)
     */
    String ORDERS_ROUTE = "/api/orders";
    /*
     * POST ORDERS - Создание заказа
     * GET ORDERS - Получение заказа конкретного пользователя (Нужна авторизация)
     */
}
