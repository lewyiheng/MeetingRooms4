package com.example.meetingrooms4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.meetingrooms4.Adapters.BookingsAdapter;
import com.example.meetingrooms4.Adapters.RoomsAdapter;
import com.example.meetingrooms4.Classes.Bookings;
import com.example.meetingrooms4.Classes.Rooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import io.opencensus.tags.Tag;

public class AllBookingsFragment extends Fragment {

    private String TAG = "ASDF";

    ListView lv;
    ArrayList<Bookings> al = new ArrayList<Bookings>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference booking = db.collection("booking");
    CollectionReference room = db.collection("room");
    CollectionReference bks = db.collection("booking_status");
    CollectionReference player = db.collection("user");
    String roomName;
    BookingsAdapter aa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_allbookings, container, false);

        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setTitle(Html.fromHtml("<font color='#000000'>My Bookings</font>"));

        lv = view.findViewById(R.id.bookingsLv);

        Date todaysDate = Calendar.getInstance().getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
        final String today = sfd.format(todaysDate);

        SharedPreferences sp = this.getActivity().getApplicationContext().getSharedPreferences("sp", Context.MODE_PRIVATE);
        final int userId = sp.getInt("id", 0);
        final String user_id = sp.getString("idString",null);

        readBookings(new GetBookings() {
            @Override
            public void onCallback(ArrayList<Bookings> bookList) {
                al = bookList;

                //remove expired
                for (int i = al.size() - 1; i >= 0; i--) {
                    String date = sortDate(al.get(i).getBook_date());
                    int date1 = Integer.parseInt(date);
                    int today1 = Integer.parseInt(today);
                    if (date1 < today1) {
                        al.remove(i);
                    }
                }

                //remove not user
                    for (int i = al.size() - 1; i >= 0; i--) {
                        //int id = Integer.parseInt(al.get(i).getUser_id());
                        String id5 = al.get(i).getUser_id();
                        if (!user_id.equalsIgnoreCase(id5)) {
                            al.remove(i);
                            Log.d(TAG, id5 + " this is getUser_id()");
                            Log.d(TAG, user_id + " this is user id");

                        }
                    }

                    //Log.d(TAG,userId+ "");

                //Sort by date
                Collections.sort(al, new Comparator<Bookings>() {
                    @Override
                    public int compare(Bookings o1, Bookings o2) {
                        return sortDate(o1.getBook_date()).compareTo(sortDate(o2.getBook_date()));
                    }
                });

                //Setting date
                for (int i = 0; al.size() > i; i++) {
                    String date = al.get(i).getBook_date();
                    String displayDate = getDate(date, true);
                    String book_id = String.format("%6s", String.valueOf(i)).replace(' ', '0');
                    al.get(i).setBook_date(displayDate);
                }

                //Setting room ID to name
                for (int i = 0; al.size() > i; i++) {
                    String room_id = al.get(i).getRoom_id();
                    final Bookings newBooking = al.get(i);
                    room.document(room_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                String roomname = task.getResult().getData().get("room_name").toString();
                                newBooking.setRoom_id(roomname);
                                try {
                                    aa.notifyDataSetChanged();
                                } catch (Exception e) {

                                }
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

                if (isAdded()) {
                    aa = new BookingsAdapter(getActivity(), R.layout.row_bookings, al);
                    lv.setAdapter(aa);
                    aa.notifyDataSetChanged();
                }
            }
        });


        return view;
    }

    public String sortDate(String date) {
        String date1;

        String[] dateString = date.split("-");
        date1 = dateString[2] + dateString[1] + dateString[0];

        return date1;

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

    private void readBookings(final GetBookings gb) {

        booking.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot snapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                int status = 0;
                al.clear();
                for (QueryDocumentSnapshot document : snapshot) {
                    final Bookings book = new Bookings();
                    final String date = document.getData().get("book_date").toString();
                    final String desc = document.getData().get("book_purpose").toString();
                    final String endTime = document.getData().get("end_time").toString();
                    final String startTime = document.getData().get("start_time").toString();
                    final String roomId = document.getData().get("room_id").toString();
                    final int user = Integer.parseInt(document.getData().get("user_id").toString());
                    status = Integer.parseInt(document.getData().get("bks_id").toString());

                    String userString = String.format("%6s",user).replace(' ','0');

                    String statusString = String.valueOf(status);
//For when user table is included
                    book.setStart_time(startTime);
                    book.setEnd_time(endTime);
                    book.setBook_date(date);
                    book.setBook_purpose(desc);
                    book.setRoom_id(roomId);
                    book.setBks_id(statusString);
                    book.setUser_id(userString);

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

//                    player.document(userString).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                String realName = task.getResult().getData().get("name").toString();
//                                String userid = task.getResult().getData().
//                                book.setUser_id(realName);
//                                try {
//                                    aa.notifyDataSetChanged();
//                                } catch (Exception e) {
//
//                                }
//                            }
//                        }
//                    });
                    al.add(book);
                }
                gb.onCallback(al);
            }

        });
    }

    private interface GetBookings {
        void onCallback(ArrayList<Bookings> bookList);
    }
}
