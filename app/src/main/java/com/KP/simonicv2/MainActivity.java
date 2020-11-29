package com.KP.simonicv2;
//p
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.KP.simonicv2.Individu.Detail_Individu;
import com.KP.simonicv2.Individu.Individu;
import com.KP.simonicv2.Individu.IndividuAdapter;
import com.KP.simonicv2.Individu.Informasi;
import com.KP.simonicv2.Login.Login;

//import com.KP.simonicv2.Radar.MCrypt;
//import com.KP.simonicv2.Radar.Main;
//import com.KP.simonicv2.Radar.SharedPreference;
import com.KP.simonicv2.Profile.Profile;
import com.KP.simonicv2.Profile.ProfileFragment;
import com.KP.simonicv2.Registrasi.Registrasi;
import com.KP.simonicv2.Reportc.ReportcFragment;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IndividuAdapter.OnindListener {

    CardView cd_regis,cd_info,cd_radar,cd_setting;
    private FirebaseUser mUser;
    FirebaseUser mlogout;
    protected static final String TAG = "MonitoringActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    //private TextView textViewTittle;
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;
    //private static final int PERMISSION_REQUEST_COARSE_LOCATION = 3;

    AlertDialog.Builder alertdialogsettingRadius;
    AlertDialog.Builder alertdialogsettingInterval;

    //MCrypt mcrypt;
    private static final Integer request= 1;

    //checking data firebase
    IndividuAdapter adapter;
    private DatabaseReference reference;
    private ArrayList<Individu> dataList = new ArrayList<>();
    private EditText test;
    private FirebaseAuth auth;
    private String GetUserID;
    public String TA = "FIREBASE MESSAGING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        validasi();
        checkPermission();
        auth = FirebaseAuth.getInstance();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        getdata();
        cd_regis = (CardView) findViewById(R.id.cd_register);
        cd_regis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Registrasi.class);
                startActivity(intent);
            }
        });
        cd_info = (CardView) findViewById(R.id.cd_info);
        cd_info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Informasi.class);
                startActivity(intent);
            }
        });
        cd_radar = (CardView) findViewById(R.id.cd_radar);
        cd_radar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Intent intent= new Intent(MainActivity.this, Main.class);
                //startActivity(intent);

            }
        });
        cd_setting = (CardView) findViewById(R.id.cd_setting);
        cd_setting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

    }
    public void validasi(){
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null){
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();
        }
    }

    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Constant.statuspermission = true;
               // startServices();
                //BeaconRadar.mLocationProvider.connect();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("This app needs background location access");
                            builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @TargetApi(23)
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                            PERMISSION_REQUEST_BACKGROUND_LOCATION);
                                }

                            });
                            builder.show();
                        }
                        else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Functionality limited");
                            builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons in the background.  Please go to Settings -> Applications -> Permissions and grant background location access to this app.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }

                            });
                            builder.show();
                        }
                    }
                    else
                    {
                        //Constant.statuspermission = true;
                        //BeaconRadar.mLocationProvider.connect();
                        //startServices();
                    }
                }
            } else {
                if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            PERMISSION_REQUEST_FINE_LOCATION);
                }
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location access to this app.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
            }
        }
        else
        {
            //startServices();
        }
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPlayServices()) {
            Toast.makeText(this,"You need to install Google Play Services to use the App properly",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.d(TAG, "fine location permission granted");
                    //Constant.statuspermission = true;
                    //BeaconRadar.mLocationProvider.connect();
                    //startServices();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
            case PERMISSION_REQUEST_BACKGROUND_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "background location permission granted");
                    //Constant.statuspermission = true;
                    //BeaconRadar.mLocationProvider.connect();
                    //startServices();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }


    /*private void startServices()
    {
        if(!SharedPreference.getEnablepermission(getApplicationContext()))
            SharedPreference.setEnablepermission(getApplicationContext(),true);
    }*/

    public void getdata() {
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Data ODP").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                                        Individu individu = Snapshot.getValue(Individu.class);

                    dataList.add(individu);

                    //adapter = new IndividuAdapter(dataList, MainActivity.this);
                    String lat = (String) Snapshot.child("lat").getValue();
                    String lng = (String) Snapshot.child("lng").getValue();
                    String lat_me = (String) Snapshot.child("lat_me").getValue();
                    String lng_me = (String) Snapshot.child("lng_me").getValue();
                    String nama = (String) Snapshot.child("nama").getValue();



                    Double lat2 = Double.parseDouble(lat);
                    Double lng2 = Double.parseDouble(lng);
                    Double lat_me2 = Double.parseDouble(lat_me);
                    Double lng_me2 = Double.parseDouble(lng_me);

                    double radiusInKilometers = 1;
                    double numberOfSides = 64;
                    double distanceX = radiusInKilometers / (111.319 * Math.cos(lat2 * Math.PI / 180));
                    double distanceY = radiusInKilometers / 110.574;
                    int id = 1;

                    double slice = (2 * Math.PI) / numberOfSides;
                    double theta;
                    double x1,y1,a1,b1,a2,b2;
                        theta = 64 * slice;
                        x1 = distanceX * Math.cos(theta);
                        y1 = distanceY * Math.sin(theta);

                        a1 = lat2 + y1;
                        a2 = lat2 - y1;
                        b1 = lng2 + x1;
                        b2 = lng2 - x1;
                        if (((a1 > lat_me2) && (lat_me2 > a2)) || ((b1 > lng_me2) && (lng_me2 > b2))) {

                        }else{
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "My Notification");
                            builder.setContentTitle("My title");
                            builder.setContentText(nama+" test");
                            builder.setSmallIcon(android.R.drawable.sym_action_chat);
                            builder.setAutoCancel(true);

                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                            managerCompat.notify(id, builder.build());
                        }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DynamicToast.makeError(MainActivity.this, "eror");
            }
        });

    }


    @Override
    public void onindClick(int position) {
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

    private void verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //finish();
                        //System.exit(0);
                    }
                });
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    //finish();
                    //System.exit(0);
                }

            });
            builder.show();

        }

    }
}
//test