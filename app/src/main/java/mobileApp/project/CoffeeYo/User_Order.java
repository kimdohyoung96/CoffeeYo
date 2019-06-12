package mobileApp.project.CoffeeYo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class User_Order extends Fragment {
    private OnFragmentInteractionListener mListener;
    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;
    private String cafe_name, take, menu, count, price = "";
    private ArrayList<menuitem> menu_list;
    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private TextView TextCongestion;
    private RadioButton r_btn1, r_btn2;
    private RadioGroup radioGroup;
    private Button YesButton;
    public String mymoney = "";
    public String uid = "";

    ArrayList<String[]> list = new ArrayList<>();

    public User_Order() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    //////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user__order, container, false);
        mAuth= FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        cafe_name = getArguments().getString("cafe_name");
        menu_list = new ArrayList<menuitem>();
        recyclerView = view.findViewById(R.id.cafemenulist);
        adapter = new MenuAdapter();

        TextCongestion = (TextView)view.findViewById(R.id.textViewcon);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setItemClick(new MenuAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {

                //클릭시 실행될 함수 작성
            }
        });

        mPostReference = FirebaseDatabase.getInstance().getReference();
        getFirebaseDatabase();
        getFirebaseDatabase1();
        radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        r_btn1 = (RadioButton)view.findViewById(R.id.r_btn1);
        r_btn2 = (RadioButton)view.findViewById(R.id.r_btn2);

        YesButton = (Button) view.findViewById(R.id.Yesbutton);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (r_btn2.isChecked() == true) {
                    take = "Take-out";
                } else if (r_btn1.isChecked() == true) {
                    take = "For-here";

                }
            }
        });




        YesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment OrderCheckFragment = new OrderCheckFragment();
                Fragment LoadingCFragment = new LoadingCFragment();
                Bundle bundle = new Bundle();
                Bundle bundle1 = new Bundle();
                bundle.putString("cafe_name", cafe_name);
                bundle1.putString("cafe_name", cafe_name);
                String[] order = new String[10];
                int i = menu_list.size();
                int Num = 0;
                int Sum = 0;

                for (menuitem men : menu_list) {
                    menu = men.getMenu();
                    count = men.getCount();
                    price = men.getPrice();
                    if (Integer.parseInt(count) >= 1) {
                        Sum += Integer.parseInt(price) * Integer.parseInt(count);
                        bundle.putString(menu, count);
                        bundle1.putString(menu, count);
                        order[Num] = menu;
                        Num++;
                    }
                }
                for (int j = 0; j < list.size(); j++) {
                    if (uid.equals(list.get(j)[2])) {
                        mymoney = list.get(j)[3];
                    }
                }
                if (Integer.parseInt(mymoney) > Sum) {
                    String su = String.valueOf(Sum);
                    bundle.putStringArray("order", order);
                    bundle.putString("Num", String.valueOf(Num));
                    bundle.putString("take", take);
                    bundle.putString("Sum", su);
                    bundle1.putStringArray("order", order);
                    bundle1.putString("Num", String.valueOf(Num));
                    bundle1.putString("take", take);
                    bundle1.putString("Sum", su);

                    OrderCheckFragment.setArguments(bundle);
                    LoadingCFragment.setArguments(bundle1);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, OrderCheckFragment);

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    cafe_name = "";
                    take = "";

                }
                else{
                    Toast.makeText(getContext(), "잔액이 부족합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getFirebaseDatabase();
        return view;
    }

    //잠깐만 이거 불러오는 거랑 넣는 게 달라
    //불러오는 건 cafe 메뉴 정보인데 넣는 건 주문 내역이야
    public void getFirebaseDatabase() {

        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                menu_list.clear();

                for(DataSnapshot postListener: dataSnapshot.child("menu").getChildren()) {

                    String key = postListener.getKey();
                    CafeMenudatabase get = postListener.getValue(CafeMenudatabase.class);
                    menu_list.add(new menuitem(get.menu_name, get.price, "0"));
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + get.menu_name + get.price);
                }

                String congestion;
                if(dataSnapshot.child("congestion").getValue()!=null) {
                    congestion = dataSnapshot.child("congestion").getValue().toString();
                }else{
                    congestion = "-";
                }
                TextCongestion.setText("카페 밀도: "+congestion);


                adapter.setItems(menu_list);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("cafe_list").child(cafe_name).addValueEventListener(postListener);


    }
    public void getFirebaseDatabase1() {
        final ValueEventListener postListener1 = new ValueEventListener() {
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
        mPostReference.child("user_list").addValueEventListener(postListener1); //id_list 의 서브트리부터 밑으로만 접근하겟다.
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}