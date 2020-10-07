import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    public static String getWeather(String message, Model model) throws IOException { //Метод

        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=7574ca481c2b6d6203e708852ff85d23");

        JSONObject object = new JSONObject(url.getContent());
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i=0;i<getArray.length();i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon ((String)obj.get("icon"));
            model.setMain ((String)obj.get("main"));
        }

        return "Город: " + model.getName() + "\n" +
                "Температура: " + model.getTemp() + "C" + "\n" +
                "Влажность: " + model.getHumidity() + "%" + "\n" +
                "Main: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/w/" + model.getIcon() + ".png";
    }
}