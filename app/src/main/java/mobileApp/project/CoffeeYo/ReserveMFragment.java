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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

    public ListView myCafeInfo;
    public ArrayList<CafeItem> cafeInfo;
    private CafeAdapter arrayAdapter_cafeInfo;

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

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        final Context contextRegister = container.getContext();

        myCafeInfo = (ListView) view.findViewById(R.id.myCafe);

        cafeInfo = new ArrayList<CafeItem>();

        arrayAdapter_cafeInfo = new CafeAdapter(this
                , cafeInfo);
 //       myCafeInfo.setAdapter(arrayAdapter_cafeInfo);
        getFirebaseDatabaseCafeInfo();

        return inflater.inflate(R.layout.fragment_reserve_m, container, false);
    }

    public void getFirebaseDatabaseCafeInfo(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is updated");

                cafeInfo.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    CafeInfo get = postSnapshot.getValue(CafeInfo.class);

                    CafeItem item = new CafeItem(get.manager_id, get.cafe_name, get.cafe_longitude, get.cafe_latitude, get.menu1, get.menu2, get.menu3, get.menu4, get.menu5);
                    cafeInfo.add(item);
                }
                arrayAdapter_cafeInfo.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {         }
        };
        ((ManagerActivity)getActivity()).mPostReference_cafeInfo.child("cafe_list").addValueEventListener(postListener); //user_id로 고쳐야해 + query join
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
