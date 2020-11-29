package com.KP.simonicv2.Individu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.KP.simonicv2.AppController;
import com.KP.simonicv2.MainActivity;
import com.KP.simonicv2.R;
import com.KP.simonicv2.Registrasi.Map_coordinate;
import com.KP.simonicv2.Registrasi.Registrasi;
import com.KP.simonicv2.Registrasi.Registrasi_gs;
import com.KP.simonicv2.Reportm.Report_m;
import com.KP.simonicv2.Reportm.ReportmAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class Edit_Profile extends AppCompatActivity {
    Spinner spProvinsi,jeniskelamin,spinner,spKota,spKecamatan,spKeluruhan;
    private String TAG = Edit_Profile.class.getSimpleName();
    ArrayList <Edit_gs> editlist = new ArrayList<>();
    ArrayList <String> deviceidlist = new ArrayList<String>();
    TextView tglmulai,tglselesai,jdl_coordinate;
    ImageButton exit;
    Button edit;
    String namaa,nikk,alamatt,jk,idd,tgl1,tgl2,riwayatt,provinsii,kotaa,kecamatann,kelurahann,uuidd,text;
    EditText nama,nik,alamat,riwayat,coordinate,uuid,id,coordinate_baru;
    double lat3,lng3;
    int i;
    long mid = 0;
    private ProgressDialog pDialog;
    private static String url = "https://dev.farizdotid.com/api/daerahindonesia/provinsi";
    private String jabar = "https://services5.arcgis.com/VS6HdKS0VfIhv8Ct/arcgis/rest/services/COVID19_Indonesia_per_Provinsi/FeatureServer/0/query?where=Provinsi%20%3D%20%27JAWA%20BARAT%27&outFields=*&outSR=4326&f=json";
    private DatabaseReference reference;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth auth;
    private String jsonResponse;
    ArrayList<String> categories = new ArrayList<>();
    ArrayList<String> categories2 = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter;
    double lat2,lng2;
    //https://stackoverflow.com/questions/41296416/updating-the-data-on-firebase-android/41296760
    //https://www.wildantechnoart.net/2018/04/crud-firebase-realtime-database-membuat-fungsi-update-data.html
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_individu);
        spProvinsi = (Spinner) findViewById(R.id.sp_name);
        spKota =(Spinner) findViewById(R.id.sp_name2);
        spKecamatan=(Spinner) findViewById(R.id.sp_name3);
        spKeluruhan=(Spinner) findViewById(R.id.sp_name4);
        spinner = (Spinner) findViewById(R.id.sp_jk);
        text = spinner.getSelectedItem().toString();
        id = (EditText) findViewById(R.id.txt_device);
        id.setEnabled(false);
        edit = (Button) findViewById(R.id.btn_edit);
        tglmulai = (TextView) findViewById(R.id.txt_durasi);
        tglselesai = (TextView) findViewById(R.id.txt_selesai);
        nama = (EditText) findViewById(R.id.txt_nama);
        nik = (EditText) findViewById(R.id.txt_nik);
        alamat = (EditText) findViewById(R.id.txt_alamat);
        spinner = (Spinner) findViewById(R.id.sp_jk);
        uuid = (EditText) findViewById(R.id.txt_uuid);
        uuid.setEnabled(false);
        coordinate = (EditText) findViewById(R.id.txt_coordinate);
        coordinate_baru = (EditText) findViewById(R.id.txt_coordinate_baru);
        jdl_coordinate = (TextView) findViewById(R.id.jdl_coordinate);
        coordinate.setFocusable(false);
        coordinate.setEnabled(false);
        coordinate_baru.setFocusable(false);
        //getintent();
        makeJsonArrayRequest();
        ambildata();
        spinnerAdapter = new ArrayAdapter<>(Edit_Profile.this, R.layout.simple_spinner_item, deviceidlist);
        //setdata();
        text = spinner.getSelectedItem().toString();
        Intent intent = this.getIntent();
        if(intent != null) {
            Bundle b = intent.getExtras();
            if(b != null) {
                lat2 = b.getDouble("latt");
                lng2 = b.getDouble("lngg");
                namaa = b.getString("nama");
                nikk = b.getString("nik");
                alamatt = b.getString("alamat");
                jk = b.getString("jk");
                idd = b.getString("id");
                tgl1 = b.getString("tglmulai");
                tgl2 = b.getString("tglselesai");
                provinsii = b.getString("provinsi");
                kotaa = b.getString("kota");
                kecamatann = b.getString("kecamatan");
                kelurahann = b.getString("kelurahan");
                uuidd = b.getString("uuid");
                coordinate_baru.setText(lat2+" ,"+lng2);
                nama.setText(namaa);
                nik.setText(nikk);
                alamat.setText(alamatt);
                spinner.setSelection(getIndex(spinner, jk));
                id.setText(idd);
                tglmulai.setText(tgl1);
                tglselesai.setText(tgl2);
                spProvinsi.setSelection(getIndex(spProvinsi,provinsii));
                spKota.setSelection(getIndex(spKota,kotaa));
                spKecamatan.setSelection(getIndex(spKecamatan,kecamatann));
                spKeluruhan.setSelection(getIndex(spKeluruhan,kelurahann));
                uuid.setText(uuidd);
            }
        }
        final Calendar calendar = Calendar.getInstance();
