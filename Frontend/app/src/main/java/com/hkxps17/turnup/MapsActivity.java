package com.hkxps17.turnup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.FragmentActivity;

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

import java.util.ArrayList;
import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    AppCompatRadioButton rbLeft, rbRight;
    ListView listView;
    ImageButton likedEvents, accountButton, add, searchLoc;
    int trigger = 1;

    String[] EventTitles = {"Event 1", "Event 2", "Event 3", "Event 4", "Event 5"};
    String[] EventLocations = {"Location 1", "Location 2", "Location 3", "Location 4", "Location 5"};
    String[] EventDate = {"Date 1", "Date 2", "Date 3", "Date 4", "Date 5"};
    String [] EventDescription = {"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean facilisis nibh enim, eleifend tempus neque porta ut. Phasellus viverra sit amet augue eu suscipit. Phasellus lobortis dictum libero. Vestibulum accumsan malesuada eros, quis dictum est aliquet in. Donec sollicitudin tristique eros sed maximus. In ac risus est. Fusce sagittis nisl at diam iaculis, et fermentum ipsum tempor. Pellentesque eros dolor, aliquet eu nisl vitae, blandit rhoncus libero. Donec ut ex sit amet felis aliquam luctus ut suscipit felis. Etiam lectus orci, pharetra in nunc ut, fringilla faucibus ipsum. Donec dignissim viverra viverra. Nulla at turpis non odio luctus euismod sed a nibh. Nullam nibh purus, vehicula ac dapibus et, aliquam non velit.\n" +
            "\n" +
            "Etiam tincidunt neque a lorem posuere vestibulum at ut mi. Nunc vehicula, augue at dignissim auctor, purus risus egestas massa, feugiat iaculis mauris est eu magna. Sed massa magna, pulvinar et est vel, pretium ullamcorper quam. Ut vestibulum felis eget odio condimentum, a sodales ipsum lacinia. In non velit rutrum, molestie ex feugiat, convallis mi. Integer at leo rhoncus, ultricies magna faucibus, viverra urna. Morbi elementum, ante quis bibendum suscipit, neque dui sollicitudin urna, dictum volutpat lorem metus in erat. Cras viverra congue sapien ac semper. Phasellus diam nisi, lacinia ac massa nec, laoreet imperdiet eros. Fusce varius sit amet justo eu molestie. In hac habitasse platea dictumst. Proin eu ipsum sem. Curabitur non dui eget magna cursus lobortis vel vel risus. In venenatis elementum turpis quis porta. Quisque id sapien luctus, porta velit ut, pretium neque. Fusce vehicula magna magna, a rutrum arcu pellentesque ut.\n" +
            "\n" +
            "Donec id orci cursus, bibendum augue vel, molestie lorem. In in nisl facilisis, aliquet nisl eu, ornare felis. Donec tempus tempor ligula vitae laoreet. Donec sodales sit amet velit ac viverra. Nulla imperdiet massa in massa pharetra, vel interdum nibh ultricies. Cras nec commodo nisi, gravida rhoncus quam. Aliquam dignissim felis in purus convallis scelerisque.\n" +
            "\n" +
            "Pellentesque laoreet quam vitae justo laoreet sodales. Duis ut diam pellentesque, fermentum turpis vitae, feugiat augue. Nulla non massa orci. Nunc elementum, felis et malesuada aliquet, mi ex congue ipsum, sed iaculis odio dui at sem. Suspendisse a sapien non tellus luctus iaculis in sit amet erat. Pellentesque eget cursus eros. Suspendisse neque magna, cursus at maximus in, volutpat ut felis. Aenean efficitur felis ac neque porttitor, ut feugiat diam mattis. Fusce non mauris at eros dapibus volutpat. Mauris fringilla fringilla sem, id dignissim arcu commodo ac. Suspendisse sagittis rutrum ex, hendrerit tempor dolor semper at. Nam rhoncus mi quis urna fermentum congue. Nunc tempor porttitor justo, ut dapibus velit posuere non. Phasellus eleifend dui vestibulum scelerisque egestas.\n" +
            "\n" +
            "Etiam et nunc vel turpis imperdiet faucibus. Aliquam ultrices, magna nec elementum luctus, neque velit porttitor ligula, sed bibendum orci leo sit amet elit. Praesent sit amet mi id tellus semper egestas. Nulla facilisi. Aenean vitae consectetur sapien. Duis malesuada aliquam sapien, ac aliquam mi suscipit at. Sed lacus diam, hendrerit non malesuada tristique, blandit id quam. Nullam auctor rutrum libero, non ornare neque sollicitudin vitae. Cras dictum, velit at accumsan commodo, elit dolor feugiat orci, vitae finibus ante eros ut quam. Fusce sed tellus vitae nibh congue maximus hendrerit ac urna. Curabitur ipsum elit, tincidunt vitae lacus a, tristique laoreet massa. Morbi id urna sapien. Phasellus aliquam, ante eget tempus molestie, urna ex viverra mi, ut congue enim tortor a tortor. Donec vel convallis est. Proin et lectus sed sem vestibulum hendrerit vel quis lacus. Maecenas nec risus et tellus convallis commodo id viverra purus.", "Blue Mountain Hike", "Walk by the frozen lake", "Picnic at the parl", "11 a side Football drop-in"};
    String[] EventImages = {"https://www.adobe.com/content/dam/cc/us/en/creativecloud/photography/discover/concert-photography/thumbnail.jpeg", "https://images.unsplash.com/photo-1551632811-561732d1e306?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8aGlraW5nfGVufDB8fDB8fA%3D%3D&w=1000&q=80", "https://www.jetsetter.com//uploads/sites/7/2019/05/GettyImages-174871839-1380x1035.jpg", "https://www.thespruce.com/thmb/TIUYmTRJ3NOFnY9LJ6FzMd_9oBc=/2571x1928/smart/filters:no_upscale()/small-garden-ideas-and-inspiration-4101842-01-5e0462c2365e42de86a4f3ebc2152c1b.jpg", "https://upload.wikimedia.org/wikipedia/commons/b/b9/Football_iu_1996.jpg"};
    String[] EventCoordinates = {"49.264301;-123.256088", "49.269010;-123.248750", "49.259369;-123.256375", "49.250966;-123.235247"};
    // GET COORDINATES FROM SCREEN
    // CALL MAP
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rbLeft = findViewById(R.id.rbleft);
        rbRight = findViewById(R.id.rbright);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        likedEvents = findViewById(R.id.liked_button);
        likedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent likedList = new Intent(MapsActivity.this, LikedEventsActivity.class);
                startActivity(likedList);
            }
        });

    }

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
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
        ArrayList<LatLng> locs = new ArrayList<>();

        Yourcustominfowindowadpater adapter = new Yourcustominfowindowadpater(MapsActivity.this);
        mMap.setInfoWindowAdapter(adapter);

        for (int i = 0; i<EventCoordinates.length; i++) {
            String lat = EventCoordinates[i].split(";")[0];
            String lon = EventCoordinates[i].split(";")[1];
            float latf = Float.parseFloat(lat);
            float lonf = Float.parseFloat(lon);
            LatLng location = new LatLng(latf, lonf);
            locs.add(location);
        }


        for (int i = 0; i< locs.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(locs.get(i)).title(EventTitles[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locs.get(0), 13.0f));
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                int position = Arrays.asList(EventTitles).indexOf(marker.getTitle());
                Intent i = new Intent(MapsActivity.this, EventActivity.class);
                i.putExtra("EventTitles", marker.getTitle());
                i.putExtra("EventLocations", EventLocations[position]);
                i.putExtra("EventDate", EventDate[position]);
                i.putExtra("EventDescription", EventDescription[position]);
                i.putExtra("EventImages", EventImages[position]);
                startActivity(i);
            }
        });
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 4.0f);
                mMap.animateCamera(location);
                mMap.setPadding(0,600,0,0);
                return false;
            }
        });

        Switch mySwitch = findViewById(R.id.add_location);
        searchLoc = findViewById(R.id.map_search_button);
        searchLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trigger == 1) {
                    mySwitch.setVisibility(View.VISIBLE);
                    trigger *= -1;
                }
                else {
                    mySwitch.setVisibility(View.INVISIBLE);
                    trigger *= -1;
                }
            }
        });

