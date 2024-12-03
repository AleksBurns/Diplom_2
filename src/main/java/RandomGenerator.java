
import java.util.Random;
import models.User;

public class RandomGenerator {
    public RandomGenerator() {
    }

    public static String randomString(int length) {
        Random random = new Random();
        int leftLimit = 97;
        int rightLimit = 122;
        StringBuilder buffer = new StringBuilder(length);

        for(int i = 0; i < length; ++i) {
            int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (float)(rightLimit - leftLimit + 1));
            buffer.append(Character.toChars(randomLimitedInt));
        }

        return buffer.toString();
    }

    public static User randomUser() {
        return (new User()).setEmail(randomString(8) + "@yandex.ru").setPassword(randomString(10)).setName(randomString(6));
    }

    public static User randomUserData() {
        return new User(randomString(8) + "@yandex.ru", randomString(6));
    }

    public static User randomUserWithoutEmail() {
        return new User((String)null, randomString(10), randomString(6));
    }

    public static User randomUserWithoutPassword() {
        return new User(randomString(8) + "@yandex.ru", (String)null, randomString(6));
    }

    public static User randomUserWithoutName() {
        return new User(randomString(8) + "@yandex.ru", randomString(10), (String)null);
    }

    public static String randomIngredient() {
        return randomString(24);
    }
}