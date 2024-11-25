package constants;

public interface IApiRoutes {
    String BASE_URI = "https://stellarburgers.nomoreparties.site";
    String USER_REGISTER_ROUTE = "/api/auth/register";
    String USER_ROUTE = "/api/auth/user";
    /**
     * GET USER- Получение данных о зарегистрированном пользователе (Нужна авторизация)
     * PATCH USER- Изменение имейла и имени пользователя (Нужна авторизация)
     * DELETE USER- Удаление пользователя (Нужна авторизация)
     */
    String ORDERS_ROUTE = "/api/orders";
}