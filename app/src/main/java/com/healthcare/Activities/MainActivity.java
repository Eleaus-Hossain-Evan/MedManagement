package com.healthcare.Activities;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.healthcare.Fragments.HomeFragment;
import com.healthcare.Fragments.module_family_record.fam_frag1;
import com.healthcare.Fragments.module_pill_reminder.PillFragment;
import com.healthcare.Fragments.module_vaccination.VaccineFragment;
import com.healthcare.Fragments.module_visitScheduler.FragDocVisit;
import com.healthcare.R;
import com.healthcare.handlers.DBHandler;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ActionBarDrawerToggle actionBarDrawerToggle;

    Button btnLogin;
    TextView drawerId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("users");

    ActionBarDrawerToggle toggle;
    public static Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.drawer_close);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close) {
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View localView = this.navigationView.getHeaderView(0);
        TextView localTextView2 = (TextView) localView.findViewById(R.id.drawerId);
        localTextView2.setText(DBHandler.getName(this));



        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new HomeFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String showName = "Anonymous";
            if (user.getDisplayName() != null){
                showName = user.getDisplayName();
            }else {
                showName = user.getEmail();
            }
            drawerId = findViewById(R.id.drawerId);
            if (!showName.isEmpty()){
                drawerId.setText(showName);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            DBHandler.clearDb(getApplicationContext());
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
        navigationView.getMenu().getItem(2).setChecked(false);
        navigationView.getMenu().getItem(3).setChecked(false);
        navigationView.getMenu().getItem(4).setChecked(false);
        navigationView.getMenu().getItem(5).setChecked(false);
        switch (item.getItemId()) {
            case R.id.nav_Home:

                navigationView.getMenu().getItem(0).setChecked(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new HomeFragment())
                        .commit();
                break;
            case R.id.nav_vaccine:
                navigationView.getMenu().getItem(1).setChecked(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new VaccineFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                //toggle.setDrawerIndicatorEnabled(false);
                break;
            case R.id.nav_visit:
                navigationView.getMenu().getItem(4).setChecked(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new FragDocVisit())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;
            case R.id.nav_pill_reminder:
                navigationView.getMenu().getItem(2).setChecked(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new PillFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;
            case R.id.nav_family:
                navigationView.getMenu().getItem(3).setChecked(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new fam_frag1())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;

            case R.id.nav_ambulance:
                navigationView.getMenu().getItem(5).setChecked(true);
                Intent intent1 = new Intent(this, MapsActivity.class);
                startActivity(intent1);
            case R.id.nav_logout:
                DBHandler.clearDb(getApplicationContext());
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(this, LoginActivity.class);
                startActivity(intent2);

                String push=DBHandler.getPushId(this);
                myRef.child(push).child("loggedIn").setValue("false");
                finish();
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }
}


