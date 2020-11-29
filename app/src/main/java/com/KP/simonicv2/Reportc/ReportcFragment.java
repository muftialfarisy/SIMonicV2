package com.KP.simonicv2.Reportc;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.KP.simonicv2.R;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skyhope.expandcollapsecardview.ExpandCollapseCard;
import com.skyhope.expandcollapsecardview.ExpandCollapseListener;

import java.util.ArrayList;

public class ReportcFragment extends Fragment   {



    ArrayList<Report_c> reportlist = new ArrayList<>();
    RecyclerView Rv_reportc;
     ReportcAdapter adapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    int i;
    long mid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        String uuid = getActivity().getIntent().getStringExtra("device_id");
        Rv_reportc = (RecyclerView) view.findViewById(R.id.rv_reportc);
addData();
        Rv_reportc.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        Rv_reportc.setLayoutManager(layoutManager);


            /*for (i=0;i<50;i++) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Data ODP").child(uuid).child("laporan_checkup");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot Snapshot : snapshot.getChildren()) {
                            if (!Snapshot.exists()) {
                                mid = mid + 1;

                            } else {
                                mid = (Snapshot.getChildrenCount());
                                Report_c individu = Snapshot.getValue(Report_c.class);
                                reportlist.add(individu);

                                adapter = new ReportcAdapter(getActivity(), reportlist);
                                Rv_reportc.setAdapter(adapter);

                            }
                        }
                   }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }*/


        return view;
    }

    public void addData(){
        String uuid = getActivity().getIntent().getStringExtra("device_id");
        reference = FirebaseDatabase.getInstance().getReference();
        reportlist.clear();
        //for (i=0;i<50;i++) {
      reference.child("Data ODP").child(uuid).child("laporan_checkup").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                        if (!Snapshot.exists()) {
                            //mid = mid + 1;

                        } else {
                            mid = (Snapshot.getChildrenCount());
                            Report_c individu = Snapshot.getValue(Report_c.class);
                            reportlist.add(individu);
                            adapter = new ReportcAdapter(getActivity(), reportlist);
                            Rv_reportc.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //}


    }

}