package mobileApp.project.CoffeeYo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.CheckBox;




public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final CheckBox checkm = (CheckBox)findViewById(R.id.checkBox_m);
        final CheckBox checku = (CheckBox)findViewById(R.id.checkBox_u);


        Button button = (Button)findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkm.isChecked() == true){
                    Intent intent = new Intent(Login.this, ManagerActivity.class);
                    startActivity(intent);
                }else if(checku.isChecked() == true) {
                    Intent intent = new Intent(Login.this, UserActivity.class);
                    startActivity(intent);
                }






            }

        });

    }
}