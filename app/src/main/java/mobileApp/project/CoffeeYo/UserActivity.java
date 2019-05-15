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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ReserveFragment.OnFragmentInteractionListener, NameSearch.OnFragmentInteractionListener {
    private Fragment reserveFragment;
    private Fragment NameSearch;

    DrawerLayout drawer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Mode");
        Intent intent = getIntent();
        reserveFragment = new ReserveFragment();
        NameSearch = new NameSearch();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, reserveFragment);
        transaction.add(R.id.content_main, NameSearch);
        transaction.addToBackStack(null);
        transaction.commit();


        map_Search a = new map_Search();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.logout :
                finish();
                return true ;

            default :
                return super.onOptionsItemSelected(null);
        }
        /*if (id == R.id.action_right_menu) {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                //drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
            return true;
        }*/


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
            transaction.replace(R.id.content_main, reserveFragment);
            transaction.commit();
        } else if (id == R.id.nav_searchmap) {
            Toast.makeText(UserActivity.this, "지도검색을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserActivity.this, map_Search.class);
            startActivity(intent);
        } else if (id == R.id.nav_star) {
            Toast.makeText(UserActivity.this, "상품평을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_check) {
            Toast.makeText(UserActivity.this, "조회를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_setting) {
            Toast.makeText(UserActivity.this, "설정을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}