package com.KP.simonicv2.Reportc;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.KP.simonicv2.R;
import com.skyhope.expandcollapsecardview.ExpandCollapseCard;
import com.skyhope.expandcollapsecardview.ExpandCollapseListener;

import java.util.ArrayList;

public class ReportcAdapter extends RecyclerView.Adapter<ReportcAdapter.ViewHolder>{
    public ArrayList<Report_c> reportlist;
    Activity activity;


    public ReportcAdapter(Activity activity, ArrayList<Report_c> reportlist) {
        super();
        this.activity = activity;
        this.reportlist = reportlist;

    }

    @Override
    public ReportcAdapter.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent,
                                                        int viewType) {
        // create a new view
        /*View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_reportc,parent, null);*/
        View itemLayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_reportc, parent, false);
        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.namars.setText(reportlist.get(position).getNama_rs());
        Report_c movie = reportlist.get(position);
        holder.tglreportc.setText(reportlist.get(position).getTgl_reportc());
        boolean isExpanded = reportlist.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.cdreportc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Report_c movie = reportlist.get(position);

                movie.setExpanded(!movie.isExpanded());
                holder.btncolapse.setRotation(270f);
                notifyItemChanged(position);

            }
        });
        if(movie.isExpanded()){
            holder.btncolapse.setRotation(270f);
        }else {
            holder.btncolapse.setRotation(90f);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (reportlist != null) ? reportlist.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView namars,tglreportc;
        RelativeLayout expandableLayout;
        public ImageView btncolapse;
        public CardView cdreportc;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            namars = (TextView) itemLayoutView.findViewById(R.id.nama_rs);
            tglreportc = (TextView) itemLayoutView.findViewById(R.id.tgl_reportc);
            expandableLayout = itemLayoutView.findViewById(R.id.rl_expand);
            btncolapse = itemLayoutView.findViewById(R.id.btn_collapse);
            cdreportc = itemLayoutView.findViewById(R.id.cd_reportc);

        }
    }

}

