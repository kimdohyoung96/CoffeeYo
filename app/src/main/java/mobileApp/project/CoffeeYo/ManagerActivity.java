package mobileApp.project.CoffeeYo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManagerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ReserveMFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener, OrderFragment.OnFragmentInteractionListener, CongestionFragment.OnFragmentInteractionListener, LoadingMFragment.OnFragmentInteractionListener, CafeMenuFragment.OnFragmentInteractionListener {
    private Fragment reserveMFragment;
    private Fragment registerFragment;
    private Fragment orderFragment;
    private Fragment congestionFragment;
    private Fragment cafemenuFragment;
    private Fragment loadingMFragment;
    DrawerLayout drawer;

    public DatabaseReference mPostReference;
    FirebaseAuth fb = FirebaseAuth.getInstance();
    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mgoogleApiClient;
    String uid;
    String currentCafeName;
    int flag;
    String mymoney = "";
    String myemail = "";
    String cafe_name = "";
    ArrayList<String[]> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manager Mode");

        reserveMFragment = new ReserveMFragment();
        registerFragment = new RegisterFragment();
        orderFragment = new OrderFragment();
        congestionFragment = new CongestionFragment();
        cafemenuFragment = new CafeMenuFragment();
        loadingMFragment = new LoadingMFragment();

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        cafe_name = intent.getStringExtra("cafe_name");
        mPostReference = FirebaseDatabase.getInstance().getReference();

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        flag = 0;
        getFirebaseDatabaseCheckMyCafe();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Snackbar snackbar = Snackbar.make(view, "커피요를 쉽게 이용하는 방법\n1.내 카페를 등록한다.\n2.내 카페의 메뉴를 추가한다.\n3.내 카페의 혼잡도를 설정한다.\n4.주문 예약 내역의 주문 리스트를 클릭해 주문을 완료한다.\n5.완료 주문 내역 탭에서 지금까지 완료된 주문을 확인한다.", Snackbar.LENGTH_INDEFINITE);
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(10);  // show multiple line
                snackbar.setActionTextColor(Color.RED)
                        .setAction("확인", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(ManagerActivity.this, "Let's Get Some Coffee", Toast.LENGTH_SHORT);
                                snackbar.dismiss();
                            }
                        }).show();

            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, loadingMFragment);

        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void getFirebaseDatabaseCheckMyCafe(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(uid).exists()){
                    FirebasePost get = dataSnapshot.child(uid).getValue(FirebasePost.class);
                    String info = get.cafe_name;
                    if(info != null) {
                        if (!(info.equals("0"))) {
                            currentCafeName = info;
                            flag = 1;
                        }
                    }
                }
                list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) { //노드 다시 읽어서 추가
                    String key = postSnapshot.getKey();
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                    list.add(new String[]{get.email, get.name, get.uid, get.money, get.cafe_name});


                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + list.get(0)[2] + list.get(0)[3]);

                }
                for(int j = 0 ; j < list.size(); j++) {
                    if (uid.equals(list.get(j)[2])) {
                        myemail = list.get(j)[0];
                        mymoney = list.get(j)[3];
                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
                        View headerView = navigationView.getHeaderView(0); // 여기가 문제
                        TextView useremail = (TextView) headerView.findViewById(R.id.myemail);
                        TextView usermoney = (TextView) headerView.findViewById(R.id.mymoney);
                        useremail.setText(" Email : " + myemail);
                        useremail.setSelected(true);
                        usermoney.setText(" Money : " + mymoney + " Won");
                        usermoney.setSelected(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.child("user_list").addValueEventListener(postListener);
    }

    public void transactionFromLoadingToReserveM(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, reserveMFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public String getUid(){
        return uid;
    }
    public int getFlag(){
        return flag;
    }
    public String getCurrentCafeName(){
        return currentCafeName;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_right_side, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.logout:
                signOut();

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(null);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        int id = item.getItemId();

        if(id == R.id.nav_register){
            transaction.replace(R.id.content_main, registerFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(id == R.id.nav_cafemenu){
            transaction.replace(R.id.content_main, cafemenuFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(id == R.id.nav_reserved){
            transaction.replace(R.id.content_main, orderFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(id == R.id.nav_congestion){
            transaction.replace(R.id.content_main, congestionFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }  else if (id == R.id.nav_cvt2usr) {
            Toast.makeText(ManagerActivity.this, "유저 모드를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ManagerActivity.this, UserActivity.class).putExtra("uid",uid).putExtra("cafe_name",cafe_name).putExtra("class","manager");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout1);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void signOut() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mgoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

            @Override
            public void onConnected(@Nullable Bundle bundle) {

                fb.signOut();
                if (mgoogleApiClient.isConnected()) {

                    Auth.GoogleSignInApi.signOut(mgoogleApiClient).setResultCallback(new ResultCallback<Status>() {

                        @Override
                        public void onResult(@NonNull Status status) {



                            finish();
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {


                finish();
            }
        });
    }
}