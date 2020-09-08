package com.KP.simonicv2.Individu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.KP.simonicv2.Profile.ProfileFragment;
import com.KP.simonicv2.Registrasi.Registrasi;
import com.KP.simonicv2.Registrasi.Registrasi_gs;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.KP.simonicv2.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

public class Informasi extends AppCompatActivity implements IndividuAdapter.OnindListener {
    private ArrayList<Individu> dataList = new ArrayList<>();
    private ArrayList<Registrasi_gs> registrasilist = new ArrayList<>();
    private RecyclerView recyclerView;
    private IndividuAdapter adapter;
    private DatabaseReference reference;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth auth;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informasi);
        //addData();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        auth = FirebaseAuth.getInstance();
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Informasi.this);
        recyclerView.setLayoutManager(layoutManager);
        /*FirebaseRecyclerOptions<Individu> options=
                new FirebaseRecyclerOptions.Builder<Individu>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Data ODP").child(auth.getUid()),Individu.class)
                        .build();

        adapter=new IndividuAdapter(options);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Informasi.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);*/
getdata();

    }


    public void getdata(){
        String getUserID = auth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Data ODP").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList = new ArrayList<>();

                for(DataSnapshot Snapshot : snapshot.getChildren()){
                    Individu individu = Snapshot.getValue(Individu.class);

                    dataList.add(individu);

                    adapter = new IndividuAdapter(dataList,Informasi.this);





                    recyclerView.setAdapter(adapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DynamicToast.makeError(Informasi.this,"eror");
            }
        });
        /*FirebaseRecyclerOptions<Individu> options=
                new FirebaseRecyclerOptions.Builder<Individu>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Data ODP").child(auth.getUid()),Individu.class)
                        .build();

        adapter=new IndividuAdapter(options);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Informasi.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem mSearch = menu.findItem(R.id.appSearchBar);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.CENTER));
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up

        return false;
    }

    @Override
    public void onindClick(int position) {
        Intent intent = new Intent(Informasi.this, Detail_Individu.class);
        intent.putExtra("nama", dataList.get(position).getNama());
        intent.putExtra("wilayah", dataList.get(position).getWilayah());
        startActivity(intent);

        Intent intent2 = new Intent(Informasi.this, ProfileFragment.class);
        intent2.putExtra("nama",dataList.get(position).getNama());
        intent2.putExtra("nik",dataList.get(position).getNik());
        intent2.putExtra("alamat",dataList.get(position).getAlamat());
        intent2.putExtra("provinsi",dataList.get(position).getProvinsi());
        intent2.putExtra("kota",dataList.get(position).getKota());
        intent2.putExtra("kecamatan",dataList.get(position).getKecamatan());
        intent2.putExtra("kelurahan",dataList.get(position).getKelurahan());
        intent2.putExtra("tgl_mulai",dataList.get(position).getDurasi());
        intent2.putExtra("tgl_selesai",dataList.get(position).getSelesai());
        startActivity(intent2);
    }

}
