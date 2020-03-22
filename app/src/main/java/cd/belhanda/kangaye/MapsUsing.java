package cd.belhanda.kangaye;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class MapsUsing extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MarkerOptions marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_using);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);

        if(!TextUtils.isEmpty(getIntent().getStringExtra("longdraw"))){
            double longitudedraw1 = Double.parseDouble(getIntent().getStringExtra("longdraw"));
            double latitudedraw1 = Double.parseDouble(getIntent().getStringExtra("latdraw"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudedraw1, longitudedraw1), 16));
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if(!TextUtils.isEmpty(getIntent().getStringExtra("position"))){
                    mMap.clear();
                    marker = new MarkerOptions().position(latLng).title("Position menacée");
                    mMap.addMarker(marker);

                    Intent intent = new Intent();
                    intent.putExtra("longitude", String.valueOf(latLng.longitude));
                    intent.putExtra("latitude", String.valueOf(latLng.latitude));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });


        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mMap.clear();

                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(position).title("Votre position"));

                if(!TextUtils.isEmpty(getIntent().getStringExtra("longdraw"))){
                    double longitudedraw1 = Double.parseDouble(getIntent().getStringExtra("longdraw"));
                    double latitudedraw1 = Double.parseDouble(getIntent().getStringExtra("latdraw"));

                    LatLng destination = new LatLng(latitudedraw1, longitudedraw1);

                    GoogleDirection.withServerKey("AIzaSyBhFzvniYsolFOFAX3qmgqC59RwPFtRL7E")
                            .from(position)
                            .to(destination)
                            .execute(new DirectionCallback() {
                                @Override
                                public void onDirectionSuccess(Direction direction) {
                                    if(direction.isOK()) {

                                        Route route = direction.getRouteList().get(0);
                                        Leg leg = route.getLegList().get(0);

                                        ArrayList<LatLng> pointList = leg.getDirectionPoint();

                                        Info distanceInfo = leg.getStepList().get(0).getDistance();
                                        Info durationInfo = leg.getStepList().get(0).getDuration();
                                        String distance = distanceInfo.getText();
                                        String duration = durationInfo.getText();

                                        ArrayList<LatLng> directionPositionList = pointList;
                                        PolylineOptions polylineOptions = DirectionConverter.createPolyline(getApplicationContext(),
                                                directionPositionList, 5, Color.RED);

                                        mMap.addMarker(new MarkerOptions().position(position).title("Destination"));
                                        mMap.addPolyline(polylineOptions);

                                        findViewById(R.id.backusing).setVisibility(View.VISIBLE);
                                        findViewById(R.id.backusing).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                finish();
                                            }
                                        });

                                        findViewById(R.id.lineardistance).setVisibility(View.VISIBLE);
                                        TextView textView = findViewById(R.id.distance);
                                        textView.setText("Distance : " + distance + " Durée " + duration);
                                    } else {
                                    }
                                }

                                @Override
                                public void onDirectionFailure(Throwable t) {
                                    // Do something
                                }
                            });

                }
            }
        });
    }
}