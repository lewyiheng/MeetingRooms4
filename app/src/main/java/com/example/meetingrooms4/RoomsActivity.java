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
import android.widget.Toolbar;

import com.example.meetingrooms4.Adapters.BookingsAdapter;
import com.example.meetingrooms4.Classes.Bookings;

import java.util.ArrayList;

public class RoomsActivity extends AppCompatActivity {

    Button plus, minus, book;
    TextView hours, gvClickedItem, description;
    CalendarView datepicker;

    //Testing
    ListView lv;
    ArrayAdapter aa;
    ArrayList<Bookings> al = new ArrayList<Bookings>();
    Button roomTestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        centerTitle("Room name");

        plus = findViewById(R.id.roomPlus);
        minus = findViewById(R.id.roomMinus);
        hours = findViewById(R.id.roomHours);
        datepicker = findViewById(R.id.roomDatePicker);
        book = findViewById(R.id.roomBook);
        gvClickedItem = findViewById(R.id.gvClickedItem);
        description = findViewById(R.id.roomDesc);
        lv = findViewById(R.id.roomLv);

        duration(minus, plus, hours);


        //LV (testing)
        al.clear();

        al.add(new Bookings("User2", "User2", " ", " ", "0800 - 1000", "Short briefing on something"));
        al.add(new Bookings("User2", "User 3", " ", " ", "1000 - 1200", " "));
        al.add(new Bookings("User2", "User 7", " ", " ", "1300 - 1500", "Meeting for planning an event"));
        al.add(new Bookings("User2", "User 10", " ", " ", "1600 - 1800", " "));

        aa = new BookingsAdapter(getApplicationContext(), R.layout.row_bookings, al);
        lv.setAdapter(aa);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ConfirmActivity.class);
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
                int number = Integer.parseInt(tv.getText().toString());

                if (number == 9) {
                } else {
                    int finalNumber = number + 1;
                    String stringNumber = Integer.toString(finalNumber);
                    tv.setText(stringNumber);
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(tv.getText().toString());

                if (number <= 1) {
                } else {
                    int finalNumber = number - 1;
                    String stringNumber = Integer.toString(finalNumber);
                    tv.setText(stringNumber);
                }
            }
        });
    }
}
