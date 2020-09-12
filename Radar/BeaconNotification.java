package com.KP.simonicv2.Radar;

import android.Manifest;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Build;
import android.os.RemoteException;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.KP.simonicv2.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Collection;

import static com.KP.simonicv2.Radar.Constant.serverDevicesList;

public class BeaconNotification extends Application implements BootstrapNotifier, RangeNotifier {
    static final private String TAG = BeaconNotification.class.getSimpleName();

    public static BeaconManager beaconManager;
    public static BackgroundPowerSaver backgroundPowerSaver;
    public static RegionBootstrap regionBootstrap;

    private RequestQueue queue;
    private MCrypt mCrypt = new MCrypt();
    private String decrypted = null;
    private String[] dataparse;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    public static Location locationGPS = null;


    @Override
    public void onCreate() {
        super.onCreate();

        cekBluetooth();
        initbeacon();
        //get data from server
        queue = Volley.newRequestQueue(this);
        TraccarAPI.getData(getApplicationContext(), queue);
        //gps
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(Constant.LocationInterval);
        locationRequest.setFastestInterval(Constant.LocationFastestInterval);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    locationGPS = locationResult.getLastLocation();
                } else
                    locationGPS = locationResult.getLastLocation();
            }
        };

        if (SharedPreference.getEnablepermission(this)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void getLocation()
    {
        if(SharedPreference.getEnablepermission(this))
        {
            try {
                fusedLocationProviderClient.getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    locationGPS = task.getResult();
                                } else {
                                    locationGPS = task.getResult();
                                }
                            }
                        });
            } catch (SecurityException unlikely) {
                //Log.e(TAG, "Lost location permission." + unlikely);
            }

            //fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            //    @Override
            //    public void onSuccess(Location location) {
            //        if(location!=null)
            //            locationGPS = location;
            //else
            //    fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
            //    }
            //});
        }
    }

    private double getBatteryLevel() {
        Intent batteryIntent = this.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (batteryIntent != null) {
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
            return (level * 100.0) / scale;
        }
        return 0;
    }

    private void cekBluetooth()
    {
        // Getting the bluetooth adapter object
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Checking if bluetooth is supported by device or not
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Bluetooth Not Supported",Toast.LENGTH_LONG).show();
        } else {
            // if bluetooth is supported but not enabled then enable it
            if (!mBluetoothAdapter.isEnabled()) {
                Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                bluetoothIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(bluetoothIntent);
            }
        }
    }

    private void initbeacon()
    {
        //getting beaconManager instance (object) for Main Activity class
        beaconManager = BeaconManager.getInstanceForApplication(this);

        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        //beaconManager.getBeaconParsers().add(new BeaconParser().
        //       setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.URI_BEACON_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("s:0-1=fd6f,p:0-0:-63,i:2-17,d:18-21"));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("s:0-1=fd6f,p:-:-59,i:2-17,d:18-21"));
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_baseline_my_location_24);
        builder.setContentTitle(Constant.NotificationTitle);
        builder.setContentText(Constant.NotificationTxt);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constant.CHANNEL_ID,
                    Constant.CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setDescription(Constant.CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channel.getId());
        }
        beaconManager.enableForegroundServiceScanning(builder.build(), 456);

        // For the above foreground scanning service to be useful, you need to disable
        // JobScheduler-based scans (used on Android 8+) and set a fast background scan
        // cycle that would otherwise be disallowed by the operating system.
        //
        beaconManager.setEnableScheduledScanJobs(false);
        beaconManager.setForegroundScanPeriod(1100L);
        beaconManager.setForegroundBetweenScanPeriod(Constant.nilaisettingInterval[Constant.Intervalposisi]);
        beaconManager.setAndroidLScanningDisabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {	//Android 8+, limited to scanning every ~15 minutes
            beaconManager.setBackgroundBetweenScanPeriod(Constant.nilaisettingInterval[Constant.Intervalposisi]);
        } else {
            beaconManager.setBackgroundBetweenScanPeriod(Constant.nilaisettingInterval[Constant.Intervalposisi]);
        }
        //beaconManager.setBackgroundBetweenScanPeriod(0);
        beaconManager.setBackgroundScanPeriod(1100L);

        try {
            //Updates an already running scan
            beaconManager.updateScanPeriods();
        } catch (Exception e) {
        }

        beaconManager.addRangeNotifier(this);

        regionBootstrap = new RegionBootstrap( this, Constant.regionbackground);

        backgroundPowerSaver = new BackgroundPowerSaver(this);
        //Binding MainActivity to the BeaconService.
    }

    @Override
    public void didEnterRegion(Region region) {
        //TraccarApi.getData(getApplicationContext(),queue);
        try {
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void didExitRegion(Region region) {
        try {
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons.size() > 0) {
            decrypted = null;
            for (Beacon b:beacons){
                String plaintext = String.valueOf(b.getId1()).replace("-","");
                //decrypt uuid
                try {
                    decrypted = new String(mCrypt.decrypt(plaintext) );
                    dataparse = decrypted.split("-");
                    if(!Constant.statusserver) {
                        if (!serverDevicesList.isEmpty()) {
                            if (serverDevicesList.contains(String.valueOf(b.getId1())) && dataparse[0].equals(Constant.prevname) && dataparse[1].equals(String.valueOf(b.getId2())) && dataparse[2].equals(String.valueOf(b.getId3())))//serverDevicesList.contains(String.valueOf(b.getId1())+"-"+String.valueOf(b.getId2()) + "-" + String.valueOf(b.getId3())))
                            {
                                if (b.getDistance() < Constant.RADAR_RADIS_VISION_METERS) {
                                    //getLocation();
                                    //sendNotification("Found ODP/PDP COVID-19", "#Stay at Home\n#Physical Distancing");
                                    if (locationGPS != null) {
                                        sendNotification("Found ODP/PDP COVID-19", "#Stay at Home\n#Physical Distancing");
                                        sendtoServer(locationGPS, Constant.URLserver, String.valueOf(b.getId1()), getBatteryLevel());
                                        //fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                        //Toast.makeText(this,"lat: "+String.valueOf(locationGPS.getLatitude())+"long: "+String.valueOf(locationGPS.getLongitude()),Toast.LENGTH_LONG).show();
                                    } else
                                        Toast.makeText(this, "SiMonic: GPS not locked", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                if (b.getServiceUuid() != 0xfeaa && dataparse[0].equals(Constant.prevname)) {
                                    String id = String.valueOf(b.getId1());//+"-"+String.valueOf(b.getId2()) + "-" + String.valueOf(b.getId3());
                                    String name = Constant.prevname + "-" + String.valueOf(b.getId2()) + "-" + String.valueOf(b.getId3());
                                    TraccarAPI.sendData(getApplicationContext(), queue, name, id);
                                    //get data
                                    TraccarAPI.getData(getApplicationContext(), queue);
                                }
                            }
                        } else {
                            if (b.getServiceUuid() != 0xfeaa && dataparse[0].equals(Constant.prevname)) {
                                String id = String.valueOf(b.getId1());//+"-"+String.valueOf(b.getId2()) + "-" + String.valueOf(b.getId3());
                                String name = Constant.prevname + "-" + String.valueOf(b.getId2()) + "-" + String.valueOf(b.getId3());
                                TraccarAPI.sendData(getApplicationContext(), queue, name, id);
                                //get data
                                TraccarAPI.getData(getApplicationContext(), queue);
                            }
                        }
                    } else{
                        if (dataparse[0].equals(Constant.prevname) && dataparse[1].equals(String.valueOf(b.getId2())) && dataparse[2].equals(String.valueOf(b.getId3())))//serverDevicesList.contains(String.valueOf(b.getId1())+"-"+String.valueOf(b.getId2()) + "-" + String.valueOf(b.getId3())))
                        {
                            if (b.getDistance() < Constant.RADAR_RADIS_VISION_METERS) {
                                //getLocation();
                                //sendNotification("Found ODP/PDP COVID-19", "#Stay at Home\n#Physical Distancing");
                                if (locationGPS != null) {
                                    sendNotification("Found ODP/PDP COVID-19", "#Stay at Home\n#Physical Distancing");
                                    sendtoServer(locationGPS, Constant.URLserver, String.valueOf(b.getId1()), getBatteryLevel());
                                    //fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    //Toast.makeText(this,"lat: "+String.valueOf(locationGPS.getLatitude())+"long: "+String.valueOf(locationGPS.getLongitude()),Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(this, "SiMonic: GPS not locked", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void sendtoServer(Location location, String URL, String ID, double batt)
    {

        String url = ProtocolFormatter.formatRequest(ID,URL,location,batt,"NO");
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    Toast.makeText(getApplicationContext(),"SiMonic: Error Update Location",Toast.LENGTH_LONG).show();
                }catch (Exception e){}
            }
        });

        queue.add(stringRequest);

    }

    private void sendNotification(String title, String message)
    {
        /**Creates an explicit intent for an Activity in your app**/
        /*Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
         */
        /**Creates an explicit intent for an Activity in your app**/
        Intent resultIntent = new Intent();
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(this);
        //mBuilder.setSmallIcon(R.drawable.ic_my_location_red_24dp);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                //.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(Constant.CHANNEL_ID2, Constant.CHANNEL_NAME2, importance);
            notificationChannel.setDescription(Constant.CHANNEL_DESCRIPTION2);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //assert mNotificationManager != null;
            mBuilder.setChannelId(notificationChannel.getId());
            mBuilder.setSmallIcon(R.drawable.ic_baseline_my_location_24);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        else{
            mBuilder.setSmallIcon(R.drawable.ic_baseline_my_location_24);
            mBuilder.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }

        mNotificationManager.notify(Constant.NotificationID, mBuilder.build());
    }

    public void disableMonitoring() {
        if (regionBootstrap != null) {
            regionBootstrap.disable();
            regionBootstrap = null;
        }
    }

    public void enableMonitoring() {
        regionBootstrap = new RegionBootstrap(this, Constant.regionbackground);
    }
}
