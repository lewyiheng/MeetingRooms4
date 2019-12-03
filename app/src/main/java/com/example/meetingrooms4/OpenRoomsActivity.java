package com.example.meetingrooms4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.meetingrooms4.Adapters.OccupiedAdapter;
import com.example.meetingrooms4.Adapters.OccupiedTimeAdapter;
import com.example.meetingrooms4.Adapters.RoomsAdapter;
import com.example.meetingrooms4.Classes.Bookings;
import com.example.meetingrooms4.Classes.Rooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.model.value.ServerTimestampValue;
import com.google.firestore.v1beta1.DocumentTransform;

import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class OpenRoomsActivity extends AppCompatActivity {

    private String TAG = "lol";
    ArrayList<Rooms> al = new ArrayList<Rooms>();
    ArrayList<Bookings> al2 = new ArrayList<Bookings>();
    ArrayAdapter aa;
    ListView lv;
    TextView tvTimeInfo;
    RecyclerView rv;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference booking = db.collection("booking");
    CollectionReference room = db.collection("room");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_rooms);

        lv = findViewById(R.id.openList);
        tvTimeInfo = findViewById(R.id.openTimeInfo);
        rv = findViewById(R.id.openRv);

        centerTitle("Available Rooms");

        Intent i = getIntent();
        final String desc = i.getStringExtra("desc");
        final String startTime = i.getStringExtra("startTime");
        final String endTime = i.getStringExtra("endTime");
        final String date = i.getStringExtra("date");

        //Set info
        tvTimeInfo.setText("Available rooms from\n" + startTime + " to " + endTime);

        //Load rooms

//        al.clear();
//        al2.clear();

        //Occupied Rooms

        booking.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String date1 = document.getData().get("date").toString();
                        String desc = document.getData().get("desc").toString();
                        String endTime = document.getData().get("endTime").toString();
                        String startTime = document.getData().get("startTime").toString();
                        String room = document.getData().get("room").toString();
                        String status = document.getData().get("status").toString();
                        String user = document.getData().get("user").toString();

                        if (date.equalsIgnoreCase(date1)) {
                            Bookings book = new Bookings(user, room, startTime, endTime, date1, desc, status);
                            al2.add(book);
                        }
                    }
                }
                rv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, RecyclerView.HORIZONTAL, false));
                OccupiedAdapter aa2 = new OccupiedAdapter(getApplicationContext(), al2);
                rv.setAdapter(aa2); //Set occupied rooms
            }
        });

        room.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        final String roomName = document.getData().get("roomName").toString();
                        final String location = document.getId();
                        final String capacityString = document.getData().get("capacity").toString();
                        final int capacity = Integer.parseInt(capacityString);
                        final String av = document.getData().get("description").toString();
                        final String group = document.getData().get("roomGroup").toString();
                        al.add(new Rooms(roomName, capacity, av, group, location));

                        booking.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String date1 = document.getData().get("date").toString();
                                        String desc = document.getData().get("desc").toString();
                                        String endTime = document.getData().get("endTime").toString();
                                        String startTime = document.getData().get("startTime").toString();
                                        String room = document.getData().get("room").toString();
                                        String status = document.getData().get("status").toString();
                                        String user = document.getData().get("user").toString();

                                        if (date.equalsIgnoreCase(date1)) {
                                            Bookings book = new Bookings(user, room, startTime, endTime, date1, desc, status);
                                            al2.add(book);
                                        }

                                    }
                                    onDone(al2, al, startTime, endTime);
                                }
                            }
                        });
                    }
                }
                aa = new RoomsAdapter(getApplicationContext(), R.layout.row_rooms, al);
                lv.setAdapter(aa);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String roomChosen = al.get(position).getRoomName();
                final String roomLocation = al.get(position).getLocation();
                String msg = "Room: " + roomChosen + "\n"
                        + "Date: " + date + "\n"
                        + "Time: " + startTime + " - " + endTime + "\n"
                        + "Purpose: " + "\n"
                        + desc + "\n";
                AlertDialog.Builder alert = new AlertDialog.Builder(OpenRoomsActivity.this);
                alert.setTitle("Confirm booking?");
                alert.setMessage(msg);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("frag", "fragBookings");

                        final Bookings book = new Bookings("User1", roomLocation, startTime, endTime, date, desc, "Pending");

                        //getid?
                        booking.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String id = "000001";
                                    int count = 1;
                                    for (DocumentSnapshot document : task.getResult()) {
                                        count++;
                                        if (document.exists() == false) {
                                            id = "000001";
                                        } else {
                                            id = docId(count);
                                        }
                                    }
                                    booking.document(id).set(book);
                                }
                            }
                        });
                        startActivity(i);
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
            }
        });
//100 000
    }

    public String docId(int count) {
        String id = Integer.toString(count);

        if (count > 99999) {
            return id; // id = 100000
        } else if (count > 9999) {
            return "0" + id; // id = 010000
        } else if (count > 999) {
            return "00" + id; // id = 001000
        } else if (count > 99) {
            return "000" + id; // id = 000100
        } else if (count > 9) {
            return "0000" + id; // id = 000010;
        } else {
            return "00000" + id; // id = 000001;
        }
    }

    private void centerTitle(String title) {
        ArrayList<View> textViews = new ArrayList<>();

        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

        if (textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if (textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for (View v : textViews) {
                    if (v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }

            if (appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.actionbar));
        ab.setTitle(Html.fromHtml("<font color='#000000'>" + title + " </font>"));
    }

    public void onDone(ArrayList<Bookings> al2, ArrayList<Rooms> al, String b1, String b2) {
        ArrayList<Bookings> alString = new ArrayList<>();

        for (int i = 0; al2.size() > i; i++) {
            for (int i2 = 0; al.size() > i2; i2++) {
                if (al.get(i2).getLocation().equalsIgnoreCase(al2.get(i).getRoom())) {
                    alString.add(al2.get(i));

                    for (int i3 = 0; alString.size() > i3; i3++) {
                        Log.d(TAG, alString.get(i3).getRoom());
                        int a1 = Integer.parseInt(alString.get(i3).getStartTime());
                        int a2 = Integer.parseInt(alString.get(i3).getEndTime());

                        int c1 = Integer.parseInt(b1);
                        int c2 = Integer.parseInt(b2);

                        if (c1 >= a1 && c1 < a2) {
                            al.remove(i2);
                        } else if (c2 > a1 && c2 <= a2) {
                            al.remove(i2);
                        } else if (c1 <= a1 && c2 >= a2) {
                            al.remove(i2);
                        } else {
                        }
                    }
                    alString.clear();
                    aa.notifyDataSetChanged();
                }
            }
        }
    }
}
