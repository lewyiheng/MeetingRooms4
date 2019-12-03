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
                al.clear();
                for (QueryDocumentSnapshot document : snapshot) {
                    String date = document.getData().get("date").toString();
                    String desc = document.getData().get("desc").toString();
                    String endTime = document.getData().get("endTime").toString();
                    String startTime = document.getData().get("startTime").toString();
                    String room = document.getData().get("room").toString();
                    String status = document.getData().get("status").toString();
                    String user = document.getData().get("user").toString();
                    Bookings book = new Bookings(user, room, startTime, endTime, date, desc, status);
                    al.add(book);
                }

                Collections.sort(al, new Comparator<Bookings>() {
                    public int compare(Bookings o1, Bookings o2) {
                        return sortDate(o1.getDate()).compareTo(sortDate(o2.getDate()));
                    }
                });
                if (isAdded()) {
                    BookingsAdapter aa = new BookingsAdapter(getActivity(), R.layout.row_bookings, al);
                    lv.setAdapter(aa);
                    aa.notifyDataSetChanged();
                }
            }
        });

//        al.add(new Bookings("User2", "Serenity Room", "1400", "1500", "27 November 2019", "Short briefing on something", "Confirmed"));
//        al.add(new Bookings("User2", "Vigilance Room", "1500", "1600", "28 November 2019", " ", "Pending"));
//        al.add(new Bookings("User2", "Integrity Room", "1500", "1600", "31 November 2019", "Meeting for planning an event", "Pending"));
//        al.add(new Bookings("User2", "Training Room", "1500", "1600", "12 December 2020", " ", "Cancelled"));
//        al.add(new Bookings("User2", "Integrity Room", "1500", "1600", "15 December 2019", " ", "Cancelled"));
//        al.add(new Bookings("User2", "Integrity Room", "1500", "1600", "2 January 2020", " ", "Pending"));
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
