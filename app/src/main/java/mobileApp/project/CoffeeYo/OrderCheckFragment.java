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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
    private String cafe_name, uid, Num, state, take, count = "";
    private Button OkButton;
    private String order[];
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

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

        mPostReference = FirebaseDatabase.getInstance().getReference();


        data = new ArrayList<String>();
        listView = (ListView)view.findViewById(R.id.checklist);
        data.add("카페: "+cafe_name);
        for (int i=0; i<Integer.parseInt(Num); i++) {
            String a = order[i];
            count = getArguments().getString(a);
            data.add(a+"  :"+count+"개");
        }
        data.add(take);

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
