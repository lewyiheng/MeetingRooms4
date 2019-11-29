package com.example.meetingrooms4.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meetingrooms4.Classes.Rooms;
import com.example.meetingrooms4.R;

import java.util.ArrayList;

public class RoomsAdapter extends ArrayAdapter<Rooms> {

    private ArrayList<Rooms> room;
    private Context context;
    private TextView roomName,roomDesc,roomAV,roomPAX;

    public RoomsAdapter(Context context, int resource, ArrayList<Rooms> objects) {
        super(context, resource, objects);
        room = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_rooms, parent, false);

        roomName = rowView.findViewById(R.id.name);
        roomDesc = rowView.findViewById(R.id.desc);
        roomAV = rowView.findViewById(R.id.descAV);
        roomPAX = rowView.findViewById(R.id.pax);

        Rooms results = room.get(position);

        int capacity = results.getCapacity();
        String capacityString = String.valueOf(capacity);

        roomName.setText(results.getRoomName());
        roomDesc.setText(results.getLocation());
        roomPAX.setText(capacityString);
        roomAV.setText(results.getDescription());

        return rowView;

    }
}