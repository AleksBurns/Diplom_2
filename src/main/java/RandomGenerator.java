import models.User;

import java.util.Random;

public class RandomGenerator {

    public static String randomString(int length) {
        Random random = new Random();
        int leftLimit = 97;
        int rightLimit = 122;
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (float) (rightLimit - leftLimit + 1));
            buffer.append(Character.toChars(randomLimitedInt));
        }

        return buffer.toString();
    }

    public static User randomUser() {
        return new User()
                .setEmail(randomString(8) + "@yandex.ru")
                .setPassword(randomString(10))
                .setName(randomString(6));
    }
    public static User randomUserData() {
        return new User()
                .setEmail(randomString(8) + "@yandex.ru")
                .setName(randomString(6));
    }

    public static User randomUserWithoutEmail() {
        return new User(
                null,
                RandomGenerator.randomString(10),
                RandomGenerator.randomString(6));
    }
    public static User randomUserWithoutPassword() {
        return new User(
                RandomGenerator.randomString(8) + "@yandex.ru",
                null,
                RandomGenerator.randomString(6));
    }
    public static User randomUserWithoutName() {
        return new User(
                RandomGenerator.randomString(8) + "@yandex.ru",
                RandomGenerator.randomString(10),
                null);
    }
}