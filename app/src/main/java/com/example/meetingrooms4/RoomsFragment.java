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

import java.util.ArrayList;

public class RoomsFragment extends Fragment {

    ArrayList<Rooms> al;
    ArrayAdapter aa;
    ListView roomsList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_rooms, container, false);

        //View
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setTitle(Html.fromHtml("<font color='#000000'>By Room</font>"));

        roomsList = view.findViewById(R.id.roomsList);

        al = new ArrayList<Rooms>();
        al.clear();
        al.add(new Rooms("BIS2 discussion room", "6,AE05, "));
        al.add(new Rooms("Courage room", "12,AE05-2,AV"));
        al.add(new Rooms("Excellence room", "10,WW05-5, "));
        al.add(new Rooms("Faith room", "12,AW05-1,AV"));
        al.add(new Rooms("Integrity room", "16,WW05-2,AV"));
        al.add(new Rooms("OPL discussion Room", "8,WW05, "));
        al.add(new Rooms("Perseverance room", "12,AE05-1,AV"));
        al.add(new Rooms("Serenity room", "8,WW05-1, "));
        al.add(new Rooms("Training room", "18,WW05-4,AV"));
        al.add(new Rooms("Vigilance room", "10,WW05-3,AV"));

        //Set rooms list
        aa = new RoomsAdapter(getActivity(), R.layout.row_rooms, al);
        roomsList.setAdapter(aa);

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
