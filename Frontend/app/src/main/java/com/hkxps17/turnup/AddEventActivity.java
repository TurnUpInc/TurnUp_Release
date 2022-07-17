package com.hkxps17.turnup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class AddEventActivity extends AppCompatActivity {

    ImageView img;
    ImageButton uploadImg;
    ImageButton done;
    ImageButton delete;
    EditText titleText;
    EditText locationText;
    EditText descriptionText;
    EditText imgURL;
    Button dateButton;
    String dateTime;
    Spinner spinner;
    String category;
    String[] spins = {"Adventure", "Leisure", "Sports", "Club/Concert/Party"};
    String emailID = "";
    String title;
    String photoURL;
    JSONObject event = new JSONObject();
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent intent = this.getIntent();

        Set<String> retS = PreferenceManager.getDefaultSharedPreferences(AddEventActivity.this)
                .getStringSet("id", new HashSet<String>());
        List<String> retL = new ArrayList<String>(retS);
        emailID = retL.get(0);

        img = findViewById(R.id.add_event_image);
        uploadImg = findViewById(R.id.image_upload_button);
        done = findViewById(R.id.event_done_button);
        delete = findViewById(R.id.event_delete_button);
        titleText = findViewById(R.id.add_event_title);
        dateButton = findViewById(R.id.add_event_date);
        locationText = findViewById(R.id.add_event_location);
        descriptionText = findViewById(R.id.add_event_description);
        imgURL = findViewById(R.id.img_url);
        spinner = findViewById(R.id.event_spinner);

        spinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        if (intent.getStringExtra("Manage") != null) {
            titleText.setText(intent.getStringExtra("EventTitle"));
            title = intent.getStringExtra("EventTitle");
            locationText.setText(intent.getStringExtra("EventLocation"));
            dateButton.setText(intent.getStringExtra("EventDate"));
            dateTime = intent.getStringExtra("EventDate");
            photoURL = intent.getStringExtra("EventImage");
            Picasso.get().load(photoURL).into(img);
            descriptionText.setText(intent.getStringExtra("EventDescription"));
            String spin = intent.getStringExtra("EventCategory");

            if (Objects.equals(spin, "Adventure")) pos = 0;
            else if (Objects.equals(spin, "Leisure")) pos = 1;
            else if (Objects.equals(spin, "Sports")) pos = 2;
            else if (Objects.equals(spin, "Club/Concert/Party")) pos = 3;
            else pos = 1;
        }

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoURL = imgURL.getText().toString();
                if (!photoURL.equals("")){
                    imgURL.setText("");
                    Picasso.get().load(photoURL).into(img);
                }
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
                handleDateButton();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddEventActivity.this, android.R.layout.simple_spinner_item, spins);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(pos);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Nothing", "nothing selected");
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String putUrl = "http://20.122.91.139:8081/event/" + emailID + "/" + title;
                title = titleText.getText().toString();
                String location = locationText.getText().toString();
                String description = descriptionText.getText().toString();
                String cord = getLocationFromAddress(AddEventActivity.this, location);
                if (!Objects.equals(title, "") && !location.equals("") && !Objects.equals(cord, "") && !Objects.equals(category, "") && !description.equals("") && !Objects.equals(dateTime, "") && title != null && cord != null && category != null && dateTime != null) {
                    try {
                        event.put("title", title);
                        event.put("location", location);
                        event.put("coordinates", cord);
                        event.put("category", category);
                        event.put("description", description);
                        event.put("date", dateTime);
                        event.put("photoURL", photoURL);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (intent.getStringExtra("Manage") != null) {
                        Log.e("check", event.toString());
                        Log.e("check", putUrl);
                        putEvent(event, putUrl);
                    } else {
                        postEvent(event, "http://20.122.91.139:8081/event/" + emailID);
                    }
                    Intent list = new Intent(AddEventActivity.this, EventListActivity.class);
                    startActivity(list);
                }
                else {
                    Toast.makeText(AddEventActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intent.getStringExtra("Manage") != null) {
                    deleteEvent(event, "http://20.122.91.139:8081/event/"+title);
                }
                //TODO: change delete button to close UI
                Intent list = new Intent(AddEventActivity.this, EventListActivity.class);
                startActivity(list);
            }
        });
    }

    public void postEvent(JSONObject obj, String addEventApiPostUrl) {
        JsonObjectRequest eventsReq = new JsonObjectRequest(Request.Method.POST, addEventApiPostUrl, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //handle POST response
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("error", error.toString());
                Toast.makeText(AddEventActivity.this, "Event Added!", Toast.LENGTH_LONG).show();
            }});
        RequestQueue queue = Volley.newRequestQueue(AddEventActivity.this);
        queue.add(eventsReq);
    }

    public void putEvent(JSONObject obj, String addEventApiPutUrl) {
        JsonObjectRequest eventsReq = new JsonObjectRequest(Request.Method.PUT, addEventApiPutUrl, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //handle POST response
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(AddEventActivity.this, "Event Updated!", Toast.LENGTH_LONG).show();
            }});
        RequestQueue queue = Volley.newRequestQueue(AddEventActivity.this);
        queue.add(eventsReq);
    }

    public void deleteEvent(JSONObject obj, String addEventApiPutUrl) {
        JsonObjectRequest eventsReq = new JsonObjectRequest(Request.Method.DELETE, addEventApiPutUrl, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //handle POST response
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(AddEventActivity.this, "Event Deleted!", Toast.LENGTH_LONG).show();
            }});
        RequestQueue queue = Volley.newRequestQueue(AddEventActivity.this);
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

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                dateTime = DateFormat.format("MMM d, yyyy", calendar1).toString();
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();

    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                dateTime += " @ " + DateFormat.format("hh:mm a", calendar1).toString();
                dateButton.setText(dateTime);
            }
        }, HOUR, MINUTE, true);

        timePickerDialog.show();
    }
}