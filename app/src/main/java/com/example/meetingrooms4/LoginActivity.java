package com.example.meetingrooms4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.meetingrooms4.Classes.Bookings_Insert;
import com.example.meetingrooms4.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText id, pass;
    ArrayList<User> al = new ArrayList<>();
    ArrayList<String> al3 = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userDb = db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

////Check if came from notif
//        Intent i = getIntent();
//        final String userID = i.getStringExtra("userID");
//        final String roomChosen = i.getStringExtra("room");
//        final String startTime = i.getStringExtra("startTime");
//        if (userID != null) {
//            final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
//            alert.setTitle("Confirm/Cancel booking?");
//            alert.setMessage("Reservation for " + roomChosen + " at " + startTime);
//            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                    i.putExtra("frag", "fragBookings");
//                    SharedPreferences sp = getApplicationContext().getSharedPreferences("sp", 0);
//                    SharedPreferences.Editor e = sp.edit();
//                    e.putInt("id", Integer.parseInt(userID));
//                    e.apply();
//                    startActivity(i);
//
//                }
//            });
//            alert.setNegativeButton("No", null);
//            alert.show();
//        }

//Hide action bar
        getSupportActionBar().hide();

        login = findViewById(R.id.login);
        id = findViewById(R.id.loginId);
        pass = findViewById(R.id.loginPass);

//LOGIN button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReadUser(new GetUser() {
                    @Override
                    public void onCallback(ArrayList<User> userList, ArrayList<String> IDal) {
                        al = userList;
                        al3 = IDal;
                        ArrayList<User> al2 = new ArrayList<>();
                        String loginId = id.getText().toString(); //Get ID
                        String loginPass = pass.getText().toString(); //Get password
                        String ayDee = new String();
                        String username = new String();
                        al2.clear();

                        for (int i = 0; al.size() > i; i++) {
                            if (loginId.equalsIgnoreCase(al.get(i).getUsername())) {//Check if username is right
                                if (al.get(i).getPassword().contains(loginPass)) { //Check if password is right
                                    al2.add(al.get(i));
                                    username = al.get(i).getName();
                                    ayDee = al3.get(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Password is wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Username is wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

//If the account is correct
                        if (al2.size() > 0) {
                            SharedPreferences sp = getApplicationContext().getSharedPreferences("sp", 0);
                            SharedPreferences.Editor e = sp.edit();
                            int idid = Integer.parseInt(ayDee);
                            e.putInt("id", idid);
                            e.putString("idString", ayDee);//Put login ID
                            e.apply();
                            Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i2);
                            finish();

                            Toast.makeText(getApplicationContext(), "Welcome, " + username, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

//Interface for USER DB
    private void ReadUser(final GetUser gu) {

        userDb.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<User> al = new ArrayList<>();
                ArrayList<String> IDal = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String user = doc.getData().get("username").toString();
                    String pass = doc.getData().get("password").toString();
                    String role = doc.getData().get("role").toString();
                    int phone = Integer.parseInt(doc.getData().get("mobile_number").toString());
                    int officePhone = Integer.parseInt(doc.getData().get("office_number").toString());
                    String name = doc.getData().get("name").toString();
                    int employment = Integer.parseInt(doc.getData().get("employment_status").toString());
                    String groupName = doc.getData().get("group_name").toString();

                    IDal.add(doc.getId());
                    al.add(new User(user, pass, role, phone, officePhone, name, employment, groupName));
                }
                gu.onCallback(al, IDal);
            }
        });
    }

    private interface GetUser {
        void onCallback(ArrayList<User> userList, ArrayList<String> IDal);
    }
}
