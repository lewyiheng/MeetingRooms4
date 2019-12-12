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

import com.example.meetingrooms4.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText id, pass;
    ArrayList<User> al = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userDb = db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        centerTitle(" ");

        login = findViewById(R.id.login);
        id = findViewById(R.id.loginId);
        pass = findViewById(R.id.loginPass);

        getSupportActionBar().hide();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReadUser(new GetUser() {
                    @Override
                    public void onCallback(ArrayList<User> userList, String id1) {
                        al = userList;
                        String loginId = id.getText().toString(); //Get ID
                        String loginPass = pass.getText().toString(); //Get password

                        for (int i = 0; al.size() > i; i++) {
                            if (al.get(i).getUsername().contains(loginId)) {
                                if (al.get(i).getPassword().contains(loginPass)) {


                                    SharedPreferences sp = getApplicationContext().getSharedPreferences("sp", 0);
                                    SharedPreferences.Editor e = sp.edit();
                                    e.putString("id", id1); //Put login ID
                                    e.commit();
                                    Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i2);

                                    Toast.makeText(getApplicationContext(), "Welcome, " + al.get(i).getName() , Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(getApplicationContext(), "Password is wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Username is wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }

    private void centerTitle(String title) {
        ArrayList<View> textViews = new ArrayList<>();

        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

        if (textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if (textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for (View v : textViews) {
                    if (v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }

            if (appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.actionbar));
        ab.setTitle(Html.fromHtml("<font color='#000000'>" + title + " </font>"));
    }

    private void ReadUser(final GetUser gu) {

        userDb.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<User> al = new ArrayList<>();
                String idl = new String();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String user = doc.getData().get("username").toString();
                    String pass = doc.getData().get("password").toString();
                    String role = doc.getData().get("role").toString();
                    int phone = Integer.parseInt(doc.getData().get("mobile_number").toString());
                    int officePhone = Integer.parseInt(doc.getData().get("office_number").toString());
                    String name = doc.getData().get("name").toString();
                    int employment = Integer.parseInt(doc.getData().get("employment_status").toString());
                    String groupName = doc.getData().get("group_name").toString();

                    idl = doc.getId();

                    al.add(new User(user, pass, role, phone, officePhone, name, employment, groupName));
                }
                gu.onCallback(al,idl);
            }
        });
    }

    private interface GetUser {
        void onCallback(ArrayList<User> userList, String id1);
    }
}
