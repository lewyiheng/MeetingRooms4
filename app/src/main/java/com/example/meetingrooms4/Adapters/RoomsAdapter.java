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
import com.example.meetingrooms4.OpenRoomsActivity;
import com.example.meetingrooms4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RoomsAdapter extends ArrayAdapter<Rooms> {

    private ArrayList<Rooms> room;
    private Context context;
    private TextView roomName, roomDesc, roomAV, roomPAX;
    private String roomId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference colRef = db.collection("Room");

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

        int capacity = results.getRoom_capacity();

        String capacityString = String.valueOf(capacity);
        roomName.setText(results.getRoom_name());
        roomDesc.setText(results.getRoom_status());
        roomPAX.setText(capacityString);
        roomAV.setText(results.getRoom_description());
        notifyDataSetChanged();

        return rowView;

    }
}