package com.hkxps17.turnup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LikedEventsActivity extends AppCompatActivity {

    ListView listView;
    ImageButton search;
    ImageButton accountButton;
    String emailID = "";
    List<String> leColor = new ArrayList<>();

    ArrayList<String> EventTitles = new ArrayList<>();
    ArrayList<String> EventLocations = new ArrayList<>();
    ArrayList<String> EventDates = new ArrayList<>();
    ArrayList<String> EventRatings = new ArrayList<>();
    ArrayList<String> EventCategories = new ArrayList<>();
    ArrayList<String> EventDescriptions = new ArrayList<>();
    ArrayList<String> EventImages = new ArrayList<>();
    ArrayList<String> likedBy = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_events);

        Set<String> retS = PreferenceManager.getDefaultSharedPreferences(LikedEventsActivity.this)
                .getStringSet("id", new HashSet<String>());
        List<String> retL = new ArrayList<String>(retS);
        emailID = retL.get(0);

        Gson gson = new Gson();

        String ColorSet = PreferenceManager.getDefaultSharedPreferences(LikedEventsActivity.this)
                .getString("leColor", null);
        if (ColorSet != null) {
            leColor = Arrays.asList(gson.fromJson(ColorSet, String[].class));
        }

        accountButton = findViewById(R.id.account_button);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acc = new Intent(LikedEventsActivity.this, UserActivity.class);
                startActivity(acc);
            }
        });

        listView = findViewById(R.id.likedListView);

        //Get LikedEvents
        String getAllLikedEventsApiUrl = "http://20.122.91.139:8081/likedevents/"+emailID;
        StringRequest eventsReq = new StringRequest(Request.Method.GET, getAllLikedEventsApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonEventsArray = new JSONArray(response);

                    for(int i = 0; i<jsonEventsArray.length(); i++) {
                        JSONObject jsonEventObj = jsonEventsArray.getJSONObject(i);
                        EventTitles.add(jsonEventObj.getString("title"));
                        EventLocations.add(jsonEventObj.getString("location"));
                        EventDates.add(jsonEventObj.getString("date"));
                        EventDescriptions.add(jsonEventObj.getString("description"));
                        EventImages.add(jsonEventObj.getString("photoURL"));
                        EventRatings.add(jsonEventObj.getString("rating"));
                        EventCategories.add(jsonEventObj.getString("category"));
                        likedBy.add(jsonEventObj.getString("likedBy"));
                    }
                    LikedEventAdapter eventAdapter = new LikedEventAdapter(LikedEventsActivity.this, EventTitles.toArray(new String[0]), EventLocations.toArray(new String[0]), EventDates.toArray(new String[0]));
                    listView.setAdapter(eventAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LikedEventsActivity.this, "Error Loading Event Data", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(LikedEventsActivity.this, "Volley Error", Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(LikedEventsActivity.this);
        queue.add(eventsReq);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(LikedEventsActivity.this, ChatActivity.class);
                i.putExtra("EventTitle", EventTitles.get(position));
                startActivity(i);
            }
        });

        search = findViewById(R.id.like_search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent list = new Intent(LikedEventsActivity.this, EventListActivity.class);
                startActivity(list);
            }
        });
    }

    public class LikedEventAdapter extends ArrayAdapter<String> {
        Context context;
        String[] eTitles;
        String[] eLocations;
        String[] eDates;

        LikedEventAdapter(Context c, String[] titles, String[] locations, String[] dates) {
            super(c, R.layout.liked_event_item, R.id.EventTitle, titles);
            this.context = c;
            this.eTitles = titles;
            this.eDates = dates;
            this.eLocations = locations;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View eventItem = layoutInflater.inflate(R.layout.liked_event_item, parent, false);
            TextView title = eventItem.findViewById(R.id.EventTitle);
            TextView date = eventItem.findViewById(R.id.EventDate);
            TextView loc = eventItem.findViewById(R.id.EventLocation);
            View res = eventItem.findViewById(R.id.liked_event_background);
            title.setText(eTitles[position]);
            date.setText((eDates[position]));
            loc.setText(eLocations[position]);

            if (!(leColor.isEmpty())) {
                int[] color = new int[2];
                color[0] = Integer.parseInt(leColor.get(0));
                color[1] = Integer.parseInt(leColor.get(1));
                if (res != null) {
                    Drawable background = res.getBackground();
                    GradientDrawable gradientDrawable = (GradientDrawable) background;
                    gradientDrawable.setColors(color);
                    gradientDrawable.setOrientation(GradientDrawable.Orientation.BL_TR);
                }
            }

            return eventItem;
        }

    }
}