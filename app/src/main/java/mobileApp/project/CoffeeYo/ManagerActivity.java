package mobileApp.project.CoffeeYo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mPostReference = FirebaseDatabase.getInstance().getReference();

        flag = 0;
        // check if user already has cafe
        mPostReference.child("user_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(uid).exists()){
                    FirebasePost get = dataSnapshot.child(uid).getValue(FirebasePost.class);
                    String info = get.cafe_name;
                    if(!(info.equals("0"))){
                        currentCafeName = info;
                        flag = 1;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });

        // count number of existing cafe
        /*mPostReference.child("cafe_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newCafeID = dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Snackbar snackbar = Snackbar.make(view, "커피요를 쉽게 이용하는 방법\n1.커피요머니를 충전한다.\n2.이름 혹은 지도로 카페를 검색한다\n-지도검색시 화면을 옮기고 현재 보이는 화면에서 카페를 찾고 싶다면 왼쪽 상단에 커피 아이콘을 클릭한다.\n3.카페 혼잡도 여부와 커피 매진여부를 확인 후 커피를 주문한다.\n4.커피를 즐긴다.", Snackbar.LENGTH_INDEFINITE);
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

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, loadingMFragment);
        //transaction.addToBackStack(null);
        transaction.commit();
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
            Toast.makeText(ManagerActivity.this, "카페 등록/변경", Toast.LENGTH_SHORT).show();
            transaction.replace(R.id.content_main, registerFragment);
            transaction.commit();
        }
        else if(id == R.id.nav_cafemenu){
            transaction.replace(R.id.content_main, cafemenuFragment);
            transaction.commit();
        }
        else if(id == R.id.nav_reserved){
            transaction.replace(R.id.content_main, orderFragment);
            transaction.commit();
        }
        else if(id == R.id.nav_congestion){
            transaction.replace(R.id.content_main, congestionFragment);
            transaction.commit();
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