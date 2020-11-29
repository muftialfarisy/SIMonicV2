package com.KP.simonicv2.Reportm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.KP.simonicv2.R;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ReportmFragment extends Fragment {

    private ArrayList<Report_m> reportmlist = new ArrayList<>();
    private ArrayList<Report_sos> reportsoslist = new ArrayList<>();
    private RecyclerView Rv_reportm;
    private ReportmAdapter adapter;
    private ReportsAdapter adapters;
    int i;
    long mid = 0;
    Chip chipIn, chipJb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reportm, container, false);

        chipIn = (Chip) view.findViewById(R.id.indonesia);
        chipJb = (Chip) view.findViewById(R.id.jabar);
        adddata();
        Rv_reportm = (RecyclerView) view.findViewById(R.id.rv_reportm);

        Rv_reportm.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        Rv_reportm.setLayoutManager(layoutManager);
        chipIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddata();
            }
        });
        chipJb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddata2();
            }
        });
        return view;
    }
    /*private void addData(){
        String uuid = getActivity().getIntent().getStringExtra("device_id");
        if (i < 0){
        }else{
            for (i=0;i<50;i++) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Data ODP").child(uuid).child("laporan_sos").child(String.valueOf(i));
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            mid = mid + 1;

                        } else {

                            mid = (snapshot.getChildrenCount());
                            Report_m individu = snapshot.getValue(Report_m.class);
                            reportmlist.add(individu);

                            adapter = new ReportmAdapter(getActivity(), reportmlist,reportsoslist);
                            Rv_reportm.setAdapter(adapter);

                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }

    }*/

    private void adddata(){
        String uuid = getActivity().getIntent().getStringExtra("device_id");


                //for (i=0;i<50;i++) {
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Data ODP").child(uuid).child("laporan_masalah");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot Snapshot : snapshot.getChildren()) {
                                if (!Snapshot.exists()) {
                                    //mid = mid + 1;

                                } else {

                                    mid = (Snapshot.getChildrenCount());
                                    Report_m individu = Snapshot.getValue(Report_m.class);
                                    reportmlist.add(individu);

                                    adapter = new ReportmAdapter(getActivity(), reportmlist);
                                    Rv_reportm.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                //}


         /*else if (chipJb.isChecked()){
            if (i < 0){
            }else{
                for (i=0;i<50;i++) {
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Data ODP").child(uuid).child("laporan_sos").child(String.valueOf(i));
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                mid = mid + 1;

                            } else {

                                mid = (snapshot.getChildrenCount());
                                Report_m individu = snapshot.getValue(Report_m.class);
                                reportmlist.add(individu);

                                adapters = new ReportsAdapter(getActivity(),reportsoslist);
                                Rv_reportm.setAdapter(adapters);

                                adapters.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        }*/
    }
    private void adddata2(){
        String uuid = getActivity().getIntent().getStringExtra("device_id");
        if (chipJb.isChecked()){
            if (i < 0){
            }else{
                for (i=0;i<50;i++) {
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Data ODP").child(uuid).child("laporan_sos").child(String.valueOf(i));
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                //mid = mid + 1;

                            } else {

                                mid = (snapshot.getChildrenCount());
                                Report_sos individu2 = snapshot.getValue(Report_sos.class);
                                reportsoslist.add(individu2);

                                adapters = new ReportsAdapter(getActivity(),reportsoslist);
                                Rv_reportm.setAdapter(adapters);

                                adapters.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        }
    }
}