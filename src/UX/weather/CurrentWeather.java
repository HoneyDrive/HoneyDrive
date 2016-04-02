package UX.weather;

import javafx.scene.image.Image;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class CurrentWeather {
    private String icon;
    private long time;
    private double temperature;
    private double humidity;
    private double precipChance;
    private String summary;
    private String timeZone;

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Image getIconId() {
        String iconURL = "images/clear_day.png";

        switch (icon) {
            case "clear-day":
                iconURL = "images/clear_day.png";
                break;
            case "clear-night":
                iconURL = "images/clear_night.png";
                break;
            case "rain":
                iconURL = "images/rain.png";
                break;
            case "snow":
                iconURL = "images/snow.png";
                break;
            case "sleet":
                iconURL = "images/sleet.png";
                break;
            case "wind":
                iconURL = "images/wind.png";
                break;
            case "fog":
                iconURL = "images/fog.png";
                break;
            case "cloudy":
                iconURL = "images/cloudy.png";
                break;
            case "partly-cloudy-day":
                iconURL = "images/partly_cloudy.png";
                break;
            case "partly-cloudy-night":
                iconURL = "images/cloudy_night.png";
                break;
        }
        return new Image(CurrentWeather.class.getResourceAsStream(iconURL));
    }

    public long getTime() {
        return time;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime() * 1000);
        String timeString = formatter.format(dateTime);

        return timeString;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
