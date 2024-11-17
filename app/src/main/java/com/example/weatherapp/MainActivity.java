package com.example.weatherapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.weatherapp.api.WeatherApiClient;
import com.example.weatherapp.api.WeatherApiService;
import com.example.weatherapp.models.WeatherData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private WeatherApiService weatherApiService;

    private TextView cityName;
    private TextView cityTemp;
    private ImageView imageCondition;
    private TextView cityCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        weatherApiService = WeatherApiClient.getApiClient().create(WeatherApiService.class);
        cityCondition = findViewById(R.id.textView);
        cityName = findViewById(R.id.textView2);
        cityTemp = findViewById(R.id.textView3);
        imageCondition = findViewById(R.id.imageView);

        Button favoriteButton = findViewById(R.id.button);
        favoriteButton.setOnClickListener(this::onClick);

        Button InfoButton = findViewById(R.id.button11);
        InfoButton.setOnClickListener(this::onClick);


        LinearLayout parentlayout = findViewById(R.id.parentLayout);

        SearchView searchView = new SearchView(this);
        LinearLayout.LayoutParams searchParams = new LinearLayout.LayoutParams(
                700,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        searchView.setLayoutParams(searchParams);
        searchView.setQueryHint("Search for any location");
        searchView.setBackgroundResource(R.drawable.rounded_search_view);

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                105,
                105
        );
        imageParams.gravity = Gravity.CENTER_VERTICAL;
        imageParams.setMargins(25,0,0,0);
        imageView.setLayoutParams(imageParams);
        imageView.setImageResource(android.R.drawable.btn_star);

        parentlayout.addView(searchView);
        parentlayout.addView(imageView);

        imageCondition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ObjectAnimator rotate = ObjectAnimator.ofFloat(imageCondition, "rotation", 0f, 360f);
                rotate.setDuration(500);
                rotate.start();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            public boolean onQueryTextSubmit(String query) {
                fetchWeatherData(query);
                return true;
            }

            public boolean onQueryTextChange(String newText){
                return  false;
            }
        });

    }

    private void fetchWeatherData(String city) {
        String apikey = getString(R.string.api_key);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        imageCondition.setVisibility(View.GONE);
        cityTemp.setVisibility(View.GONE);
        cityName.setVisibility(View.GONE);
        cityCondition.setVisibility(View.GONE);

        Call<WeatherData> call = weatherApiService.getWeather(apikey,city);

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                progressBar.setVisibility(View.GONE);
                imageCondition.setVisibility(View.VISIBLE);
                cityTemp.setVisibility(View.VISIBLE);
                cityName.setVisibility(View.VISIBLE);
                cityCondition.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "city not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                imageCondition.setVisibility(View.VISIBLE);
                cityTemp.setVisibility(View.VISIBLE);
                cityName.setVisibility(View.VISIBLE);
                cityCondition.setVisibility(View.VISIBLE);

                Log.e("Mainactivity", "Error al obtener datos: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error al obtener datos",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(WeatherData weatherData) {
        cityName.setText(weatherData.getLocation().getName());
        cityTemp.setText(String.format("%s°C", weatherData.getCurrent().getTemp_c()));
        cityCondition.setText(weatherData.getCurrent().getCondition().getText());

        String iconURL = "https:" + weatherData.getCurrent().getCondition().getIcon();
        Glide.with(this).load(iconURL).into(imageCondition);
    }

    public void onClick(View view) {
        Intent intent;

        if(view.getId() == R.id.button) {
            intent = new Intent(this, FavoriteActivity.class);
            startActivity(intent);
        }

        if (view.getId() == R.id.button11) {
            intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        }

    }


}