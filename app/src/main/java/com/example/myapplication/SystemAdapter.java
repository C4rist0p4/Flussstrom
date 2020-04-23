package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SystemAdapter extends RecyclerView.Adapter<SystemAdapter.SystemViewHolder>  {
    private Context context;
    private ArrayList<SystemItem> systemList;
    private OnSystemListener onSystemListener;

    SystemAdapter(Context mContext, ArrayList<SystemItem> list, OnSystemListener onSystemListener_) {
        context = mContext;
        systemList = list;
        this.onSystemListener = onSystemListener_;
    }

    @NonNull
    @Override
    public SystemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.system_item, parent, false);
        return new SystemViewHolder(v, onSystemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SystemViewHolder holder, int position) {
        SystemItem currentItem = systemList.get(position);

        String systemName = currentItem.getName();
       //String systemOutput24 = currentItem.getOutput24();
       //String systemOutputAverage = currentItem.getOutputAverage();

        holder.systemName.setText(systemName);
    }

    @Override
    public int getItemCount() {
        return systemList.size();
    }

    public class SystemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView systemName;
        OnSystemListener onSystemListener;

        SystemViewHolder(@NonNull View itemView, OnSystemListener onSystemListener) {
            super(itemView);
            systemName = itemView.findViewById(R.id.systemname);
            this.onSystemListener = onSystemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSystemListener.onSystemClick(getAdapterPosition());
        }
    }

    public interface OnSystemListener{
        void onSystemClick(int position);
    }
}
