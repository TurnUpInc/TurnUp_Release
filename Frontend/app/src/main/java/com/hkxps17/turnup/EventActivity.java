package com.hkxps17.turnup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.hkxps17.turnup.databinding.ActivityEventBinding;
import com.squareup.picasso.Picasso;

public class EventActivity extends AppCompatActivity {

    ActivityEventBinding binding;
    ImageButton eventLike;
    TextView category;
    RatingBar rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if (intent != null) {
            String eventTitle = intent.getStringExtra("EventTitles");
            String eventLocations = intent.getStringExtra("EventLocations");
            String eventDate = intent.getStringExtra("EventDate");
            String eventDescription = intent.getStringExtra("EventDescription");
            String eventImages = intent.getStringExtra("EventImages");

            binding.EventTitle.setText(eventTitle);
            binding.EventLocation.setText(eventLocations);
            binding.EventDate.setText(eventDate);
            binding.EventDescription.setText(eventDescription);
            Picasso.get().load(eventImages).into(binding.EventImage);
            binding.EventCategory.setText("Adventure");
        }

        rating = findViewById(R.id.event_rating);
        rating.setRating(4.5F);

        eventLike = findViewById(R.id.event_like_button);
        eventLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventLike.setSelected(!eventLike.isPressed());

                if (eventLike.isPressed()) {
                    eventLike.setImageResource(R.drawable.ic_liked);
                }
                else {
                    eventLike.setImageResource(R.drawable.ic_like);
                }
            }
        });


    }
}