//        LatLng yvr = new LatLng(49.2827, -123.1207);
//        LatLng stanleyPark = new LatLng(49.3043, -123.1443);
//        mMap.addMarker(new MarkerOptions().position(yvr).title("YVR"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yvr, 13.0f));
//        Marker temp = mMap.addMarker(new MarkerOptions().position(stanleyPark).title("StanleyPark").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
//        temp.setVisible(false);
//
//        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    temp.setVisible(true);
//                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(stanleyPark, 13.0f);
//                    mMap.animateCamera(location);                }
//                else {
//                    temp.setVisible(false);
//                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(yvr, 13.0f);
//                    mMap.animateCamera(location);
//                }
//            }
//        });



    }

    class Yourcustominfowindowadpater implements GoogleMap.InfoWindowAdapter {
        private final View mymarkerview;

        Yourcustominfowindowadpater(Context context) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mymarkerview = layoutInflater.inflate(R.layout.map_marker, null);
        }

        public View getInfoWindow(Marker marker) {
            render(marker, mymarkerview);
            return mymarkerview;
        }

        public View getInfoContents(Marker marker) {
            return null;
        }

        private void render(Marker marker, View view) {
            ImageView img = view.findViewById(R.id.event_mapImage);
            TextView title = view.findViewById(R.id.event_mapTitle);
            TextView date = view.findViewById(R.id.event_mapDate);

            int position = Arrays.asList(EventTitles).indexOf(marker.getTitle());

            Picasso.get().load(EventImages[position]).into(img);
            title.setText(EventTitles[position]);
            date.setText((EventDate[position]));
        }

    }
}
