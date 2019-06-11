package mobileApp.project.CoffeeYo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class User_Finished_Order extends Fragment {


    private OnFragmentInteractionListener mListener;
    private DatabaseReference mPostReference;


    String uid, state;
    ListView listView;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;

    public User_Finished_Order() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user__finished__order, container, false);
        final Context contextRegister = container.getContext();
        uid = getArguments().getString("uid");

        data = new ArrayList<String>();
        listView = (ListView)view.findViewById(R.id.orderlist);
        mPostReference = FirebaseDatabase.getInstance().getReference();

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
        getFirebaseDatabase();


        return view;
    }

    public void getFirebaseDatabase() {
        try {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.d("onDataChange", "Data is Updated");
                    data.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String key = postSnapshot.getKey();
                        Orderfirebase get = postSnapshot.getValue(Orderfirebase.class);
                        String[] info = {get.cafe_name, get.order, get.state};
                        state = "old";
                        if (state.equals(info[2])) {
                            String result = info[0] + ": " + info[1];
                            data.add(result);
                            Log.d("getFirebaseDatabase", "key: " + key);
                            Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2]);
                        }

                    }

                arrayAdapter.clear();
                arrayAdapter.addAll(data);
                arrayAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("user_list").child(uid).child("order").addValueEventListener(postListener);
        }catch(Exception e){}
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
}
