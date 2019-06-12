package mobileApp.project.CoffeeYo;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;


public class map_Search extends AppCompatActivity implements GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,OnMapReadyCallback, LocationListener, PlacesListener {
    private GoogleMap google = null;
    LatLng now = null;
    List<Marker> prevMarkers;
    private DatabaseReference mPostReference;
    private ImageButton button;
    private Button curbutton;
    private EditText edittext;
    private Geocoder geocoder;
    double latitude;
    double longitude;
    double curlatitude;
    double curlongitude;
    LatLng currentPosition = new LatLng(37.56, 126.97);
    String cafename;
    ArrayList<String[]> list = new ArrayList<>();
    HashMap<String, String> infowindow = new HashMap<String, String>();
    String uid = "";
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
    public void onPlacesFailure(PlacesException e) {
    }
    @Override
    public void onPlacesStart() {
    }
    @Override
    public void onPlacesFinished() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_search);
        mPostReference = FirebaseDatabase.getInstance().getReference();
        getFirebaseDatabase();
        edittext = (EditText)findViewById(R.id.editPlace);
        button = (ImageButton) findViewById(R.id.button);
        curbutton = (Button)findViewById(R.id.curbutton);
        prevMarkers = new ArrayList<>(); //마커 목록들

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        final LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(map_Search.this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        }
        else{
            Toast.makeText(map_Search.this, "LocationManager is ready!", Toast.LENGTH_SHORT).show();
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    100,
                    0,
                    networkLocationListener);
        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        google = map;

        final LatLng SEOUL = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        //map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(16));
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(map_Search.this, new String[]
                    {android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

        }
        if ( Build.VERSION.SDK_INT < 23 ||
                ContextCompat.checkSelfPermission( getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED){
            google.setMyLocationEnabled(true);
            //현재위치 표현 옵션
            google.setIndoorEnabled(true);
            google.setBuildingsEnabled(true);
            //줌 버튼 표시 여부
            google.getUiSettings().setZoomControlsEnabled(true);
            //showPlaceInformation(SEOUL);
            //prepareMap();
            //drawMap();
            button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String str = edittext.getText().toString();

                            List<Address> addr = null;
                            geocoder = new Geocoder(map_Search.this, Locale.getDefault());
                            try {
                                // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                                addr = geocoder.getFromLocationName(
                                        str, // 주소
                                        1); // 최대 검색 결과 개수
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (addr != null) {
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
                                google.clear();
                                CircleOptions circle1KM = new CircleOptions() //원점
                                        .center(point)
                                        .radius(1000)      //반지름 단위 : m
                                        .strokeWidth(1)  //선너비 0f : 선없음
                                        .fillColor(0x220000FF)
                                        .visible(true);
                                google.addCircle(circle1KM);
                                // 마커 추가
                                //google.addMarker(mOptions2);
                                // 해당 좌표로 화면 줌
                                google.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
                                Log.d("here circle", "location : "+ point.latitude + " , " + point.longitude);

                                //showPlaceInformation(point);
                                Location loc = new Location("target");
                                loc.setLongitude(log);
                                loc.setLatitude(lat);
                                int len = list.size();
                                MarkerOptions marker[] = new MarkerOptions[len];
                                Log.d("개수", Integer.toString(len));
                                int i =0;
                                for(String item[] : list){
                                    Location loc1 = new Location(item[0]);
                                    loc1.setLongitude(Double.parseDouble(item[1]));
                                    loc1.setLatitude(Double.parseDouble(item[2]));
                                    Log.d("targetloc", "location : "+   loc1.getLatitude()+ " , " + loc1.getLongitude()  );
                                    double distance = loc1.distanceTo(loc);
                                    Log.d("targetloc", Double.toString(distance));
                                    if(distance < 1000){
                                        Log.d("마커생서어엉", "마커생성어어엉");
                                        marker[i] = new MarkerOptions();
                                        LatLng ln = new LatLng(Double.parseDouble(item[2]), Double.parseDouble(item[1]));
                                        Log.d("targetloc", "location : "+ item[0] + " + "+  item[1]+ " + " + item[2]  );
                                        marker[i].position(ln);
                                        marker[i].title(item[0]);
                                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.mappin);
                                        Bitmap b=bitmapdraw.getBitmap();
                                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false);
                                        marker[i].icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                                        infowindow.put(marker[i].getTitle(), marker[i].getTitle() );
                                        google.addMarker(marker[i]);
                                        //infowindow.put(Marker[i].getTitle(),"cafe_id 넣어야함. 나중에 접근할때 infolistener에서 if()");
                                        //infowindow.containsKey("markertitle ") 있는지 if 문으로 확인하고
                                        //있으면 cafeid_fragment 로 이동.
                                        //없으면 toast message 보내면될듯
                                    }
                                    i++;
                                }

                            } else {
                                Toast.makeText(map_Search.this, "검색할 키워드를 입력하세요", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });

            curbutton.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //google.clear();
                            curlatitude = google.getCameraPosition().target.latitude;
                            curlongitude = google.getCameraPosition().target.longitude;
                            currentPosition = new LatLng(curlatitude, curlongitude);
                            //CircleOptions circle1KM = new CircleOptions().center(currentPosition) //원점
                            //        .radius(1000)      //반지름 단위 : m
                            //        .strokeWidth(0f)  //선너비 0f : 선없음
                            //        .fillColor(Color.parseColor("#880000ff"));
                            //google.addCircle(circle1KM);
                            //Location loc = new Location("target");
                            //loc.setLongitude(curlongitude);
                            //loc.setLatitude(curlatitude);
                            /*
                            int len = list.size();
                            MarkerOptions marker[] = new MarkerOptions[len];

                            int i =0;
                            for(String item[] : list) {
                                Location loc1 = new Location(item[0]);
                                loc1.setLongitude(Double.parseDouble(item[1]));
                                loc1.setLatitude(Double.parseDouble(item[2]));
                                Log.d("targetloc", "location : " + loc1.getLatitude() + " , " + loc1.getLongitude());
                                double distance = loc1.distanceTo(loc);
                                Log.d("targetloc", Double.toString(distance));
                                if (distance < 1000) {
                                    Log.d("마커생서어엉", "마커생성어어엉");
                                    marker[i] = new MarkerOptions();
                                    LatLng ln = new LatLng(Double.parseDouble(item[2]), Double.parseDouble(item[1]));
                                    marker[i].position(ln);
                                    marker[i].title(item[0]);
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.mappin);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false);
                                    marker[i].icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                                    infowindow.put(marker[i].getTitle(), marker[i].getTitle());
                                    google.addMarker(marker[i]);
                                    //infowindow.put(Marker[i].getTitle(),"cafe_id 넣어야함. 나중에 접근할때 infolistener에서 if()");
                                    //infowindow.containsKey("markertitle ") 있는지 if 문으로 확인하고

                                    //있으면 cafeid_fragment 로 이동.
                                    //없으면 toast message 보내면될듯
                                }
                                i++;
                            }*/
                            showPlaceInformation(currentPosition);
                        }
                    });

                }
            });

            google.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override//info 눌렀을때 무슨행동으로 넘어갈지.
                public void onInfoWindowClick(Marker marker) {
                    if(infowindow.containsKey(marker.getTitle())){
                        Intent intent = new Intent(map_Search.this, UserActivity.class).putExtra("cafe_name",marker.getTitle()).putExtra("class","map").putExtra("uid",uid);
                        Log.d("A->B 액티비티먼저전환", "onInfoWindowClick: cafename : " + marker.getTitle());
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(map_Search.this, "아직 커피요에 등록되지 않은 카페입니다. \n 사장님께 커피요 등록에 관해 문의 바랍니다.\n", Toast.LENGTH_LONG).show();

                    }
                }
            });

        }}


    public void showPlaceInformation(LatLng location) {
        //google.clear();

        latitude = location.latitude;
        longitude = location.longitude;
        if (latitude != 0 && longitude != 0) {
            if (prevMarkers != null) {
                prevMarkers.clear();
            }
            new NRPlaces.Builder()
                    .listener(this)
                    .key("AIzaSyBDLQiG_YkuMT7ySUSG0MjQBG8tLdd3WT4")
                    .latlng(latitude, longitude)
                    .radius(1000)
                    .type(PlaceType.CAFE)
                    .language("ko", "KR")
                    .build()
                    .execute();
        }
    }
    @Override
    public void onPlacesSuccess(final List<Place> places) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (noman.googleplaces.Place place : places) {
                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet(place.getVicinity());
                    Marker item = google.addMarker(markerOptions);
                    prevMarkers.add(item);
                }
                //중복 마커 제거
                HashSet<Marker> hashSet = new HashSet<Marker>();
                hashSet.addAll(prevMarkers);
                prevMarkers.clear();
                prevMarkers.addAll(hashSet);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case 1:
                if((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    onMapReady(google);
                    // prepareMap();
                    // drawMap();
                }
                break;
            default:
                break;
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
    public void getFirebaseDatabase(){
        final ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    CafeInfo get = dataSnapshot.child("/"+key+"/cafe_info").getValue(CafeInfo.class);
                    list.add(new String[]{get.cafe_name, get.cafe_longitude, get.cafe_latitude});
                    Log.d("겟파베", get.cafe_name +" + "+ get.cafe_longitude+" + "+ get.cafe_latitude);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        mPostReference.child("cafe_list").addValueEventListener(postListener);
    }


}