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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReserveFragment extends Fragment {

    private ReserveFragment.OnFragmentInteractionListener mListener;
    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;


    String uid, state;
    ListView listView;
    ArrayList<String> currentOrderList;
    ArrayAdapter<String> orderArrayAdapter;

    public ReserveFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reserve, container, false);
        final Context contextRegister = container.getContext();
        listView = (ListView)view.findViewById(R.id.orderlist);
        mPostReference = FirebaseDatabase.getInstance().getReference();

        mAuth= FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        currentOrderList = new ArrayList<String>();
        orderArrayAdapter = new ArrayAdapter<String>(contextRegister, android.R.layout.simple_list_item_1);
        listView.setAdapter(orderArrayAdapter);
        getFirebaseDatabase();

        return view;
    }

    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentOrderList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Orderfirebase get = snapshot.getValue(Orderfirebase.class);
                    String[] order = {get.cafe_name, get.state, get.take};
                    if(order[1].equals("current")) {
                        String menu_list = "";
                        for(DataSnapshot snapshot1 : snapshot.child("menu").getChildren()) {
                            CafemenuCount get1 = snapshot1.getValue(CafemenuCount.class);
                            String[] menu = {get1.cafe_menu, get1.count};
                            menu_list = menu_list + menu[0] + ": " + menu[1] + "개 ";
                        }
                        String result = order[0] + "\n" + menu_list + "\n" + order[2];
                        currentOrderList.add(result);
                    }
                }
                orderArrayAdapter.clear();
                orderArrayAdapter.addAll(currentOrderList);
                orderArrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.child("user_list/"+uid+"/order").addValueEventListener(postListener);
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
        if (context instanceof ReserveFragment.OnFragmentInteractionListener) {
            mListener = (ReserveFragment.OnFragmentInteractionListener) context;
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