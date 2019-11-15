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

        //Title
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setTitle(Html.fromHtml("<font color='#000000'>By Room</font>"));

        roomsList = view.findViewById(R.id.roomsList);

        al = new ArrayList<Rooms>();
        al.clear();
        al.add(new Rooms("Faith Room", "8,No AV,https://cdn.discordapp.com/attachments/449905908248739850/611117360463740938/projJhin.png"));
        al.add(new Rooms("Integrity Room", "10,Has AV,https://cdn.discordapp.com/attachments/449905908248739850/582483927835344930/payday3.png"));
        al.add(new Rooms("Serenity Room", " "));
        al.add(new Rooms("Training Room", " "));
        al.add(new Rooms("Vigilance Room", " "));


        aa = new RoomsAdapter(getActivity(), R.layout.row_rooms, al);
        roomsList.setAdapter(aa);

        roomsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),RoomsActivity.class);
                startActivity(i);
            }
        });

        return view;
    }
}
