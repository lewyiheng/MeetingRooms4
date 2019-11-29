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


//        Rooms city = new Rooms("BIS2 discussion room", 6, " ","ISG", "AE05");
//        Rooms city1 = new Rooms("Courage room", 12, "AV equipped","ISG", "AE05-2");
//        Rooms city2 = new Rooms("Excellence room", 10, " ","ISG", "WW05-5");
//        Rooms city3 = new Rooms("Faith room", 12, "AV equipped","ISG", "AW05-1");
//        Rooms city4 = new Rooms("OPL discussion room", 8, " ","ISG", "WW05");
//        Rooms city5 = new Rooms("Perseverance room", 12, "AV equipped","ISG", "AE05-1");
//        Rooms city6 = new Rooms("Training centre", 6, "AV equipped","ISG", "WW05-4");
//
//        db.collection("room").document(city.getLocation()).set(city);
//        db.collection("room").document(city1.getLocation()).set(city1);
//        db.collection("room").document(city2.getLocation()).set(city2);
//        db.collection("room").document(city3.getLocation()).set(city3);
//        db.collection("room").document(city4.getLocation()).set(city4);
//        db.collection("room").document(city5.getLocation()).set(city5);
//        db.collection("room").document(city6.getLocation()).set(city6);
        al = new ArrayList<Rooms>();
        al.clear();

        //Set rooms list
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
                aa = new RoomsAdapter(getActivity(), R.layout.row_rooms, al);
                roomsList.setAdapter(aa);
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

                String roomChosen = al.get(position).getRoomName();
                Intent i = new Intent(getActivity(), RoomsActivity.class);
                i.putExtra("room", roomChosen);
                startActivity(i);
            }
        });

        return view;
    }
}
