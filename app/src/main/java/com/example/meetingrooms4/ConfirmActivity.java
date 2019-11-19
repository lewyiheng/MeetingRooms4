package com.example.meetingrooms4;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class ConfirmActivity extends AppCompatActivity {

    ImageView iv;
    TextView  time, desc, date,title;
    Button book;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);



        iv = findViewById(R.id.confirmRoomImage);
        book = findViewById(R.id.confirmBook);
        time = findViewById(R.id.confirmTime);
        title = findViewById(R.id.confirmTitle);
        desc = findViewById(R.id.confirmDesc);
        date= findViewById(R.id.confirmDate);

        Intent i = getIntent();
        String room1 = i.getStringExtra("room");
        String startTime1 = i.getStringExtra("startTime");
        String endTime1 = i.getStringExtra("endTime");
        String desc1 = i.getStringExtra("desc");
        String date1 = i.getStringExtra("date");

        date.setText(date1);
        title.setText(room1);
        desc.setText(desc1);

        time.setText(startTime1 + " - " +endTime1);

        centerTitle(room1);

        Picasso.get().load("https://cdn.discordapp.com/attachments/449905908248739850/638541087568298004/conference_room_3.jpg").resize(1280, 720).into(iv);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ConfirmActivity.this);
                alert.setTitle("Confirm Reservation?");
                alert.setMessage("Are you sure you want to book this room?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//Set notif
                        Calendar timer = Calendar.getInstance();
                        timer.add(Calendar.SECOND, 5);
                        Intent i = new Intent(ConfirmActivity.this, NotificationReceiver.class);
                        PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 12345, i, PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                        am.set(AlarmManager.RTC_WAKEUP, timer.getTimeInMillis(), pIntent);


                        Intent i2 = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("frag", "fragBookings");
                        startActivity(i2);

                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
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
}
