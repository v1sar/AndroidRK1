package com.example.qwerty.pogoda_movchan;

import android.app.IntentService;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.Weather;
import ru.mail.weather.lib.WeatherStorage;
import ru.mail.weather.lib.WeatherUtils;

public class WeatherService extends IntentService {

    public static final String WEATHER_ERROR_ACTION = "ru.mail.park.WEATHER_ERROR_ACTION";
    public static final String WEATHER_CHANGED_ACTION = "ru.mail.park.WEATHER_CHANGED_ACTION";
    public static final String WEATHER_LOAD_ACTION = "ru.mail.park.WEATHER_LOAD_ACTION";

    public WeatherService() {
        super("Weather");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        if (intent != null & WEATHER_LOAD_ACTION.equals(intent.getAction())) {
            try {
                City city = WeatherStorage.getInstance(this).getCurrentCity();
                Weather weather = WeatherUtils.getInstance().loadWeather(city);
                WeatherStorage.getInstance(this).saveWeather(city, weather);
                broadcastManager.sendBroadcast(new Intent(WEATHER_CHANGED_ACTION));
            } catch (IOException e) {
            broadcastManager.sendBroadcast(new Intent(WEATHER_ERROR_ACTION));
            e.printStackTrace();
            }
        }
    }
}
