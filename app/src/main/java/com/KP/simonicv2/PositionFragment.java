package com.KP.simonicv2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Looper.getMainLooper;

public class PositionFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {
    private PermissionsManager permissionsManager;
    private MapView mapView;
    private MapboxMap map;
    private Button startButton;
    TextView nama,alamat;
    private LocationEngine locationEngine;
    private Point originPosition;
    private Point destinationPosition;
    private static final String TAG = "Detail";
    private static final String SA = "ODP";
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
            new LocationChangeListeningActivityLocationCallback(this);
    DatabaseReference reff,zona;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getContext(),getString(R.string.mapbox_access_token));
        View view = inflater.inflate (R.layout.fragment_position,container,false);

        mapView = view.findViewById(R.id.mapview2);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        Log.d(TAG, "onCreate: started.");

        startButton = view.findViewById(R.id.startButton);
        return view;

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
                                        mapboxMap.addPolygon(generatePerimeter(
                                                new LatLng(lat2, lng2),
                                                1,
                                                64,
                                                lat,
                                                lng));
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

    @SuppressLint("ResourceAsColor")
    private PolygonOptions generatePerimeter(LatLng centerCoordinates, double radiusInKilometers, int numberOfSides, double lat, double lng) {
        List<LatLng> positions = new ArrayList<>();
        TextView background;
        double distanceX = radiusInKilometers / (111.319 * Math.cos(centerCoordinates.getLatitude() * Math.PI / 180));
        double distanceY = radiusInKilometers / 110.574;

        double slice = (2 * Math.PI) / numberOfSides;
        double theta,theta2;
        double x,y,x1,y1,a1,b1,a2,b2;
        LatLng position;
        for (int i = 0; i < numberOfSides; ++i) {
            theta = i * slice;
            theta2 = 64 * slice;
            x = distanceX * Math.cos(theta);
            y = distanceY * Math.sin(theta);
            x1 = distanceX * Math.cos(theta2);
            y1 = distanceY * Math.sin(theta2);
            /*
            if(((centerCoordinates.getLatitude() + y1) >= lat) && ((centerCoordinates.getLongitude() + x1) >= lng) ||
                    ((centerCoordinates.getLatitude() - y1) <= lat) && ((centerCoordinates.getLongitude() - x1) <= lng) ||
                    ((centerCoordinates.getLatitude() + y1) >= lat) && ((centerCoordinates.getLongitude() - x1) <= lng) ||
                    ((centerCoordinates.getLatitude() - y1) <= lat) && ((centerCoordinates.getLongitude() + x1) >= lng) ||
                    ((centerCoordinates.getLatitude() + y1) >= lat) && ((centerCoordinates.getLongitude() - x1) <= lng) ||
                    ((centerCoordinates.getLongitude() + x1) >= lng) && ((centerCoordinates.getLatitude() - y1) <= lat) ||
                    ((centerCoordinates.getLongitude() + x1) >= lng) && ((centerCoordinates.getLongitude() - x1) <= lng) ||
                    ((centerCoordinates.getLatitude() + y1) >= lat) && ((centerCoordinates.getLatitude() - y1) <= lat)){

             */
            a1 = centerCoordinates.getLatitude() + y1;
            a2 = centerCoordinates.getLatitude() - y1;
            b1 = centerCoordinates.getLongitude() + x1;
            b2 = centerCoordinates.getLongitude() - x1;
            if((a1 < lat) && (lat > a2) || ((b1 < lng) && (lng > b2)))
            {
                background = (TextView) getView().findViewById(R.id.zona);
                background.setBackgroundColor(getResources().getColor(R.color.green));
                background.setText("Di dalam zona");
            } else {
                background = (TextView) getView().findViewById(R.id.zona);
                background.setBackgroundColor(getResources().getColor(R.color.red));
                background.setText("Di luar Zona");
            }
            position = new LatLng(centerCoordinates.getLatitude() + y,
                    centerCoordinates.getLongitude() + x);
            positions.add(position);
        }
        return new PolygonOptions()
                .addAll(positions)
                .fillColor(Color.RED)
                .alpha(0.4f);
    }


    private void getRoute(Point origin, Point destination){
        NavigationRoute.builder(getContext())
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
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
                NavigationLauncher.startNavigation(getActivity(), options);
            }
        });
    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
            LocationComponent locationComponent = map.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getContext(), loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);
            initLocationEngine();

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(getContext());

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();
        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);

    }
    private class LocationChangeListeningActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<PositionFragment> activityWeakReference;

        LocationChangeListeningActivityLocationCallback(PositionFragment activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }



        @Override
        public void onSuccess(LocationEngineResult result) {
            PositionFragment activity = activityWeakReference.get();

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