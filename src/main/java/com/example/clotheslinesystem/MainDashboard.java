package com.example.clotheslinesystem;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainDashboard extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private ProgressBar progressBar;
    private TextView textView;

    private String previousHumid = "";
    private String previousRain = "";
    private String previousRemarks = "";
    private String previousTemp = "";

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.battery);

        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.circle, null));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(Color.parseColor("#273B69"));
        }

        Button historyButton = findViewById(R.id.historybtn);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainDashboard.this, MainHistory.class);
                startActivity(intent);
            }
        });

        Button anotherHistoryButton = findViewById(R.id.notifbtn);
        anotherHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainDashboard.this, MainNotification.class);
                startActivity(intent);
            }
        });

        // BATTERY PERCENTAGE
        database = FirebaseDatabase.getInstance();
        DatabaseReference batteryRef = database.getReference("Battery");

        batteryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String batteryLevel = snapshot.getValue(String.class);

                if (batteryLevel != null) {
                    int level = Integer.parseInt(batteryLevel);
                    progressBar.setProgress(level);
                    textView.setText(batteryLevel + "%");

                    if (level == 50 && level >= 40) {
                        showBatteryNotification("Warning", "50% battery remaining.");
                    } else if (level < 40) {
                        showBatteryNotification("Low Battery", "Battery level is below 40%.");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            @SuppressLint("MissingPermission")
            private void showBatteryNotification(String title, String message) {
                createBatteryNotificationChannel();

                Intent intent = new Intent(MainDashboard.this, MainDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainDashboard.this, 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainDashboard.this, "battery_channel")
                        .setSmallIcon(R.drawable.baseline_notifications_24)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainDashboard.this);
                notificationManager.notify(1, builder.build());
            }
        });



//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

        //NOTIFICATION
        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference myRefData = database2.getReference("Data");

        myRefData.addValueEventListener(new ValueEventListener() {
            boolean initialDataLoaded = false;
            int notificationCount = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!initialDataLoaded) {
                    // Skip notification for initial data load
                    initialDataLoaded = true;
                    return;
                }

                if (snapshot.exists()) {
                    long count = snapshot.getChildrenCount();
                    if (count > notificationCount) {
                        // New data has been added
                        showDataNotification("New Data", "Check Status! New data has been added.");
                        notificationCount = (int) count; // Update the notificationCount
                    }
                }
            }

            @SuppressLint("MissingPermission")
            private void showDataNotification(String title, String message) {
                createDataNotificationChannel();

                Intent intent = new Intent(MainDashboard.this, MainNotification.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainDashboard.this, 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainDashboard.this, "data_channel")
                        .setSmallIcon(R.drawable.baseline_notifications_24)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainDashboard.this);
                notificationManager.notify(0, builder.build());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });

    }

    private void createBatteryNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Battery Channel";
            String description = "Channel for Battery Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("battery_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createDataNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Data Channel";
            String description = "Channel for Data Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("data_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
