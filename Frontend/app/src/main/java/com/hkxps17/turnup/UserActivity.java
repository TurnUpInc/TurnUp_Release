package com.hkxps17.turnup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
import java.util.Objects;
import java.util.Set;
import yuku.ambilwarna.AmbilWarnaDialog;

public class UserActivity extends AppCompatActivity {

    ListView listView;
    ImageButton done;
    Button reset;
    TextView name;
    int mDefaultColor;
    int elF = 0;
    int meF = 0;
    int leF = 0;
    int msg = 0;
    List<String> meColor = new ArrayList<>();

    String emailID = "";
    String[] me = {"0", "1"};
    String[] el = {"0", "1"};
    String[] le = {"0", "1"};
    String[] cr = {"0", "1"};

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

        Gson gson = new Gson();

        String ColorSet = PreferenceManager.getDefaultSharedPreferences(UserActivity.this)
                .getString("meColor", null);
        if (ColorSet != null) {
            meColor = Arrays.asList(gson.fromJson(ColorSet, String[].class));
        }

        name = findViewById(R.id.your_name);

        if (Objects.equals(emailID, "Guest")) {
            findViewById(R.id.managelist).setVisibility(View.INVISIBLE);
            findViewById(R.id.manageview).setVisibility(View.INVISIBLE);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            name.setText(emailID);
            findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent login = new Intent(UserActivity.this, LoginActivity.class);
                    startActivity(login);
                }
            });
        } else {
            name.setText(emailID.substring(0, emailID.indexOf("@")));
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        }

        StringRequest eventsReq = apiCall();
        RequestQueue queue = Volley.newRequestQueue(UserActivity.this);
        queue.add(eventsReq);



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


        reset = findViewById(R.id.reset_prefs);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserActivity.this, "UI has Reset!", Toast.LENGTH_SHORT).show();
                me[0] = String.valueOf(Color.parseColor("#2D388A"));
                me[1] = String.valueOf(Color.parseColor("#00AEEF"));
                le[0] = String.valueOf(Color.parseColor("#6867AC"));
                le[1] = String.valueOf(Color.parseColor("#CE7BB0"));
                el[0] = String.valueOf(Color.parseColor("#043927"));
                el[1] = String.valueOf(Color.parseColor("#33C58E"));
                cr[0] = String.valueOf(Color.parseColor("#2D388A"));
                cr[1] = String.valueOf(Color.parseColor("#00AEEF"));
                elF = 1;
                leF = 1;
                meF = 1;
                msg = 1;
                submitChanges();
            }
        });

        setColorL(R.id.color1_spinner1);
        setColorR(R.id.color1_spinner2);
        setColorL(R.id.color2_spinner1);
        setColorR(R.id.color2_spinner2);
        setColorL(R.id.color3_spinner1);
        setColorR(R.id.color3_spinner2);
        setColorL(R.id.color4_spinner1);
        setColorR(R.id.color4_spinner2);

        done = findViewById(R.id.done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitChanges();
            }
        });
    }

    private void submitChanges() {
        Gson gson = new Gson();

        if (elF == 1) {
            String s1 = gson.toJson(el);
            PreferenceManager.getDefaultSharedPreferences(UserActivity.this)
                    .edit()
                    .putString("elColor", s1)
                    .commit();

        }
        if (meF == 1) {
            String s2 = gson.toJson(me);
            PreferenceManager.getDefaultSharedPreferences(UserActivity.this)
                    .edit()
                    .putString("meColor", s2)
                    .commit();

        }
        if (leF == 1) {
            String s3 = gson.toJson(le);
            PreferenceManager.getDefaultSharedPreferences(UserActivity.this)
                    .edit()
                    .putString("leColor", s3)
                    .commit();
        }
        if (msg == 1) {
            String s4 = gson.toJson(cr);
            PreferenceManager.getDefaultSharedPreferences(UserActivity.this)
                    .edit()
                    .putString("crColor", s4)
                    .commit();
        }

        Intent list = new Intent(UserActivity.this, EventListActivity.class);
        startActivity(list);
    }


    private void setColorL(int btnL) {
        mDefaultColor = ContextCompat.getColor(UserActivity.this, R.color.black);
        Button cBtnL = findViewById(btnL);
        cBtnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(emailID, "Guest")) {
                    Toast.makeText(UserActivity.this, "Please Log In!", Toast.LENGTH_SHORT).show();
                }
                else {
                    AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(UserActivity.this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override
                        public void onCancel(AmbilWarnaDialog dialog) {
                            Log.d("Nothing", "Cancel selection");
                        }

                        @Override
                        public void onOk(AmbilWarnaDialog dialog, int color) {
                            mDefaultColor = color;
                            if (btnL == R.id.color1_spinner1) {
                                el[0] = String.valueOf(mDefaultColor);
                                if (Objects.equals(el[1], "1")) {
                                    el[1] = String.valueOf(R.color.black);
                                }
                                elF = 1;
                            } else if (btnL == R.id.color2_spinner1) {
                                me[0] = String.valueOf(mDefaultColor);
                                if (Objects.equals(me[1], "1")) {
                                    me[1] = String.valueOf(R.color.black);
                                }
                                meF = 1;
                            } else if (btnL == R.id.color3_spinner1) {
                                le[0] = String.valueOf(mDefaultColor);
                                if (Objects.equals(le[1], "1")) {
                                    le[1] = String.valueOf(R.color.black);
                                }
                                leF = 1;
                            } else {
                                cr[0] = String.valueOf(mDefaultColor);
                                if (Objects.equals(cr[1], "1")) {
                                    cr[1] = String.valueOf(R.color.black);
                                }
                                msg = 1;
                            }
                            cBtnL.setBackgroundColor(mDefaultColor);
                        }
                    });
                    colorPicker.show();
                }
            }
        });
    }

    private void setColorR(int btnR) {
        mDefaultColor = ContextCompat.getColor(UserActivity.this, R.color.black);
        Button cBtnR = findViewById(btnR);
        cBtnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(UserActivity.this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        Log.d("Nothing", "Cancel selection");
                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mDefaultColor = color;
                        if (btnR == R.id.color1_spinner2) {
                            el[1] = String.valueOf(mDefaultColor);
                            if (Objects.equals(el[0], "0")) {
                                el[0] = String.valueOf(R.color.black);
                            }
                            elF = 1;
                        }
                        else if (btnR == R.id.color2_spinner2) {
                            me[1] = String.valueOf(mDefaultColor);
                            if (Objects.equals(me[0], "0")) {
                                me[0] = String.valueOf(R.color.black);
                            }
                            meF = 1;
                        }
                        else if (btnR == R.id.color3_spinner2){
                            le[1] = String.valueOf(mDefaultColor);
                            if (Objects.equals(le[0], "0")) {
                                le[0] = String.valueOf(R.color.black);
                            }
                            leF = 1;
                        }
                        else {
                            cr[1] = String.valueOf(mDefaultColor);
                            if (Objects.equals(cr[0], "0")) {
                                cr[0] = String.valueOf(R.color.black);
                            }
                            msg = 1;
                        }
                        cBtnR.setBackgroundColor(mDefaultColor);
                    }
                });
                colorPicker.show();
            }
        });
    }

    private StringRequest apiCall() {
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
        return eventsReq;
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
            View res = eventItem.findViewById(R.id.manage_event_background);
            title.setText(eTitles[position]);
            date.setText((eDates[position]));
            loc.setText(eLocations[position]);
            if (!(meColor.isEmpty())) {
                int[] color = new int[2];
                color[0] = Integer.parseInt(meColor.get(0));
                color[1] = Integer.parseInt(meColor.get(1));
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
