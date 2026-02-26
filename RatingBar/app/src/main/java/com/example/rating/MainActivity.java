package com.example.rating;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ratingBar = findViewById(R.id.rating);
        button = findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int no_of_stars = ratingBar.getNumStars();
                Toast.makeText(MainActivity.this, "Num of Star " + no_of_stars, Toast.LENGTH_SHORT).show();
                float f = ratingBar.getNumStars();
                Toast.makeText(MainActivity.this, "Rating is " + f, Toast.LENGTH_SHORT).show();

            }
        });

    }
}