package com.KP.simonicv2.Reportm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.KP.simonicv2.R;
import com.KP.simonicv2.Reportc.Report_c;
import com.KP.simonicv2.Reportc.ReportcAdapter;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;


public class ReportmFragment extends Fragment {

    private ArrayList<Report_m> reportmlist = new ArrayList<>();
    private ArrayList<Report_sos> reportsoslist;
    private RecyclerView Rv_reportm;
    private ReportcAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reportm, container, false);
        addData();
        Rv_reportm = (RecyclerView) view.findViewById(R.id.rv_reportm);

        Rv_reportm.setHasFixedSize(true);
        ReportmAdapter adapter = new ReportmAdapter(getActivity(),reportmlist,reportsoslist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        Rv_reportm.setLayoutManager(layoutManager);
        Rv_reportm.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        return view;
    }
    private void addData(){
        reportmlist = new ArrayList<>();
        reportsoslist = new ArrayList<>();
        reportmlist.add(new Report_m("Trombosit naik","30 agustus 2020","14.00"));
        reportsoslist.add(new Report_sos("tiba tiba demam tinggi","4 september 2020","21.00"));

    }
}