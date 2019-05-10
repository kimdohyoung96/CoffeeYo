package mobileApp.project.CoffeeYo;


import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
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
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;

public class map_search extends AppCompatActivity implements GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,OnMapReadyCallback {
    private GoogleMap google = null;
    MarkerOptions markerOptions = new MarkerOptions();
    LatLng now = null;
    private boolean LocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private ImageButton button;
    private EditText edittext;
    private Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_search);
        edittext = (EditText)findViewById(R.id.editPlace);
        button = (ImageButton) findViewById(R.id.button);

        Intent intent = getIntent();
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        final LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(map_search.this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        }
        else{
            Toast.makeText(map_search.this, "LocationManager is ready!", Toast.LENGTH_SHORT).show();
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    100,
                    0,
                    networkLocationListener);

        }

    }
    /*
    public void search(View v){

        String place = edittext.getText().toString();
        Geocoder coder = new Geocoder(getApplicationContext());
        List<Address> addr = null;
        try{
            addr = coder.getFromLocationName(place, 1);
            Address address = addr.get(0);
            double lat = address.getLatitude();
            double log = address.getLongitude();
            LatLng searchPlace = new LatLng(lat,log);
            google.animateCamera(CameraUpdateFactory.zoomTo(16.0F));
            MarkerOptions marker = new MarkerOptions();
            marker.position(searchPlace);
            google.addMarker(marker);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
*/


    @Override
    public void onMapReady(final GoogleMap map) {
        google = map;
        LatLng SEOUL = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);

        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(map_search.this, new String[]
                    {android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

        }
        google.setMyLocationEnabled(true);
        //현재위치 표현 옵션
        google.setIndoorEnabled(true);
        google.setBuildingsEnabled(true);
        //줌 버튼 표시 여부
        google.getUiSettings().setZoomControlsEnabled(true);

        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str=edittext.getText().toString();
                List<Address> addr = null;
                geocoder = new Geocoder(map_search.this, Locale.getDefault());
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addr = geocoder.getFromLocationName(
                            str, // 주소
                            1); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = addr.get(0);
                double lat = address.getLatitude();
                double log = address.getLongitude();
                // 좌표(위도, 경도) 생성
                LatLng point = new LatLng(lat, log);
                // 마커 생성
                MarkerOptions mOptions2 = new MarkerOptions();
                mOptions2.title("search result");
                mOptions2.snippet(str);
                mOptions2.position(point);
                // 마커 추가
                google.addMarker(mOptions2);
                // 해당 좌표로 화면 줌
                google.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
            }
        });
        google.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override//info 눌렀을때 무슨행동으로 넘어갈지.
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:01075178860"));
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });
    }
    private void getLocationPermission(){
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationPermissionGranted = true;
        }else{
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

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