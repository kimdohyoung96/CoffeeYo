package mobileApp.project.CoffeeYo;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class map_search extends AppCompatActivity implements GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,OnMapReadyCallback {
    private GoogleMap google = null;
    MarkerOptions markerOptions = new MarkerOptions();
    LatLng now = null;
    ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_search);
        Intent intent = getIntent();
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        final LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( map_search.this, new String[]
                    { android.Manifest.permission.ACCESS_FINE_LOCATION },0 );
        }
        else{
            Toast.makeText(map_search.this, "LocationManager is ready!", Toast.LENGTH_SHORT).show();
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    100,
                    0,
                    networkLocationListener);

        }
        button = (ImageButton)findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                google.moveCamera(CameraUpdateFactory.newLatLng(now));
                google.animateCamera(CameraUpdateFactory.zoomTo(16.0F));
                google.addMarker(markerOptions);
            }
        });
    }
    public void search(View v){
        EditText id = map_search.this.findViewById(R.id.edittxt);
        String place = id.getText().toString();
        Geocoder coder = new Geocoder(getApplicationContext());
        List<Address> addr = null;
        try{
            addr = coder.getFromLocationName(place, 1);
        }catch(IOException e){
            e.printStackTrace();
        }
        Address address = addr.get(0);
        double lat = address.getLatitude();
        double log = address.getLongitude();
        LatLng searchPlace = new LatLng(lat,log);
        google.animateCamera(CameraUpdateFactory.zoomTo(16.0F));
        MarkerOptions marker = new MarkerOptions();
        marker.position(searchPlace);
        google.addMarker(marker);
    }
    @Override
    public void onMapReady(final GoogleMap map) {
        google = map;
        LatLng SEOUL = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);

        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
        //현재위치 표현 옵션
        //google.setMyLocationEnabled(true);//권한체크때메 발생한오류
        google.setIndoorEnabled(true);
        google.setBuildingsEnabled(true);
        //줌 버튼 표시 여부
        google.getUiSettings().setZoomControlsEnabled(true);

    }


    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            Toast.makeText(this, "The user gestured on the map.",
                    Toast.LENGTH_SHORT).show();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
            Toast.makeText(this, "The user tapped something on the map.",
                    Toast.LENGTH_SHORT).show();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
            Toast.makeText(this, "The app moved the camera.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCameraMove() {
        Toast.makeText(this, "The camera is moving.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraMoveCanceled() {
        Toast.makeText(this, "Camera movement canceled.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraIdle() {
        Toast.makeText(this, "The camera has stopped moving.",
                Toast.LENGTH_SHORT).show();
    }


    final LocationListener networkLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
            Log.d("Update Network", "" + provider + " " + longitude + " " + latitude);
            now = new LatLng(latitude, longitude);
            //markerOptions.position(now);
            //markerOptions.title("현재위치");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };

}