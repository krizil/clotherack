package com.example.clotheslinesystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainNotification extends AppCompatActivity {

    TextView textView;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final List<MyItemsHistory> myItemsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        ImageView back = findViewById(R.id.arrowNotification);
        // TextView title = findViewById(R.id.toolbar_t_notif);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = this.getWindow();
            window.setStatusBarColor(Color.parseColor("#012A4A"));
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainNotification.this, MainDashboard.class);
                startActivity(intent);
            }
        });
        databaseReference.child("Data").orderByChild("timestamp").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myItemsList.clear();

                // Get the latest child of the "Sample" node
                DataSnapshot latestChild = snapshot.getChildren().iterator().next();
                for (DataSnapshot data : snapshot.getChildren()) {
                    // ...
                }

                // Display the latest child of "Sample" node in the TextView elements
                String latestChildData = latestChild.child("Rain").getValue(String.class) + " "
                        + latestChild.child("Remarks").getValue(String.class) + " "
                        + latestChild.child("Temp").getValue(String.class) + " "
                        + latestChild.child("Humid").getValue(String.class) + " "
                        + latestChild.child("Date").getValue(String.class);

                TextView date = findViewById(R.id.dates1TV);
                TextView hum = findViewById(R.id.humids1TV);
                TextView tem = findViewById(R.id.temps1TV);
                TextView rain = findViewById(R.id.rains1TV);
                TextView remarks = findViewById(R.id.remarks1TV);
                TextView battery = findViewById(R.id.batts1TV);

                date.setText(latestChild.child("Date").getValue(String.class));
                hum.setText(latestChild.child("Humid").getValue(String.class));
                tem.setText(latestChild.child("Temp").getValue(String.class));
                rain.setText(latestChild.child("Rain").getValue(String.class));
                remarks.setText(latestChild.child("Remarks").getValue(String.class));

                databaseReference.child("Battery").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String batteryValue = snapshot.getValue(String.class);
                        battery.setText(batteryValue);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle any errors
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });

    }
}