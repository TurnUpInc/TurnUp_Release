package com.hkxps17.turnup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LikedEventsActivity extends AppCompatActivity {

    ListView listView;
    ImageButton search;

    // GET ALL EVENTS

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_events);

        listView = findViewById(R.id.likedListView);

        LikedEventAdapter eventAdapter = new LikedEventAdapter(this, EventTitles, EventLocations, EventDate);
        listView.setAdapter(eventAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(LikedEventsActivity.this, EventActivity.class);
                i.putExtra("EventTitles", EventTitles[position]);
                i.putExtra("EventLocations", EventLocations[position]);
                i.putExtra("EventDate", EventDate[position]);
                i.putExtra("EventDescription", EventDescription[position]);
                i.putExtra("EventImages", EventImages[position]);
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

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View eventItem = layoutInflater.inflate(R.layout.liked_event_item, parent, false);
            TextView title = eventItem.findViewById(R.id.EventTitle);
            TextView date = eventItem.findViewById(R.id.EventDate);
            TextView loc = eventItem.findViewById(R.id.EventLocation);

            title.setText(eTitles[position]);
            date.setText((eDates[position]));
            loc.setText(eLocations[position]);

            Set<String> tasksSet = PreferenceManager.getDefaultSharedPreferences(context)
                    .getStringSet("tasks_set", new HashSet<String>());
            List<String> tasksList = new ArrayList<String>(tasksSet);
            Log.d("TEST", tasksList.toString() + "!");
            if (!tasksList.isEmpty()) {
                if (tasksList.contains("LikeGreen"))
                    eventItem.findViewById(R.id.liked_event_background).setBackground(getDrawable(R.drawable.background_list));
                else if (tasksList.contains("LikeBlue"))
                    eventItem.findViewById(R.id.liked_event_background).setBackground(getDrawable(R.drawable.background_manage));
                else
                    eventItem.findViewById(R.id.liked_event_background).setBackground(getDrawable(R.drawable.background_liked_list));
            }

            return eventItem;
        }

    }
}