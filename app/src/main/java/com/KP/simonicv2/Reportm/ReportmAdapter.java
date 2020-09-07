package com.KP.simonicv2.Reportm;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.KP.simonicv2.R;
import com.KP.simonicv2.Reportc.Report_c;
import com.KP.simonicv2.Reportc.ReportcAdapter;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class ReportmAdapter extends RecyclerView.Adapter<ReportmAdapter.ViewHolder> {
    public ArrayList<Report_m> reportmlist;
    public ArrayList<Report_sos> reportsoslist;
    Activity activity;

    public ReportmAdapter(Activity activity, ArrayList<Report_m> reportmlist, ArrayList<Report_sos> reportsoslist) {
        super();
        this.activity = activity;
        this.reportmlist = reportmlist;
        this.reportsoslist = reportsoslist;
    }

    @Override
    public ReportmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                        int viewType) {
        // create a new view
        /*View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_reportc,parent, null);*/
        View itemLayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_reportm, parent, false);
        // create ViewHolder

        ReportmAdapter.ViewHolder viewHolder = new ReportmAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.chipIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.masalah.setText(reportmlist.get(position).getMasalah());
                holder.tglmasalah.setText(reportmlist.get(position).getTgl());
                holder.jammasalah.setText(reportmlist.get(position).getJam());
            }
        });
        holder.chipJb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.masalah.setText(reportsoslist.get(position).getSos());
                holder.tglmasalah.setText(reportsoslist.get(position).getTgl());
                holder.jammasalah.setText(reportsoslist.get(position).getJam());
            }
        });


    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (reportmlist != null) ? reportmlist.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView masalah,tglmasalah,jammasalah;
        public Chip chipIn, chipJb;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            masalah = (TextView) itemLayoutView.findViewById(R.id.txt_masalah);
            tglmasalah = (TextView) itemLayoutView.findViewById(R.id.txt_tglmasalah);
            jammasalah = (TextView) itemLayoutView.findViewById(R.id.txt_jammasalah);
            chipIn = (Chip) itemLayoutView.findViewById(R.id.indonesia);
            chipJb = (Chip) itemLayoutView.findViewById(R.id.jabar);



        }
    }

}

