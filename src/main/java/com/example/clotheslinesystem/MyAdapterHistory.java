package com.example.clotheslinesystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapterHistory extends RecyclerView.Adapter<MyAdapterHistory.MyViewHolder> {

    private final List<MyItemsHistory> items;

    private final Context context;
    public MyAdapterHistory(List<MyItemsHistory> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MyItemsHistory myItems = items.get(position);

        holder.rain.setText(myItems.getRain());
        holder.remarks.setText(myItems.getRemarks());
        holder.temp.setText(myItems.getTemp());
        // holder.battery.setText(myItems.getBattery());
        holder.date.setText(myItems.getDate());
        holder.humid.setText(myItems.getHumid());


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView rain, remarks, temp,  date, humid;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rain = itemView.findViewById(R.id.rainsTV);
            remarks = itemView.findViewById(R.id.remarksTV);
            temp = itemView.findViewById(R.id.tempsTV);
            // battery = itemView.findViewById(R.id.battTV);
            date = itemView.findViewById(R.id.datesTV);
            humid = itemView.findViewById(R.id.humidsTV);

        }
    }
}
