package com.KP.simonicv2.Registrasi;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.KP.simonicv2.AppController;
import com.KP.simonicv2.HttpHandler;
import com.KP.simonicv2.Individu.Detail_Individu;
import com.KP.simonicv2.Individu.Individu;
import com.KP.simonicv2.Individu.Informasi;
import com.KP.simonicv2.Login.Login;
import com.KP.simonicv2.MainActivity;
import com.KP.simonicv2.R;
import com.KP.simonicv2.Reportc.Report_c;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class Registrasi extends AppCompatActivity {
    Spinner spProvinsi,jeniskelamin,spinner,spKota,spKecamatan,spKeluruhan;
    private String TAG = Registrasi.class.getSimpleName();
    ArrayList <Registrasi_gs> registerlist = new ArrayList<>();
    private ArrayList<Individu> dataList = new ArrayList<>();
    ArrayList <String> List = new ArrayList<>();
    TextView tglmulai,tglselesai,jdl_coordinate;
    ImageButton exit;
    Button registrasi;
    EditText nama,nik,alamat,id,riwayat,coordinate,uuid;

    private ProgressDialog pDialog;
    private static String url = "https://dev.farizdotid.com/api/daerahindonesia/provinsi";
    private String jabar = "https://services5.arcgis.com/VS6HdKS0VfIhv8Ct/arcgis/rest/services/COVID19_Indonesia_per_Provinsi/FeatureServer/0/query?where=Provinsi%20%3D%20%27JAWA%20BARAT%27&outFields=*&outSR=4326&f=json";
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String jsonResponse;
    ArrayList<String> categories = new ArrayList<>();
    ArrayList<String> categories2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrasi);
        registerlist = new ArrayList<>();
        spProvinsi = (Spinner) findViewById(R.id.sp_name);
        spKota =(Spinner) findViewById(R.id.sp_name2);
        spKecamatan=(Spinner) findViewById(R.id.sp_name3);
        spKeluruhan=(Spinner) findViewById(R.id.sp_name4);
        tglmulai = (TextView) findViewById(R.id.txt_durasi);
        tglselesai = (TextView) findViewById(R.id.txt_selesai);
        nama = (EditText) findViewById(R.id.txt_nama);
        nik = (EditText) findViewById(R.id.txt_nik);
        alamat = (EditText) findViewById(R.id.txt_alamat);
        id = (EditText) findViewById(R.id.txt_device);
        spinner = (Spinner) findViewById(R.id.sp_jk);
        makeJsonArrayRequest();
        //makeJsonObjectRequest();

        final String text = spinner.getSelectedItem().toString();
        pDialog = new ProgressDialog(Registrasi.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        riwayat = (EditText) findViewById(R.id.txt_riwayat);
        coordinate = (EditText) findViewById(R.id.txt_coordinate);
        jdl_coordinate = (TextView) findViewById(R.id.jdl_coordinate);
        coordinate.setFocusable(false);
        Intent intent = this.getIntent();
        if(intent != null) {
            Bundle b = intent.getExtras();
            if(b != null) {
                //double lat = intent.getDoubleExtra("lat", 0.00);
                //double lng = intent.getDoubleExtra("lng", 0.00);
                double lat = b.getDouble("lat");
                double lng = b.getDouble("lng");
                coordinate.setText(lat+" ,"+lng);
            }
        }
        coordinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registrasi.this, Map_coordinate.class);
                startActivity(intent);
            }
        });
        uuid = (EditText) findViewById(R.id.txt_uuid);
        exit = (ImageButton) findViewById(R.id.exit_regis);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registrasi.this, MainActivity.class);
                startActivity(intent);
            }
        });


        final Calendar calendar = Calendar.getInstance();
