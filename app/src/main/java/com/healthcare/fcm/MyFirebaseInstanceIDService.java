package com.healthcare.fcm;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.healthcare.other.PrefManager;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);

        String refreshedToken;

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(
                new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String refreshedToken = task.getResult();
                        // Saving reg id to shared preferences
                        PrefManager.setToken(refreshedToken,getApplicationContext());

                        // sending reg id to your server
                        sendRegistrationToServer(refreshedToken);

                        // Notify UI that registration has completed, so the progress indicator can be hidden.
                        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
                        registrationComplete.putExtra("token", refreshedToken);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
                    }
                }
        );

    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.v(TAG, "sendRegistrationToServer: " + token);
    }


}