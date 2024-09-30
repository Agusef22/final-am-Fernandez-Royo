package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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

        Button favoriteButton = findViewById(R.id.button);
        favoriteButton.setOnClickListener(this::onClick);

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

    }

    public void onClick(View view) {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }


}