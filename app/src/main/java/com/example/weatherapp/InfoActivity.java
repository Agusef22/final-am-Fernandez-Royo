package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.weatherapp.models.WeatherData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InfoActivity extends AppCompatActivity {

    private TextView cityName;
    private String cityForMain;
    private TextView cityRegion;
    private TextView cityCountry;
    private TextView cityLocalTime;
    private TextView cityTemp;
    private TextView cityCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this::onClick);

        cityName = findViewById(R.id.textView14);
        cityRegion = findViewById(R.id.textView15);
        cityCountry = findViewById(R.id.textView16);
        cityLocalTime = findViewById(R.id.textView17);
        cityTemp = findViewById(R.id.textView18);
        cityCondition = findViewById(R.id.textView19);

        WeatherData weatherData = (WeatherData) getIntent().getSerializableExtra("cityWeather");

        cityForMain = weatherData.getLocation().getName();

        if (weatherData != null) {
            cityName.setText(weatherData.getLocation().getName());
            cityTemp.setText(String.format("%s°C", weatherData.getCurrent().getTemp_c()));
            cityCondition.setText(weatherData.getCurrent().getCondition().getText());
            cityRegion.setText(weatherData.getLocation().getRegion());
            cityCountry.setText(weatherData.getLocation().getCountry());
            cityLocalTime.setText(weatherData.getLocation().getLocaltime());
        } else {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("cityName", cityForMain);
        startActivity(intent);
    }
}