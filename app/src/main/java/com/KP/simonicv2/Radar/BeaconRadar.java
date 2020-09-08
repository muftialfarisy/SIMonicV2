package com.KP.simonicv2.Radar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import static com.KP.simonicv2.Radar.BeaconNotification.locationGPS;
import static com.KP.simonicv2.Radar.Constant.serverDevicesList;

public class BeaconRadar extends Fragment implements BeaconConsumer {//}, LocationProvider.LocationCallback {

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(getActivity());
    TextView detectedbeacon;

    SensorManager mSensorManager;
    Sensor accSensor;
    Sensor magnetSensor;
    RadarUIView mRadar;
    RequestQueue queue;
    MCrypt mCrypt = new MCrypt();
    String decrypted = null;

    String[] dataparse;

    private Notification.Builder mBuilder;
    private NotificationManager mNotificationManager;
    final  String NOTIFICATION_CHANNEL_ID = "10003";

    private TextView textViewTittle;
    //private Location locationGPS = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    //Constructing a new Region object to be used for Ranging or Monitoring
    //final Region region = new Region("myBeaonsradar",null, null, null);
    //public static LocationProvider mLocationProvider;
    //RequestQueue queue;
    //private double statusbattery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initbeacon();

        mSensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        queue = Volley.newRequestQueue(getApplicationContext());
        TraccarAPI.getData(getContext(),queue);
    }

    private void initbeacon()
    {
        //getting beaconManager instance (object) for Main Activity class
        beaconManager = BeaconManager.getInstanceForApplication(getActivity());

        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        //beaconManager.getBeaconParsers().add(new BeaconParser().
        //       setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.URI_BEACON_LAYOUT));
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("s:0-1=fd6f,p:0-0:-63,i:2-17,d:18-21"));
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("s:0-1=fd6f,p:-:-59,i:2-17,d:18-21"));

        //Binding MainActivity to the BeaconService.
        beaconManager.bind(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.beaconscope, container, false);
        mRadar = (RadarUIView)fragmentView.findViewById(R.id.radar);

        detectedbeacon = (TextView)fragmentView.findViewById(R.id.textViewSizedetectedBeacon);
        textViewTittle = fragmentView.findViewById(R.id.textViewTittle);
        mRadar.setUseMetric(true);

        return fragmentView;
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mRadar, accSensor);
        mSensorManager.unregisterListener(mRadar, magnetSensor);
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(Constant.statuspermission)
                mLocationProvider.disconnect();
        }
        else
        {
            mLocationProvider.disconnect();
        }

         */
        super.onPause();
    }

    @Override
    public void onStop() {
        mRadar.stopSweep();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRadar.startSweep();
        //if (mBeaconManager.isBound(this)) mBeaconManager.setBackgroundMode(false);
        mSensorManager.registerListener(mRadar, accSensor, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mRadar, magnetSensor, SensorManager.SENSOR_DELAY_GAME);
        /*if (beaconManager.isBound(this)) {
            if (beaconManager.isBound(this)) {
                try {
                    beaconManager.startRangingBeaconsInRegion(Constant.regionbackground);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            initbeacon();
        }*/
        if(!beaconManager.isBound(this)){
            beaconManager.bind(this);
            //try {
            //    beaconManager.startRangingBeaconsInRegion(Constant.regionbackground);
            //} catch (RemoteException e) {
            //    e.printStackTrace();
            //}
        }
        BeaconNotification beaconNotification = (BeaconNotification) this.getApplicationContext();
        beaconNotification.disableMonitoring();
        if(serverDevicesList.isEmpty())
            TraccarAPI.getData(getContext(),queue);
    }

    @Override
    public void onBeaconServiceConnect() {
        //Specifies a class that should be called each time the BeaconService sees or stops seeing a Region of beacons.
/*        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
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
            public void didDetermineStateForRegion(int state, Region region) {
                System.out.println( "I have just switched from seeing/not seeing beacons: "+state);
            }
        });*/
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, Region region) {
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
                                                mRadar.onDetectedBeacons(beacons);
                                                sendNotification("Found ODP/PDP COVID-19", "#Stay at Home\n#Physical Distancing");
                                                sendtoServer(locationGPS, Constant.URLserver, String.valueOf(b.getId1()), getBatteryLevel());
                                                //fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                                //Toast.makeText(this,"lat: "+String.valueOf(locationGPS.getLatitude())+"long: "+String.valueOf(locationGPS.getLongitude()),Toast.LENGTH_LONG).show();
                                            } else
                                                Toast.makeText(getApplicationContext(), "SiMonic: GPS not locked", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        if (b.getServiceUuid() != 0xfeaa && dataparse[0].equals(Constant.prevname)) {
                                            String id = String.valueOf(b.getId1());//+"-"+String.valueOf(b.getId2()) + "-" + String.valueOf(b.getId3());
                                            String name = Constant.prevname + "-" + String.valueOf(b.getId2()) + "-" + String.valueOf(b.getId3());
                                            TraccarAPI.sendData(getContext(), queue, name, id);
                                            //get data
                                            TraccarAPI.getData(getContext(), queue);
                                        }
                                    }
                                } else {
                                    if (b.getServiceUuid() != 0xfeaa && dataparse[0].equals(Constant.prevname)) {
                                        String id = String.valueOf(b.getId1());//+"-"+String.valueOf(b.getId2()) + "-" + String.valueOf(b.getId3());
                                        String name = Constant.prevname + "-" + String.valueOf(b.getId2()) + "-" + String.valueOf(b.getId3());
                                        TraccarAPI.sendData(getContext(), queue, name, id);
                                        //get data
                                        TraccarAPI.getData(getContext(), queue);
                                    }
                                }
                            }else{
                                if (dataparse[0].equals(Constant.prevname) && dataparse[1].equals(String.valueOf(b.getId2())) && dataparse[2].equals(String.valueOf(b.getId3())))//serverDevicesList.contains(String.valueOf(b.getId1())+"-"+String.valueOf(b.getId2()) + "-" + String.valueOf(b.getId3())))
                                {
                                    if (b.getDistance() < Constant.RADAR_RADIS_VISION_METERS) {
                                        //getLocation();
                                        //sendNotification("Found ODP/PDP COVID-19", "#Stay at Home\n#Physical Distancing");
                                        if (locationGPS != null) {
                                            mRadar.onDetectedBeacons(beacons);
                                            sendNotification("Found ODP/PDP COVID-19", "#Stay at Home\n#Physical Distancing");
                                            sendtoServer(locationGPS, Constant.URLserver, String.valueOf(b.getId1()), getBatteryLevel());
                                            //fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                            //Toast.makeText(this,"lat: "+String.valueOf(locationGPS.getLatitude())+"long: "+String.valueOf(locationGPS.getLongitude()),Toast.LENGTH_LONG).show();
                                        } else
                                            Toast.makeText(getApplicationContext(), "SiMonic: GPS not locked", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        });

        try {
            //Tells the BeaconService to start looking for beacons that match the passed Region object.
            //beaconManager.startMonitoringBeaconsInRegion(Constant.regionbackground);
            beaconManager.startRangingBeaconsInRegion(Constant.regionbackground);
        } catch (RemoteException e) {    }
    }

    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return getActivity().bindService(intent, serviceConnection, i);
    }


    public void sendtoServer(Location location, String URL, String ID, double batt)
    {

        String url = ProtocolFormatter.formatRequest(ID,URL,location,batt,"no");
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

    private double getBatteryLevel() {
        Intent batteryIntent = getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (batteryIntent != null) {
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
            return (level * 100.0) / scale;
        }
        return 0;
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

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());
        //mBuilder.setSmallIcon(R.drawable.ic_my_location_red_24dp);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                //.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (beaconManager.isBound(this)) {
            beaconManager.unbind(this);
        }
        BeaconNotification beaconNotification = (BeaconNotification) this.getApplicationContext();
        beaconNotification.enableMonitoring();
    }
}