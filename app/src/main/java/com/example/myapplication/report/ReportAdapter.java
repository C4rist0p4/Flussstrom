package com.example.myapplication.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.entiy.Meldungen;

import java.util.ArrayList;
import java.util.List;


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private List<Meldungen> meldungen = new ArrayList<>();

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Meldungen currentItem = meldungen.get(position);

        String meldungstyp = currentItem.getMeldungstyp();
        String date = currentItem.getDatum() + " " + currentItem.getTimestamp_device();
        String bemerkungMel = currentItem.getBemerkungMel();

        holder.meldungstypTV.setText(meldungstyp);
        holder.dateTV.setText(date);
        holder.bemerkungMelTV.setText(bemerkungMel);
    }

    @Override
    public int getItemCount() {
        return meldungen.size();
    }

    public void setMeldungen(List<Meldungen> meldungen) {
        this.meldungen = meldungen;
        notifyDataSetChanged();
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
