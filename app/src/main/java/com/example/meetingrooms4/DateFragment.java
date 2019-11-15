package com.example.meetingrooms4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFragment extends Fragment {

    TimePicker timePicker;
    Button plus, minus, find;
    TextView duration, desc;
    CalendarView calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_date, container, false);

        //Title
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setTitle(Html.fromHtml("<font color='#000000'>By Date</font>"));

        calendar = view.findViewById(R.id.dateCalendar);
        timePicker = view.findViewById(R.id.dateTimePicker);
        plus = view.findViewById(R.id.datePlus);
        minus = view.findViewById(R.id.dateMinus);
        find = view.findViewById(R.id.dateBook);
        duration = view.findViewById(R.id.dateDuration);
        desc = view.findViewById(R.id.dateDesc);

        timePicker.setIs24HourView(true);

        //getDate
        Date date1 = new Date(calendar.getDate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String date = sdf.format(date1);

        // Minus Button
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(duration.getText().toString());

                if (number <= 1) {
                } else {
                    int finalNumber = number - 1;
                    String stringNumber = Integer.toString(finalNumber);
                    duration.setText(stringNumber);
                }
            }
        });

        //Plus Button
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(duration.getText().toString());

                if (number == 9) {
                } else {
                    int finalNumber = number + 1;
                    String stringNumber = Integer.toString(finalNumber);
                    duration.setText(stringNumber);
                }
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),OpenRoomsActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

}
