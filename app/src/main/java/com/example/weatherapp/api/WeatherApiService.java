package com.example.weatherapp.api;


import com.example.weatherapp.models.WeatherData;

import retrofit2.Call;
import retrofit2.http.Query;

public interface WeatherApiService {
    Call<WeatherData> getWeather(@Query("key") String apiKey,
                                 @Query("q") String city );
}
