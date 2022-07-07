package com.hkxps17.turnup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {

    ImageView img;
    ImageButton uploadImg, done, delete;
    EditText titleText, locationText, descriptionText, imgURL;
    Button dateButton;
    String dateTime;
    Spinner spinner;
    String category;
    String[] spins = {"Adventure", "Leisure", "Sports", "Club/Concert/Party"};
    JSONObject event = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent intent = this.getIntent();
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
            titleText.setText(intent.getStringExtra("EventTitles"));
            locationText.setText(intent.getStringExtra("EventLocations"));
            dateButton.setText(intent.getStringExtra("EventDate"));
            int eventImages = intent.getIntExtra("EventImages", R.drawable.hiking);
            img.setImageResource(eventImages);
            descriptionText.setText(intent.getStringExtra("EventDescription"));
        }

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = imgURL.getText().toString();
                if (!url.equals("")){
                    imgURL.setText("");
                    Picasso.get().load(url).into(img);
                }

                try {
                    event.put("image", url);
                } catch (JSONException e) {
                    e.printStackTrace();
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = titleText.getText().toString();
                String location = locationText.getText().toString();
                String cord = getLocationFromAddress(AddEventActivity.this, location);
                try {
                    event.put("title", title);
                    event.put("location", location);
                    event.put("coordinates", cord);
                    event.put("category", category);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("ADDEVENT", event.toString());


                if (intent.getStringExtra("Manage") != null) {
                    /* TODO: PUT TO DB */
                }
                else {
                    /* TODO: POST TO DB */
                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intent.getStringExtra("Manage") != null) {
                    /* TODO: DELETE FROM DB */
                }
                else {
                    Intent list = new Intent(AddEventActivity.this, EventListActivity.class);
                    startActivity(list);
                }
            }
        });
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
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                dateTime += " @ " + DateFormat.format("h:mm a", calendar1).toString();
                Log.d("DATETIME", dateTime);
                dateButton.setText(dateTime);

                try {
                    event.put("date", dateTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

    }
}