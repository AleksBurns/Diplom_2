package constants;

public interface URI {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    public static final String USER_REGISTER = "/api/auth/register";
    public static final String USER = "/api/auth/user";
    /**
     * GET USER- Получение данных о зарегистрированном пользователе (Нужна авторизация)
     * PATCH USER- Изменение имейла и имени пользователя (Нужна авторизация)
     * DELETE USER- Удаление пользователя (Нужна авторизация)
     */
    public static final String ORDERS = "/api/orders";
}
