package UX.weather;

import javafx.scene.image.Image;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class CurrentWeather {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public Image getIconId() {
        String iconURL = "images/clear_day.png";

        switch (mIcon) {
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
        return mTime;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime() * 1000);
        String timeString = formatter.format(dateTime);

        return timeString;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public double getPrecipChance() {
        return mPrecipChance;
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    @Override
    public String toString() {
        return getFormattedTime();
    }
}
