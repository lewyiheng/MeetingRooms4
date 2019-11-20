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

public class OccupiedAdapter extends ArrayAdapter<Rooms> {

    private ArrayList<Rooms> room;
    private Context context;
    private TextView roomName;

    public OccupiedAdapter(Context context, int resource, ArrayList<Rooms> objects) {
        super(context, resource, objects);
        room = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_occupied, parent, false);

        Rooms results = room.get(position);

        roomName = rowView.findViewById(R.id.name);
        roomName.setText(results.getRoomName());

        return rowView;

    }
}
