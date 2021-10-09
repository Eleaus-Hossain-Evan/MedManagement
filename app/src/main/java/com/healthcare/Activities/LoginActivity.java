package com.healthcare.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthcare.R;
import com.healthcare.handlers.DBHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;


    TextView register, tvLogin;
    String Email, Password;

    Button btnLogin;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("users");

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private Dialog loadingDialog;

    String TAG = "^^^TAG^^^";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(getApplicationContext());
        register = (TextView) findViewById(R.id.register);
        btnLogin = (Button) findViewById(R.id.button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetConnected())
                    invokeLogin(btnLogin);
                else
                    Snackbar.make(findViewById(R.id.loginLinearLayout), "Check your internet connection!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", null)
                            .show();

            }
        });
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/font.ttf");
        tvLogin.setTypeface(tf);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                finish();
                startActivity(intent);
            }
        });
        editTextEmail = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser) {
//            updateUI(currentUser);
//        }
//    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;
    }

    public void invokeLogin(View view) {
        Email = editTextEmail.getText().toString();
        Password = editTextPassword.getText().toString();
        if (Email != null && Password != null) {


                    login(Email, Password);

        } else {
            Snackbar.make(findViewById(R.id.loginLinearLayout), "Enter both the values!", Snackbar.LENGTH_LONG)
                    .setAction("Ok", null)
                    .show();
        }


    }

    private void login(final String Email, String Password) {

        class LoginAsync extends AsyncTask<String, Void, String> {



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(LoginActivity.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];
                String pass = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", uname));
                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = "success";

                try {
//                    HttpClient httpClient = new DefaultHttpClient();
//                    HttpPost httpPost = new HttpPost(
//                            "http://anigupta.esy.es/login.php");
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//
//                    HttpEntity entity = response.getEntity();
//
//                    is = entity.getContent();
//
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
//                    StringBuilder sb = new StringBuilder();
//
//                    String line = null;
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line + "\n");
//                    }
//                    result = sb.toString();

                    mAuth.signInWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        Toast.makeText(getApplication(), "LOG IN Successful", Toast.LENGTH_SHORT).show();
                                        firebaseUser = mAuth.getCurrentUser();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());

                                        Snackbar.make(findViewById(R.id.loginLinearLayout), "Authentication failed.", Snackbar.LENGTH_LONG)
                                                .setAction("Ok", null)
                                                .show();
                                    }
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(result);
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                String s = result.trim();

                if (s.equalsIgnoreCase("success")) {



                    DBHandler.setLoggedIn(true, getApplicationContext());
                    saveUserDetails(Email);


                } else {

                    editTextEmail.setError("Check Username!!");
                    editTextPassword.setError("Check Password!!");
                    loadingDialog.dismiss();


                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(Email, Password);

    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

    private void saveUserDetails(final String text) {

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot k : dataSnapshot.getChildren()) {
                    if (k.child("email").getValue().toString().equals(text) || k.child("username").getValue().toString().equals(text)) {
                        DBHandler.setName(k.child("name").getValue().toString(), LoginActivity.this);
                        DBHandler.setUserName(k.child("username").getValue().toString(), LoginActivity.this);
                        DBHandler.setPushId(k.getKey(),LoginActivity.this);
                        DBHandler.setPhone(k.child("phone").getValue().toString(),LoginActivity.this);
                    }
                }
                loadingDialog.dismiss();
                myRef.child(firebaseUser.getUid()).child("loggedIn").setValue("true");
                finish();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