//test
        final int tahun = calendar.get(Calendar.YEAR);
        final int bulan = calendar.get(Calendar.MONTH);
        final int hari = calendar.get(Calendar.DAY_OF_MONTH);

        tglmulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Edit_Profile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayofmonth) {
                        String sDate = dayofmonth + "/" + month + "/" + year;
                        tglmulai.setText(sDate);
                    }
                }, tahun, bulan, hari
                );
                datePickerDialog.show();
            }
        });

        tglselesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Edit_Profile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayofmonth) {
                        String sDate = dayofmonth + "/" + month + "/" + year;
                        tglselesai.setText(sDate);
                    }
                }, tahun, bulan, hari
                );
                datePickerDialog.show();
            }
        });
        coordinate_baru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Edit_Profile.this, MapEdit.class);
                intent.putExtra("nama", nama.getText().toString());
                intent.putExtra("nik", nik.getText().toString());
                intent.putExtra("alamat", alamat.getText().toString());
                intent.putExtra("jk", spinner.getSelectedItem().toString());
                intent.putExtra("id", id.getText().toString());
                intent.putExtra("tglmulai", tglmulai.getText().toString());
                intent.putExtra("tglselesai", tglselesai.getText().toString());
                intent.putExtra("provinsi", spProvinsi.getSelectedItem().toString());
                intent.putExtra("kota", spKota.getSelectedItem().toString());
                intent.putExtra("kecamatan", spKecamatan.getSelectedItem().toString());
                intent.putExtra("keluruhan", spKeluruhan.getSelectedItem().toString());
                intent.putExtra("uuid", uuid.getText().toString());
                startActivity(intent);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama2 = nama.getText().toString();
                String nik2 = nik.getText().toString();
                String alamat2 = alamat.getText().toString();
                String id2 = id.getText().toString();
                String coordinatee = coordinate.getText().toString();
                String lat = String.valueOf(lat2);
                String lng = String.valueOf(lng2);
                String jk2 = spinner.getSelectedItem().toString();
                if (nama2.isEmpty() || nik2.isEmpty() || alamat2.isEmpty() ||
                        //idd.isEmpty() ||
                        coordinatee.isEmpty() ||
                        (tglmulai.getText().equals("Show Calendar")) ||
                        (tglselesai.getText().equals("Show Calendar"))) {
                    DynamicToast.makeError(Edit_Profile.this, "Harus Diisi semua!").show();
                }
                else {
                    auth = FirebaseAuth.getInstance();
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(Edit_Profile.this);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference getReference;
                    getReference = database.getReference();
                    String provinsi = spProvinsi.getSelectedItem().toString();
                    String kota = spKota.getSelectedItem().toString();
                    String kecamatan = spKecamatan.getSelectedItem().toString();
                    String kelurahan = spKeluruhan.getSelectedItem().toString();
                    String lat_me = "-6.865762";
                    String lng_me = "107.647618";

                    /*registerlist.add(new Registrasi_gs(alamatt, coordinatee, idd, tglmulai.getText().toString(), text, namaa, nikk,
                            riwayatt, tglselesai.getText().toString(), uuid.getText().toString()
                            , provinsi, kota, kecamatan, kelurahan));*/
                    //Mendapatkan Instance dari Database

                    getReference.child("Data ODP").child(id2)
                            .setValue(new Edit_gs(alamat2, lat, lat_me, lng, lng_me, idd, tglmulai.getText().toString(), text, nama2, nik2,
                                    tglselesai.getText().toString(), uuid.getText().toString()
                                    , provinsi, kota, kecamatan, kelurahan,id2))
                            .addOnSuccessListener(Edit_Profile.this, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                                    editlist.add(new Edit_gs(alamat2, lat, lat_me, lng, lng_me, idd, tglmulai.getText().toString(), text, nama2, nik2,
                                            tglselesai.getText().toString(), uuid.getText().toString()
                                            , provinsi, kota, kecamatan, kelurahan,id2));
                                    emptydata();
                                    DynamicToast.makeSuccess(Edit_Profile.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                                    Intent intent2 = new Intent(Edit_Profile.this, Informasi.class);
                                    startActivity(intent2);

                                }
                            });
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("KEMBALI")
                .setMessage("Apakah Anda Yakin?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Edit_Profile.super.onBackPressed();
                        //back problem netnot
                        Intent intent = new Intent(Edit_Profile.this, Informasi.class);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                }).create().show();
    }
    //get JK
    private int getIndex(Spinner spinner, String myString){
        for(int i = 0;i<spinner.getCount();i++){
            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }


public void ambildata(){

    String uuidd = getIntent().getStringExtra("device_id");
    reference = FirebaseDatabase.getInstance().getReference();

    if(uuidd != null) {
        reference.child("Data ODP").child(uuidd).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot Snapshot : snapshot.getChildren()) {

                    // Individu individu = Snapshot.getValue(Individu.class);

                    String namas = snapshot.child("nama").getValue(String.class);
                    String niks = snapshot.child("nik").getValue(String.class);
                    String alamats = snapshot.child("alamat").getValue(String.class);
                    String mulais = snapshot.child("durasi").getValue(String.class);
                    String selesais = snapshot.child("selesai").getValue(String.class);
                    String provinsis = snapshot.child("provinsi").getValue(String.class);
                    String kotas = snapshot.child("kota").getValue(String.class);
                    String kecamatans = snapshot.child("kecamatan").getValue(String.class);
                    String kelurahans = snapshot.child("kelurahan").getValue(String.class);
                    String jks = snapshot.child("jenis_kelamin").getValue(String.class);
                    String lats = snapshot.child("lat").getValue(String.class);
                    String lngs = snapshot.child("lng").getValue(String.class);
                    String uuids = snapshot.child("uuid").getValue(String.class);


                    coordinate.setText(lats + " ," + lngs);
                    nama.setText(namas);
                    nik.setText(niks);
                    alamat.setText(alamats);
                    spinner.setSelection(getIndex(spinner, jks));
                    //id masih error
                    id.setText(uuidd);
                    tglmulai.setText(mulais);
                    tglselesai.setText(selesais);
                    spProvinsi.setSelection(getIndex(spProvinsi, provinsis));
                    spKota.setSelection(getIndex(spKota, kotas));
                    spKecamatan.setSelection(getIndex(spKecamatan, kecamatans));
                    spKeluruhan.setSelection(getIndex(spKeluruhan, kelurahans));
                    uuid.setText(uuids);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DynamicToast.makeError(Edit_Profile.this, "eror");
            }
        });
    }
}

        public void emptydata () {
            nama.setText("");
            nik.setText("");
            alamat.setText("");
            coordinate.setText("");

            tglselesai.setText("Show Calendar");
            tglmulai.setText("Show Calendar");
            uuid.setText("");

        }


        private void makeJsonArrayRequest () {


            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                    url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());
                    try {
                        // Parsing json array response
                        // loop through each json object
                        jsonResponse = "";

                        JSONArray prov = response.getJSONArray("provinsi");
                        for (int i = 0; i < prov.length(); i++) {

                            JSONObject person = (JSONObject) prov.getJSONObject(i);
                            String idp = person.getString("id");
                            String name = person.getString("nama");
                            categories.add(name);
                            categories2.add(idp);
                            //riwayat.setText(kota);
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Edit_Profile.this, R.layout.simple_spinner_item, categories);
                            //spinnerAdapter.addAll(categories);
                            spinnerAdapter.notifyDataSetChanged();
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spProvinsi.setAdapter(spinnerAdapter);
                            //jsonResponse += "Nama: " + name + "\n\n";
                        }
                        spProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                //getdata
                                String provinsi = adapterView.getItemAtPosition(i).toString();
                                String url = "https://dev.farizdotid.com/api/daerahindonesia/kota?id_provinsi=" + categories2.get(i);
                                JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                                        url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d(TAG, response.toString());
                                        try {
                                            // Parsing json array response
                                            // loop through each json object
                                            jsonResponse = "";
                                            ArrayList<String> categories = new ArrayList<>();
                                            ArrayList<String> categories2 = new ArrayList<>();
                                            JSONArray prov = response.getJSONArray("kota_kabupaten");
                                            for (int i = 0; i < prov.length(); i++) {
                                                JSONObject person = (JSONObject) prov.getJSONObject(i);
                                                String idp = person.getString("id");
                                                String name = person.getString("nama");
                                                categories.add(name);
                                                categories2.add(idp);
                                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Edit_Profile.this, R.layout.simple_spinner_item, categories);
                                                spinnerAdapter.notifyDataSetChanged();
                                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spKota.setAdapter(spinnerAdapter);
                                                spKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        //getdata
                                                        String kota = adapterView.getItemAtPosition(i).toString();
                                                        String url = "https://dev.farizdotid.com/api/daerahindonesia/kecamatan?id_kota=" + categories2.get(i);
                                                        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                                                                url, null, new Response.Listener<JSONObject>() {

                                                            @Override
                                                            public void onResponse(JSONObject response) {
                                                                Log.d(TAG, response.toString());
                                                                try {
                                                                    // Parsing json array response
                                                                    // loop through each json object
                                                                    jsonResponse = "";
                                                                    ArrayList<String> categories = new ArrayList<>();
                                                                    ArrayList<String> categories2 = new ArrayList<>();
                                                                    JSONArray prov = response.getJSONArray("kecamatan");
                                                                    for (int i = 0; i < prov.length(); i++) {
                                                                        JSONObject person = (JSONObject) prov.getJSONObject(i);
                                                                        String idp = person.getString("id");
                                                                        String name = person.getString("nama");
                                                                        categories.add(name);
                                                                        categories2.add(idp);
                                                                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Edit_Profile.this, R.layout.simple_spinner_item, categories);
                                                                        spinnerAdapter.notifyDataSetChanged();
                                                                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                        spKecamatan.setAdapter(spinnerAdapter);
                                                                        spKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                            @Override
                                                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                                //getdata
                                                                                String kecamaatan = adapterView.getItemAtPosition(i).toString();
                                                                                String url = "https://dev.farizdotid.com/api/daerahindonesia/kelurahan?id_kecamatan=" + categories2.get(i);
                                                                                JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                                                                                        url, null, new Response.Listener<JSONObject>() {

                                                                                    @Override
                                                                                    public void onResponse(JSONObject response) {
                                                                                        Log.d(TAG, response.toString());
                                                                                        try {
                                                                                            // Parsing json array response
                                                                                            // loop through each json object
                                                                                            jsonResponse = "";
                                                                                            ArrayList<String> categories = new ArrayList<>();
                                                                                            ArrayList<String> categories2 = new ArrayList<>();
                                                                                            JSONArray prov = response.getJSONArray("kelurahan");
                                                                                            for (int i = 0; i < prov.length(); i++) {
                                                                                                JSONObject person = (JSONObject) prov.getJSONObject(i);
                                                                                                String idp = person.getString("id");
                                                                                                String name = person.getString("nama");
                                                                                                categories.add(name);
                                                                                                categories2.add(idp);
                                                                                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Edit_Profile.this, R.layout.simple_spinner_item, categories);
                                                                                                spinnerAdapter.notifyDataSetChanged();
                                                                                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                                                spKeluruhan.setAdapter(spinnerAdapter);
                                                                                            }

                                                                                        } catch (JSONException e) {
                                                                                            e.printStackTrace();
                                                                                            Toast.makeText(getApplicationContext(),
                                                                                                    "Error: " + e.getMessage(),
                                                                                                    Toast.LENGTH_LONG).show();
                                                                                        }
                                                                                    }

                                                                                }, new Response.ErrorListener() {

                                                                                    @Override
                                                                                    public void onErrorResponse(VolleyError error) {
                                                                                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                                                                                        Toast.makeText(getApplicationContext(),
                                                                                                error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                        // hide the progress dialog

                                                                                    }
                                                                                });


                                                                                // Adding request to request queue
                                                                                AppController.getInstance().addToRequestQueue(req);
                                                                            }

                                                                            @Override
                                                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                                                            }
                                                                        });
                                                                    }

                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                    Toast.makeText(getApplicationContext(),
                                                                            "Error: " + e.getMessage(),
                                                                            Toast.LENGTH_LONG).show();
                                                                }
                                                            }

                                                        }, new Response.ErrorListener() {

                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                                                Toast.makeText(getApplicationContext(),
                                                                        error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                // hide the progress dialog

                                                            }
                                                        });


                                                        // Adding request to request queue
                                                        AppController.getInstance().addToRequestQueue(req);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                    }
                                                });
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(),
                                                    "Error: " + e.getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }

                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                                        Toast.makeText(getApplicationContext(),
                                                error.getMessage(), Toast.LENGTH_SHORT).show();
                                        // hide the progress dialog

                                    }
                                });


                                // Adding request to request queue
                                AppController.getInstance().addToRequestQueue(req);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                    // hide the progress dialog

                }
            });


            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);
        }
public void updatedata(){


}
    }
