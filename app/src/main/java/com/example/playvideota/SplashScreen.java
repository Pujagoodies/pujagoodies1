package com.example.playvideota;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashScreen extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();


        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(4000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {

                    Intent intent = new Intent(SplashScreen.this, AuthActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(currentUser != null){
//            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//            startActivity(intent);
//        }
//
//    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}