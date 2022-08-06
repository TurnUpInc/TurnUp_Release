package com.hkxps17.turnup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
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
import com.google.gson.Gson;
import com.hkxps17.turnup.databinding.ActivityMapsBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener  {

    AppCompatRadioButton rbLeft;
    AppCompatRadioButton rbRight;
    String emailID = "";
    ImageButton likedEvents;
    ImageButton accountButton;
    ImageButton add;
    ImageButton searchLoc;
    int trigger = 1;
    int flag = 1;

    List<String> EventTitles = new ArrayList<>();
    List<String> EventLocations = new ArrayList<>();
    List<String> EventDates = new ArrayList<>();
    List<String> EventRatings = new ArrayList<>();
    List<String> EventCategories = new ArrayList<>();
    List<String> EventDescriptions = new ArrayList<>();
    List<String> EventImages = new ArrayList<>();
    List<String> likedBy = new ArrayList<>();
    List<String> EventCoordinates = new ArrayList<>();
    List<String> LocationCoordinates = new ArrayList<>();
    List<String> LocationTitles = new ArrayList<>();
    List<String> LocationLikes = new ArrayList<>();
    List<String> LocationVisits = new ArrayList<>();

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rbLeft = findViewById(R.id.rbleft);
        rbRight = findViewById(R.id.rbright);

        clearEventsList();
        clearLocationLists();

        Set<String> retS = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getStringSet("id", new HashSet<String>());
        List<String> retL = new ArrayList<String>(retS);
        emailID = retL.get(0);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        for(int i = 0; i<EventCoordinates.size(); i++) {
            String coords [] = EventCategories.get(i).split(";");

            if(Math.abs(Double.parseDouble(coords[0]) - lat)<=100 && Math.abs(Double.parseDouble(coords[1]) - lng)<=100) {
                LocationVisits.set(i, String.valueOf(Integer.parseInt(LocationVisits.get(i)) + 1));
            }
        }
    }

    private void extractEvents() {
        Gson gson = new Gson();
        String et = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getString("title", null);
        EventTitles = Arrays.asList(gson.fromJson(et, String[].class));

        String el = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getString("location",  null);
        EventLocations = Arrays.asList(gson.fromJson(el, String[].class));
        String eda = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getString("dates",  null);
        EventDates = Arrays.asList(gson.fromJson(eda, String[].class));
        String ede = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getString("decs",  null);
        EventDescriptions = Arrays.asList(gson.fromJson(ede, String[].class));
        String ei = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getString("img",  null);
        EventImages = Arrays.asList(gson.fromJson(ei, String[].class));
        String er = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getString("rating",  null);
        EventRatings = Arrays.asList(gson.fromJson(er, String[].class));
        String ec = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getString("cat",  null);
        EventCategories = Arrays.asList(gson.fromJson(ec, String[].class));
        String ecd = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getString("cord",  null);
        EventCoordinates = Arrays.asList(gson.fromJson(ecd, String[].class));
        String elb = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getString("lb",  null);
        likedBy = Arrays.asList(gson.fromJson(elb, String[].class));
    }

    private void extractLocs() {
        Gson gson = new Gson();
        String lt = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getString("lt", null);
        LocationTitles = Arrays.asList(gson.fromJson(lt, String[].class));
        String ll = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getString("ll", null);
        LocationLikes = Arrays.asList(gson.fromJson(ll, String[].class));
        String lv = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getString("lv", null);
        LocationVisits = Arrays.asList(gson.fromJson(lv, String[].class));
        String lc = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                .getString("lc", null);
        LocationCoordinates = Arrays.asList(gson.fromJson(lc, String[].class));
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

        extractLocs();
        locs.clear();
        updateLocationCoordinates(locs);
        evnts.clear();
        updateEventCoordinates(evnts);

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
                    extractLocs();
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
                    extractEvents();
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

    public void clearEventsList() {
        EventTitles.clear();
        EventLocations.clear();
        EventDates.clear();
        EventRatings.clear();
        EventCategories.clear();
        EventDescriptions.clear();
        EventImages.clear();
        likedBy.clear();
        EventCoordinates.clear();
    }

    public void clearLocationLists() {
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
