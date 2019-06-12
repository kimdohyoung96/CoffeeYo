package mobileApp.project.CoffeeYo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
    private RadioButton r_btn1, r_btn2;
    private RadioGroup radioGroup;
    private static final String TAG = "GoogleActivity";
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;
    int check2 = -1;
    private DatabaseReference mPostReference;
    String email="",name="",uid="";
    ArrayList<String[]> list = new ArrayList<>();
    String money = "0";
    String cafe_name = "0";
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        flag = 0;
        mPostReference = FirebaseDatabase.getInstance().getReference();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        r_btn1 = (RadioButton) findViewById(R.id.r_btn1);
        r_btn2 = (RadioButton) findViewById(R.id.r_btn2);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SignInButton button = (SignInButton) findViewById(R.id.sign_in_button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (r_btn2.isChecked() == true) {
                            check2 = 1;
                            signIn();
                        } else if (r_btn1.isChecked() == true) {
                            check2 = 2;
                            signIn();
                        }
                    }
                });

            }
        });
        getFirebaseDatabase();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("FirebaseAuthwithGoogle", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                name = user.getDisplayName();
                                email = user.getEmail();
                                uid = user.getUid();
                                if (check2 == 1) {
                                    for(int j = 0 ; j < list.size(); j++){
                                        if(uid.equals(list.get(j)[2])){
                                            money = list.get(j)[3];
                                            cafe_name = list.get(j)[4];
                                            flag = 1;
                                            //  Map<String, Object> childUpdates = new HashMap<>();
                                            // Map<String, Object> postValues = null;
                                            // FirebasePost post = new FirebasePost(list.get(j).email, list.get(j).name, list.get(j).uid,list.get(j).money,list.get(j).cafe_name);
                                            // postValues = post.toMap();
                                            //childUpdates.put("/user_list/" + uid, postValues); //여기서 추가 - 이름을 뭘로할지 /memo_list/title 의 이름으로 만들어짐.
                                            //mPostReference.updateChildren(childUpdates);
                                            startActivity(new Intent(Login.this, ManagerActivity.class).putExtra("email",email).putExtra("money",money).putExtra("uid",uid).putExtra("cafe_name",cafe_name).putExtra("name",name));

                                        }
                                    }
                                    if(flag ==0) {
                                        Map<String, Object> childUpdates = new HashMap<>();
                                        Map<String, Object> postValues = null;
                                        FirebasePost post = new FirebasePost(email, name, uid, "0", "0");
                                        postValues = post.toMap();
                                        childUpdates.put("/user_list/" + uid, postValues); //여기서 추가 - 이름을 뭘로할지 /memo_list/title 의 이름으로 만들어짐.
                                        mPostReference.updateChildren(childUpdates);
                                        startActivity(new Intent(Login.this, ManagerActivity.class).putExtra("email",email).putExtra("money",money).putExtra("uid",uid).putExtra("cafe_name",cafe_name).putExtra("name",name));
                                    }
                                } else if (check2 == 2) {
                                    //push
                                    for(int j = 0 ; j < list.size(); j++){
                                        if(uid.equals(list.get(j)[2])){
                                            money = list.get(j)[3];
                                            cafe_name = list.get(j)[4];
                                            flag = 1;
                                            //  Map<String, Object> childUpdates = new HashMap<>();
                                            // Map<String, Object> postValues = null;
                                            //FirebasePost post = new FirebasePost(list.get(j).email, list.get(j).name, list.get(j).uid,list.get(j).money,list.get(j).cafe_name);
                                            //postValues = post.toMap();
                                            // childUpdates.put("/user_list/" + uid, postValues); //여기서 추가 - 이름을 뭘로할지 /memo_list/title 의 이름으로 만들어짐.
                                            // mPostReference.updateChildren(childUpdates);
                                            startActivity(new Intent(Login.this, UserActivity.class).putExtra("email",email).putExtra("money",money).putExtra("uid",uid).putExtra("cafe_name",cafe_name).putExtra("name",name).putExtra("class","Login"));
                                        }
                                    }
                                    if(flag ==0) {
                                        Map<String, Object> childUpdates = new HashMap<>();
                                        Map<String, Object> postValues = null;
                                        FirebasePost post1 = new FirebasePost(email, name, uid, "0", "0");
                                        postValues = post1.toMap();
                                        childUpdates.put("/user_list/" + uid, postValues); //여기서 추가 - 이름을 뭘로할지 /memo_list/title 의 이름으로 만들어짐.

                                        mPostReference.updateChildren(childUpdates);
                                        startActivity(new Intent(Login.this, UserActivity.class).putExtra("email",email).putExtra("money",money).putExtra("uid",uid).putExtra("cafe_name",cafe_name).putExtra("name",name).putExtra("class","Login"));
                                    }
                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
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

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("user_list").addValueEventListener(postListener); //id_list 의 서브트리부터 밑으로만 접근하겟다.
    }

}