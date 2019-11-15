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

import java.util.ArrayList;

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

        al.add(new Bookings("User2", "Serenity Room", "1400", "1500", "25 October 2019", "Short briefing on something"));
        al.add(new Bookings("User2", "Vigilance Room", "1500", "1600", "26 November 2019", " "));
        al.add(new Bookings("User2", "Integrity Room", "1500", "1600", "7 December 2019", "Meeting for planning an event"));
        al.add(new Bookings("User2", "Training Room", "1500", "1600", "1 January 2020", " "));
        al.add(new Bookings("User2", "Integrity Room", "1500", "1600", "13 October 2019", " "));
        al.add(new Bookings("User2", "Integrity Room", "1500", "1600", "21 November 2019", " "));

        aa = new BookingsAdapter(getActivity(), R.layout.row_bookings, al);
        lv.setAdapter(aa);
        return view;
    }
}
