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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class User_Finished_Order extends Fragment {


    private OnFragmentInteractionListener mListener;
    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;


    String uid, state;
    ListView listView;
    ArrayList<MemoItem> data;
    MemoAdapter memoAdapter;

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

        data = new ArrayList<MemoItem>();
        listView = (ListView)view.findViewById(R.id.orderlist);
        mPostReference = FirebaseDatabase.getInstance().getReference();

        mAuth= FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        memoAdapter = new MemoAdapter(getContext(), data);
        listView.setAdapter(memoAdapter);
        getFirebaseDatabase();

        return view;
    }

    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                data.clear();
                ArrayList<MemoItem> memos = new ArrayList<MemoItem>();

                for (DataSnapshot postListener : dataSnapshot.getChildren()) {
                    String key = postListener.getKey();
                    Orderfirebase get = postListener.getValue(Orderfirebase.class);
                    String[] info = {get.cafe_name, get.take, get.state};
                    state = "old";
                    String menu_count = "";
                    if (state.equals(info[2])) {
                        CafemenuCount get1 = postListener.child("menu").getValue(CafemenuCount.class);
                        menu_count = menu_count+get1.cafe_menu +": "+get1.count+"개"+"  ";
                        Log.d("getFirebaseDatabase", "key: " + key);
                        Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2]+menu_count);
                        MemoItem result = new MemoItem(info[0],info[1],menu_count);
                        memos.add(result);
                    }

                }
                memoAdapter = new MemoAdapter(getContext(), memos);
                listView.setAdapter(memoAdapter);
                memoAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("user_list").child(uid).child("order").addValueEventListener(postListener);
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
