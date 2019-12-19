package com.example.meetingrooms4.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingrooms4.Classes.Bookings;
import com.example.meetingrooms4.Classes.Rooms;
import com.example.meetingrooms4.Classes.User;
import com.example.meetingrooms4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OccupiedTimeAdapter extends RecyclerView.Adapter<OccupiedTimeAdapter.ViewHolder> {

    private String TAG = "call";
    private ArrayList<Bookings> room;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef = db.collection("booking");
    private CollectionReference room3 = db.collection("room");
    private CollectionReference user1 = db.collection("user");


    public OccupiedTimeAdapter(Context context, ArrayList<Bookings> objects) {
        room = objects;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView user, time, desc, status;
        Button call;


        ViewHolder(View rowView) {
            super(rowView);
            user = rowView.findViewById(R.id.occupiedTimeUser);
            time = rowView.findViewById(R.id.occupiedTimeTime);
            desc = rowView.findViewById(R.id.occupiedTimeDesc);
            status = rowView.findViewById(R.id.occupiedTimeStatus);
            call = rowView.findViewById(R.id.occupiedTimeCall);
        }
    }

    @NonNull
    @Override
    public OccupiedTimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_occupied_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bookings results = room.get(position);
        holder.user.setText(results.getUser_id());
        holder.desc.setText(results.getBook_purpose());
        holder.status.setText(results.getBks_id());

        final String date = results.getBook_date();
        final String startTime = results.getStart_time();
        final String room = results.getRoom_id();

        String endTime = results.getEnd_time();
        holder.time.setText(startTime + " - " + endTime);

        if (holder.status.getText().toString().equalsIgnoreCase("Confirmed")) {
            holder.status.setTextColor(Color.parseColor("#55EE55"));
            holder.status.setBackgroundColor(Color.parseColor("#55EE55"));
        } else if (holder.status.getText().toString().equalsIgnoreCase("Pending")) {
            holder.status.setTextColor(Color.parseColor("#ffb555"));
            holder.status.setBackgroundColor(Color.parseColor("#ffb555"));
        } else {
            holder.status.setTextColor(Color.parseColor("#CCCCCC"));
            holder.status.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                getDBs(new readDb() {
                    @Override
                    public void onCallback(ArrayList<Rooms> roomList, ArrayList<Bookings> bookList, ArrayList<User> userList) {
                        String roomID = new String();
                        for (int i = 0; roomList.size() > i; i++) {
                            if (roomList.get(i).getRoom_name().equalsIgnoreCase(room)) {
                                roomID = roomList.get(i).getRoom_status();
                            }
                        }
                        colRef.whereEqualTo("book_date", date).whereEqualTo("start_time", startTime).whereEqualTo("room_id", roomID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                String userid = new String();
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    userid = doc.getData().get("user_id").toString();
                                    userid = String.format("%6s", userid).replace(' ', '0');
                                    Log.d(TAG,userid);
                                }
                                user1.document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot doc = task.getResult();

                                        String phone = doc.getData().get("mobile_number").toString();
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        callIntent.setData(Uri.parse("tel:"+phone));
                                        context.startActivity(callIntent);

                                        }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return room.size();
    }

    private void getDBs(final OccupiedTimeAdapter.readDb rdb) {
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
                            final ArrayList<Bookings> al2 = new ArrayList();
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

                                user1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        ArrayList<User> al3 = new ArrayList();
                                        al3.clear();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            int employ = Integer.parseInt(document.getData().get("employment_status").toString());
                                            String group = document.getData().get("group_name").toString();
                                            int mobile = Integer.parseInt(document.getData().get("mobile_number").toString());
                                            String name = document.getData().get("name").toString();
                                            int office = Integer.parseInt(document.getData().get("office_number").toString());
                                            String password = "123";
                                            String role = document.getData().get("role").toString();
                                            String username = document.getData().get("username").toString();

                                            al3.add(new User(username, password, role, mobile, office, name, employ, group));
                                        }
                                        rdb.onCallback(al, al2, al3);

                                    }
                                });
                            }
                        }
                    });
                }

            }
        });
    }

    private interface readDb {
        void onCallback(ArrayList<Rooms> roomList, ArrayList<Bookings> bookList, ArrayList<User> userList);
    }

    public String getDate(String date, Boolean i) {

        if (i == true) {
            String day1;
            String realMonth;
            String year1;

            String[] dateSplit = date.split("-");

            int day = Integer.parseInt(dateSplit[0]);
            int datePickerMonth = Integer.parseInt(dateSplit[1]);
            datePickerMonth--;
            int year = Integer.parseInt(dateSplit[2]);

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
}
