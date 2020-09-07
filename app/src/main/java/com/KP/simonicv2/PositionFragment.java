package com.KP.simonicv2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.MapBoxTileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;


public class PositionFragment extends Fragment implements LocationListener {

    MapView map = null;
    private MapView osm;
    private MapController mc;
    private LocationManager locationManager;
    private CompassOverlay compassOverlay;
    private DirectedLocationOverlay locationOverlay;
    private MyLocationNewOverlay mLocationOverlay;
    private static final int REQUEST_LOCATION = 123;
    LocationManager mgr;
    Marker myLocation;
    Context context;
    boolean added = false;
    boolean followme=true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_position, container, false);
        osm = (MapView) view.findViewById(R.id.mapview2);
        Context ctx = getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        final MapBoxTileSource tileSource = new MapBoxTileSource();
        osm.setTileSource(TileSourceFactory.MAPNIK);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);

        mc = (MapController)osm.getController();
        mc.setZoom(15);
        permission();
        Polygon oPolygon = new org.osmdroid.views.overlay.Polygon(osm);
        final double radius = 1610;
        ArrayList<GeoPoint> circlePoints = new ArrayList<GeoPoint>();
        for (float f = 0; f < 360; f += 1){
            circlePoints.add(new GeoPoint(-6.919739 , 107.569431 ).destinationPoint(radius, f));
        }
        oPolygon.setPoints(circlePoints);
        osm.getOverlays().add(oPolygon);

        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()),osm);
        osm.getOverlays().add(this.mLocationOverlay);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation();
        osm.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                Log.i("Script", "onScroll()");
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                Log.i("Script", "onZoom()");
                return false;
            }
        });

        return view;
    }
    public void permission(){
        if (Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                String[] permission= {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION };
                requestPermissions(permission, REQUEST_LOCATION);

            }else{
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0, 0, this);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // Se a solicitação de permissão foi cancelada o array vem vazio.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permissão cedida, recria a activity para carregar o mapa, só será executado uma vez
                    getActivity().recreate();

                }

            }
        }
    }

    public void onResume() {
        super.onResume();

    }


    public void onPause(){
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if (locationManager != null){
            locationManager.removeUpdates(this);
        }
    }


}