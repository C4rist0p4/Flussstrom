package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private Context context;
    private ArrayList<ReportItem> reportList;

    ReportAdapter(Context mContext, ArrayList<ReportItem> list) {
        context = mContext;
        reportList = list;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.report_itme, parent, false);
        return new ReportViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        ReportItem currentItem = reportList.get(position);

        String bemerkungMel = currentItem.getBemerkungMel();
        String date = currentItem.getDate();
        String meldungstyp = currentItem.getMeldungstyp();

        holder.meldungstypTV.setText(meldungstyp);
        holder.dateTV.setText(date);
        holder.bemerkungMelTV.setText(bemerkungMel);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        private TextView meldungstypTV;
        private TextView dateTV;
        private  TextView bemerkungMelTV;

        ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            meldungstypTV = itemView.findViewById(R.id.meldungstypTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            bemerkungMelTV = itemView.findViewById(R.id.bemerkungMelTV);
        }
    }
}
