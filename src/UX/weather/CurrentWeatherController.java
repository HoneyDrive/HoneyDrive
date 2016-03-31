package UX.weather;


import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class CurrentWeatherController {

    String apiKey = "545e8d825b5251e5b2ba1a3064bf1a52";
    double latitude = 63.417494;
    double longitude = 10.404292;
    String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
            "/" + latitude + "," + longitude;


    OkHttpClient client = new OkHttpClient();

    private String getJSONForecastData() throws IOException {
        Request request = new Request.Builder()
                .url(forecastUrl)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public CurrentWeather getCurrentDetails() throws JSONException, IOException {
        JSONObject forecast = new JSONObject(getJSONForecastData());
        String timezone = forecast.getString("timezone");

        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(fahrenheitToCelsius(currently.getDouble("temperature")));
        currentWeather.setTimeZone(timezone);

        return currentWeather;
    }

    private double fahrenheitToCelsius(double temp) {
        return (temp - 32) * (5.0/9);
    }
}
















