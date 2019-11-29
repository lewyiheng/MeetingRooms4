package com.example.meetingrooms4.Adapters;

import android.content.Context;
import android.content.DialogInterface;
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
import com.example.meetingrooms4.R;

import java.util.ArrayList;

public class TimingAdapter extends ArrayAdapter<Bookings> {

    private ArrayList<Bookings> bookings;
    private Context context;
    private TextView date, time, place, user, desc;
    private Button btn;

    public TimingAdapter(Context context, int resource, ArrayList<Bookings> objects) {
        super(context, resource, objects);
        bookings = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_timings, parent, false);

        final Bookings results = bookings.get(position);

        time = rowView.findViewById(R.id.timingTime);
        user = rowView.findViewById(R.id.timingUser);
        btn = rowView.findViewById(R.id.timingCall);
        desc = rowView.findViewById(R.id.timingDesc);


        if (results.getDesc().equalsIgnoreCase(" ")) {
            desc.setText(" ");
        } else {
            desc.setText("\"" + results.getDesc() + "\"");
        }


        //Set time
        String startTime = results.getStartTime();
        String endTime = results.getEndTime();
        String duration = startTime + " - " + endTime;

        time.setText(duration);
        user.setText(results.getUser());

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder alert = new AlertDialog.Builder(context);
//                alert.setTitle("Call " + results.getUser() + " ?");
//                alert.setMessage("Are you sure you want to release the room?");
//                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        bookings.remove(position);
//                        notifyDataSetChanged();
//                        Toast.makeText(context, "Room has been released", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                alert.setNegativeButton("No", null);
//                alert.show();
//
//
//            }
//        });
        return rowView;
    }
}
