package com.example.meetingrooms4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.meetingrooms4.Adapters.BookingsAdapter;
import com.example.meetingrooms4.Classes.Bookings;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class AllBookingsFragment extends Fragment {

    ListView lv;
    ArrayList<Bookings> al = new ArrayList<Bookings>();
    ArrayAdapter aa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_allbookings, container, false);

        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setTitle(Html.fromHtml("<font color='#000000'>My Bookings</font>"));

        lv = view.findViewById(R.id.bookingsLv);

        al.clear();

        al.add(new Bookings("User2", "Serenity Room", "1400", "1500", "13 October 2019", "Short briefing on something","Confirmed"));
        al.add(new Bookings("User2", "Vigilance Room", "1500", "1600", "21 October 2019", " ","Pending"));
        al.add(new Bookings("User2", "Integrity Room", "1500", "1600", "31 October 2019", "Meeting for planning an event","Pending"));
        al.add(new Bookings("User2", "Training Room", "1500", "1600", "12 November 2020", " ","Expired"));
        al.add(new Bookings("User2", "Integrity Room", "1500", "1600", "15 December 2019", " ","Cancelled"));
        al.add(new Bookings("User2", "Integrity Room", "1500", "1600", "2 January 2020", " ","Pending"));

        aa = new BookingsAdapter(getActivity(), R.layout.row_bookings, al);
        lv.setAdapter(aa);


        return view;
    }
}
