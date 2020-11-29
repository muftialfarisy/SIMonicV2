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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.KP.simonicv2.Profile.Profile;
import com.KP.simonicv2.Profile.ProfileFragment;
import com.KP.simonicv2.Registrasi.Registrasi;
import com.KP.simonicv2.Registrasi.Registrasi_gs;
import com.KP.simonicv2.Reportc.ReportcFragment;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.KP.simonicv2.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private ShimmerRecyclerView recyclerView;
     IndividuAdapter adapter;
    private DatabaseReference reference;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth auth;
    private String GetUserID;
    FragmentManager fragmentManager = getSupportFragmentManager();
    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informasi);
        //addData();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        recyclerView = (ShimmerRecyclerView) findViewById(R.id.recycler_view);
        auth = FirebaseAuth.getInstance();
        getdata();

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Informasi.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.showShimmerAdapter();
        /*FirebaseRecyclerOptions<Individu> options=
                new FirebaseRecyclerOptions.Builder<Individu>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Data ODP").child(auth.getUid()),Individu.class)
                        .build();

        adapter=new IndividuAdapter(options);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Informasi.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);*/

    }


    public void getdata() {
        String getUserID = auth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Data ODP").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot Snapshot : snapshot.getChildren()) {

                    Individu individu = Snapshot.getValue(Individu.class);

                    dataList.add(individu);

                    adapter = new IndividuAdapter(dataList, Informasi.this);


                    recyclerView.setAdapter(adapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DynamicToast.makeError(Informasi.this, "eror");
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

public void getdata2(){
    String getUserID = auth.getCurrentUser().getUid();
    reference = FirebaseDatabase.getInstance().getReference();

    reference.child("Data ODP").push().addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            for (DataSnapshot Snapshot : snapshot.getChildren()) {

                Individu individu = Snapshot.getValue(Individu.class);

                dataList.add(individu);

                adapter = new IndividuAdapter(dataList, Informasi.this);


                recyclerView.setAdapter(adapter);
            }


        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            DynamicToast.makeError(Informasi.this, "eror");
        }
    });

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
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);


    }

    private void processsearch(String s) {
        FirebaseRecyclerOptions<Individu>options=
                new FirebaseRecyclerOptions.Builder<Individu>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Data ODP").orderByChild("name").startAt(s).endAt(s+"\uf8ff"), Individu.class)
                        .build();
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
        intent.putExtra("nik", dataList.get(position).getNik());
        intent.putExtra("alamat", dataList.get(position).getAlamat());
        intent.putExtra("jk", dataList.get(position).getJenis_kelamin());
        intent.putExtra("provinsi", dataList.get(position).getProvinsi());
        intent.putExtra("kota", dataList.get(position).getKota());
        intent.putExtra("kecamatan", dataList.get(position).getKecamatan());
        intent.putExtra("kelurahan", dataList.get(position).getKelurahan());
        intent.putExtra("tgl_mulai", dataList.get(position).getDurasi());
        intent.putExtra("tgl_selesai", dataList.get(position).getSelesai());
        intent.putExtra("lat", dataList.get(position).getLat());
        intent.putExtra("lng", dataList.get(position).getLng());
        intent.putExtra("lat_me", dataList.get(position).getLat_Me());
        intent.putExtra("lng_me", dataList.get(position).getLng_Me());
        intent.putExtra("uuid", dataList.get(position).getUuid());
        intent.putExtra("device_id",dataList.get(position).getDevice_Id());
        startActivity(intent);

        Intent intent2 = new Intent(Informasi.this, Edit_Profile.class);
        intent2.putExtra("nama", dataList.get(position).getNama());
        intent2.putExtra("wilayah", dataList.get(position).getWilayah());
        intent2.putExtra("nik", dataList.get(position).getNik());
        intent2.putExtra("alamat", dataList.get(position).getAlamat());
        intent2.putExtra("jk", dataList.get(position).getJenis_kelamin());
        intent2.putExtra("provinsi", dataList.get(position).getProvinsi());
        intent2.putExtra("kota", dataList.get(position).getKota());
        intent2.putExtra("kecamatan", dataList.get(position).getKecamatan());
        intent2.putExtra("kelurahan", dataList.get(position).getKelurahan());
        intent2.putExtra("tgl_mulai", dataList.get(position).getDurasi());
        intent2.putExtra("tgl_selesai", dataList.get(position).getSelesai());
        intent2.putExtra("lat", dataList.get(position).getLat());
        intent2.putExtra("lng", dataList.get(position).getLng());
        intent2.putExtra("device_id", dataList.get(position).getDevice_Id());
        intent2.putExtra("uuid", dataList.get(position).getUuid());
        intent2.putExtra("device_id",dataList.get(position).getDevice_Id());
/*
        Intent intent2 = new Intent(getApplicationContext(), ProfileFragment.class);
        intent2.putExtra("nama", dataList.get(position).getNama());
        intent2.putExtra("nik", dataList.get(position).getNik());
        intent2.putExtra("alamat", dataList.get(position).getAlamat());
        intent2.putExtra("provinsi", dataList.get(position).getProvinsi());
        intent2.putExtra("kota", dataList.get(position).getKota());
        intent2.putExtra("kecamatan", dataList.get(position).getKecamatan());
        intent2.putExtra("kelurahan", dataList.get(position).getKelurahan());
        intent2.putExtra("tgl_mulai", dataList.get(position).getDurasi());
        intent2.putExtra("tgl_selesai", dataList.get(position).getSelesai());
        startActivity(intent2);
*/
        FirebaseUser user = auth.getCurrentUser();
        GetUserID = user.getUid();
        ProfileFragment fragmentB = new ProfileFragment();
        ReportcFragment fragmentC = new ReportcFragment();
        reference.child("Data ODP").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    Profile profile = Snapshot.getValue(Profile.class);
                    /*Intent intent2 = new Intent(getApplicationContext(), ProfileFragment.class);
                    intent2.putExtra("nama", profile.getNama());
                    intent2.putExtra("nik", profile.getNik());
                    startActivity(intent2);*/

                    Bundle bundle = new Bundle();
                    Bundle bundle2 = new Bundle();
                    bundle.getString("nama",dataList.get(position).getNama());
                    bundle2.getString("device",dataList.get(position).getUuid());
                    fragmentB.setArguments(bundle);
                    fragmentC.setArguments(bundle2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}

