package com.example.meetingrooms4;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.meetingrooms4.Adapters.OccupiedAdapter;
import com.example.meetingrooms4.Adapters.RoomsAdapter;
import com.example.meetingrooms4.Classes.Rooms;

import java.util.ArrayList;

public class OpenRoomsActivity extends AppCompatActivity {

    ArrayList<Rooms> al, al2;
    ArrayAdapter aa, aa2;
    ListView lv;
    GridView gv;
    TextView tvTimeInfo;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_rooms);

        centerTitle("Available Rooms");

        lv = findViewById(R.id.openList);
        tvTimeInfo = findViewById(R.id.openTimeInfo);
        //rv = findViewById(R.id.rv);
        gv = findViewById(R.id.openGv);

        Intent i = getIntent();
        final String desc = i.getStringExtra("desc");
        final String startTime = i.getStringExtra("startTime");
        final String endTime = i.getStringExtra("endTime");
        final String date = i.getStringExtra("date");

        //Set info
        tvTimeInfo.setText("Available rooms from\n" + startTime + " to " + endTime);

        //Load rooms
        al = new ArrayList<Rooms>();
        al2 = new ArrayList<>();
        al.clear();
        al.add(new Rooms("Faith room", " "));
        al.add(new Rooms("Integrity room", " "));
        al.add(new Rooms("Serenity room", " "));
        al.add(new Rooms("Training room", " "));
        al.add(new Rooms("Vigilance room", " "));

        al2.add(new Rooms("Courage room \nUser3", " "));
        //al2.add(new Rooms("Perseverance room \nUser4", " "));
        al2.add(new Rooms("Excellence room\nUser5", " "));
        al2.add(new Rooms("OPL room\nUser6", " "));
        al2.add(new Rooms("BIS2 room\nUser7", " "));
        aa = new RoomsAdapter(getApplicationContext(), R.layout.row_rooms, al);
        aa2 = new OccupiedAdapter(getApplicationContext(), R.layout.row_occupied, al2);
        lv.setAdapter(aa);
        gv.setAdapter(aa2);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ConfirmActivity.class);
                String roomChosen = al.get(position).getRoomName();
                i.putExtra("room", roomChosen);
                i.putExtra("startTime", startTime);
                i.putExtra("endTime", endTime);
                i.putExtra("desc", desc);
                i.putExtra("date", date);
                startActivity(i);
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(OpenRoomsActivity.this);
                alert.setTitle("Excellence room");
                alert.setMessage("Name: User5\nTime: 1600 to 1700\nPurpose:\nShort Discussion");
                alert.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.setNegativeButton("Close", null);
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
