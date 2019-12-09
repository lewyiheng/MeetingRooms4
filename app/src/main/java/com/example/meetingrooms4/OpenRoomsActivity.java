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
import com.example.meetingrooms4.Classes.Bookings_Insert;
import com.example.meetingrooms4.Classes.Rooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

public class OpenRoomsActivity extends AppCompatActivity {

    private String TAG = "lol";
    ArrayList<Rooms> al = new ArrayList<Rooms>();
    ArrayList<Bookings> al2 = new ArrayList<Bookings>();
    ArrayAdapter aa;
    ListView lv;
    TextView tvTimeInfo;
    RecyclerView rv;
    OccupiedAdapter aa2;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference booking = db.collection("booking");
    CollectionReference bks = db.collection("booking_status");
    CollectionReference player = db.collection("user");
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

        //Occupied rooms
        booking.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                al2.clear();
                int status = 0;
                int user = 0;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            final Bookings book = new Bookings();
                            String date1 = document.getData().get("book_date").toString();
                            String desc = document.getData().get("book_purpose").toString();
                            String endTime = document.getData().get("end_time").toString();
                            String startTime = document.getData().get("start_time").toString();
                            String roomId = document.getData().get("room_id").toString();
                            status = Integer.parseInt(document.getData().get("bks_id").toString());

// TODO: Get user and replace
// TODO: Check for booking status as well

                            String statusString = String.valueOf(status);
                            String userString = String.valueOf(user);
                            if (date.equalsIgnoreCase(date1)) {
                                book.setStart_time(startTime);
                                book.setEnd_time(endTime);
                                book.setBook_date(date1);
                                book.setBook_purpose(desc);

                                room.document(roomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            String roomname = task.getResult().getData().get("room_name").toString();
                                            book.setRoom_id(roomname);
                                            aa2.notifyDataSetChanged();
                                        }
                                    }
                                });

                                bks.document(statusString).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            String realStatus = task.getResult().getData().get("bks_status").toString();
                                            book.setBks_id(realStatus);
                                            aa2.notifyDataSetChanged();
                                        }
                                    }
                                });

                                player.document("000001").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            String realName = task.getResult().getData().get("name").toString();
                                            book.setUser_id(realName);
                                            aa2.notifyDataSetChanged();
                                        }
                                    }
                                });
                                al2.add(book);
                            }
                        }
                    }
                }
                rv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, RecyclerView.HORIZONTAL, false));
                aa2 = new OccupiedAdapter(getApplicationContext(), al2);
                rv.setAdapter(aa2); //Set occupied rooms
            }
        });

        //Not Occupied Rooms
        room.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                al.clear();
                String roomName;
                al.clear();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        roomName = document.getData().get("room_name").toString();
                        final String location = document.getId();
                        final String capacityString = document.getData().get("room_capacity").toString();
                        final int capacity = Integer.parseInt(capacityString);
                        final String av = document.getData().get("room_description").toString();
                        final String group = document.getData().get("room_group").toString();
                        al.add(new Rooms(roomName, capacity, av, group, location));
                    }
                }

                aa = new RoomsAdapter(getApplicationContext(), R.layout.row_rooms, al);
                lv.setAdapter(aa);
                aa.notifyDataSetChanged();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String roomChosen = al.get(position).getRoom_name();
                final String roomLocation = al.get(position).getRoom_status();
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

                        final Bookings_Insert book = new Bookings_Insert(000001, roomLocation, startTime, endTime, date, desc, 1);

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
                                            //id = docId(count);
                                            id = String.format("%6s", count).replace(' ', '0');
                                        }
                                    }
                                    booking.document(id).set(book);
                                    Map<String,Object> updates = new HashMap<>();
                                    updates.put("timestamp", FieldValue.serverTimestamp());
                                    booking.document(id).update(updates);


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
                if (al.get(i2).getRoom_status().equalsIgnoreCase(al2.get(i).getRoom_id())) {
                    alString.add(al2.get(i));

                    for (int i3 = 0; alString.size() > i3; i3++) {
                        Log.d(TAG, alString.get(i3).getRoom_id());
                        int a1 = Integer.parseInt(alString.get(i3).getStart_time());
                        int a2 = Integer.parseInt(alString.get(i3).getEnd_time());

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
                }
            }
        }
        aa.notifyDataSetChanged();
    }
}
