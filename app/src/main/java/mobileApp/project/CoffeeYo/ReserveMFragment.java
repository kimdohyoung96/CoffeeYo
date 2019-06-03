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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReserveMFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ReserveMFragment.OnFragmentInteractionListener mListener;

    TextView myCafeInfo;
    String cafeid;
    int flag;
    long cafeID;

    public ReserveMFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReserveMFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReserveMFragment newInstance(String param1, String param2) {
        ReserveMFragment fragment = new ReserveMFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reserve_m, container, false);
        final Context contextRegister = container.getContext();

        myCafeInfo = (TextView)view.findViewById(R.id.myCafe);

        flag = ((ManagerActivity)getActivity()).getFlag();
        if(flag == 0){
            cafeID = ((ManagerActivity)getActivity()).getNewCafeID();
            myCafeInfo.setText("Not registered yet.");
        }
        else{
            cafeID = ((ManagerActivity)getActivity()).getCurrentCafeID();
            cafeid = Long.toString(cafeID);
            //cafeid = "1";
            getFirebaseDatabaseCafeInfo();
        }

        return view;
    }

    public void getFirebaseDatabaseCafeInfo(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("cafe"+cafeid).exists()){
                    myCafeInfo.setText("");
                    CafeInfo get = dataSnapshot.child("/cafe"+cafeid+"/cafe_info").getValue(CafeInfo.class);
                    String[] info = {get.cafe_name, get.cafe_longitude, get.cafe_latitude, get.menu_cnt};
                    String result = "cafe ID: " + cafeid + "\nname: " + info[0] + "\nlocation: (" + info[1] + ", " + info[2] + ")\nnumber of menu: " + info[3];

                    myCafeInfo.setText(result);
                    Log.d("getCafeInfo", "cafeid: " + cafeid);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {         }
        };
        ((ManagerActivity)getActivity()).mPostReference.child("cafe_list").addValueEventListener(postListener);
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
        if (context instanceof ReserveMFragment.OnFragmentInteractionListener) {
            mListener = (ReserveMFragment.OnFragmentInteractionListener) context;
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
