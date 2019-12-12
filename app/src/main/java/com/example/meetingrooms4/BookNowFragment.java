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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BookNowFragment extends Fragment {

    Button plus, minus, book;
    TextView duration, time, date;
    EditText desc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_booknow, container, false);

        plus = view.findViewById(R.id.bookNowPlus);
        minus = view.findViewById(R.id.bookNowMinus);
        book = view.findViewById(R.id.bookNowFind);
        duration = view.findViewById(R.id.bookNowHours);
        time = view.findViewById(R.id.bookNowTimeNow);
        date = view.findViewById(R.id.bookNowDateNow);
        desc = view.findViewById(R.id.bookNowDesc);

        //View
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setTitle(Html.fromHtml("<font color='#000000'>Book Now</font>"));

        //Time Now
        final Date c = Calendar.getInstance().getTime(); //Get today's date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        final SimpleDateFormat hourOnly = new SimpleDateFormat("HH");
        final SimpleDateFormat minuteOnly = new SimpleDateFormat("mm");//Get only hour

        date.setText(dateFormat.format(c));//Set date to "dd MM yyyy"
        final String dateDB = dateFormat2.format(c);
        time.setText(timeFormat.format(c)); //Set time to HH:mm

        duration.setText("1.0");

        //Plus and minus buttons
        duration(minus, plus, duration);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (desc.getText().toString().isEmpty()) {
                    Toast.makeText(getContext().getApplicationContext(), "Please enter a purpose for booking.", Toast.LENGTH_SHORT).show();
                } else {

                    String description;
                    if (desc.getText().toString().equalsIgnoreCase(null)) {
                        description = " ";
                    } else {
                        description = desc.getText().toString();
                    }
                    String minutes = minuteOnly.format(c);
                    String hours = hourOnly.format(c);
                    String startTime = hours;

                    if (Integer.parseInt(minutes) < 30){
                        minutes = "00";
                    }else{
                        minutes = "30";
                    }

                    String durationSelected = duration.getText().toString();

                    String endTime = endTime(hours,minutes, durationSelected);
                    startTime= hours + minutes;
                    Intent i = new Intent(getActivity(), OpenRoomsActivity.class);
                    i.putExtra("startTime", startTime);
                    i.putExtra("endTime", endTime);
                    i.putExtra("desc", description);
                    i.putExtra("date", dateDB);
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
}
