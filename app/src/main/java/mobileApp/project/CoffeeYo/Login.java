package mobileApp.project.CoffeeYo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class Login extends AppCompatActivity {
    private RadioButton r_btn1, r_btn2;
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        r_btn1 = (RadioButton) findViewById(R.id.r_btn1);
        r_btn2 = (RadioButton) findViewById(R.id.r_btn2);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Button button = (Button) findViewById(R.id.button2);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (r_btn2.isChecked() == true) {
                            Intent intent = new Intent(Login.this, ManagerActivity.class);
                            startActivity(intent);
                        } else if (r_btn1.isChecked() == true) {
                            Intent intent = new Intent(Login.this, UserActivity.class);
                            startActivity(intent);
                        }


                    }

                });

            }
        });
    }
}
