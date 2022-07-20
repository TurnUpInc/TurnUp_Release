package com.hkxps17.turnup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hkxps17.turnup.databinding.ActivityMapsBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    AppCompatRadioButton rbLeft;
    AppCompatRadioButton rbRight;
    String emailID = "";
    ImageButton likedEvents;
    ImageButton accountButton;
    ImageButton add;
    ImageButton searchLoc;
    int trigger = 1;
    int flag = 1;

    ArrayList<String> EventTitles = new ArrayList<>();
    ArrayList<String> EventLocations = new ArrayList<>();
    ArrayList<String> EventDates = new ArrayList<>();
    ArrayList<String> EventRatings = new ArrayList<>();
    ArrayList<String> EventCategories = new ArrayList<>();
    ArrayList<String> EventDescriptions = new ArrayList<>();
    ArrayList<String> EventImages = new ArrayList<>();
    ArrayList<String> likedBy = new ArrayList<>();
    ArrayList<String> EventCoordinates = new ArrayList<>();
    ArrayList<String> LocationCoordinates = new ArrayList<>();
    ArrayList<String> LocationTitles = new ArrayList<>();
    ArrayList<String> LocationLikes = new ArrayList<>();
    ArrayList<String> LocationVisits = new ArrayList<>();

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rbLeft = findViewById(R.id.rbleft);
        rbRight = findViewById(R.id.rbright);

        Set<String> retS = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getStringSet("id", new HashSet<String>());
        List<String> retL = new ArrayList<String>(retS);
        emailID = retL.get(0);

        String getAllLocationsApiUrl = "http://20.122.91.139:8081/locations";
        StringRequest locationsReq = new StringRequest(Request.Method.GET, getAllLocationsApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonLocationsArray = new JSONArray(response);
                    clearArrayLists();

                    for(int i = 0; i<jsonLocationsArray.length(); i++) {
                        JSONObject jsonLocationObj = jsonLocationsArray.getJSONObject(i);
                        LocationTitles.add(jsonLocationObj.getString("title"));
                        LocationLikes.add(jsonLocationObj.getString("likes"));
                        LocationVisits.add(jsonLocationObj.getString("vists"));
                        LocationCoordinates.add(jsonLocationObj.getString("coordinates"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this, "Loading Markers!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MapsActivity.this, "Volley Error", Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
        queue.add(locationsReq);

        String getAllEventsApiUrl = "http://20.122.91.139:8081/events";
        StringRequest eventsReq = new StringRequest(Request.Method.GET, getAllEventsApiUrl, new Response.Listener<String>() {
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
                        EventCoordinates.add(jsonEventObj.getString("coordinates"));
                        likedBy.add(jsonEventObj.getString("likedBy"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this, "Loading Markers!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MapsActivity.this, "Volley Error", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(eventsReq);


        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        boolean isSelected=((AppCompatRadioButton)view).isChecked();
        switch(view.getId()) {
            case R.id.rbright:
                if (isSelected) {
                    rbRight.setChecked(true);
                }
                break;
            case R.id.rbleft:
                if (isSelected) {
                    Intent list = new Intent(MapsActivity.this, EventListActivity.class);
                    startActivity(list);
                }
                break;
            default:
                if (isSelected) {
                    rbRight.setChecked(true);
                }
                break;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
        ArrayList<LatLng> locs = new ArrayList<>();
        ArrayList<LatLng> evnts = new ArrayList<>();

        evnts.clear();
        updateEventCoordinates(evnts);
        locs.clear();
        updateLocationCoordinates(locs);


        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                onWindowClick(marker);
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                mMap.setPadding(0,600,0,0);
                return false;
            }
        });

        flag = 1;
        mMap.clear();
        CustomInfoAdapterLocation adapter = new CustomInfoAdapterLocation(MapsActivity.this);
        mMap.setInfoWindowAdapter(adapter);

        for (int i = 0; i< locs.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(locs.get(i)).title(LocationTitles.get(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        }

        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(new LatLng(49.274085, -123.174495), 10.0f);
        mMap.animateCamera(location);

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch mySwitch = findViewById(R.id.add_location);
        mySwitch.setVisibility(View.VISIBLE);
        searchLoc = findViewById(R.id.map_search_button);
        searchLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trigger == 1) {
                    mySwitch.setVisibility(View.INVISIBLE);
                    trigger *= -1;
                }
                else {
                    mySwitch.setVisibility(View.VISIBLE);
                    trigger *= -1;
                }
            }
        });

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    flag = 1;
                    mMap.clear();
                    locs.clear();
                    updateLocationCoordinates(locs);
                    CustomInfoAdapterLocation adapter = new CustomInfoAdapterLocation(MapsActivity.this);
                    mMap.setInfoWindowAdapter(adapter);

                    for (int i = 0; i< locs.size(); i++) {
                        mMap.addMarker(new MarkerOptions().position(locs.get(i)).title(LocationTitles.get(i))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    }
                }
                else {
                    flag = 0;
                    mMap.clear();
                    evnts.clear();
                    updateEventCoordinates(evnts);
                    CustomInfoAdapter adapter = new CustomInfoAdapter(MapsActivity.this);
                    mMap.setInfoWindowAdapter(adapter);

                    for (int i = 0; i< evnts.size(); i++) {
                        mMap.addMarker(new MarkerOptions().position(evnts.get(i)).title(EventTitles.get(i))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }

                }

                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(new LatLng(49.274085, -123.174495), 10.0f);
                mMap.animateCamera(location);
            }
        });

        accountButton = findViewById(R.id.map_account_button);
        likedEvents = findViewById(R.id.map_liked_button);
        add = findViewById(R.id.map_add_button);
        likedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailID.equals("Guest")) {
                    Intent likedList = new Intent(MapsActivity.this, LikedEventsActivity.class);
                    startActivity(likedList);
                }
                else {
                    Toast.makeText(MapsActivity.this, "Please Log In!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acc = new Intent(MapsActivity.this, UserActivity.class);
                startActivity(acc);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Objects.equals(emailID, "Guest")) {
                    Intent add = new Intent(MapsActivity.this, AddLocationActivity.class);
                    startActivity(add);
                }
                else {
                    Toast.makeText(MapsActivity.this, "Please Log In!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onWindowClick(Marker marker) {
        if (flag == 1) {

            Log.d("TITLE", marker.getTitle());

            String addLikedLocationPostApiUrl = "http://20.122.91.139:8081/likelocation/" + marker.getTitle();
            JsonObjectRequest eventsReq = new JsonObjectRequest(Request.Method.POST, addLikedLocationPostApiUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //handle POST response
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(MapsActivity.this, "Location Liked!", Toast.LENGTH_LONG).show();
                }});
            RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
            queue.add(eventsReq);
        }
        else {
            int position = EventTitles.indexOf(marker.getTitle());
            Intent i = new Intent(MapsActivity.this, EventActivity.class);
            i.putExtra("EventTitle", EventTitles.get(position));
            i.putExtra("EventLocation", EventLocations.get(position));
            i.putExtra("EventDate", EventDates.get(position));
            i.putExtra("EventDescription", EventDescriptions.get(position));
            i.putExtra("EventImage", EventImages.get(position));
            i.putExtra("EventRating", EventRatings.get(position));
            i.putExtra("EventCategory", EventCategories.get(position));
            if (!Objects.equals(emailID, "Guest") && likedBy.get(position).contains(emailID)) {
                i.putExtra("isLiked", true);
            }
            startActivity(i);
        }
    }

    public void updateEventCoordinates(ArrayList<LatLng> evnts) {
        for (String eventCoordinate : EventCoordinates) {
            String lat = eventCoordinate.split(";")[0];
            String lon = eventCoordinate.split(";")[1];
            float latf = Float.parseFloat(lat);
            float lonf = Float.parseFloat(lon);
            LatLng location = new LatLng(latf, lonf);
            evnts.add(location);
        }
    }

    public void updateLocationCoordinates(ArrayList<LatLng> locs) {
        for (String locationCoordinate : LocationCoordinates) {
            String lat = locationCoordinate.split(";")[0];
            String lon = locationCoordinate.split(";")[1];
            float latf = Float.parseFloat(lat);
            float lonf = Float.parseFloat(lon);
            LatLng location = new LatLng(latf, lonf);
            locs.add(location);
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
        EventCoordinates.clear();

        LocationLikes.clear();
        LocationCoordinates.clear();
        LocationTitles.clear();
        LocationVisits.clear();
    }

    class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mymarkerview;

        @SuppressLint("InflateParams")
        CustomInfoAdapter(Context context) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mymarkerview = layoutInflater.inflate(R.layout.map_event_marker, null);
        }

        public View getInfoWindow(@NonNull Marker marker) {
            render(marker, mymarkerview);
            return mymarkerview;
        }

        public View getInfoContents(@NonNull Marker marker) {
            return null;
        }

        private void render(Marker marker, View view) {
            ImageView img = view.findViewById(R.id.event_mapImage);
            TextView title = view.findViewById(R.id.event_mapTitle);
            TextView date = view.findViewById(R.id.event_mapDate);

            int position = EventTitles.indexOf(marker.getTitle());

            Picasso.get().load(EventImages.get(position)).into(img);
            title.setText(EventTitles.get(position));
            date.setText(EventDates.get(position));
        }
    }

    class CustomInfoAdapterLocation implements GoogleMap.InfoWindowAdapter {

        private final View mymarkerview;

        @SuppressLint("InflateParams")
        CustomInfoAdapterLocation(Context context) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mymarkerview = layoutInflater.inflate(R.layout.map_location_marker, null);
        }

        public View getInfoWindow(@NonNull Marker marker) {
            render(marker, mymarkerview);
            return mymarkerview;
        }

        public View getInfoContents(@NonNull Marker marker) {
            return null;
        }

        private void render(Marker marker, View view) {
            TextView liked = view.findViewById(R.id.map_textView6);
            TextView visited = view.findViewById(R.id.map_textView7);
            TextView title = view.findViewById(R.id.location_title);

            int position = LocationTitles.indexOf(marker.getTitle());
            liked.setText(LocationLikes.get(position));
            visited.setText(LocationVisits.get(position));
            title.setText(LocationTitles.get(position));

        }
    }
}
