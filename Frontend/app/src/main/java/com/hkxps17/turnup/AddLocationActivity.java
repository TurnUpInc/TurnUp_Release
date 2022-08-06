package com.hkxps17.turnup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AddLocationActivity extends AppCompatActivity {

    ImageButton done;
    ImageButton delete;
    EditText name;
    EditText address;
    JSONObject location = new JSONObject();
    String emailID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        Intent i = AddLocationActivity.this.getIntent();
        String cord = i.getStringExtra("cords");
        double latitude;
        double longitude;
        address = findViewById(R.id.add_location_text);
        Log.d("TAG", "IM HERE2");
        if (cord != null) {
            Log.d("TAG", "IM HERE");
            String[] latlong =  cord.split(",");
            latitude = Double.parseDouble(latlong[0]);
            longitude = Double.parseDouble(latlong[1]);
            Geocoder geocoder;
            String strAdd = "";

            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder("");

                    for (int k = 0; k <= returnedAddress.getMaxAddressLineIndex(); k++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(k)).append("\n");
                    }
                    strAdd = strReturnedAddress.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            address.setText(strAdd);
            Log.d("TAG", strAdd);
        }

        Set<String> retS = PreferenceManager.getDefaultSharedPreferences(AddLocationActivity.this)
                .getStringSet("id", new HashSet<String>());
        List<String> retL = new ArrayList<String>(retS);
        emailID = retL.get(0);

        done = findViewById(R.id.location_done_button);
        delete = findViewById(R.id.location_delete_button);

        name = findViewById(R.id.add_location_title);




        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = name.getText().toString();
                String cords = getLocationFromAddress(AddLocationActivity.this, address.getText().toString());

                try {
                    location.put("title", title);
                    location.put("coordinates", cords);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                postLocation(location, "http://20.122.91.139:8081/location");

                Intent map = new Intent(AddLocationActivity.this, MapsActivity.class);
                startActivity(map);

                Toast.makeText(AddLocationActivity.this, "Location Added!", Toast.LENGTH_LONG).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map = new Intent(AddLocationActivity.this, EventListActivity.class);
                startActivity(map);
            }
        });
    }

    public void postLocation(JSONObject obj, String addLocationPostApiUrl) {
        JsonObjectRequest eventsReq = new JsonObjectRequest(Request.Method.POST, addLocationPostApiUrl, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //handle POST response
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(AddLocationActivity.this, "Volley Error", Toast.LENGTH_LONG).show();
            }});
        RequestQueue queue = Volley.newRequestQueue(AddLocationActivity.this);
        queue.add(eventsReq);
    }

    public String getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        String cord = "";

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            cord = location.getLatitude() + ";" + location.getLongitude();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return cord;
    }
}