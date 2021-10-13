// {"response":
// [{"first_name":"Пашка","id":280720726,"last_name":"Белый","online":0}]}

public class Main {
    public static void main(String[] args) {
        //TODO Get user name from api
        User user = new User("108370272", "Александр Дёшин");

        // Интервал запроса статуса пользователя (в сек)
        final int INTERVAL = 60;
        // Токен для работы с API
        final String TOKEN = "8d6cedc41f20adb9c301d57fcd678a126286db8fda0f2569b3b07233cc88e12a8b8ad663c78b47386c75d";

        try {
            UserWatcher.watch(TOKEN, user, INTERVAL, "");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
