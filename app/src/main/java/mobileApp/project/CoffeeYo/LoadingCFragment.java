package mobileApp.project.CoffeeYo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class LoadingCFragment extends Fragment {

    private DatabaseReference mPostReference;
    String uid;
    String mymoney = "";
    String myemail = "";
    String cafe_name = "";
    ArrayList<String[]> list = new ArrayList<>();
    private String Num, state, take, count, Sum = "";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btn;
    private LoadingCFragment.OnFragmentInteractionListener mListener;


    public LoadingCFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoadingCFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoadingCFragment newInstance(String param1, String param2) {
        LoadingCFragment fragment = new LoadingCFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading1, container, false);
        final Context contextRegister = container.getContext();
        mPostReference = FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        uid = getArguments().getString("uid");
        cafe_name = getArguments().getString("cafe_name");
        Num = getArguments().getString("Num");
        take = getArguments().getString("take");

        Sum = getArguments().getString("Sum");
        btn = (Button)view.findViewById(R.id.button3);


        getFirebaseDatabase();
        Log.d("loadingC 까지는옴", "onCreateView: ");
        Handler Handler = new Handler();
        Handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                postfire();
            }
        }, 1000);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment ReserveFragment = new ReserveFragment();

                FragmentManager fragmentManager = getFragmentManager();
                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("uid", uid);
                ReserveFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, ReserveFragment);

                fragmentTransaction.commit();
            }
        });

        return view;
    }
    public void postfire(){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;

        for(int j = 0 ; j < list.size(); j++) {
            if (uid.equals(list.get(j)[2])) {
                String money = String.valueOf(Integer.parseInt(list.get(j)[3]) - Integer.parseInt(Sum));
                mPostReference.child("user_list/"+uid+"/money").setValue(money);
                btn.setVisibility(View.VISIBLE);
            }
            if (cafe_name.equals(list.get(j)[4])) {
                String money = String.valueOf(Integer.parseInt(list.get(j)[3]) + Integer.parseInt(Sum));
                mPostReference.child("user_list/"+uid+"/money").setValue(money);
                Log.d("더함", "onCreateView: ");
                btn.setVisibility(View.VISIBLE);
            }
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoadingCFragment.OnFragmentInteractionListener) {
            mListener = (LoadingCFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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