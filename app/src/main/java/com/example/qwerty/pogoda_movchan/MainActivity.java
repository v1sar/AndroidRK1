package com.example.qwerty.pogoda_movchan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.Weather;
import ru.mail.weather.lib.WeatherStorage;
import ru.mail.weather.lib.WeatherUtils;


public class MainActivity extends AppCompatActivity{
    private final static String TAG = MainActivity.class.getSimpleName();
    public TextView text;
    public Button btn_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_city = (Button) findViewById(R.id.select);
        btn_city.setOnClickListener(onClickListener);
        findViewById(R.id.nameCity).setOnClickListener(onDownload);
        findViewById(R.id.startService).setOnClickListener(onServiceStart);
        findViewById(R.id.stopService).setOnClickListener(onServiceStop);
        text = (TextView) findViewById(R.id.nameCity);
        WeatherStorage.getInstance(MainActivity.this);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Intent intent = new Intent(MainActivity.this, SelectActivity.class);
            startActivity(intent);
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.d(TAG, "hello");
            if (intent.getAction().equals(WeatherService.WEATHER_CHANGED_ACTION)) {
                Weather temp = WeatherStorage.getInstance(MainActivity.this).getLastSavedWeather(WeatherStorage.getInstance(MainActivity.this).getCurrentCity());
                text.setText(WeatherStorage.getInstance(MainActivity.this).getCurrentCity().toString()+" "+
                        temp.getTemperature()+" - "+temp.getDescription()); }
            if (intent.getAction().equals(WeatherService.WEATHER_ERROR_ACTION)) {
                text.setText("ERROR");
            }
        }
    };

    private final View.OnClickListener onDownload = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, WeatherService.class);
            intent.setAction("ru.mail.park.WEATHER_LOAD_ACTION");
            startService(intent);
        }
    };



    @Override
    protected void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(WeatherService.WEATHER_CHANGED_ACTION);
        filter.addAction(WeatherService.WEATHER_ERROR_ACTION);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btn_city.setText(WeatherStorage.getInstance(MainActivity.this).getCurrentCity().toString());
        text.setText(withoutNull(WeatherStorage.getInstance(MainActivity.this).getCurrentCity()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private final View.OnClickListener onServiceStart = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, WeatherService.class);
            intent.setAction("ru.mail.park.WEATHER_LOAD_ACTION");
            WeatherUtils.getInstance().schedule(MainActivity.this, intent);
            Toast.makeText(MainActivity.this, "Service started", Toast.LENGTH_LONG).show();
        }
    };

    private final View.OnClickListener onServiceStop = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, WeatherService.class);
            intent.setAction("ru.mail.park.WEATHER_LOAD_ACTION");
            WeatherUtils.getInstance().unschedule(MainActivity.this, intent);
            Toast.makeText(MainActivity.this, "Service stopped", Toast.LENGTH_LONG).show();
        }
    };

    private String withoutNull (City temp) {
        String str = null;
        try { Weather w = WeatherStorage.getInstance(MainActivity.this).getLastSavedWeather(temp);
            if (w.toString().contains("null")) {
                str = WeatherStorage.getInstance(MainActivity.this).getCurrentCity().name() + " " +
                        w.getTemperature() + " - " + w.getDescription();
            return str;
            }
        } catch (Exception e) {}
        return str;
    }
}
