package com.hkxps17.turnup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hkxps17.turnup.databinding.ActivityEventBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EventActivity extends AppCompatActivity {

    ActivityEventBinding binding;
    ImageButton eventLike;
    Boolean isLiked = false;
    String emailID = "";
    String eventTitle;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Set<String> retS = PreferenceManager.getDefaultSharedPreferences(EventActivity.this)
                .getStringSet("id", new HashSet<String>());
        List<String> retL = new ArrayList<String>(retS);
        emailID = retL.get(0);


        Intent intent = this.getIntent();

        if (intent != null) {
            eventTitle = intent.getStringExtra("EventTitle");
            String eventLocations = intent.getStringExtra("EventLocation");
            String eventDate = intent.getStringExtra("EventDate");
            String eventDescription = intent.getStringExtra("EventDescription");
            String eventImages = intent.getStringExtra("EventImage");
            String eventRating = intent.getStringExtra("EventRating");
            String eventCategory = intent.getStringExtra("EventCategory");
            if (!emailID.equals("Guest")) {
                isLiked = intent.getBooleanExtra("isLiked", false);
            }


            binding.EventTitle.setText(eventTitle);
            binding.EventLocation.setText(eventLocations);
            binding.EventDate.setText(eventDate);
            binding.EventDescription.setText(eventDescription);
            Picasso.get().load(eventImages).into(binding.EventImage);
            binding.EventCategory.setText(eventCategory);
            binding.eventRating.setRating(Float.parseFloat(eventRating));
        }

        eventLike = findViewById(R.id.event_like_button);
        if (isLiked) {
            eventLike.setImageResource(R.drawable.ic_liked);
            flag = 1;
        }

        eventLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventLike.setSelected(!eventLike.isPressed());

                if (Objects.equals(emailID, "Guest")) {
                    Toast.makeText(EventActivity.this, "Please Log In!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (eventLike.isPressed() && flag == 0) {
                        eventLike.setImageResource(R.drawable.ic_liked);
                        String addLikedEventPostAPI = "http://20.122.91.139:8081/eventliked/"+eventTitle +"/"+emailID;
                        JsonObjectRequest eventsReq = new JsonObjectRequest(Request.Method.POST, addLikedEventPostAPI, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //handle POST response
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(EventActivity.this, "Event Liked!", Toast.LENGTH_LONG).show();
                            }});
                        RequestQueue queue = Volley.newRequestQueue(EventActivity.this);
                        queue.add(eventsReq);
                    } else {
                        eventLike.setImageResource(R.drawable.ic_like);
                    }
                }
            }
        });
    }

    public void postLikedEvent() {

    }
}