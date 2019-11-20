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

    NumberPicker minutePicker;

    private static final int INTERVAL = 30;
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");

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

        setMinutePicker();
        //getDate
        Date date1 = new Date(calendar.getDate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        final String date = sdf.format(date1);

        //Plus minus
        duration(minus, plus, duration);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startTime = timePicker.getHour() + getMinute();
                String durationSelected = duration.getText().toString();
                String endTime = endTime(Integer.toString(timePicker.getHour()), durationSelected);

                Intent i = new Intent(getActivity(), OpenRoomsActivity.class);
                i.putExtra("startTime", startTime);
                i.putExtra("endTime", endTime);
                i.putExtra("date", date);
                i.putExtra("desc", desc.getText().toString());
                startActivity(i);
            }
        });
        return view;
    }

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

    private void duration(Button minus, Button plus, final TextView tv) {

        final Double numberD = Double.parseDouble(tv.getText().toString());

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
