package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.weatherapp.models.WeatherData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity {

    private String cityForMain;
    private LinearLayout favoritesContainer;
    private FirebaseFirestore firestore;


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



        if (weatherData != null) {
            cityForMain = weatherData.getLocation().getName();
        }

        favoritesContainer = findViewById(R.id.favoritesContainer);
        firestore = FirebaseFirestore.getInstance();

        loadFavorites();



    }

    private void loadFavorites() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        firestore.collection("favoriteCity")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Map<String, Object>> favoriteCities = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        favoriteCities.add(document.getData());
                    }
                    populateFavorites(favoriteCities);
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading favorites", Toast.LENGTH_SHORT).show();
                });
    }

    private void populateFavorites(List<Map<String, Object>> favoriteCities) {
        favoritesContainer.removeAllViews();

        for (Map<String, Object> city : favoriteCities) {
            String cityName = city.get("name").toString();
            String temperature = city.get("temperature").toString();
            String condition = city.get("condition").toString();


            LinearLayout cityLayout = new LinearLayout(this);
            cityLayout.setOrientation(LinearLayout.HORIZONTAL);
            cityLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            cityLayout.setPadding(10, 20, 10, 20);
            cityLayout.setGravity(Gravity.CENTER_VERTICAL);


            TextView cityNameView = new TextView(this);
            cityNameView.setLayoutParams(new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            cityNameView.setText(cityName);
            cityNameView.setGravity(Gravity.CENTER_VERTICAL);
            cityNameView.setTextSize(15);
            cityNameView.setPadding(10, 0, 10, 0);


            TextView tempView = new TextView(this);
            tempView.setLayoutParams(new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            tempView.setText(String.format("%s°C - %s", temperature, condition));
            tempView.setGravity(Gravity.CENTER_VERTICAL);
            tempView.setTextSize(15);


            ImageButton favoriteButton = new ImageButton(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1
            );

            favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
            favoriteButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            favoriteButton.setOnClickListener(v -> {
                if (cityName != null) {
                    firestore.collection("favoriteCity")
                            .whereEqualTo("name", cityName)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    if (!task.getResult().isEmpty()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            firestore.collection("favoriteCity")
                                                    .document(document.getId())
                                                    .delete()
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(this, cityName + " removed from favorites.", Toast.LENGTH_SHORT).show();
                                                        loadFavorites();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(this, "Failed to remove " + cityName, Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(this, cityName + " is not in favorites.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(this, "Error fetching favorites.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(this, "City name is null.", Toast.LENGTH_SHORT).show();
                }
            });


            cityLayout.addView(cityNameView);
            cityLayout.addView(tempView);
            cityLayout.addView(favoriteButton);

            favoritesContainer.addView(cityLayout);
        }
    }


    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        if (cityForMain != null) {
            intent.putExtra("cityName", cityForMain);
        }
        startActivity(intent);
    }
}