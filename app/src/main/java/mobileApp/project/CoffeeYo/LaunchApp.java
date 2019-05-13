package mobileApp.project.CoffeeYo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LaunchApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_app);
        Handler Handler = new Handler();
        Handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LaunchApp.this, Login.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}


