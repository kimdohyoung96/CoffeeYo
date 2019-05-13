package mobileApp.project.CoffeeYo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

public class menu_right_side extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_right_side, menu);
        return true;
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   // 버튼누르면 어느 액티비티로 시작할건지
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_search:
                startActivity(new Intent(this, ));
                return true;

            case R.id.action_call:
                startActivity(new Intent(this, .class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */ //여기는 클래스 일단 만들어놓고 시작.
}
