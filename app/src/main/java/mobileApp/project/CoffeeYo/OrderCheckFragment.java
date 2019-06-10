package mobileApp.project.CoffeeYo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OrderCheckFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;
    private String cafe_name, uid, order, state, take = "";
    private Button OkButton;
    private TextView TextCafe;
    private TextView TextOrder;
    private TextView TextTake;

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

        // Inflate the layout for this fragment
        cafe_name = getArguments().getString("cafe_name");
        order = getArguments().getString("order");
        take = getArguments().getString("take");

        mAuth= FirebaseAuth.getInstance();
        data = new ArrayList<String>();
        uid = mAuth.getCurrentUser().getUid();
        mPostReference = FirebaseDatabase.getInstance().getReference();

        TextCafe = (TextView)view.findViewById(R.id.textView6);
        TextOrder = (TextView)view.findViewById(R.id.textView7);
        TextTake = (TextView)view.findViewById(R.id.textView8);
        TextCafe.setText("카페: "+cafe_name);
        TextOrder.setText("메뉴: "+order);
        TextTake.setText(take);

        OkButton = (Button) view.findViewById(R.id.button2);


        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Map<String, Object> childUpdates = new HashMap<>();
                Map<String, Object> postValues = null;

                state = "current";
                Userorderdata post = new Userorderdata(cafe_name, order, state, take);
                postValues = post.toMap();
                String order_id = mPostReference.push().getKey();


                childUpdates.put("/user_list/" + uid + "/order/"+order_id, postValues);
                mPostReference.updateChildren(childUpdates);

                state = "";
                order = "";
                take = "";
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
}
