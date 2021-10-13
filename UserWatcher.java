import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserWatcher {
    // Пользователь
    private User m_user;
    // Интервал запроса статуса
    private int m_interval;
    // Путь к файлу логирования
    private String m_logDir;
    // Путь к файлу логирования
    private String m_token;

    public UserWatcher(User _user, int _interval, String _logDir, String _token) {
        this.m_user = _user;
        this.m_interval = _interval;
        this.m_logDir = _logDir;
        this.m_token = _token;
    }

    public static void watch(String _token, User _user, int _interval, String _logDir) {
        Thread run = new Thread(new Runnable() {
            @Override
            public void run() {
                int minutesOnline = 0;

                while (true) {
                    try {
                        final String urlRequest = "https://api.vk.com/method/users.get?user_id=" + _user.id() + "&fields=online&v=5.131&access_token=" + _token;
                        URL url = new URL(urlRequest);

                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");

                        BufferedReader inBufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = inBufferReader.readLine()) != null) {
                            response.append(inputLine);
                        }
                        inBufferReader.close();

                        JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(response.toString());
                        JSONArray userInfoArray = (JSONArray) jsonObject.get("response");
                        // По данному запросу всегда будет 1 элемент, поэтому опасно достаем его
                        JSONObject userData = (JSONObject) userInfoArray.get(0);

                        Date dateNow = new Date();
                        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

                        if (_user.hasName() == false) {
                            _user.setName(userData.get("first_name").toString() + " " + userData.get("last_name").toString());
                        }

                        boolean isOnline = (long)userData.get("online") == 1;
                        if (isOnline == true) {

                            if (minutesOnline == 0) {
                                System.out.println(_user.name() + ":" + "has connected " + formatForDateNow.format(dateNow));
                            }
                            else {
                                System.out.println(_user.name() + ":" + "is online " + minutesOnline + " minutes");
                            }

                            minutesOnline += 1;
                        }
                        else {
                            System.out.println(_user.name() + ":" + "has disconnected " + formatForDateNow.format(dateNow));
                            minutesOnline = 0;
                        }

                        Thread.sleep(1000 * _interval);
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        run.start();
    }
}
