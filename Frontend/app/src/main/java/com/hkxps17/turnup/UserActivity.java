package com.hkxps17.turnup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UserActivity extends AppCompatActivity {

    ListView listView;
    ImageButton done;
    EditText name;

    Spinner color1;
    Spinner color2;
    Spinner color3;
    String[] colors = {"Green", "Blue", "Pink"};
    String[] prefs = {"Green", "Blue", "Pink"};
    String[] userprf = {"Green", "Blue", "Pink"};

    String emailID = "";

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
        setContentView(R.layout.activity_user);

        listView = findViewById(R.id.manage_listview);

        Set<String> retS = PreferenceManager.getDefaultSharedPreferences(UserActivity.this)
                .getStringSet("id", new HashSet<String>());
        List<String> retL = new ArrayList<String>(retS);
        emailID = retL.get(0);

        if (Objects.equals(emailID, "Guest")) {
            findViewById(R.id.managelist).setVisibility(View.INVISIBLE);
            findViewById(R.id.manageview).setVisibility(View.INVISIBLE);
        }

        String getAllMyEventsApiUrl = "http://20.122.91.139:8081/myevents/"+emailID;
        StringRequest eventsReq = new StringRequest(Request.Method.GET, getAllMyEventsApiUrl, new Response.Listener<String>() {
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

                    if (!Objects.equals(emailID, "Guest")) {
                        ManageEventAdapter eventAdapter = new ManageEventAdapter(UserActivity.this, EventTitles.toArray(new String[0]), EventLocations.toArray(new String[0]), EventDates.toArray(new String[0]));
                        listView.setAdapter(eventAdapter);
                    }
                    else {
                        findViewById(R.id.managelist).setVisibility(View.INVISIBLE);
                        findViewById(R.id.manageview).setVisibility(View.INVISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(UserActivity.this, "Error Loading Event Data", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(UserActivity.this, "Loading", Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(UserActivity.this);
        queue.add(eventsReq);

        Log.d("EMAILUSER", emailID);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(UserActivity.this, AddEventActivity.class);
                i.putExtra("Manage", "YES");
                i.putExtra("EventTitle", EventTitles.get(position));
                i.putExtra("EventLocation", EventLocations.get(position));
                i.putExtra("EventDate", EventDates.get(position));
                i.putExtra("EventDescription", EventDescriptions.get(position));
                i.putExtra("EventImage", EventImages.get(position));
                i.putExtra("EventRating", EventRatings.get(position));
                i.putExtra("EventCategory", EventCategories.get(position));
                startActivity(i);
            }
        });

        name = findViewById(R.id.your_name);
        name.setText(emailID);

        color1 = findViewById(R.id.color1_spinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(UserActivity.this, android.R.layout.simple_spinner_item, colors);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color1.setAdapter(adapter1);

        if (Objects.equals(userprf[0], "Blue")) color1.setSelection(1);
        else if (Objects.equals(userprf[0], "Pink")) color1.setSelection(2);
        else color1.setSelection(0);


        color1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) prefs[0] = "ListGreen";
                else if (i == 1) prefs[0] = "ListBlue";
                else prefs[0] = "ListPink";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Nothing", "nothing selected");
            }
        });

        color2 = findViewById(R.id.color2_spinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(UserActivity.this, android.R.layout.simple_spinner_item, colors);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color2.setAdapter(adapter2);

        if (Objects.equals(userprf[1], "Green")) color2.setSelection(0);
        else if (Objects.equals(userprf[1], "Pink")) color2.setSelection(2);
        else color2.setSelection(1);

        color2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) prefs[1] = "ManageGreen";
                else if (i == 1) prefs[1] = "ManageBlue";
                else prefs[1] = "ManagePink";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        color3 = findViewById(R.id.color3_spinner);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(UserActivity.this, android.R.layout.simple_spinner_item, colors);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color3.setAdapter(adapter3);

        if (Objects.equals(userprf[2], "Green")) color3.setSelection(0);
        else if (Objects.equals(userprf[2], "Pink")) color3.setSelection(2);
        else color3.setSelection(1);


        color3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) prefs[2] = "LikeGreen";
                else if (i == 1) prefs[2] = "LikeBlue";
                else prefs[2] = "LikePink";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        done = findViewById(R.id.done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> prefList = Arrays.asList(prefs);
                Set<String> tasksSet = new HashSet<String>(prefList);
                PreferenceManager.getDefaultSharedPreferences(UserActivity.this)
                        .edit()
                        .putStringSet("tasks_set", tasksSet)
                        .commit();

                Log.d("TEST", prefList.toString() + "!!");
                Intent list = new Intent(UserActivity.this, EventListActivity.class);
                startActivity(list);
            }
        });
    }

    public class ManageEventAdapter extends ArrayAdapter<String> {
        Context context;
        String[] eTitles;
        String[] eLocations;
        String[] eDates;

        ManageEventAdapter(Context c, String[] titles, String[] locations, String[] dates) {
            super(c, R.layout.manage_event_item, R.id.EventTitle, titles);
            this.context = c;
            this.eTitles = titles;
            this.eDates = dates;
            this.eLocations = locations;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View eventItem = layoutInflater.inflate(R.layout.manage_event_item, parent, false);
            TextView title = eventItem.findViewById(R.id.EventTitle);
            TextView date = eventItem.findViewById(R.id.EventDate);
            TextView loc = eventItem.findViewById(R.id.EventLocation);
            title.setText(eTitles[position]);
            date.setText((eDates[position]));
            loc.setText(eLocations[position]);

            Set<String> tasksSet = PreferenceManager.getDefaultSharedPreferences(context)
                    .getStringSet("tasks_set", new HashSet<String>());
            List<String> tasksList = new ArrayList<String>(tasksSet);
            if (!tasksList.isEmpty()) {
                if (tasksList.contains("ManageGreen"))
                    eventItem.findViewById(R.id.manage_event_background).setBackground(getDrawable(R.drawable.background_list));
                else if (tasksList.contains("ManagePink"))
                    eventItem.findViewById(R.id.manage_event_background).setBackground(getDrawable(R.drawable.background_liked_list));
                else
                    eventItem.findViewById(R.id.manage_event_background).setBackground(getDrawable(R.drawable.background_manage));
            }

            return eventItem;
        }

    }
}