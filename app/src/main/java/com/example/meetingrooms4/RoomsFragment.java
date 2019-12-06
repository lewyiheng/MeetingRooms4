package com.example.meetingrooms4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.meetingrooms4.Adapters.RoomsAdapter;
import com.example.meetingrooms4.Classes.Rooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RoomsFragment extends Fragment {

    ArrayList<Rooms> al;
    ArrayAdapter aa;
    ListView roomsList;

   // String roomid;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference room = db.collection("room");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_rooms, container, false);

        //View
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setTitle(Html.fromHtml("<font color='#000000'>By Room</font>"));

        roomsList = view.findViewById(R.id.roomsList);


//        Rooms city = new Rooms("BIS2 discussion room", 6, " ", "ISG", "Available");
//        Rooms city1 = new Rooms("Courage room", 12, "AV", "ISG", "Available");
//        Rooms city2 = new Rooms("Excellence room", 10, " ", "ISG", "Available");
//        Rooms city3 = new Rooms("Faith room", 12, "AV", "ISG", "Available");
//        Rooms city7 = new Rooms("Integrity room", 16, "AV", "ISG", "Available");
//        Rooms city4 = new Rooms("OPL discussion room", 8, " ", "ISG", "Available");
//        Rooms city5 = new Rooms("Perseverance room", 12, "AV", "ISG", "Available");
//        Rooms city8 = new Rooms("Serenity room", 8, "AV", "ISG", "Available");
//        Rooms city6 = new Rooms("Training centre", 6, "AV", "ISG", "Available");
//
//        db.collection("room").document("AE05").set(city);
//        db.collection("room").document("AE05-2").set(city1);
//        db.collection("room").document("WW05-5").set(city2);
//        db.collection("room").document("AW05-1").set(city3);
//        db.collection("room").document("WW05").set(city4);
//        db.collection("room").document("AE05-1").set(city5);
//        db.collection("room").document("WW05-4").set(city6);
//        db.collection("room").document("WW05-2").set(city7);
//        db.collection("room").document("WW05-1").set(city8);

        al = new ArrayList<Rooms>();
        al.clear();

        //Set rooms list
        room.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String avail = document.getData().get("room_status").toString();
                        if (avail.equalsIgnoreCase("Available")) {
                            String roomName = document.getData().get("room_name").toString();
                            String roomid = document.getId();
                            String capacityString = document.getData().get("room_capacity").toString();
                            int capacity = Integer.parseInt(capacityString);
                            String av = document.getData().get("room_description").toString();
                            String group = document.getData().get("room_group").toString();
                            al.add(new Rooms(roomName, capacity, av, group, roomid));
                        }
                    }
                }
                Collections.sort(al, new Comparator<Rooms>() {
                    @Override
                    public int compare(Rooms o1, Rooms o2) {
                        return o1.getRoom_name().compareToIgnoreCase(o2.getRoom_name());
                    }
                });
                if (isAdded()) {
                    aa = new RoomsAdapter(getActivity(), R.layout.row_rooms, al);
                    roomsList.setAdapter(aa);
                    aa.notifyDataSetChanged();
                }
            }
        });

//        al.add(new Rooms("BIS2 discussion room", "6,AE05, "));
//        al.add(new Rooms("Courage room", "12,AE05-2,AV"));
//        al.add(new Rooms("Excellence room", "10,WW05-5, "));
//        al.add(new Rooms("Faith room", "12,AW05-1,AV"));
//        al.add(new Rooms("Integrity room", "16,WW05-2,AV"));
//        al.add(new Rooms("OPL discussion Room", "8,WW05, "));
//        al.add(new Rooms("Perseverance room", "12,AE05-1,AV"));
//        al.add(new Rooms("Serenity room", "8,WW05-1, "));
//        al.add(new Rooms("Training room", "18,WW05-4,AV"));
//        al.add(new Rooms("Vigilance room", "10,WW05-3,AV"));

        //Set room click
        roomsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String roomChosen = al.get(position).getRoom_name();
                String roomid = al.get(position).getRoom_status();
                Intent i = new Intent(getActivity(), RoomsActivity.class);
                i.putExtra("room", roomChosen);
                i.putExtra("roomid",roomid);
                startActivity(i);
            }
        });

        return view;
    }
}
