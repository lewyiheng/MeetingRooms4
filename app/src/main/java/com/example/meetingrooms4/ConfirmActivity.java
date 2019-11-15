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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ConfirmActivity extends AppCompatActivity {

    ImageView iv;
    TextView title, time, desc, date;
    Button book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        centerTitle("Room Name here");

        iv = findViewById(R.id.confirmRoomImage);
        book = findViewById(R.id.confirmBook);

        Picasso.get().load("https://cdn.discordapp.com/attachments/449905908248739850/638541087568298004/conference_room_3.jpg").resize(1280, 720).into(iv);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("frag","fragBookings");
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
}
