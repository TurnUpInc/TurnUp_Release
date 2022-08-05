package com.hkxps17.turnup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EventListActivity extends AppCompatActivity {

    int trigger = 1;

    AppCompatRadioButton rbLeft;
    AppCompatRadioButton rbRight;
    Chip byRating;
    Chip adventure;
    Chip club;
    Chip leisure;
    Chip sports;
    ListView listView;
    ImageButton likedEvents;
    ImageButton accountButton;
    ImageButton add;
    ImageButton filter;
    String emailID = "";
    List<String> elColor = new ArrayList<>();

    ArrayList<String> EventTitles = new ArrayList<>();
    ArrayList<String> EventLocations = new ArrayList<>();
    ArrayList<String> EventDates = new ArrayList<>();
    ArrayList<String> EventRatings = new ArrayList<>();
    ArrayList<String> EventCategories = new ArrayList<>();
    ArrayList<String> EventDescriptions = new ArrayList<>();
    ArrayList<String> EventImages = new ArrayList<>();
    ArrayList<String> EventCoordinates = new ArrayList<>();
    ArrayList<String> likedBy = new ArrayList<>();
    ArrayList<String> LocationCoordinates = new ArrayList<>();
    ArrayList<String> LocationTitles = new ArrayList<>();
    ArrayList<String> LocationLikes = new ArrayList<>();
    ArrayList<String> LocationVisits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Set<String> retS = PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .getStringSet("id", new HashSet<String>());
        List<String> retL = new ArrayList<String>(retS);
        emailID = retL.get(0);

        Gson gson = new Gson();

        String ColorSet = PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .getString("elColor", null);
        if (ColorSet != null) {
            elColor = Arrays.asList(gson.fromJson(ColorSet, String[].class));
        }


        rbLeft = findViewById(R.id.rbleft);
        rbRight = findViewById(R.id.rbright);

        listView = findViewById(R.id.listview);

        byRating = findViewById(R.id.chip1);
        adventure = findViewById(R.id.chip2);
        leisure = findViewById(R.id.chip3);
        sports = findViewById(R.id.chip4);
        club = findViewById(R.id.chip5);

        filter = findViewById(R.id.search_button);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trigger == 1) findViewById(R.id.chipGroupFilter).setVisibility(View.VISIBLE);
                else findViewById(R.id.chipGroupFilter).setVisibility(View.INVISIBLE);
                trigger *= -1;
            }
        });

        likedEvents = findViewById(R.id.liked_button);
        likedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailID.equals("Guest")) {
                    Intent likedList = new Intent(EventListActivity.this, LikedEventsActivity.class);
                    startActivity(likedList);
                }
                else {
                    Toast.makeText(EventListActivity.this, "Please Log In!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        accountButton = findViewById(R.id.account_button);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acc = new Intent(EventListActivity.this, UserActivity.class);
                startActivity(acc);
            }
        });

        add = findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Objects.equals(emailID, "Guest")) {
                    Intent add = new Intent(EventListActivity.this, AddEventActivity.class);
                    startActivity(add);
                }
                else {
                    Toast.makeText(EventListActivity.this, "Please Log In!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        StringRequest locationsReq = getLocationsApiCall();
        RequestQueue locs = Volley.newRequestQueue(EventListActivity.this);
        locs.add(locationsReq);

        if (byRating.isChecked()){
            StringRequest eventsReq = getAllEevntsApiCall();
            RequestQueue queue = Volley.newRequestQueue(EventListActivity.this);
            queue.add(eventsReq);
        }

        byRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest eventsReq = getAllEventsByRatingApiCall();
                RequestQueue queue = Volley.newRequestQueue(EventListActivity.this);
                queue.add(eventsReq);
            }
        });

        club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest eventsReq = getAllEventsClubApiCall();
                RequestQueue queue = Volley.newRequestQueue(EventListActivity.this);
                queue.add(eventsReq);
            }
        });

        adventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest eventsReq = getAllEventsAdventureApiCall();
                RequestQueue queue = Volley.newRequestQueue(EventListActivity.this);
                queue.add(eventsReq);
            }
        });

        leisure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest eventsReq = getAllEventsLeisureApiCall();
                RequestQueue queue = Volley.newRequestQueue(EventListActivity.this);
                queue.add(eventsReq);
            }
        });

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest eventsReq = getAllEventsSportsApiCall();
                RequestQueue queue = Volley.newRequestQueue(EventListActivity.this);
                queue.add(eventsReq);
            }
        });
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                putEventIntent(position);
            }
        });
    }

    private StringRequest getLocationsApiCall() {
        String getAllLocationsApiUrl = "http://20.122.91.139:8081/locations";
        StringRequest locationsReq = new StringRequest(Request.Method.GET, getAllLocationsApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    OnLocAPIResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EventListActivity.this, "Loading Markers!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(EventListActivity.this, "Volley Error", Toast.LENGTH_LONG).show();
            }
        });

        return locationsReq;
    }

    private StringRequest getAllEevntsApiCall() {
        String getAllEventsApiUrl = "http://20.122.91.139:8081/events";
        StringRequest eventsReq = new StringRequest(Request.Method.GET, getAllEventsApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    OnAPIResponse(response);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("hk", e.toString());
                    Toast.makeText(EventListActivity.this, "Error Loading Event Data", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(EventListActivity.this, "Loading...", Toast.LENGTH_LONG).show();
            }
        });
        return eventsReq;
    }

    private StringRequest getAllEventsByRatingApiCall() {
        String getAllEventsApiUrl = "http://20.122.91.139:8081/events";
        StringRequest eventsReq = new StringRequest(Request.Method.GET, getAllEventsApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    OnAPIResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EventListActivity.this, "Error Loading Event Data", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(EventListActivity.this, "Loading...", Toast.LENGTH_LONG).show();
            }
        });
        return eventsReq;
    }

    private StringRequest getAllEventsSportsApiCall() {
        String getAllEventsSportsApiUrl = "http://20.122.91.139:8081/events/Sports";
        StringRequest eventsReq = new StringRequest(Request.Method.GET, getAllEventsSportsApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    OnAPIResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EventListActivity.this, "Error Loading Event Data", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(EventListActivity.this, "Loading...", Toast.LENGTH_LONG).show();
            }
        });
        return eventsReq;
    }

    private StringRequest getAllEventsLeisureApiCall() {
        String getAllEventsLeisureApiUrl = "http://20.122.91.139:8081/events/Leisure";
        StringRequest eventsReq = new StringRequest(Request.Method.GET, getAllEventsLeisureApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    OnAPIResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EventListActivity.this, "Error Loading Event Data", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(EventListActivity.this, "Loading...", Toast.LENGTH_LONG).show();
            }
        });
        return eventsReq;
    }

    private StringRequest getAllEventsAdventureApiCall() {
        String getAllEventsAdventureApiUrl = "http://20.122.91.139:8081/events/Adventure";
        StringRequest eventsReq = new StringRequest(Request.Method.GET, getAllEventsAdventureApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    OnAPIResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EventListActivity.this, "Error Loading Event Data", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(EventListActivity.this, "Loading...", Toast.LENGTH_LONG).show();
            }
        });
        return eventsReq;
    }

    private StringRequest getAllEventsClubApiCall() {
        String getAllEventsClubApiUrl = "http://20.122.91.139:8081/events/club";
        StringRequest eventsReq = new StringRequest(Request.Method.GET, getAllEventsClubApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    OnAPIResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EventListActivity.this, "Error Loading Event Data", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(EventListActivity.this, "Loading...", Toast.LENGTH_LONG).show();
            }
        });
        return eventsReq;
    }

    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        boolean isSelected=((AppCompatRadioButton)view).isChecked();
        switch(view.getId()) {
            case R.id.rbleft:
                if (isSelected) {
                    rbLeft.setChecked(true);
                }
                break;
            case R.id.rbright:
                if (isSelected) {
                    Intent map = new Intent(EventListActivity.this, MapsActivity.class);
                    startActivity(map);
                }
                break;
            default:
                if (isSelected) {
                    rbRight.setChecked(true);
                }
                break;
        }
    }

    public class EventAdapter extends ArrayAdapter<String> {
        Context context;
        String[] eTitles;
        String[] eLocations;
        String[] eDates;
        String[] eImages;
        String[] eCategory;

        EventAdapter(Context c, String[] titles, String[] locations, String[] dates, String[] images, String[] category) {
            super(c, R.layout.event_list_item, R.id.EventTitle, titles);
            this.context = c;
            this.eTitles = titles;
            this.eDates = dates;
            this.eLocations = locations;
            this.eImages = images;
            this.eCategory = category;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View eventItem = layoutInflater.inflate(R.layout.event_list_item, parent, false);
            ImageView img = eventItem.findViewById(R.id.EventImage);
            TextView title = eventItem.findViewById(R.id.EventTitle);
            TextView date = eventItem.findViewById(R.id.EventDate);
            TextView loc = eventItem.findViewById(R.id.EventLocation);
            TextView des = eventItem.findViewById(R.id.EventCategory);
            View res = eventItem.findViewById(R.id.event_list_background);
            Picasso.get().load(eImages[position]).into(img);
            title.setText(eTitles[position]);
            date.setText((eDates[position]));
            loc.setText(eLocations[position]);
            des.setText(eCategory[position]);
            if (!(elColor.isEmpty())) {
                int[] color = new int[2];
                color[0] = Integer.parseInt(elColor.get(0));
                color[1] = Integer.parseInt(elColor.get(1));
                if (res != null) {
                    Drawable background = res.getBackground();
                    GradientDrawable gradientDrawable = (GradientDrawable) background;
                    gradientDrawable.setColors(color);
                }
            }
            return eventItem;
        }
    }

    public void clearArrayLists() {
        EventTitles.clear();
        EventLocations.clear();
        EventDates.clear();
        EventRatings.clear();
        EventCategories.clear();
        EventDescriptions.clear();
        EventImages.clear();
        likedBy.clear();
    }

    private void OnAPIResponse(String response) throws JSONException {
        JSONArray jsonEventsArray = new JSONArray(response);
        clearArrayLists();

        for (int i = 0; i < jsonEventsArray.length(); i++) {
            JSONObject jsonEventObj = jsonEventsArray.getJSONObject(i);
            EventTitles.add(jsonEventObj.getString("title"));
            EventLocations.add(jsonEventObj.getString("location"));
            EventDates.add(jsonEventObj.getString("date"));
            EventDescriptions.add(jsonEventObj.getString("description"));
            EventImages.add(jsonEventObj.getString("photoURL"));
            EventRatings.add(jsonEventObj.getString("rating"));
            EventCategories.add(jsonEventObj.getString("category"));
            EventCoordinates.add(jsonEventObj.getString("coordinates"));
            likedBy.add(jsonEventObj.getString("likedBy"));
        }
        EventAdapter eventAdapter = new EventAdapter(EventListActivity.this,
                EventTitles.toArray(new String[0]), EventLocations.toArray(new String[0]),
                EventDates.toArray(new String[0]), EventImages.toArray(new String[0]), EventCategories.toArray(new String[0]));
        listView.setAdapter(eventAdapter);

        saveEventData();


    }

    private void saveEventData() {

        Gson gson = new Gson();

        String E_title = gson.toJson(EventTitles);
        String E_location = gson.toJson(EventLocations);
        String E_dates = gson.toJson(EventDates);
        String E_description = gson.toJson(EventDescriptions);
        String E_images = gson.toJson(EventImages);
        String E_ratings = gson.toJson(EventRatings);
        String E_categories = gson.toJson(EventCategories);
        String E_cords = gson.toJson(EventCoordinates);
        String liked_by = gson.toJson(likedBy);

//        Log.d("EVNTS",EventTitles.toString());
//        Log.d("EVNTS",EventImages.toString());
//        Log.d("EVNTS",EventCoordinates.toString());
//        Log.d("EVNTS",EventLocations.toString());
//        Log.d("EVNTS",EventCategories.toString());
//        Log.d("EVNTS",EventDescriptions.toString());


//        Log.d("EVNTS",E_title.toString());
//        Log.d("EVNTS", E_images.toString());
//        Log.d("EVNTS",E_cords.toString());
//        Log.d("EVNTS",E_location.toString());
//        Log.d("EVNTS",E_categories.toString());
//        Log.d("EVNTS",E_description.toString());


        PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .edit()
                .putString("title", E_title)
                .commit();
        PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .edit()
                .putString("location", E_location)
                .commit();
        PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .edit()
                .putString("dates", E_dates)
                .commit();
        PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .edit()
                .putString("decs", E_description)
                .commit();
        PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .edit()
                .putString("img", E_images)
                .commit();
        PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .edit()
                .putString("rating", E_ratings)
                .commit();
        PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .edit()
                .putString("cat", E_categories)
                .commit();
        PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .edit()
                .putString("cord", E_cords)
                .commit();
        PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .edit()
                .putString("lb", liked_by)
                .commit();
    }

    private void OnLocAPIResponse(String response) throws JSONException {
        JSONArray jsonLocationsArray = new JSONArray(response);
        clearArrayLists();

        for(int i = 0; i<jsonLocationsArray.length(); i++) {
            JSONObject jsonLocationObj = jsonLocationsArray.getJSONObject(i);
            LocationTitles.add(jsonLocationObj.getString("title"));
            LocationLikes.add(jsonLocationObj.getString("likes"));
            LocationVisits.add(jsonLocationObj.getString("vists"));
            LocationCoordinates.add(jsonLocationObj.getString("coordinates"));
        }

        saveLocationData();

    }

    private void saveLocationData() {

        Gson gson = new Gson();

        String L_title = gson.toJson(LocationTitles);
        String L_likes = gson.toJson(LocationLikes);
        String L_visits = gson.toJson(LocationVisits);
        String L_cords = gson.toJson(LocationCoordinates);

        PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .edit()
                .putString("lt", L_title)
                .commit();
        PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .edit()
                .putString("ll", L_likes)
                .commit();
        PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .edit()
                .putString("lv", L_visits)
                .commit();
        PreferenceManager.getDefaultSharedPreferences(EventListActivity.this)
                .edit()
                .putString("lc", L_cords)
                .commit();
    }

    private void putEventIntent(int position) {
        Intent i = new Intent(EventListActivity.this, EventActivity.class);
        i.putExtra("EventTitle", EventTitles.get(position));
        i.putExtra("EventLocation", EventLocations.get(position));
        i.putExtra("EventDate", EventDates.get(position));
        i.putExtra("EventDescription", EventDescriptions.get(position));
        i.putExtra("EventImage", EventImages.get(position));
        i.putExtra("EventRating", EventRatings.get(position));
        i.putExtra("EventCategory", EventCategories.get(position));
        if (!Objects.equals(emailID, "")) {
            i.putExtra("user", emailID);
            if (likedBy.get(position).contains(emailID)) {
                i.putExtra("isLiked", true);
            }
        }
        startActivity(i);
    }
}
