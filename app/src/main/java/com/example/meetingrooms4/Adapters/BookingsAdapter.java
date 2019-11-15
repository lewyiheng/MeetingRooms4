package com.example.meetingrooms4.Adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.meetingrooms4.Classes.Bookings;
import com.example.meetingrooms4.MainActivity;
import com.example.meetingrooms4.R;

import java.util.ArrayList;
import java.util.Calendar;

public class BookingsAdapter extends ArrayAdapter<Bookings> {

    private ArrayList<Bookings> bookings;
    private Context context;
    private TextView date, time, place, user, desc;
    private Button btn;

    public BookingsAdapter(Context context, int resource, ArrayList<Bookings> objects) {
        super(context, resource, objects);
        bookings = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_bookings, parent, false);

        Bookings results = bookings.get(position);

        date = rowView.findViewById(R.id.bookDate);
        time = rowView.findViewById(R.id.bookTime);
        place = rowView.findViewById(R.id.bookRoom);
        btn = rowView.findViewById(R.id.bookNowDelete);
        desc = rowView.findViewById(R.id.bookDesc);

        date.setText(results.getDate());

        if (results.getDesc().equalsIgnoreCase(" ")) {
            desc.setText(" ");
        } else {
            desc.setText("\"" + results.getDesc() + "\"");
        }
        //Set time
        String startTime = results.getStartTime();
        String endTime = results.getEndTIme();
        String duration = startTime + " - " + endTime;

        time.setText(duration);
        place.setText(results.getRoom());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Confirm release?");
                alert.setMessage("Are you sure you want to release the room?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        bookings.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Room has been released", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();


            }
        });

        return rowView;
    }
}
