package com.kmd.uog.logbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageViewActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button btnPrevious;
    private Button btnNext;

    private int[] imageResources = {
            R.drawable.catcute,
            R.drawable.corgi,

            // Add more image resources here
    };

    private int currentImageIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        imageView = findViewById(R.id.imageView);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);

        //Initially, display the first image
        imageView.setImageResource(imageResources[currentImageIndex]);

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreviousImage();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextImage();
            }
        });
    }
    private void showPreviousImage()
    {
        if(currentImageIndex >0)
        {
            currentImageIndex--;
            imageView.setImageResource(imageResources[currentImageIndex]);

        }

    }

    private void showNextImage()
    {
        if(currentImageIndex < imageResources.length - 1)
        {
            currentImageIndex++;
            imageView.setImageResource(imageResources[currentImageIndex]);
        }
    }

}



