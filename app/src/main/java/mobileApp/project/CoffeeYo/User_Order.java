package mobileApp.project.CoffeeYo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class User_Order extends Fragment {
    private OnFragmentInteractionListener mListener;
    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;

    String cafe_name, order = "";

    private ListView mListView;
    private ArrayList<String> data;

    private ArrayAdapter<String> arrayAdapter;

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


        cafe_name = getArguments().getString("cafe_name");
        data = new ArrayList<String>();
        mListView = view.findViewById(R.id.cafemenulist);


        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        mListView.setAdapter(arrayAdapter);
        mPostReference = FirebaseDatabase.getInstance().getReference();


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                order = data.get(position);



                Fragment OrderCheckFragment = new OrderCheckFragment();
                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cafe_name", cafe_name);
                bundle.putString("order", order);
                OrderCheckFragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, OrderCheckFragment);

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


                order = "";
                cafe_name = "";

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
                data.clear();

                for(DataSnapshot postListener: dataSnapshot.child(cafe_name).child("menu").getChildren()) {

                    String key = postListener.getKey();
                    data.add(key);
                }


                arrayAdapter.clear();
                arrayAdapter.addAll(data);
                arrayAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("cafe_list").addValueEventListener(postListener);
    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
