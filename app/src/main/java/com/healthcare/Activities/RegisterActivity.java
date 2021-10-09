package com.healthcare.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.healthcare.R;
import com.healthcare.User;
import com.healthcare.other.PrefManager;

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
import java.util.concurrent.Executor;


public class RegisterActivity extends AppCompatActivity {

    EditText etFirstName, etLastName, etPassword, etPasswordAgain, etEmail, etPhone;
    String FirstName, LastName, Password, PasswordAgain, Email;

    TextView tvRegister;
    Button btnRegister;
    String Phone;

    String TAG = "^^^TAG^^^";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("users").push();
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetConnected())
                    invokeRegister(btnRegister);
                else
                    Snackbar.make(findViewById(R.id.registerLinearLayout), "Check your internet connection!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", null)
                            .show();

            }

        });

        etFirstName = (EditText) findViewById(R.id.editTextName);
        etLastName = (EditText) findViewById(R.id.editTextUserName);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
        etPasswordAgain = (EditText) findViewById(R.id.editTextPasswordAgain);
        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etPhone = (EditText) findViewById(R.id.editTextPhone);

        tvRegister = (TextView) findViewById(R.id.tvRegister);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/font.ttf");
        tvRegister.setTypeface(tf);


    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
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

    public void invokeRegister(View view) {
        FirstName = etFirstName.getText().toString();
        LastName = etLastName.getText().toString();
        Password = etPassword.getText().toString();
        PasswordAgain = etPasswordAgain.getText().toString();
        Email = etEmail.getText().toString();
        Phone = etPhone.getText().toString();

        if (!(FirstName.equals("") || LastName.equals("") || Password.equals("") || PasswordAgain.equals("") || Email.equals("") || Phone.equals(""))) {
            if (Phone.length() < 10) {
                Snackbar.make(findViewById(R.id.registerLinearLayout), "Enter valid phone number!", Snackbar.LENGTH_LONG)
                        .setAction("Ok", null)
                        .show();
            } else if (Password.equals(PasswordAgain)) {
                if (isValidEmail(Email)) {

                    register();

                } else {
                    etEmail.setText("");
                    etEmail.setError("Enter valid email!");

                    Snackbar.make(findViewById(R.id.registerLinearLayout), "Enter valid email!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", null)
                            .show();


                }

            } else {
                etPassword.setText("");
                etPasswordAgain.setText("");
                etPasswordAgain.setError("Passwords do not match!");
                Snackbar.make(findViewById(R.id.registerLinearLayout), "Passwords do not match!", Snackbar.LENGTH_LONG)
                        .setAction("Ok", null)
                        .show();

            }


        } else
            Snackbar.make(findViewById(R.id.registerLinearLayout), "Please enter all the details!", Snackbar.LENGTH_LONG)
                    .setAction("Ok", null)
                    .show();

    }

    private void register() {
        class RegisterAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(RegisterActivity.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String name = params[0];
                String uname = params[1];
                String pass = params[2];
                String email = params[3];
                String phone = params[4];


                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", uname));
                nameValuePairs.add(new BasicNameValuePair("password", pass));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("phone", phone));

                String result = null;

                try {
//                    HttpClient httpClient = new DefaultHttpClient();
//                    //HttpPost httpPost = new HttpPost(
//                            "http://anigupta.esy.es/register.php");
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

                    mAuth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        Toast.makeText(getApplication(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                        firebaseUser = mAuth.getCurrentUser();
                                        assert firebaseUser != null;
                                        user = new User(Email,FirstName,LastName,firebaseUser.getUid(),Phone,false,"None");
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });


                } catch(Exception e) {
                    e.printStackTrace();
                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                String s = result.trim();
                loadingDialog.dismiss();
                if (s.equalsIgnoreCase("success")) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

//                    myRef.child("email").setValue(Email);
//                    myRef.child("lastname").setValue(LastName);
//                    myRef.child("firstname").setValue(FirstName);
//                    myRef.child("phone").setValue(Phone);
//                    myRef.child("friends").setValue("None");
//                    myRef.child("loggedIn").setValue("false");
//                    myRef.child("token").setValue(PrefManager.getToken());
                    myRef.child(firebaseUser.getUid()).setValue(user);

                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
            }
        }

        RegisterAsync la = new RegisterAsync();
        la.execute(FirstName, LastName, Password, Email, Phone);

    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }


    boolean isValidEmail(String e) {
        for (int i = 0; i < e.length(); i++) {
            if (e.charAt(i) == '@')
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }


}
