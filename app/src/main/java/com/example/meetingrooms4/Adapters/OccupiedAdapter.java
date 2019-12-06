package com.example.meetingrooms4.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingrooms4.Classes.Bookings;
import com.example.meetingrooms4.Classes.Rooms;
import com.example.meetingrooms4.R;

import java.util.ArrayList;

public class OccupiedAdapter extends RecyclerView.Adapter<OccupiedAdapter.ViewHolder> {

    private ArrayList<Bookings> room;
    private Context context;

    public OccupiedAdapter(Context context, ArrayList<Bookings> objects) {
        room = objects;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView user, time, desc, status, place;

        ViewHolder(View rowView) {
            super(rowView);
            place = rowView.findViewById(R.id.occupiedRoom);
            user = rowView.findViewById(R.id.occupiedName);
            time = rowView.findViewById(R.id.occupiedTime);
            desc = rowView.findViewById(R.id.occupiedDesc);
            status = rowView.findViewById(R.id.occupiedStatus);
        }
    }

    @NonNull
    @Override
    public OccupiedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_occupied, parent, false);
        return new OccupiedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OccupiedAdapter.ViewHolder holder, int position) {
        Bookings results = room.get(position);
        String userid = String.valueOf(results.getUser_id());
        String bksid = String.valueOf(results.getBks_id());
        holder.user.setText(userid);
        holder.desc.setText(results.getBook_purpose());
        holder.status.setText(bksid);
        holder.place.setText(results.getRoom_id());

        String startTime = results.getStart_time();
        String endTime = results.getEnd_time();
        holder.time.setText(startTime + " - " + endTime);

        if (holder.status.getText().toString().equalsIgnoreCase("Confirmed")) {
            holder.status.setTextColor(Color.parseColor("#000000"));
            holder.status.setBackgroundColor(Color.parseColor("#55EE55"));
        } else if (holder.status.getText().toString().equalsIgnoreCase("Pending")) {
            holder.status.setTextColor(Color.parseColor("#000000"));
            holder.status.setBackgroundColor(Color.parseColor("#ffb555"));
        } else {
            holder.status.setTextColor(Color.parseColor("#000000"));
            holder.status.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }
    }

    @Override
    public int getItemCount() {
        return room.size();
    }


}
