package com.KP.simonicv2.Individu;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.KP.simonicv2.R;
import com.KP.simonicv2.Reportc.ReportcFragment;
import com.KP.simonicv2.TabAdapter;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;

public class Detail_Individu extends AppCompatActivity {
    private static final String TAG = "detail";
    ViewPager viewPager;
    private TabLayout tabLayout;
    TextView txtnama;
    private int[] tabIcons = {
            R.drawable.ic_note,
            R.drawable.ic_suhu,
            R.drawable.ic_street_view
    };
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_individu);
        viewPager = findViewById(R.id.pager);
        txtnama = findViewById(R.id.txt_nama2);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Report CheckUp"));
        tabLayout.addTab(tabLayout.newTab().setText("Report Masalah"));
        tabLayout.addTab(tabLayout.newTab().setText("Suhu Tubuh"));
        tabLayout.addTab(tabLayout.newTab().setText("Posisi Individu"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        final TabAdapter adapter = new TabAdapter(this,getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        getIncomingIntent();
    }


    private void getIncomingIntent(){
        if (getIntent().hasExtra("nama")&& getIntent().hasExtra("wilayah")){
            String nama = getIntent().getStringExtra("nama");

            Log.d(TAG,"On Create:" +nama.toString());
            txtnama.setText(""+nama);

        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void durasi(){
        String awal = getIntent().getStringExtra("tgl_mulai").;
        String selesai = getIntent().getStringExtra("tgl_selesai");
        LocalDate birthDate = LocalDate.of(awal);
    }
    }

