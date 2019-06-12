package mobileApp.project.CoffeeYo;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OrderCheckFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;

    private String cafe_name, uid, Num, state, take, count, Sum = "";
    private Button OkButton;
    private String order[];
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    ArrayList<String[]> list = new ArrayList<>();
    private ArrayList<String> data;

    public OrderCheckFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_check, container, false);
        final Context contextRegister = container.getContext();
        order = new String[10];
        // Inflate the layout for this fragment
        cafe_name = getArguments().getString("cafe_name");
        order = getArguments().getStringArray("order");
        Num = getArguments().getString("Num");
        take = getArguments().getString("take");

        Sum = getArguments().getString("Sum");

        mPostReference = FirebaseDatabase.getInstance().getReference();
        getFirebaseDatabase();

        data = new ArrayList<String>();
        listView = (ListView)view.findViewById(R.id.checklist);
        data.add("카페: "+cafe_name);
        for (int i=0; i<Integer.parseInt(Num); i++) {
            String a = order[i];
            count = getArguments().getString(a);
            data.add(a+"  :"+count+"개");
        }
        data.add(take);
        data.add(Sum+"원");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, data);
        listView.setAdapter(arrayAdapter);




        mAuth= FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mPostReference = FirebaseDatabase.getInstance().getReference();



        OkButton = (Button) view.findViewById(R.id.button2);


        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Map<String, Object> childUpdates = new HashMap<>();
                Map<String, Object> postValues = null;

                state = "current";
                Userorderdata post = new Userorderdata(cafe_name, state, take);
                postValues = post.toMap();
                String order_id = mPostReference.push().getKey();

                childUpdates.put("/user_list/" + uid + "/order/"+order_id, postValues);
                mPostReference.updateChildren(childUpdates);

                state = "";
                //order = "";
                take = "";

                for (int i=0; i<Integer.parseInt(Num); i++) {
                    String a = order[i];
                    count = getArguments().getString(a);


                    Map<String, Object> childUpdates2 = new HashMap<>();
                    Map<String, Object> postValues2 = null;

                    userorderorder post2 = new userorderorder(a, count);
                    postValues2 = post2.toMap();
                    String menu_id = mPostReference.push().getKey();


                    childUpdates2.put("/user_list/" + uid + "/order/" +order_id+ "/menu/" + menu_id, postValues2);
                    mPostReference.updateChildren(childUpdates2);

                    a = "";
                    count = "";

                }

                Fragment LoadingCFragment = new LoadingCFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("cafe_name", cafe_name);
                bundle1.putStringArray("order", order);
                bundle1.putString("Num", String.valueOf(Num));
                bundle1.putString("take", take);
                bundle1.putString("Sum",Sum);
                bundle1.putString("uid",uid);
                LoadingCFragment.setArguments(bundle1);
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.content_main, LoadingCFragment);
                fragmentTransaction1.commit();

            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("user_list").addValueEventListener(postListener); //id_list 의 서브트리부터 밑으로만 접근하겟다.
    }
}