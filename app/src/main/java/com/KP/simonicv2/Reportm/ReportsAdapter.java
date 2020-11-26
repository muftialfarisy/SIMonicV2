package com.KP.simonicv2.Reportm;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.KP.simonicv2.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class ReportsAdapter extends RecyclerView.Adapter<ReportmAdapter.ViewHolder> {
    //public ArrayList<Report_m> reportmlist;
    public ArrayList<Report_sos> reportsoslist;
    Activity activity;

    public ReportsAdapter(Activity activity, ArrayList<Report_sos> reportsoslist) {
        super();
        this.activity = activity;
        //this.reportmlist = reportmlist;
        this.reportsoslist = reportsoslist;
    }

    @Override
    public ReportmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                        int viewType) {
        // create a new views
        /*View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_reportc,parent, null);*/
        View itemLayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_reportm, parent, false);
        // create ViewHolder

        ReportmAdapter.ViewHolder viewHolder = new ReportmAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReportmAdapter.ViewHolder holder, final int position) {

        /*holder.chipIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {*/
        holder.masalah.setText(reportsoslist.get(position).getMasalah());
        holder.tglmasalah.setText(reportsoslist.get(position).getTanggal());
        holder.jammasalah.setText(reportsoslist.get(position).getJam());
        //}
        //});
        /*holder.chipJb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.masalah.setText(reportsoslist.get(position).getMasalah());
                holder.tglmasalah.setText(reportsoslist.get(position).getTanggal());
                holder.jammasalah.setText(reportsoslist.get(position).getJam());
            }
        });*/


    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (reportsoslist != null) ? reportsoslist.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView masalah,tglmasalah,jammasalah;
        public Chip chipIn, chipJb;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            masalah = (TextView) itemLayoutView.findViewById(R.id.txt_masalah);
            tglmasalah = (TextView) itemLayoutView.findViewById(R.id.txt_tglmasalah);
            jammasalah = (TextView) itemLayoutView.findViewById(R.id.txt_jammasalah);
            //chipIn = (Chip) itemLayoutView.findViewById(R.id.indonesia);
            //chipJb = (Chip) itemLayoutView.findViewById(R.id.jabar);



        }
    }

}