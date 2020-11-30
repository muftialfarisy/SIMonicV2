package com.KP.simonicv2.Suhu;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.KP.simonicv2.R;
import com.KP.simonicv2.Reportm.Report_m;
import com.KP.simonicv2.Reportm.ReportmAdapter;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SuhuFragment extends Fragment {

//https://medium.com/@leelaprasad4648/creating-linechart-using-mpandroidchart-33632324886d

    private ArrayList<Suhu> suhulist = new ArrayList<>();

    private LineChart mChart;
    LineDataSet set1;
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    private DatabaseReference reference;
    long mid = 0;
    LineData lineData;
    TextView indikator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suhu, container, false);
 TextView lingkaran = (TextView)view.findViewById(R.id.lingkaran_suhu);
        mChart = view.findViewById(R.id.chart);
        indikator = (TextView) view.findViewById(R.id.lingkaran_suhu);
        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        mChart.setPinchZoom(true);
        renderData();
        getdata();
        indikator_suhu();
        return view;
    }
    public void renderData() {
        LimitLine llXAxis = new LimitLine(30f, "Index 10");
        llXAxis.setLineWidth(1f);
        llXAxis.enableDashedLine(1f, 1f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(1f, 1f, 0f);
        xAxis.setAxisMaximum(30f);
        xAxis.setAxisMinimum(1f);
        xAxis.setDrawLimitLinesBehindData(true);

        LimitLine ll1 = new LimitLine(38f, "Maximum Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(36f, "Minimum Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(50f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);

        mChart.getAxisRight().setEnabled(false);
        getdata();
    }

    /*private void setData() {


        values.add(new Entry(1, 35));
        values.add(new Entry(2, 36));
        values.add(new Entry(3, 40));

        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Sample Data");
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.DKGRAY);
            set1.setCircleColor(Color.DKGRAY);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_blue);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.DKGRAY);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
        }
    }*/

    public void getdata(){
        String uuid = getActivity().getIntent().getStringExtra("device_id");
        reference = FirebaseDatabase.getInstance().getReference();
        ArrayList<Entry> values = new ArrayList<Entry>();
        reference.child("Data ODP").child(uuid).child("suhu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    Suhu suhu = Snapshot.getValue(Suhu.class);
                    values.add(new Entry(suhu.getTanggal(),suhu.getSuhu()));

                }
                    //showchart(values);
                    if (mChart.getData() != null &&
                            mChart.getData().getDataSetCount() > 0) {
                        set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                        set1.setValues(values);
                        mChart.getData().notifyDataChanged();
                        mChart.notifyDataSetChanged();
                    } else {
                        set1 = new LineDataSet(values, "Sample Data");
                        set1.setDrawIcons(false);
                        set1.enableDashedLine(10f, 5f, 0f);
                        set1.enableDashedHighlightLine(10f, 5f, 0f);
                        set1.setColor(Color.DKGRAY);
                        set1.setCircleColor(Color.DKGRAY);
                        set1.setLineWidth(1f);
                        set1.setCircleRadius(3f);
                        set1.setDrawCircleHole(false);
                        set1.setValueTextSize(9f);
                        set1.setDrawFilled(true);
                        set1.setFormLineWidth(1f);
                        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                        set1.setFormSize(15.f);

                        if (Utils.getSDKInt() >= 18) {
                            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_blue);
                            set1.setFillDrawable(drawable);
                        } else {
                            set1.setFillColor(Color.DKGRAY);
                        }
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set1);
                        LineData data = new LineData(dataSets);
                        mChart.setData(data);
                    }
                } else{
                    mChart.clear();
                    mChart.invalidate();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
private void indikator_suhu(){
    String uuid = getActivity().getIntent().getStringExtra("device_id");
    //for (i=0;i<50;i++) {
    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Data ODP").child(uuid).child("suhu_history");
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot Snapshot : snapshot.getChildren()) {


                    //Suhu_history history = Snapshot.getValue(Suhu_history.class);
                    Integer hs = (Integer) Snapshot.getValue(Integer.class);
                indikator.setText(""+hs);
                    if(hs< 38){
                        indikator.setBackgroundColor(getResources().getColor(R.color.green));
                    } else if(hs >= 38){
                        indikator.setBackgroundColor(getResources().getColor(R.color.red));
                    }



            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}
}
