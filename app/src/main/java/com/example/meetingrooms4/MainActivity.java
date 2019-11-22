package com.example.meetingrooms4;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        centerTitle("MainActivity");

        //View
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

//        //Bottom Navigation
//        bottomNav.setOnNavigationItemSelectedListener(navListener);
//        Fragment f = new BookNowFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
//        bottomNav.setSelectedItemId(R.id.navBookNow);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Intent i = getIntent();
        String frag = i.getStringExtra("frag");
        if (frag == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BookNowFragment()).commit();
        } else if (frag.equalsIgnoreCase("fragBookings")) {
            Fragment f = new AllBookingsFragment();
            bottomNav.setSelectedItemId(R.id.navBookings);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();


        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navBookNow:
                    selectedFragment = new BookNowFragment();
                    break;
                case R.id.navDate:
                    selectedFragment = new DateFragment();
                    break;
                case R.id.navRoom:
                    selectedFragment = new RoomsFragment();
                    break;
                case R.id.navBookings:
                    selectedFragment = new AllBookingsFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

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
}
