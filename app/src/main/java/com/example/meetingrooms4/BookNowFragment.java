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

        //Title
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setTitle(Html.fromHtml("<font color='#000000'>Book Now</font>"));

        //Time Now
        final Date c = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        final SimpleDateFormat hourOnly = new SimpleDateFormat("HH");

        date.setText(dateFormat.format(c));
        time.setText(timeFormat.format(c));

        duration.setText("1.0");
        duration(minus, plus, duration);



        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String description;
                if (desc.getText().toString().equalsIgnoreCase(null)){
                    description = " ";
                }else{
                    description = desc.getText().toString();
                }

                final String startTime = hourOnly.format(c) + "00";
                String durationSelected = duration.getText().toString();
                String[] timeSplit = durationSelected.split("\\.");

                final String endTime;
                if (timeSplit[1].equalsIgnoreCase("0")){
                    int hour = Integer.parseInt(hourOnly.format(c)) + Integer.parseInt(timeSplit[0]);
                    String minute = "00";
                    endTime = hour + minute;
                }else{
                    int hour = Integer.parseInt(hourOnly.format(c)) + Integer.parseInt(timeSplit[0]);
                    String minute = "30";
                    endTime = hour + minute;
                }


                Intent i = new Intent(getActivity(), OpenRoomsActivity.class);
                i.putExtra("startTime",startTime);
                i.putExtra("endTime",endTime);
                i.putExtra("desc",description);
                i.putExtra("date",date.getText().toString());
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
