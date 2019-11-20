package com.example.meetingrooms4;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toolbar;

import com.example.meetingrooms4.Adapters.BookingsAdapter;
import com.example.meetingrooms4.Adapters.TimingAdapter;
import com.example.meetingrooms4.Classes.Bookings;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RoomsActivity extends AppCompatActivity {

    Button plus, minus, book;
    TextView hours, gvClickedItem, description;
    CalendarView datepicker;
    TimePicker timePicker;

    NumberPicker minutePicker;


    private static final int INTERVAL = 30;
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");

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

        centerTitle(roomChosen);
        setMinutePicker();

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

                final String startTime = timePicker.getHour() + getMinute();
                String durationSelected = hours.getText().toString();

                String endTime = endTime(Integer.toString(timePicker.getHour()), durationSelected);

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
