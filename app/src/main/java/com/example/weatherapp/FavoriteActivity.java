package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.weatherapp.models.WeatherData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FavoriteActivity extends AppCompatActivity {

    private String cityForMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FloatingActionButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this::onClick);


        WeatherData weatherData = (WeatherData) getIntent().getSerializableExtra("cityWeather");

        cityForMain = weatherData.getLocation().getName();



    }

    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("cityName", cityForMain);
        startActivity(intent);
    }
}