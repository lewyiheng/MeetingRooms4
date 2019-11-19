package com.example.meetingrooms4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFragment extends Fragment {

    TimePicker timePicker;
    Button plus, minus, find;
    TextView duration, desc;
    CalendarView calendar;

    private TimePicker picker; // set in onCreate
    private NumberPicker minutePicker;

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
        duration.setText("1.0");

        //getDate
        Date date1 = new Date(calendar.getDate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        final String date = sdf.format(date1);



        //Plus minus
        duration(minus, plus, duration);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String startTime = Integer.toString(timePicker.getHour()) + timePicker.getMinute();
                String durationSelected = duration.getText().toString();
                String[] timeSplit = durationSelected.split("\\.");

                final String endTime;
                if (timeSplit[1].equalsIgnoreCase("0")) {
                    int hour = timePicker.getHour() + Integer.parseInt(timeSplit[0]);
                    String minute = "00";
                    endTime = hour + minute;
                } else {
                    int hour = timePicker.getHour() + Integer.parseInt(timeSplit[0]);
                    String minute = "30";
                    endTime = hour + minute;
                }



                Intent i = new Intent(getActivity(), OpenRoomsActivity.class);
                i.putExtra("startTime",startTime);
                i.putExtra("endTime",endTime);
                i.putExtra("date",date);
                i.putExtra("desc",desc.getText().toString());
                startActivity(i);


            }
        });
        return view;
    }

    private void duration(Button minus, Button plus, final TextView tv) {
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    int number = Integer.parseInt(tv.getText().toString());
                Double numberD = Double.parseDouble(tv.getText().toString());

                if (numberD == 9) {
                } else {
                    //  int finalNumber = number + 1;
                    Double finalNumberD = numberD + 0.5;
                    //   String stringNumber = Integer.toString(finalNumber);
                    String stringNumberD = Double.toString(finalNumberD);
                    tv.setText(stringNumberD);
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     int number = Integer.parseInt(tv.getText().toString());
                Double numberD = Double.parseDouble(tv.getText().toString());

                if (numberD <= 0.5) {
                } else {
                    //        int finalNumber = number - 1;
                    Double finalNumberD = numberD - 0.5;
                    String stringNumberD = Double.toString(finalNumberD);

                    //      String stringNumber = Integer.toString(finalNumber);
                    tv.setText(stringNumberD);
                }
            }
        });
    }
}
