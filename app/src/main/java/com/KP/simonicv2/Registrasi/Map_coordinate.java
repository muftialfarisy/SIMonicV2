package com.KP.simonicv2.Registrasi;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.KP.simonicv2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Looper.getMainLooper;

public class Map_coordinate extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {
    private PermissionsManager permissionsManager;
    private MapView mapView;
    private MapboxMap map;
    private Button startButton;
    TextView nama,alamat;
    private LocationEngine locationEngine;
    private Point originPosition;
    private Point destinationPosition;
    private SymbolManager symbolManager;
    private List<Symbol> symbols = new ArrayList<>();
    private static final String TAG = "detail rumah sakit";
    private static final String SA = "rs";
    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private static final String TURF_CALCULATION_FILL_LAYER_ID = "TURF_CALCULATION_FILL_LAYER_ID";
    private static final String CIRCLE_CENTER_ICON_ID = "CIRCLE_CENTER_ICON_ID";
    private static final String CIRCLE_CENTER_LAYER_ID = "CIRCLE_CENTER_LAYER_ID";
    private static final String CIRCLE_CENTER_SOURCE_ID = "CIRCLE_CENTER_SOURCE_ID";
    private static final String TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID
            = "TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID";
    private NavigationMapRoute navigationMapRoute;
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    private LocationChangeListeningActivityLocationCallback callback =
            new LocationChangeListeningActivityLocationCallback(Map_coordinate.this);
    DatabaseReference reff,zona;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        setContentView(R.layout.map_coordinate);
        mapView = findViewById(R.id.mapcoordinate);

        mapView.getMapAsync(Map_coordinate.this);
        Log.d(TAG, "onCreate: started.");
    }
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        map = mapboxMap;
        reff = FirebaseDatabase.getInstance().getReference().child("Data ODP").child("alfa").child("coordinate");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS,
                        new Style.OnStyleLoaded() {
                            @Override public void onStyleLoaded(@NonNull Style style) {
                                enableLocationComponent(style);

                                final LatLng mapTargetLatLng = mapboxMap.getCameraPosition().target;
                                Double lat = (Double) dataSnapshot.child("Lat").getValue();
                                Double lng = (Double) dataSnapshot.child("Lng").getValue();
                                SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);
                                symbolManager.create(new SymbolOptions()
                                        .withLatLng(new LatLng(lat, lng))
                                        .withIconImage(RED_PIN_ICON_ID)
                                        .withTextField("lokasi")
                                        .withIconSize(2.0f));
                                destinationPosition = Point.fromLngLat(lng,lat);
                                originPosition = Point.fromLngLat(mapTargetLatLng.getLongitude(),mapTargetLatLng.getLatitude());
                                getRoute(originPosition,destinationPosition);

                                startButton.setEnabled(true);
                                startButton.setBackgroundResource(R.color.mapbox_blue);
                                zona = FirebaseDatabase.getInstance().getReference().child("Zona");
                                zona.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Double lat2 = (Double) dataSnapshot.child("Lat").getValue();
                                        Double lng2 = (Double) dataSnapshot.child("Lng").getValue();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getRoute(Point origin, Point destination){
        NavigationRoute.builder(Map_coordinate.this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<DirectionsResponse> call, @NotNull Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Log.e(SA, "tidak ada rute,tolong check kembali wisata tujuan");
                            return;
                        } else if (response.body().routes().size() == 0) {
                            Log.e(SA, "tidak ditemukan rute");
                            return;
                        }

                        DirectionsRoute currentRoute = response.body().routes().get(0);
                        navigationMapRoute = new NavigationMapRoute(null, mapView, map);
                        navigationMapRoute.addRoute(currentRoute);
                        getNavigation(currentRoute);


                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG, "Error: "+t.getMessage());
                    }
                });
    }

    private void getNavigation(DirectionsRoute currentRoute){
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(false)
                        .build();
                NavigationLauncher.startNavigation(Map_coordinate.this, options);
            }
        });
    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(Map_coordinate.this)) {
            LocationComponent locationComponent = map.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(Map_coordinate.this, loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);
            initLocationEngine();

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(Map_coordinate.this);
        }
    }
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(Map_coordinate.this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();
        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);

    }
    private class LocationChangeListeningActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<Map_coordinate> activityWeakReference;

        LocationChangeListeningActivityLocationCallback(Map_coordinate activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }



        @Override
        public void onSuccess(LocationEngineResult result) {
            Map_coordinate activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();
                if (location == null) {
                    return;
                }
                if (activity.map != null && result.getLastLocation() != null) {
                    activity.map.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception) {

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //Toast.makeText(this, "", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            map.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);

                }
            });
            //finish();
            //overridePendingTransition(0, 0);
            //startActivity(getIntent());
            //overridePendingTransition(0, 0);
        }
        //else {
        //Toast.makeText(this, "", Toast.LENGTH_LONG).show();
        //finish();
        //}
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(locationEngine != null){
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
