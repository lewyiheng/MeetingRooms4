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
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFragment extends Fragment {

    TimePicker timePicker;
    Button plus, minus, find;
    TextView duration, desc, check;
    CalendarView calendar;

    NumberPicker minutePicker;

    private static final int INTERVAL = 30;
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_date, container, false);

        //View
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setTitle(Html.fromHtml("<font color='#000000'>By Date</font>"));

        calendar = view.findViewById(R.id.dateCalendar);
        timePicker = view.findViewById(R.id.dateTimePicker);
        plus = view.findViewById(R.id.datePlus);
        minus = view.findViewById(R.id.dateMinus);
        find = view.findViewById(R.id.dateBook);
        duration = view.findViewById(R.id.dateDuration);
        desc = view.findViewById(R.id.dateDesc);
        check = view.findViewById(R.id.dateTVCHECK);

        timePicker.setIs24HourView(true);
        duration.setText("1.0");

//Set Minute Picker to 30min intervals
        setMinutePicker();

//Get date frm calendarView
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String formattedDate = df.format(c);
        check.setText(formattedDate);

//Get date SELECTED from calendarView
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String day;
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else {
                    day = Integer.toString(dayOfMonth);
                }
                String month1 = String.valueOf(month + 1);
                String formatMonth = String.format("%2s",month1).replace(' ','0');

                String date = year + "-" + formatMonth + "-" + day;
                check.setText(date);
            }
        });

//Set min date to today
        calendar.setMinDate(System.currentTimeMillis() - 1000);

//Plus minus
        duration(minus, plus, duration);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desc.getText().toString().isEmpty()) {
                    Toast.makeText(getContext().getApplicationContext(), "Please enter a purpose for booking.", Toast.LENGTH_SHORT).show();
                } else {

                    String startTime = timePicker.getHour() + getMinute();
                    String durationSelected = duration.getText().toString();
                    String endTime = endTime(Integer.toString(timePicker.getHour()), getMinute(), durationSelected);

                    startTime = String.format("%4s", startTime).replace(' ', '0');
                    endTime = String.format("%4s", endTime).replace(' ', '0');

                    Intent i = new Intent(getActivity(), OpenRoomsActivity.class);
                    i.putExtra("startTime", startTime);
                    i.putExtra("endTime", endTime);
                    i.putExtra("date", check.getText().toString());
                    i.putExtra("desc", desc.getText().toString());
                    startActivity(i);
                }
            }
        });
        return view;
    }

    private String endTime(String startHour, String minute1, String duration) {
        String endTiming;

        String[] timeSplit = duration.split("\\."); //Split duration by decimal place
        if (timeSplit[1].equalsIgnoreCase("0")) {
            if (minute1.equalsIgnoreCase("30")) {
                int hour = Integer.parseInt(startHour) + Integer.parseInt(timeSplit[0]);
                String minute = "30";
                endTiming = hour + minute;
            } else {
                int hour = Integer.parseInt(startHour) + Integer.parseInt(timeSplit[0]);
                String minute = "00";
                endTiming = hour + minute;
            }
        } else {
            if (minute1.equalsIgnoreCase("30")) {
                int hour = Integer.parseInt(startHour) + Integer.parseInt(timeSplit[0]);
                String minute = "00";
                endTiming = (hour + 1) + minute;
            } else {
                int hour = Integer.parseInt(startHour) + Integer.parseInt(timeSplit[0]);
                String minute = "30";
                endTiming = hour + minute;
            }
        }
        return endTiming;
    }

    private void duration(Button minus, Button plus, final TextView tv) {

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double numberD = Double.parseDouble(tv.getText().toString());

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
                Double numberD = Double.parseDouble(tv.getText().toString());

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
