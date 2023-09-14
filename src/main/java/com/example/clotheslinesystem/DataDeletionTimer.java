package com.example.clotheslinesystem;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class DataDeletionTimer {

    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                deleteDataFromFirebase();
            }
        };

        // Set the timer to run every desired interval (e.g., every 24 hours)
       // long intervalMillis = 24 * 60 * 60 * 1000; // 24 hours
       // long intervalMillis = 5 * 1000; // 5 seconds
        long intervalMillis = 7 * 24 * 60 * 60 * 1000; // 7 days
        timer.scheduleAtFixedRate(timerTask, intervalMillis, intervalMillis);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void deleteDataFromFirebase() {
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference("Data1");

        // Delete data
        dataRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("DataDeletionTimer", "Data deleted successfully");
                } else {
                    Log.e("DataDeletionTimer", "Failed to delete data: " + task.getException());
                }
            }
        });
    }
}
