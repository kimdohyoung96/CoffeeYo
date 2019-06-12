package mobileApp.project.CoffeeYo;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mobileApp.project.CoffeeYo.*;
//import iamport.kr.iamportniceandroid.R;
import mobileApp.project.CoffeeYo.NiceWebViewClient;

public class NiceMainActivity extends AppCompatActivity {

    private String TAG = "iamport";
    private WebView mainWebView;
    private NiceWebViewClient niceClient;
    private final String APP_SCHEME = "practice://";
    int suc = 0;
    String uid = "";
    String myuid = "";
    String myname ="";
    String mycafe_name = "";
    String mymoney ="";
    String myemail ="";
    int flag = 1;
    private FirebaseAuth mAuth;
    ArrayList<String[]> list = new ArrayList<>();
    private DatabaseReference mPostReference;
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.payment);
        mPostReference = FirebaseDatabase.getInstance().getReference();

        getFirebaseDatabase();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        Uri intentData = intent.getData();
        for (int j = 0; j < list.size(); j++) {
            if (myuid.equals(list.get(j)[2])) {
                myemail = list.get(j)[0];
                myname = list.get(j)[1];
                myuid = list.get(j)[2];
                mymoney = list.get(j)[3];
                mycafe_name = list.get(j)[4];
            }
        }
        flag = intent.getIntExtra("flag",0);
        Intent intent1 = new Intent(NiceMainActivity.this, UserActivity.class);
        mainWebView = (WebView) findViewById(R.id.mainWebView);
        niceClient = new NiceWebViewClient(this, mainWebView);
        mainWebView.setWebViewClient(niceClient);
        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mainWebView, true);
        }
        //mymoney = Integer.toString(Integer.parseInt(mymoney.replaceAll("\"","")) + 10000);
        //postFirebaseDatabase(true);
        if ( intentData == null ) {
            mainWebView.loadUrl("https://paymentprac.firebaseapp.com/");

        } else {
            Log.d(TAG, intentData.toString());
            //isp 인증 후 복귀했을 때 결제 후속조치
           String url = intentData.toString();
            if ( url.startsWith(APP_SCHEME) ) {
                String redirectURL = url.substring(APP_SCHEME.length()+3); //"://"가 추가로 더 전달됨
                mainWebView.loadUrl(redirectURL);
                startActivity(intent1);
            }
            intent.putExtra("uid",myuid).putExtra("name",myname).putExtra("email",myemail).putExtra("money",mymoney).putExtra("cafe_name",mycafe_name);
            startActivity(intent1);
        }
            getFirebaseDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        /* 실시간 계좌이체 인증 후 후속처리 루틴 */
        String resVal = data.getExtras().getString("bankpay_value");
        String resCode = data.getExtras().getString("bankpay_code");

        if("000".equals(resCode)){
            niceClient.bankPayPostProcess(resCode, resVal);
            suc = 1;
        }else if("091".equals(resCode)){//계좌이체 결제를 취소한 경우
            Log.e(TAG, "계좌이체 결제를 취소하였습니다.");
        }else if("060".equals(resCode)){
            Log.e(TAG, "타임아웃");
        }else if("050".equals(resCode)){
            Log.e(TAG, "전자서명 실패");
        }else if("040".equals(resCode)){
            Log.e(TAG, "OTP/보안카드 처리 실패");
        }else if("030".equals(resCode)){
            Log.e(TAG, "인증모듈 초기화 오류");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    String url = intent.toString();
        if(url.startsWith(APP_SCHEME)){
            String redirectURL = url.substring(APP_SCHEME.length() + "://".length()) ;
            mainWebView.loadUrl(redirectURL);
        }
    }
    public void getFirebaseDatabase(){
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

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("user_list").addValueEventListener(postListener); //id_list 의 서브트리부터 밑으로만 접근하겟다.
    }


}
