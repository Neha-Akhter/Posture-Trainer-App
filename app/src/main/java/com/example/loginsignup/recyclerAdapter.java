package com.example.loginsignup;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private ArrayList<BluetoothDevice> machineList;
    private RecyclerViewClickListener listener;

    public recyclerAdapter(ArrayList<BluetoothDevice> machineList, RecyclerViewClickListener listener){
        this.machineList = machineList;
        this.listener = listener;
    }
    public void clear(){
        machineList.clear();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nametxt;
        private TextView addresstxt;

        public MyViewHolder(final View view){
            super(view);
            nametxt = view.findViewById(R.id.textView);
            addresstxt = view.findViewById(R.id.textView2);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String name = machineList.get(position).getName();
        holder.nametxt.setText(name);
        String address = machineList.get(position).getAddress();
        holder.addresstxt.setText(address);

    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
