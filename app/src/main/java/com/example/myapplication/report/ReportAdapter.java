package com.example.myapplication.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.entiy.Meldungen;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private List<Meldungen> meldungen = new ArrayList<>();

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item, parent, false);
        return new ReportViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Meldungen currentItem = meldungen.get(position);

        String meldungstyp = currentItem.getMeldungstyp();
        String date = currentItem.getDatum() + " " + currentItem.getTimestamp_device();
        String bemerkungMel = currentItem.getBemerkungMel();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-dd-MM");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());


        try {
            Date oneWayTripDate = input.parse(date);
            String dateFormate = dateFormat.format(oneWayTripDate);
            holder.meldungstypTV.setText(meldungstyp);
            holder.dateTV.setText(dateFormate);
            holder.bemerkungMelTV.setText(bemerkungMel);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
