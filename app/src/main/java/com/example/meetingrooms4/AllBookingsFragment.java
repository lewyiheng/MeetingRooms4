package com.example.meetingrooms4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.Html;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class AllBookingsFragment extends Fragment {

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

        booking.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot snapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

                int status = 0;
                int user = 0;
                al.clear();
                for (QueryDocumentSnapshot document : snapshot) {
                    final Bookings book = new Bookings();
                    final String date = document.getData().get("book_date").toString();
                    final String desc = document.getData().get("book_purpose").toString();
                    final String endTime = document.getData().get("end_time").toString();
                    final String startTime = document.getData().get("start_time").toString();
                    final String roomId = document.getData().get("room_id").toString();
                    status = Integer.parseInt(document.getData().get("bks_id").toString());

                    String statusString = String.valueOf(status);
                    String userString = String.valueOf(user);
                    // book = new Bookings("null", room, startTime, endTime, date1, desc, "null");
                    //book.setRoom_id(room);

                    //String realDate = getDate(date);
                    book.setStart_time(startTime);
                    book.setEnd_time(endTime);
                    book.setBook_date(date);
                    book.setBook_purpose(desc);

                    room.document(roomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                String roomname = task.getResult().getData().get("room_name").toString();
                                book.setRoom_id(roomname);
                                try {
                                    aa.notifyDataSetChanged();
                                } catch (Exception e) {

                                }
                            }
                        }
                    });

                    bks.document(statusString).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                String realStatus = task.getResult().getData().get("bks_status").toString();
                                book.setBks_id(realStatus);
                                try {
                                    aa.notifyDataSetChanged();
                                } catch (Exception e) {

                                }
                            }
                        }
                    });

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
                if (al.size() < 1) {
                    Collections.sort(al, new Comparator<Bookings>() {
                        public int compare(Bookings o1, Bookings o2) {
                            return sortDate(o1.getBook_date()).compareTo(sortDate(o2.getBook_date()));
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
        date = dateString[2] + dateString[1] + dateString[0];

        return date;

    }

    public String getDate(String date) {

        String[] dateSplit = date.split("-");

        int day = Integer.parseInt(dateSplit[0]);
        int datePickerMonth = Integer.parseInt(dateSplit[1]);
        datePickerMonth--;
        int year = Integer.parseInt(dateSplit[2]);

        String day1;
        String realMonth;
        String year1;


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
    }
}
