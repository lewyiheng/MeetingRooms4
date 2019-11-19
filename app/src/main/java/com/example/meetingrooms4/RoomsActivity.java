package com.example.meetingrooms4;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toolbar;

import com.example.meetingrooms4.Adapters.BookingsAdapter;
import com.example.meetingrooms4.Adapters.TimingAdapter;
import com.example.meetingrooms4.Classes.Bookings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RoomsActivity extends AppCompatActivity {

    Button plus, minus, book;
    TextView hours, gvClickedItem, description;
    CalendarView datepicker;
    TimePicker timePicker;

    //Testing
    ListView lv;
    ArrayAdapter aa;
    ArrayList<Bookings> al = new ArrayList<Bookings>();
    Button roomTestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        Intent i = getIntent();
        final String roomChosen = i.getStringExtra("room");

        centerTitle(roomChosen);

        plus = findViewById(R.id.roomPlus);
        minus = findViewById(R.id.roomMinus);
        hours = findViewById(R.id.roomHour);
        datepicker = findViewById(R.id.roomDatePicker);
        book = findViewById(R.id.roomBook);
        gvClickedItem = findViewById(R.id.gvClickedItem);
        description = findViewById(R.id.roomDesc);
        lv = findViewById(R.id.roomLv);
        timePicker = findViewById(R.id.roomTimePicker);

        hours.setText("1.0");
        duration(minus, plus, hours);
        timePicker.setIs24HourView(true);

        //getDate
        Date date1 = new Date(datepicker.getDate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        final String date = sdf.format(date1);

        //LV (testing)
        al.clear();

        al.add(new Bookings("User2", "Serenity Room", "0900", "1100", "13 October 2019", "Short briefing on something"));
        al.add(new Bookings("User2", "Vigilance Room", "1100", "1300", "21 October 2019", " "));
        al.add(new Bookings("User2", "Integrity Room", "1400", "1500", "31 October 2019", "Meeting for planning an event"));
        al.add(new Bookings("User2", "Training Room", "1500", "1600", "12 November 2020", " "));
        al.add(new Bookings("User2", "Integrity Room", "1700", "1900", "15 December 2019", " "));
        al.add(new Bookings("User2", "Integrity Room", "2000", "2200", "2 January 2020", " "));

        aa = new TimingAdapter(getApplicationContext(), R.layout.row_bookings, al);
        lv.setAdapter(aa);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String startTime = Integer.toString(timePicker.getHour()) + timePicker.getMinute();
                String durationSelected = hours.getText().toString();
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

                Intent i = new Intent(getApplicationContext(), ConfirmActivity.class);
                i.putExtra("date", date);
                i.putExtra("room", roomChosen);
                i.putExtra("startTime", startTime);
                i.putExtra("endTime", endTime);
                i.putExtra("desc", description.getText().toString());
                startActivity(i);
            }
        });
    }

    private void centerTitle(String title) {
        ArrayList<View> textViews = new ArrayList<>();

        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

        if (textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if (textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for (View v : textViews) {
                    if (v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }

            if (appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.actionbar));
        ab.setTitle(Html.fromHtml("<font color='#000000'>" + title + " </font>"));
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