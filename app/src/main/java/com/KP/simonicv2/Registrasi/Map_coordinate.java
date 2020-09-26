package com.KP.simonicv2.Registrasi;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.KP.simonicv2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class Map_coordinate extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener,
        MapboxMap.OnMapClickListener {
    private PermissionsManager permissionsManager;
    private MapView mapView;
    private MapboxMap map;
    TextView txtlat,txtlng,geocode;
    Button btnlatlng;
    String nama,nik,alamat,jk,id,tglmulai,tglselesai,riwayat,provinsi,kota,kecamatan,kelurahan,uuid;
    private LocationEngine locationEngine;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final String TAG = "detail rumah sakit";
    private Point destinationPosition;
    private Marker destinationMarker;
    private MarkerView markerView;
    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private static final String TURF_CALCULATION_FILL_LAYER_ID = "TURF_CALCULATION_FILL_LAYER_ID";
    private static final String CIRCLE_CENTER_ICON_ID = "CIRCLE_CENTER_ICON_ID";
    private static final String CIRCLE_CENTER_LAYER_ID = "CIRCLE_CENTER_LAYER_ID";
    private static final String CIRCLE_CENTER_SOURCE_ID = "CIRCLE_CENTER_SOURCE_ID";
    private static final String TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID
            = "TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID";
    private NavigationMapRoute navigationMapRoute;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    private LocationChangeListeningActivityLocationCallback callback =
            new LocationChangeListeningActivityLocationCallback(Map_coordinate.this);
    DatabaseReference reff,zona;
    private LocationComponent locationComponent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(Map_coordinate.this,getString(R.string.mapbox_access_token));
        setContentView(R.layout.map_coordinate);
        mapView = findViewById(R.id.mapcoordinate);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(Map_coordinate.this);
        txtlat = (TextView) findViewById(R.id.txtlat);
        txtlng = (TextView) findViewById(R.id.txtlng);
        geocode = (TextView) findViewById(R.id.geocode_result);
        btnlatlng = (Button) findViewById(R.id.btn_latlng);
        Intent intent2 = this.getIntent();
        Bundle a = intent2.getExtras();
        if(a != null){
            nama = a.getString("nama");
            nik = a.getString("nik");
            alamat = a.getString("alamat");
            jk = a.getString("jk");
            id = a.getString("id");
            tglmulai = a.getString("tglmulai");
            tglselesai = a.getString("tglselesai");
            riwayat = a.getString("riwayat");
            provinsi = a.getString("provinsi");
            kota = a.getString("kota");
            kecamatan = a.getString("kecamatan");
            kelurahan = a.getString("kelurahan");
            uuid = a.getString("uuid");
        }
        /*
        btnlatlng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng target = map.getCameraPosition().target;

// Fill the coordinate EditTexts with the target's coordinates
                setCoordinateEditTexts(target);

// Make a geocoding search with the target's coordinates
                makeGeocodeSearch(target);
            }
        });

         */
        Log.d(TAG, "onCreate: started.");
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        map = mapboxMap;
        MarkerViewManager markerViewManager = new MarkerViewManager(mapView, mapboxMap);
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")

                        .withImage(symbolIconId, BitmapFactory.decodeResource(
                                Map_coordinate.this.getResources(), R.drawable.mapbox_marker_icon_default)),
                //mapboxMap.setStyle(Style.MAPBOX_STREETS,
                        new Style.OnStyleLoaded() {
                            @Override public void onStyleLoaded(@NonNull Style style) {
                                enableLocationComponent(style);
                                initSearchFab();
                                mapboxMap.addOnMapClickListener(Map_coordinate.this);
                                // Use an XML layout to create a View object
                                /*View customView = LayoutInflater.from(Map_coordinate.this).inflate(R.layout.marker_view_bubble, null);
                                customView.setLayoutParams(new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                                TextView titleTextView = customView.findViewById(R.id.marker_window_title);
                                titleTextView.setText("ini judul");
                                TextView snippetTextView = customView.findViewById(R.id.marker_window_snippet);
                                snippetTextView.setText("ini isinya");
// Use the View to create a MarkerView which will eventually be given to
// the plugin's MarkerViewManager class
                                markerView = new MarkerView(new LatLng(-6.919696, 107.567286), customView);
                                markerViewManager.addMarker(markerView);*/

// Create an empty GeoJSON source using the empty feature collection
                                setUpSource(style);

// Set up a new symbol layer for displaying the searched location's feature coordinates
                                setupLayer(style);

                            }
                        });
    }
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        //MarkerViewManager markerViewManager = new MarkerViewManager(mapView, map);
        map.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if(destinationMarker != null){
                    map.removeMarker(destinationMarker);
                }
                /*
                else if (style != null){
                    style.removeLayer("SYMBOL_LAYER_ID");
                    style.removeImage(symbolIconId);
                    setupLayer(style);
                }

                 */
            }
        });
        destinationMarker = map.addMarker(new MarkerOptions().position(point));
        destinationPosition = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        LatLng target = new LatLng(point.getLatitude(),point.getLongitude());
        setCoordinateEditTexts(target);
        return true;
    }
    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }
    private void makeGeocodeSearch(final LatLng latLng) {
        try {
// Build a Mapbox geocoding request
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.mapbox_access_token))
                    .query(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                    .mode(GeocodingCriteria.MODE_PLACES)
                    .build();
            client.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call,
                                       Response<GeocodingResponse> response) {
                    if (response.body() != null) {
                        List<CarmenFeature> results = response.body().features();
                        if (results.size() > 0) {

// Get the first Feature from the successful geocoding response
                            animateCameraToNewPosition(latLng);
                        } else {
                            Toast.makeText(Map_coordinate.this, "no result",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                    Timber.e("Geocoding Failure: " + throwable.getMessage());
                }
            });
        } catch (ServicesException servicesException) {
            Timber.e("Error geocoding: " + servicesException.toString());
            servicesException.printStackTrace();
        }
    }
    private void animateCameraToNewPosition(LatLng latLng) {
        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(13)
                        .build()), 1500);
    }
    private void setCoordinateEditTexts(LatLng latLng) {
        txtlat.setText(String.valueOf(latLng.getLatitude()));
        txtlng.setText(String.valueOf(latLng.getLongitude()));
        btnlatlng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LatLng target = map.getCameraPosition().target;

// Fill the coordinate EditTexts with the target's coordinates
                //setCoordinateEditTexts(latLng);

// Make a geocoding search with the target's coordinates
                //makeGeocodeSearch(latLng);
                double lat = latLng.getLatitude();
                double lng = latLng.getLongitude();
                Intent intent = new Intent(Map_coordinate.this, Registrasi.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng", lng);
                intent.putExtra("nama", nama);
                intent.putExtra("nik",nik);
                intent.putExtra("alamat",alamat);
                intent.putExtra("jk",jk);
                intent.putExtra("id",id);
                intent.putExtra("tglmulai",tglmulai);
                intent.putExtra("tglselesai",tglselesai);
                intent.putExtra("riwayat",riwayat);
                intent.putExtra("provinsi",provinsi);
                intent.putExtra("kota",kota);
                intent.putExtra("kecamatan",kecamatan);
                intent.putExtra("kelurahan",kelurahan);
                intent.putExtra("uuid",uuid);
                //nama,nik,alamat,jk,id,tglmulai,tglselesai,riwayat,provinsi,kota,kecamatan,kelurahan,uuid

                startActivity(intent);
            }
        });
    }
    private void initSearchFab() {
        findViewById(R.id.fab_location_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(Map_coordinate.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);

            }
        });

    }
//test
    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }
    private void setupLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[] {0f, -8f})
        ));
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



    private static class LocationChangeListeningActivityLocationCallback
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
// Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

// Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
// Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (map != null) {
                Style style = map.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }
                    if(destinationMarker != null){
                        map.removeMarker(destinationMarker);
                    }

// Move map camera to the selected
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
            }
        }
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
