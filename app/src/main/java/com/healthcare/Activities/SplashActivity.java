package com.healthcare.Activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.healthcare.R;
import com.healthcare.handlers.DBHandler;
import com.healthcare.other.PrefManager;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);

        final Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(5000);

                } catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {
                    Intent intent;
                    PrefManager manager=new PrefManager(getApplicationContext());
                    if(manager.isFirstTimeLaunch()){
                        intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    }
                    else if (DBHandler.isLoggedIn(getApplicationContext())) {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {

                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);

                    }


                }
            }

        };
        timer.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