//test
        final int tahun = calendar.get(Calendar.YEAR);
        final int bulan = calendar.get(Calendar.MONTH);
        final int hari = calendar.get(Calendar.DAY_OF_MONTH);

        tglmulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Registrasi.this, new DatePickerDialog.OnDateSetListener() {
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
                        Registrasi.this, new DatePickerDialog.OnDateSetListener() {
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

        registrasi = (Button) findViewById(R.id.btn_registrasi);
        registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaa = nama.getText().toString();
                String nikk = nik.getText().toString();
                String alamatt = alamat.getText().toString();
                String idd = id.getText().toString();
                String riwayatt = riwayat.getText().toString();
                String coordinatee = coordinate.getText().toString();
                if (namaa.isEmpty() || nikk.isEmpty() || alamatt.isEmpty() ||
                        idd.isEmpty() || riwayatt.isEmpty() || coordinatee.isEmpty() ||
                        (tglmulai.getText().equals("Show Calendar")) ||
                        (tglselesai.getText().equals("Show Calendar"))) {
                    DynamicToast.makeError(Registrasi.this, "Harus Diisi semua!").show();
                }
                else {
                    mAuth = FirebaseAuth.getInstance();
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(Registrasi.this);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference getReference;
                    getReference = database.getReference();
                    String provinsi = spProvinsi.getSelectedItem().toString();
                    String kota = spKota.getSelectedItem().toString();
                    String kecamatan = spKecamatan.getSelectedItem().toString();
                    String kelurahan = spKeluruhan.getSelectedItem().toString();

                    String getUserID = mAuth.getCurrentUser().getUid();
                    registerlist = new ArrayList<>();
                    /*registerlist.add(new Registrasi_gs(alamatt, coordinatee, idd, tglmulai.getText().toString(), text, namaa, nikk,
                            riwayatt, tglselesai.getText().toString(), uuid.getText().toString()
                            , provinsi, kota, kecamatan, kelurahan));*/
                    //Mendapatkan Instance dari Database

                    getReference.child("Data ODP").child(namaa)
                            .setValue(new Registrasi_gs(alamatt, coordinatee, idd, tglmulai.getText().toString(), text, namaa, nikk,
                                    riwayatt, tglselesai.getText().toString(), uuid.getText().toString()
                                    , provinsi, kota, kecamatan, kelurahan))
                            .addOnSuccessListener(Registrasi.this, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                                    dataList.add(new Individu(namaa,nikk,alamatt,provinsi,kota,kecamatan,kelurahan,tglmulai.getText().toString(),tglselesai.getText().toString()));

                                    emptydata();
                                    DynamicToast.makeSuccess(Registrasi.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }
        });
        //new GetData().execute();

    }


    public void emptydata(){
        nama.setText("");
        nik.setText("");
        alamat.setText("");
        id.setText("");
        riwayat.setText("");
        coordinate.setText("");

        tglselesai.setText("Show Calendar");
        tglmulai.setText("Show Calendar");
        uuid.setText("");

    }



    private void makeJsonArrayRequest() {



        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                url,null, new Response.Listener<JSONObject>() {

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
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Registrasi.this, R.layout.simple_spinner_item, categories);
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
                            String url ="https://dev.farizdotid.com/api/daerahindonesia/kota?id_provinsi="+ categories2.get(i);
                            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                                    url,null, new Response.Listener<JSONObject>() {

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
                                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Registrasi.this, R.layout.simple_spinner_item, categories);
                                            spinnerAdapter.notifyDataSetChanged();
                                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            spKota.setAdapter(spinnerAdapter);
                                            spKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                    //getdata
                                                    String kota = adapterView.getItemAtPosition(i).toString();
                                                    String url ="https://dev.farizdotid.com/api/daerahindonesia/kecamatan?id_kota="+categories2.get(i);
                                                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                                                            url,null, new Response.Listener<JSONObject>() {

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
                                                                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Registrasi.this, R.layout.simple_spinner_item, categories);
                                                                    spinnerAdapter.notifyDataSetChanged();
                                                                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                    spKecamatan.setAdapter(spinnerAdapter);
                                                                    spKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                        @Override
                                                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                            //getdata
                                                                            String kecamaatan = adapterView.getItemAtPosition(i).toString();
                                                                            String url ="https://dev.farizdotid.com/api/daerahindonesia/kelurahan?id_kecamatan="+categories2.get(i);
                                                                            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                                                                                    url,null, new Response.Listener<JSONObject>() {

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
                                                                                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Registrasi.this, R.layout.simple_spinner_item, categories);
                                                                                            spinnerAdapter.notifyDataSetChanged();
                                                                                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                                            spKeluruhan.setAdapter(spinnerAdapter);
                                                                                            spKeluruhan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                                                @Override
                                                                                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                                                    //getdata
                                                                                                    String kelurahan = adapterView.getItemAtPosition(i).toString();

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

                                                                            },      new Response.ErrorListener() {

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

                                                    },      new Response.ErrorListener() {

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

                            },      new Response.ErrorListener() {

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

        },      new Response.ErrorListener() {

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



    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void getdata(){


    }
}
