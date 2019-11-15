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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.meetingrooms4.Adapters.RoomsAdapter;
import com.example.meetingrooms4.Classes.Rooms;

import java.util.ArrayList;

public class OpenRoomsActivity extends AppCompatActivity {

    ArrayList<Rooms> al;
    ArrayAdapter aa;
    ListView lv;
    TextView tvTimeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_rooms);

        centerTitle("Available Rooms");

        lv = findViewById(R.id.openList);
        tvTimeInfo = findViewById(R.id.openTimeInfo);

        //Set info
        tvTimeInfo.setText("Available rooms from\n" + "1000 to 1300");

        //Load rooms
        al = new ArrayList<Rooms>();
        al.clear();
        al.add(new Rooms("Faith room", " "));
        al.add(new Rooms("Integrity room", " "));
        al.add(new Rooms("Serenity room", " "));
        al.add(new Rooms("Training room", " "));
        al.add(new Rooms("Vigilance room", " "));
        aa = new RoomsAdapter(getApplicationContext(), R.layout.row_rooms, al);
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),ConfirmActivity.class);
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
