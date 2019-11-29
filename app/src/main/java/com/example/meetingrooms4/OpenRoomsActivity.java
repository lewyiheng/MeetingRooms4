package com.example.meetingrooms4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.meetingrooms4.Adapters.OccupiedAdapter;
import com.example.meetingrooms4.Adapters.OccupiedTimeAdapter;
import com.example.meetingrooms4.Adapters.RoomsAdapter;
import com.example.meetingrooms4.Classes.Bookings;
import com.example.meetingrooms4.Classes.Rooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.model.value.ServerTimestampValue;
import com.google.firestore.v1beta1.DocumentTransform;

import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class OpenRoomsActivity extends AppCompatActivity {

    ArrayList<Rooms> al;
    ArrayList<Bookings> al2;
    ArrayAdapter aa;
    ListView lv;
    TextView tvTimeInfo;
    RecyclerView rv;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference bookings = db.collection("booking");
    CollectionReference room = db.collection("room");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_rooms);

        lv = findViewById(R.id.openList);
        tvTimeInfo = findViewById(R.id.openTimeInfo);
        rv = findViewById(R.id.openRv);

        centerTitle("Available Rooms");

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
        al2.clear();

        //Available Rooms
//        al.add(new Rooms("Excellence room", "10,WW05-5, "));
//        al.add(new Rooms("Faith room", "12,AW05-1,AV"));
//        al.add(new Rooms("Integrity room", "16,WW05-2,AV"));
//        al.add(new Rooms("Serenity room", "8,WW05-1, "));
//        al.add(new Rooms("Training room", "18,WW05-4,AV"));
//        al.add(new Rooms("Vigilance room", "10,WW05-3,AV"));

        //Occupied Rooms
//        al2.add(new Bookings("User4", "Courage room", "1300", "1400", "Today", "Short Meeting", "Confirmed"));
//        al2.add(new Bookings("User5", "Perseverance room", "1300", "1400", "Today", "Short Meeting", "Pending"));
//        al2.add(new Bookings("User6", "OPL room", "1300", "1400", "Today", "Short Meeting", "Expired"));
//        al2.add(new Bookings("User7", "BIS2 room", "1300", "1400", "Today", "Short Meeting", "Cancelled"));

        room.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String roomName = document.getData().get("roomName").toString();
                        String location = document.getId();
                        String capacityString = document.getData().get("capacity").toString();
                        int capacity = Integer.parseInt(capacityString);
                        String av = document.getData().get("description").toString();
                        String group = document.getData().get("roomGroup").toString();
                        al.add(new Rooms(roomName, capacity, av, group, location));
                    }
                }
                Collections.sort(al, new Comparator<Rooms>() {
                    @Override
                    public int compare(Rooms o1, Rooms o2) {
                        return o1.getRoomName().compareToIgnoreCase(o2.getRoomName());
                    }
                });
                aa = new RoomsAdapter(getApplicationContext(), R.layout.row_rooms, al);
                lv.setAdapter(aa);
            }
        });
//        RoomsAdapter aa = new RoomsAdapter(getApplicationContext(), R.layout.row_rooms, al);
//        lv.setAdapter(aa); //Set available rooms


//        rv.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false));
//        OccupiedAdapter aa2 = new OccupiedAdapter(getApplicationContext(), al2);
//        rv.setAdapter(aa2); //Set occupied rooms

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(getApplicationContext(), ConfirmActivity.class);
//                String roomChosen = al.get(position).getRoomName();
//                i.putExtra("room", roomChosen);
//                i.putExtra("startTime", startTime);
//                i.putExtra("endTime", endTime);
//                i.putExtra("desc", desc);
//                i.putExtra("date", date);
//                startActivity(i);
//            }
//        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String roomChosen = al.get(position).getRoomName();
                String msg = "Room: " + roomChosen + "\n"
                        + "Date: " + date + "\n"
                        + "Time: " + startTime + " - " + endTime + "\n"
                        + "Purpose: " + "\n"
                        + desc + "\n";
                AlertDialog.Builder alert = new AlertDialog.Builder(OpenRoomsActivity.this);
                alert.setTitle("Confirm booking?");
                alert.setMessage(msg);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("frag", "fragBookings");
                        startActivity(i);

                        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                        Date date1 = format.parse(date,new ParsePosition(0));
                        Bookings book = new Bookings("User1","WW05-1",startTime,endTime,date1,desc,"Pending");
                        bookings.document("000000").set(book);
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
