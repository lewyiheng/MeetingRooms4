package com.example.meetingrooms4.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meetingrooms4.Classes.Bookings;
import com.example.meetingrooms4.Classes.Rooms;
import com.example.meetingrooms4.R;

import java.util.ArrayList;

public class OccupiedAdapter extends ArrayAdapter<Bookings> {

    private ArrayList<Bookings> room;
    private Context context;
    private TextView place,user,time,desc;

    public OccupiedAdapter(Context context, int resource, ArrayList<Bookings> objects) {
        super(context, resource, objects);
        room = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_occupied, parent, false);

        Bookings results = room.get(position);

        place = rowView.findViewById(R.id.occupiedRoom);
        user = rowView.findViewById(R.id.occupiedName);
        time = rowView.findViewById(R.id.occupiedTime);
        desc = rowView.findViewById(R.id.occupiedDesc);

        place.setText(results.getRoom());
        user.setText(results.getUser());

        String startTime = results.getStartTime();
        String endTime = results.getEndTIme();
        String timing = startTime + " - " + endTime;

        time.setText(timing);
        desc.setText(results.getDesc());

        return rowView;

    }
}
