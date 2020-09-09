package com.KP.simonicv2.Profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.KP.simonicv2.Individu.Individu;
import com.KP.simonicv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.util.GeoPoint;

import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    TextView namaa,nik,alamat,provinsi,kota,kecamatan,kelurahan,tgl_mulai,tgl_selesai;
    private FirebaseAuth auth;
    private FirebaseDatabase getDatabase;
    private DatabaseReference getRefenence;
    private String GetUserID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_profile,container,false);
        namaa = (TextView) view.findViewById(R.id.text_nama_lengkap);
        nik = (TextView) view.findViewById(R.id.text_nik);
        alamat = (TextView) view.findViewById(R.id.text_alamat_individu);
        provinsi = (TextView) view.findViewById(R.id.text_provinsi);
        kota = (TextView) view.findViewById(R.id.text_kota);
        kecamatan =(TextView) view.findViewById(R.id.text_kecamatan);
        kelurahan = (TextView) view.findViewById(R.id.text_kelurahan);
        tgl_mulai = (TextView) view.findViewById(R.id.text_tglmulai);
        tgl_selesai = (TextView) view.findViewById(R.id.text_tglselesai);
        auth = FirebaseAuth.getInstance();

        //Mendapatkan User ID dari akun yang terautentikasi
        //getdata();
        getIncomingIntent();
        return view;
    }

    private void getIncomingIntent(){
        if (getActivity().getIntent().hasExtra("nama")) {
            String nama = getActivity().getIntent().getStringExtra("nama");
            String nikk = getActivity().getIntent().getStringExtra("nik");
            String alamatt = getActivity().getIntent().getStringExtra("alamat");
            String prov = getActivity().getIntent().getStringExtra("provinsi");
            String kotaa = getActivity().getIntent().getStringExtra("kota");
            String kec = getActivity().getIntent().getStringExtra("kecamatan");
            String kel = getActivity().getIntent().getStringExtra("kelurahan");
            String mulai = getActivity().getIntent().getStringExtra("tgl_mulai");
            String selesai = getActivity().getIntent().getStringExtra("tgl_selesai");

            namaa.setText("" + nama);
            nik.setText("" + nikk);
            alamat.setText("" + alamatt);
            provinsi.setText("" + prov);
            kota.setText("" + kotaa);
            kecamatan.setText("" + kec);
            kelurahan.setText("" + kel);
            tgl_mulai.setText("" + mulai);
            tgl_selesai.setText("" + selesai);


        }
        /*Bundle bundle = getArguments();
        String bnama = Objects.requireNonNull(bundle).getString("nama");
        namaa.setText(bnama);*/
    }

    private void getdata(){
        FirebaseUser user = auth.getCurrentUser();
        GetUserID = user.getUid();

        getDatabase = FirebaseDatabase.getInstance();
        getRefenence = getDatabase.getReference();

        getRefenence.child("Data ODP").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot Snapshot : snapshot.getChildren()) {
                    Profile profile = Snapshot.getValue(Profile.class);
                    namaa.setText(profile.getNama());

                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}