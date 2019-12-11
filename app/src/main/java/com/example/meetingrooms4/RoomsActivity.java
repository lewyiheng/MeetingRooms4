package com.example.meetingrooms4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.meetingrooms4.Adapters.BookingsAdapter;
import com.example.meetingrooms4.Adapters.OccupiedAdapter;
import com.example.meetingrooms4.Adapters.OccupiedTimeAdapter;
import com.example.meetingrooms4.Adapters.TimingAdapter;
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
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class RoomsActivity extends AppCompatActivity {

    private String TAG = "ASDF";
    Button plus, minus, book, showAll;
    TextView hours, gvClickedItem, description, cTv;
    CalendarView datepicker;
    TimePicker timePicker;
    ArrayList<Bookings> al = new ArrayList<Bookings>();
    ArrayList<Rooms> al2 = new ArrayList<Rooms>();

    String roomid1;

    RecyclerView rv;
    LinearLayout collapsible;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference booking = db.collection("booking");
    CollectionReference bks = db.collection("booking_status");
    CollectionReference player = db.collection("user");
    CollectionReference room1 = db.collection("room");

    OccupiedTimeAdapter aa;

    NumberPicker minutePicker;

    private static final int INTERVAL = 30;
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        Intent i = getIntent();
        final String roomChosen = i.getStringExtra("room");
        final String roomid = i.getStringExtra("roomid");

        plus = findViewById(R.id.roomPlus);
        minus = findViewById(R.id.roomMinus);
        hours = findViewById(R.id.roomHour);
        datepicker = findViewById(R.id.roomDatePicker);
        book = findViewById(R.id.roomBook);
        gvClickedItem = findViewById(R.id.gvClickedItem);
        description = findViewById(R.id.roomDesc);
        rv = findViewById(R.id.roomRv);
        timePicker = findViewById(R.id.roomTimePicker);
        showAll = findViewById(R.id.showAll);

        collapsible = findViewById(R.id.collapsible);
        cTv = findViewById(R.id.collapsibleTV);

        hours.setText("1.0");

        //Plus minus button
        duration(minus, plus, hours);
        timePicker.setIs24HourView(true);

        //Set title
        centerTitle(roomChosen);

        //Set timePicker to 30min intervals.
        setMinutePicker();

        //getDate
        Date date1 = new Date(datepicker.getDate()); //Get today's date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        gvClickedItem.setText(sdf.format(date1)); //Convert today's date to "dd MMMM yyyy"
        datepicker.setDate(Calendar.getInstance().getTimeInMillis(), false, true);

        //Set min date to today
        datepicker.setMinDate(System.currentTimeMillis() - 1000);

        al.clear();

        rv.setVisibility(View.GONE);
        cTv.setText("+");

        collapsible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rv.getVisibility() == View.GONE) {
                    rv.setVisibility(View.VISIBLE);
                    cTv.setText("-");
                } else {
                    rv.setVisibility(View.GONE);
                    cTv.setText("+");
                }
            }
        });


        datepicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                al.clear();

                String dayofmonth;
                String monthString;
                String yearString = String.valueOf(year);
                month = month + 1;

                if (dayOfMonth < 10) {
                    dayofmonth = "0" + dayOfMonth;
                } else {
                    dayofmonth = String.valueOf(dayOfMonth);
                }

                if (month < 10) {
                    monthString = "0" + (month);
                } else {
                    monthString = String.valueOf(month);
                }
                final String date2 = dayofmonth + "-" + monthString + "-" + yearString;
                gvClickedItem.setText(date2);

                Log.d(TAG, gvClickedItem.getText().toString());

                readBooking(new GetBookings() {
                    @Override
                    public void onCallback(ArrayList<Bookings> bookList) {
                        al = bookList;

                        Log.d(TAG, String.valueOf(al.size()));

                        //Remove by status
                        for (int i = al.size() - 1; i >= 0; i--) {
                            String status_id = al.get(i).getBks_id();
                            if (status_id.equalsIgnoreCase("3") || status_id.equalsIgnoreCase("4")) {
                                al.remove(i);
                            }
                        }

                        //Remove by room
                        for (int i = al.size() - 1; i >= 0; i--) {
                            String room_id = al.get(i).getRoom_id();
                            if (!room_id.equalsIgnoreCase(roomid)) {
                                al.remove(i);
                            }
                        }

                        //Remove by date
                        for (int i = al.size() - 1; i >= 0; i--) {
                            String date1 = al.get(i).getBook_date();
                            if (!date1.equalsIgnoreCase(gvClickedItem.getText().toString())) {
                                al.remove(i);
                            }
                        }


                        //Setting room ID to name
                        for (int i = 0; al.size() > i; i++) {
                            String room_id = al.get(i).getRoom_id();
                            final Bookings newBooking = al.get(i);
                            room1.document(room_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        String roomname = task.getResult().getData().get("room_name").toString();
                                        newBooking.setRoom_id(roomname);
                                        aa.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                        //Setting bks_id to status name
                        for (int i = 0; al.size() > i; i++) {
                            String status_id = al.get(i).getBks_id();
                            final Bookings newBooking = al.get(i);
                            bks.document(status_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        String realStatus = task.getResult().getData().get("bks_status").toString();
                                        newBooking.setBks_id(realStatus);
                                        try {
                                            aa.notifyDataSetChanged();
                                        } catch (Exception e) {

                                        }
                                    }
                                }
                            });
                        }

                        rv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, RecyclerView.HORIZONTAL, false));
                        aa = new OccupiedTimeAdapter(getApplicationContext(), al);
                        rv.setAdapter(aa); //Set recyclerView list.
                    }
                });

            }
        });

        readRoom(new GetRooms() {
            @Override
            public void onCallback(ArrayList<Rooms> roomList) {
                al2 = roomList;

                for (int i = 0; al2.size() > i; i++) {
                    if (roomChosen.equalsIgnoreCase(al2.get(i).getRoom_name())) {
                        roomid1 = al2.get(i).getRoom_status();
                    }
                }
            }
        });


        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (description.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a purpose for booking.", Toast.LENGTH_SHORT).show();
                } else {
                    //getting time input
                    final String startTime = timePicker.getHour() + getMinute();
                    String durationSelected = hours.getText().toString();
                    final String endTime = endTime(Integer.toString(timePicker.getHour()), durationSelected);

                    readBooking(new GetBookings() {
                        @Override
                        public void onCallback(ArrayList<Bookings> bookList) {
                            al = bookList;

                            //Remove by status
                            for (int i = al.size() - 1; i >= 0; i--) {
                                String status_id = al.get(i).getBks_id();
                                if (status_id.equalsIgnoreCase("3") || status_id.equalsIgnoreCase("4")) {
                                    al.remove(i);
                                }
                            }
                            //Remove by room
                            for (int i = al.size() - 1; i >= 0; i--) {
                                String room_id = al.get(i).getRoom_id();
                                if (!room_id.equalsIgnoreCase(roomid)) {
                                    al.remove(i);
                                }
                            }

                            //Remove by date
                            for (int i = al.size() - 1; i >= 0; i--) {
                                String date1 = al.get(i).getBook_date();
                                if (!date1.equalsIgnoreCase(gvClickedItem.getText().toString())) {
                                    al.remove(i);
                                }
                            }

                            //THEN remove by time
                            for (int i = al.size() - 1; i >= 0; i--) {
                                String b1 = al.get(i).getStart_time();
                                String b2 = al.get(i).getEnd_time();

                                if (!checkConflict(startTime, endTime, b1, b2)) {


                                    String msg = "Room: " + roomChosen + "\n"
                                            + "Date: " + gvClickedItem.getText() + "\n"
                                            + "Time: " + startTime + " - " + endTime + "\n"
                                            + "Purpose: " + "\n"
                                            + description.getText().toString() + "\n";
                                    AlertDialog.Builder alert = new AlertDialog.Builder(RoomsActivity.this);
                                    alert.setTitle("Confirm booking?");
                                    alert.setMessage(msg);
                                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                            i.putExtra("frag", "fragBookings");

                                            final Bookings_Insert book = new Bookings_Insert(000001, roomid1, startTime, endTime, gvClickedItem.getText().toString(), description.getText().toString(), 1);

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
                                } else {
                                    Toast.makeText(getApplicationContext(), "Your chosen slot is not available", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });

                }
            }
        });
    }

    // =-=-=-=-=-=-=-=-=-==-=-=-=-=
    //TODO: THIS IS WRONG
    private String endTime(String startHour, String duration) {
        String endTiming;

        String[] timeSplit = duration.split("\\."); //Split duration by decimal place
        if (timeSplit[1].equalsIgnoreCase("0")) {
            int hour = Integer.parseInt(startHour) + Integer.parseInt(timeSplit[0]);
            String minute = "00";
            endTiming = hour + minute;
        } else {
            int hour = Integer.parseInt(startHour) + Integer.parseInt(timeSplit[0]);
            String minute = "30";
            endTiming = hour + minute;
        }

        return endTiming;
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

    private void duration(Button minus, Button plus, final TextView tv) {
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double numberD = Double.parseDouble(tv.getText().toString());

                if (numberD == 9) {
                } else {
                    Double finalNumberD = numberD + 0.5;
                    String stringNumberD = Double.toString(finalNumberD);
                    tv.setText(stringNumberD);
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double numberD = Double.parseDouble(tv.getText().toString());

                if (numberD <= 0.5) {
                } else {
                    Double finalNumberD = numberD - 0.5;
                    String stringNumberD = Double.toString(finalNumberD);
                    tv.setText(stringNumberD);
                }
            }
        });
    }

    public void setMinutePicker() {
        int numValues = 60 / INTERVAL;
        String[] displayedValues = new String[numValues];
        for (int i = 0; i < numValues; i++) {
            displayedValues[i] = FORMATTER.format(i * INTERVAL);
        }

        View minute = timePicker.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
        if ((minute != null) && (minute instanceof NumberPicker)) {
            minutePicker = (NumberPicker) minute;
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(numValues - 1);
            minutePicker.setDisplayedValues(displayedValues);
        }
    }

    public String getMinute() {
        if (minutePicker != null) {
            int value = minutePicker.getValue() * INTERVAL;
            if (value == 0) {
                return ((minutePicker.getValue() * INTERVAL) + "0");
            } else {
                return Integer.toString(value);
            }
        } else {
            return Integer.toString(timePicker.getCurrentMinute());
        }
    }

    private void readRoom(final GetRooms gr) {
        room1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                al2.clear();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        final String roomName = document.getData().get("room_name").toString();
                        final String location = document.getId();
                        final String capacityString = document.getData().get("room_capacity").toString();
                        final int capacity = Integer.parseInt(capacityString);
                        final String av = document.getData().get("room_description").toString();
                        final String group = document.getData().get("room_group").toString();

                        al2.add(new Rooms(roomName, capacity, av, group, location));
                    }
                }
                gr.onCallback(al2);

            }
        });
    }

    private void readBooking(final GetBookings gb) {
        booking.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                al.clear();
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
                                            aa.notifyDataSetChanged();
                                        } catch (Exception e) {

                                        }
                                    }
                                }
                            });
                            al.add(book);
                        }
                        //}
                    }
                }
                gb.onCallback(al);
            }

        });

    }

    private interface GetBookings {
        void onCallback(ArrayList<Bookings> bookList);
    }

    private interface GetRooms {
        void onCallback(ArrayList<Rooms> roomList);
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
