package com.example.meetingrooms4.Adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.meetingrooms4.AllBookingsFragment;
import com.example.meetingrooms4.Classes.Bookings;
import com.example.meetingrooms4.MainActivity;
import com.example.meetingrooms4.R;
import com.example.meetingrooms4.RoomsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class BookingsAdapter extends ArrayAdapter<Bookings> {

    private ArrayList<Bookings> bookings;
    private Context context;
    private TextView date, time, place, user, desc, status;
    private Button btn, confirm;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef = db.collection("booking");
    private String id;


    public BookingsAdapter(Context context, int resource, ArrayList<Bookings> objects) {
        super(context, resource, objects);
        bookings = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_bookings, parent, false);

        final Bookings results = bookings.get(position);

        date = rowView.findViewById(R.id.bookDate);
        time = rowView.findViewById(R.id.bookTime);
        place = rowView.findViewById(R.id.bookRoom);
        btn = rowView.findViewById(R.id.bookNowDelete);
        desc = rowView.findViewById(R.id.bookDesc);
        status = rowView.findViewById(R.id.bookStatus);
        confirm = rowView.findViewById(R.id.bookNowConfirm);

        //Today's date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String today = sdf.format(c);

        date.setText(results.getBook_date().toString());
        status.setText(results.getBks_id());

        if (status.getText().toString().equalsIgnoreCase("Confirmed")) {
            status.setTextColor(Color.parseColor("#000000"));
            status.setBackgroundColor(Color.parseColor("#55EE55"));
        } else if (status.getText().toString().equalsIgnoreCase("Pending")) {
            status.setTextColor(Color.parseColor("#000000"));
            status.setBackgroundColor(Color.parseColor("#ffb555"));
        } else {
            status.setTextColor(Color.parseColor("#000000"));
            status.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }

        if (results.getBook_purpose().equalsIgnoreCase(" ")) {
            desc.setText(" ");
        } else {
            desc.setText("\"" + results.getBook_purpose() + "\"");
        }


        //Set time
        String startTime = results.getStart_time();
        String endTime = results.getEnd_time();
        String duration = startTime + " - " + endTime;

        place.setText(results.getRoom_id());
        time.setText(duration);


        if (status.getText().toString().equalsIgnoreCase("Confirmed")) {
            confirm.setVisibility(View.GONE);
        } else {
            if (date.getText().toString().equalsIgnoreCase(today)) {
                confirm.setVisibility(View.VISIBLE);
            }
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pos = String.format("%6s", position + 1).replace(' ', '0');
                colRef.document(pos).update("bks_id", 2);
                confirm.setVisibility(View.GONE);
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Confirm release?");
                alert.setMessage("Are you sure you want to release the room?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //bookings.remove(position);
                        String pos = String.format("%6s", position + 1).replace(' ', '0');

                        colRef.document(pos).update("bks_id", 4);

                        Toast.makeText(context, "Room has been released", Toast.LENGTH_SHORT).show();

                        //status.setText("Cancelled");
                        notifyDataSetChanged();

                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
            }
        });
        return rowView;
    }
}
