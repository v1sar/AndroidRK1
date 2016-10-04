package com.example.qwerty.pogoda_movchan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import ru.mail.weather.lib.City;
import ru.mail.weather.lib.WeatherStorage;

/**
 * Created by qwerty on 29.09.16.
 */

public class SelectActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        findViewById(R.id.c1).setOnClickListener(this);
        findViewById(R.id.c2).setOnClickListener(this);
        findViewById(R.id.c3).setOnClickListener(this);
        findViewById(R.id.c4).setOnClickListener(this);
        findViewById(R.id.c5).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.c1:
                WeatherStorage.getInstance(SelectActivity.this).setCurrentCity(City.VICE_CITY);
                break;
            case R.id.c2:
                WeatherStorage.getInstance(SelectActivity.this).setCurrentCity(City.RACCOON_CITY);
                break;
            case R.id.c3:
                WeatherStorage.getInstance(SelectActivity.this).setCurrentCity(City.SILENT_HILL);
                break;
            case R.id.c4:
                WeatherStorage.getInstance(SelectActivity.this).setCurrentCity(City.SOUTH_PARK);
                break;
            case R.id.c5:
                WeatherStorage.getInstance(SelectActivity.this).setCurrentCity(City.SPRINGFIELD);
                break;
        }
        finish();
    }

}
