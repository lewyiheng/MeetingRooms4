package com.example.meetingrooms4.Adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.meetingrooms4.AllBookingsFragment;
import com.example.meetingrooms4.Classes.Bookings;
import com.example.meetingrooms4.Classes.Rooms;
import com.example.meetingrooms4.MainActivity;
import com.example.meetingrooms4.R;
import com.example.meetingrooms4.RoomsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BookingsAdapter extends ArrayAdapter<Bookings> {

    private String TAG = "adapter";
    private ArrayList<Bookings> bookings;
    private Context context;
    private TextView date, time, place, user, desc, status;
    private Button release, confirm;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef = db.collection("booking");
    private CollectionReference room3 = db.collection("room");

    public BookingsAdapter(Context context, int resource, ArrayList<Bookings> objects) {
        super(context, resource, objects);
        bookings = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_bookings, parent, false);

        final Bookings results = bookings.get(position);

        date = rowView.findViewById(R.id.bookDate);
        time = rowView.findViewById(R.id.bookTime);
        place = rowView.findViewById(R.id.bookRoom);
        release = rowView.findViewById(R.id.bookNowDelete);
        desc = rowView.findViewById(R.id.bookDesc);
        status = rowView.findViewById(R.id.bookStatus);
        confirm = rowView.findViewById(R.id.bookNowConfirm);

        //Today's date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String today = sdf.format(c);

        date.setText(results.getBook_date().toString());
        status.setText(results.getBks_id());

        //Set text colour based on status
        if (status.getText().toString().equalsIgnoreCase("Confirmed")) {
            status.setTextColor(Color.parseColor("#000000"));
            status.setBackgroundColor(Color.parseColor("#55EE55"));
        } else if (status.getText().toString().equalsIgnoreCase("Pending")) {
            status.setTextColor(Color.parseColor("#000000"));
            status.setBackgroundColor(Color.parseColor("#ffb555"));
        } else {
            status.setTextColor(Color.parseColor("#000000"));
            status.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }

        //Prevent null in case purpose is empty
        if (results.getBook_purpose().equalsIgnoreCase(" ")) {
            desc.setText(" ");
        } else {
            desc.setText("\"" + results.getBook_purpose() + "\"");
        }


        //Set time
        String startTime = results.getStart_time();
        String endTime = results.getEnd_time();
        String duration = startTime + " - " + endTime;

        place.setText(results.getRoom_id());
        time.setText(duration);

        //Don't show "confirm" button if status == confirmed and cancelled
        if (status.getText().toString().equalsIgnoreCase("Confirmed") || status.getText().toString().equalsIgnoreCase("Cancelled")) {
            confirm.setVisibility(View.GONE);
        } else {
            if (date.getText().toString().equalsIgnoreCase(today)) {
                confirm.setVisibility(View.VISIBLE);
            }
        }

        //Set release button
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Confirm release?");
                alert.setMessage("Are you sure you want to release the room?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Log.d(TAG, results.getRoom_id() + ": this is room " + results.getBook_date() + ": this is date " + results.getStart_time());
                        getDBs(new readDb() {
                            @Override
                            public void onCallback(ArrayList<Rooms> roomList, ArrayList<Bookings> bookList) {

                                //Room name change to room ID
                                String roomName = results.getRoom_id(); //returns room name
                                String roomID = new String();
                                for (int i = 0; roomList.size() > i; i++) {
                                    if (roomList.get(i).getRoom_name().equalsIgnoreCase(roomName)) {
                                        roomID = roomList.get(i).getRoom_status();
                                    }
                                }
                                //Do remove
                                colRef.whereEqualTo("book_date", getDate(results.getBook_date(), false)).whereEqualTo("start_time", results.getStart_time()).whereEqualTo("room_id", roomID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            String bookID = doc.getId();
                                            colRef.document(bookID).update("bks_id", 3);
                                        }
                                    }
                                });
                            }
                        });
                        Toast.makeText(context, "Booking confirmed", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Confirm booking?");
                alert.setMessage("Are you sure you want to proceed with the booking?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getDBs(new readDb() {
                            @Override
                            public void onCallback(ArrayList<Rooms> roomList, ArrayList<Bookings> bookList) {

                                //Room name change to room ID
                                String roomName = results.getRoom_id(); //returns room name
                                String roomID = new String();
                                for (int i = 0; roomList.size() > i; i++) {
                                    if (roomList.get(i).getRoom_name().equalsIgnoreCase(roomName)) {
                                        roomID = roomList.get(i).getRoom_status();
                                    }
                                }
                                //Do confirm
                                colRef.whereEqualTo("book_date", getDate(results.getBook_date(), false)).whereEqualTo("start_time", results.getStart_time()).whereEqualTo("room_id", roomID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            String bookID = doc.getId();
                                            colRef.document(bookID).update("bks_id", 2);
                                        }
                                    }
                                });
                            }
                        });
                        Toast.makeText(context, "Booking confirmed", Toast.LENGTH_SHORT).show();
                        confirm.setVisibility(View.GONE);
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
            }
        });

        return rowView;
    }

    public String getDate(String date, Boolean i) {

        if (i == true) {
            String day1;
            String realMonth;
            String year1;

            String[] dateSplit = date.split("-");

            int day = Integer.parseInt(dateSplit[2]);
            int datePickerMonth = Integer.parseInt(dateSplit[1]);
            datePickerMonth--;
            int year = Integer.parseInt(dateSplit[0]);

            if (day < 10) {
                day1 = "0" + day;
            } else {
                day1 = Integer.toString(day);
            }

            if (datePickerMonth == 0) {
                realMonth = "January";
            } else if (datePickerMonth == 1) {
                realMonth = "Feburary";
            } else if (datePickerMonth == 2) {
                realMonth = "March";
            } else if (datePickerMonth == 3) {
                realMonth = "April";
            } else if (datePickerMonth == 4) {
                realMonth = "May";
            } else if (datePickerMonth == 5) {
                realMonth = "June";
            } else if (datePickerMonth == 6) {
                realMonth = "July";
            } else if (datePickerMonth == 7) {
                realMonth = "August";
            } else if (datePickerMonth == 8) {
                realMonth = "September";
            } else if (datePickerMonth == 9) {
                realMonth = "October";
            } else if (datePickerMonth == 10) {
                realMonth = "November";
            } else if (datePickerMonth == 11) {
                realMonth = "December";
            } else {
                realMonth = "null";
            }

            year1 = Integer.toString(year);

            return day1 + " " + realMonth + " " + year1;
        } else {

            String[] date1 = date.split(" ");

            String day1;
            String month1;

            int day = Integer.parseInt(date1[0]);
            String month = date1[1];
            String year = date1[2];

            if (day < 10) {
                day1 = "0" + day;
            } else {
                day1 = Integer.toString(day);
            }

            if (month.equalsIgnoreCase("January")) {
                month1 = "01";
            } else if (month.equalsIgnoreCase("February")) {
                month1 = "02";
            } else if (month.equalsIgnoreCase("March")) {
                month1 = "03";
            } else if (month.equalsIgnoreCase("April")) {
                month1 = "04";
            } else if (month.equalsIgnoreCase("May")) {
                month1 = "05";
            } else if (month.equalsIgnoreCase("June")) {
                month1 = "06";
            } else if (month.equalsIgnoreCase("July")) {
                month1 = "07";
            } else if (month.equalsIgnoreCase("August")) {
                month1 = "08";
            } else if (month.equalsIgnoreCase("September")) {
                month1 = "09";
            } else if (month.equalsIgnoreCase("October")) {
                month1 = "10";
            } else if (month.equalsIgnoreCase("November")) {
                month1 = "11";
            } else if (month.equalsIgnoreCase("December")) {
                month1 = "12";
            } else {
                month1 = "null";
            }
            return day1 + "-" + month1 + "-" + year;
        }
    }

    private void getDBs(final readDb rdb) {
        room3.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final ArrayList<Rooms> al = new ArrayList();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Boolean avail = (Boolean) document.getData().get("room_status");
                    if (avail == true) {
                        String roomName = document.getData().get("room_name").toString();
                        String roomid = document.getId();
                        String capacityString = document.getData().get("room_capacity").toString();
                        int capacity = Integer.parseInt(capacityString);
                        String av = document.getData().get("room_description").toString();
                        String group = document.getData().get("room_group").toString();
                        al.add(new Rooms(roomName, capacity, av, group, roomid));
                    }
                    colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Bookings> al2 = new ArrayList();
                            int status = 0;
                            al2.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //String id = String.format("%6s", doc.getData().get("user_id")).replace(' ', '0');

                                final Bookings book = new Bookings();
                                final String date = document.getData().get("book_date").toString();
                                final String desc = document.getData().get("book_purpose").toString();
                                final String endTime = document.getData().get("end_time").toString();
                                final String startTime = document.getData().get("start_time").toString();
                                final String roomId = document.getData().get("room_id").toString();
                                final int user = Integer.parseInt(document.getData().get("user_id").toString());
                                status = Integer.parseInt(document.getData().get("bks_id").toString());

                                String userString = String.format("%6s", user).replace(' ', '0');

                                String statusString = String.valueOf(status);
                                //For when user table is included
                                book.setStart_time(startTime);
                                book.setEnd_time(endTime);
                                book.setBook_date(date);
                                book.setBook_purpose(desc);
                                book.setRoom_id(roomId);
                                book.setBks_id(statusString);
                                book.setUser_id(userString);

                                al2.add(book);
                            }
                            rdb.onCallback(al, al2);
                        }
                    });
                }

            }
        });
    }

    private interface readDb {
        void onCallback(ArrayList<Rooms> roomList, ArrayList<Bookings> bookList);
    }
}
