package com.example.clotheslinesystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainHistory extends AppCompatActivity {
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private final List<MyItemsHistory> myItemsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        ImageView back = findViewById(R.id.arrowHistory);
        TextView title = findViewById(R.id.toolbar_title_history);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = this.getWindow();
            window.setStatusBarColor(Color.parseColor("#012A4A"));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainHistory.this, MainDashboard.class);
                startActivity(intent);
            }
        });


        final RecyclerView recyclerView = findViewById(R.id.recylerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainHistory.this));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = databaseReference.child("Data").orderByChild("Data").limitToLast(50);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                myItemsList.clear();

                for (DataSnapshot Data : snapshot.getChildren()) {

                    if (Data.hasChild("Rain") && Data.hasChild("Remarks") && Data.hasChild("Temp") && Data.hasChild("Date") && Data.hasChild("Humid")) {

                        final String getRain = Data.child("Rain").getValue(String.class);
                        final String getRemarks = Data.child("Remarks").getValue(String.class);
                        final String getTemp = Data.child("Temp").getValue(String.class);
                        //final String getBattery = Data.child("Battery").getValue(String.class);
                        final String getHumid = Data.child("Humid").getValue(String.class);
                        final String getDate = Data.child("Date").getValue(String.class);


                        MyItemsHistory myItems = new MyItemsHistory(getRain, getRemarks, getTemp, getDate, getHumid);

                        myItemsList.add(myItems);
                        Log.d("myTag", "This is my message " + myItemsList.toString());
                    }

                }

                // Sort the list in reverse order based on the date
                Collections.reverse(myItemsList);

                recyclerView.setAdapter(new MyAdapterHistory(myItemsList, MainHistory.this));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
