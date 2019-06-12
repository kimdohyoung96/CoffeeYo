package mobileApp.project.CoffeeYo;

import android.app.FragmentManager;
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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ReserveFragment.OnFragmentInteractionListener,
        User_Finished_Order.OnFragmentInteractionListener, NameSearch.OnFragmentInteractionListener, User_Order.OnFragmentInteractionListener, LoadingCFragment.OnFragmentInteractionListener {
    private Fragment User_Finished_Order;
    private Fragment NameSearch;
    private Fragment ReserveFragment;
    private Fragment User_Order;
    ArrayList<String[]> list = new ArrayList<>();
    private DatabaseReference mPostReference;
    String mymoney = "";
    String myemail = "";
    String myname = "";
    String mycafe_name = "";
    ListView listView;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;

    String uid;
    long newCafeID = 1;
    long currentCafeID;
    int flag;
    String cafename= "";
    DrawerLayout drawer;
    FirebaseAuth fb = FirebaseAuth.getInstance();
    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mgoogleApiClient;

    String client;
    String orderid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Mode");
        Intent intent = getIntent();
        String classname = intent.getStringExtra("class");
        data = new ArrayList<String>();
        listView = (ListView)findViewById(R.id.orderlist);
        //uid = intent.getStringExtra("uid");
        classname = intent.getStringExtra("class");
        NameSearch = new NameSearch();
        User_Finished_Order = new User_Finished_Order();
        Bundle bundle1 = new Bundle(1); // 파라미터는 전달할 데이터 개수
        bundle1.putString("uid", uid);
        User_Finished_Order.setArguments(bundle1);
        ReserveFragment = new ReserveFragment();
        User_Order = new User_Order();
        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수

        uid = intent.getStringExtra("uid");

        mPostReference = FirebaseDatabase.getInstance().getReference();
        if(classname.equals("map")){
            cafename = intent.getStringExtra("cafe_name");
            Fragment User_Order = new User_Order();
            Bundle bundle2 = new Bundle(1); // 파라미터는 전달할 데이터 개수
            bundle2.putString("cafe_name", cafename);
            User_Order.setArguments(bundle2);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, User_Order);
            transaction.addToBackStack(null);
            transaction.commit();
        }else{
            cafename = intent.getStringExtra("cafe_name");

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, ReserveFragment);
            bundle.putString("uid", uid);
            ReserveFragment.setArguments(bundle);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        createNotification();

        map_Search a = new map_Search();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Snackbar snackbar = Snackbar.make(view, "커피요를 쉽게 이용하는 방법\n1.커피요머니를 충전한다.\n2.이름 혹은 지도로 카페를 검색한다\n-지도검색시 화면을 옮기고 현재 보이는 화면에서 카페를 찾고 싶다면 왼쪽 상단에 커피 아이콘을 클릭한다.\n3.카페 혼잡도 여부와 커피 매진여부를 확인 후 커피를 주문한다.\n4.커피를 즐긴다.", Snackbar.LENGTH_INDEFINITE);
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
                textView.setMaxLines(10);  // show multiple line
                snackbar.setActionTextColor(Color.RED)
                        .setAction("확인", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(UserActivity.this, "Let's Get Some Coffee", Toast.LENGTH_SHORT);
                                snackbar.dismiss();
                            }
                        }).show();

            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();




        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //NavigationView navigationViewRight = (NavigationView) findViewById(R.id.nav_view_right);
        // navigationViewRight.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        getFirebaseDatabase();
    }

    public void createNotification() {
        Intent intentNotif = new Intent(this, MyService.class);
        intentNotif.putExtra("uid", uid);
        startService(intentNotif);
    }

    public long getNewCafeID(){
        return newCafeID;
    }
    public String getUid(){
        return uid;
    }
    public int getFlag(){
        return flag;
    }
    public long getCurrentCafeID(){
        return currentCafeID;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    ///
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.logout :
                signOut();

                finish();
                return true ;

            default :
                return super.onOptionsItemSelected(null);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_reserve) {
            // Handle the camera action
            Toast.makeText(UserActivity.this, "이름검색을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            transaction.replace(R.id.content_main, NameSearch);
            transaction.commit();
        } else if (id == R.id.nav_menu) {
            Toast.makeText(UserActivity.this, "주문 내역를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            transaction.replace(R.id.content_main, User_Finished_Order);
            transaction.commit();
        } else if (id == R.id.nav_searchmap) {
            Toast.makeText(UserActivity.this, "지도검색을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserActivity.this, map_Search.class).putExtra("uid",uid);
            startActivity(intent);
        } else if (id == R.id.nav_star) {
            Toast.makeText(UserActivity.this, "충전을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            postFirebaseDatabase(true);
            Intent intent = new Intent(UserActivity.this, NiceMainActivity.class);

            startActivity(intent);
        }  else if (id == R.id.nav_cvt2mgr) {
            Toast.makeText(UserActivity.this, "매니저 모드를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserActivity.this, ManagerActivity.class).putExtra("uid",uid).putExtra("cafe_name",mycafe_name).putExtra("class","user");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void postFirebaseDatabase(boolean add){ //firebase database로 데이터를 보내는 함수.
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        String money = String.valueOf(Integer.parseInt(mymoney) + 10000);
        if(add){
            FirebasePost post = new FirebasePost(myemail, myname, uid, money, mycafe_name);
            postValues = post.toMap();
        }
        childUpdates.put("/user_list/" + uid, postValues); //여기서 추가 - 이름을 뭘로할지 /memo_list/title 의 이름으로 만들어짐.
        mPostReference.updateChildren(childUpdates);
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
    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //만약에 데이터가 추가되거나 삭제되거나 바뀌면 실행됨.
                Log.d("onDataChange", "Data is Updated");
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
                        myname = list.get(j)[1];
                        mycafe_name = list.get(j)[4];
                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        View headerView = navigationView.getHeaderView(0);
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
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("user_list").addValueEventListener(postListener); //id_list 의 서브트리부터 밑으로만 접근하겟다.
    }
}