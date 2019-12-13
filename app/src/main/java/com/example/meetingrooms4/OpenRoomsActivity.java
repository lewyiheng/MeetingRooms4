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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

    //Context context = getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_rooms);

        lv = findViewById(R.id.openList);
        tvTimeInfo = findViewById(R.id.openTimeInfo);
        rv = findViewById(R.id.openRv);

        SharedPreferences sp = getSharedPreferences("sp", 0);
        final int user_id = sp.getInt("id", 0);


        centerTitle("Available Rooms");

        Intent i = getIntent();
        final String desc = i.getStringExtra("desc");
        final String startTime = i.getStringExtra("startTime");
        final String endTime = i.getStringExtra("endTime");
        final String date = i.getStringExtra("date");

        //Set info
        tvTimeInfo.setText("Available rooms from\n" + startTime + " to " + endTime);
        al.clear();
        al2.clear();


        readDb(new getList() {
            @Override
            public void onCallback(ArrayList<Rooms> roomList, ArrayList<Bookings> bookList) {
                al = roomList;
                al2 = bookList;
                ArrayList<String> conflicted = new ArrayList<>();

                //Log.d(TAG, al.size() + " Size of RoomList");
                //Log.d(TAG, al2.size() + " Size of Book List");

                //Remove by date
                for (int i = al2.size() - 1; i >= 0; i--) {
                    String date1 = al2.get(i).getBook_date();
                    if (!date1.equalsIgnoreCase(date)) {
                        al2.remove(i);
                    }
                }

                //THEN remove by time
                for (int i = al2.size() - 1; i >= 0; i--) {
                    String b1 = al2.get(i).getStart_time();
                    String b2 = al2.get(i).getEnd_time();

                    if (checkConflict(startTime, endTime, b1, b2)) {
                        conflicted.add(al2.get(i).getRoom_id());
                    } else {
                        al2.remove(i);
                    }
                }

                //Whatever left in al2 gets removed in al
                for (int i = al.size() - 1; i >= 0; i--) {
                    for (int i2 = 0; conflicted.size() > i2; i2++) {
                        if (conflicted.contains(al.get(i).getRoom_status())) {
                            al.remove(i);
                        }
                    }
                }

                //Setting room ID to name
                for (int i = 0; al2.size() > i; i++) {
                    String room_id = al2.get(i).getRoom_id();
                    final Bookings newBooking = al2.get(i);
                    room.document(room_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                String roomname = task.getResult().getData().get("room_name").toString();
                                newBooking.setRoom_id(roomname);
                                aa2.notifyDataSetChanged();
                            }
                        }
                    });
                }
                //Setting bks_id to status name
                for (int i = 0; al2.size() > i; i++) {
                    String status_id = al2.get(i).getBks_id();
                    final Bookings newBooking = al2.get(i);
                    bks.document(status_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                String realStatus = task.getResult().getData().get("bks_status").toString();
                                newBooking.setBks_id(realStatus);
                                try {
                                    aa2.notifyDataSetChanged();
                                } catch (Exception e) {

                                }
                            }
                        }
                    });
                }

                //=-=-=-=-=-=-=- Set lists =-=-=-=-=-=-=-=-=
                rv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, RecyclerView.HORIZONTAL, false));
                aa2 = new OccupiedAdapter(getApplicationContext(), al2);
                rv.setAdapter(aa2);

                aa = new RoomsAdapter(getApplicationContext(), R.layout.row_rooms, al);
                lv.setAdapter(aa);
                aa.notifyDataSetChanged();
            }
        });

        //Click on room
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

                        final Bookings_Insert book = new Bookings_Insert(user_id, roomLocation, startTime, endTime, date, desc, 1);

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
                                    Map<String, Object> updates = new HashMap<>();
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


    private void readDb(final getList gl) {

        booking.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                al2.clear();
                int status;
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
                            //if (date.equalsIgnoreCase(date1)) {
                            book.setStart_time(startTime);
                            book.setEnd_time(endTime);
                            book.setBook_date(date1);
                            book.setBook_purpose(desc);
                            book.setRoom_id(roomId);
                            book.setBks_id(statusString);

//                                room.document(roomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            String roomname = task.getResult().getData().get("room_name").toString();
//                                            book.setRoom_id(roomname);
//                                            aa2.notifyDataSetChanged();
//                                        }
//                                    }
//                                });
//
//                            bks.document(statusString).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        String realStatus = task.getResult().getData().get("bks_status").toString();
//                                        book.setBks_id(realStatus);
//                                        try {
//                                            aa2.notifyDataSetChanged();
//                                        } catch (Exception e) {
//
//                                        }
//                                    }
//                                }
//                            });

                            player.document("000001").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        String realName = task.getResult().getData().get("name").toString();
                                        book.setUser_id(realName);
                                        try {
                                            aa2.notifyDataSetChanged();
                                        } catch (Exception e) {

                                        }
                                    }
                                }
                            });
                            al2.add(book);
                        }
                        //}
                    }
                    room.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            al.clear();
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    final String roomName = document.getData().get("room_name").toString();
                                    final String location = document.getId();
                                    final String capacityString = document.getData().get("room_capacity").toString();
                                    final int capacity = Integer.parseInt(capacityString);
                                    final String av = document.getData().get("room_description").toString();
                                    final String group = document.getData().get("room_group").toString();

                                    al.add(new Rooms(roomName, capacity, av, group, location));
                                }
                            }
                            gl.onCallback(al, al2);

                        }
                    });
                }
            }
        });


    }

    private interface getList {
        void onCallback(ArrayList<Rooms> roomList, ArrayList<Bookings> bookList);

    }

    public boolean checkConflict(String chosenStart, String chosenEnd, String dbStart, String dbEnd) {
        int a1 = Integer.parseInt(dbStart);
        int a2 = Integer.parseInt(dbEnd);

        int b1 = Integer.parseInt(chosenStart);
        int b2 = Integer.parseInt(chosenEnd);

        if (b1 >= a1 && b1 < a2) {
            return true;
        } else if (b2 >= a1 && b1 < a2) {
            return true;
        } else if (b1 <= a1 && b2 >= a2) {
            return true;
        } else {
            return false;
        }
    }


}
