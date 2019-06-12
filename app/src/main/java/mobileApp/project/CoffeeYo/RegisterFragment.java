package mobileApp.project.CoffeeYo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RegisterFragment.OnFragmentInteractionListener mListener;

    Button registerB;
    EditText nameE;
    EditText longitudeE;
    EditText latitudeE;
    String  cafename;
    String longitudeS;
    String latitudeS;
    String uid;
    int flag;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        final Context contextRegister = container.getContext();

        flag = ((ManagerActivity)getActivity()).getFlag();
        if(flag == 1) cafename = ((ManagerActivity)getActivity()).getCurrentCafeName();
        uid = ((ManagerActivity)getActivity()).getUid();

        registerB = (Button) view.findViewById(R.id.registerB);
        nameE = (EditText) view.findViewById(R.id.name);
        longitudeE = (EditText) view.findViewById(R.id.longitude);
        latitudeE = (EditText) view.findViewById(R.id.latitude);

        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cafename = nameE.getText().toString();
                longitudeS = longitudeE.getText().toString();
                latitudeS = latitudeE.getText().toString();

                if ((cafename.length() * longitudeS.length() * latitudeS.length()) == 0) {
                    Toast.makeText(contextRegister, "필요한 모든 데이터를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // set cafe_name
                    ((ManagerActivity)getActivity()).mPostReference.child("/user_list/"+uid+"/cafe_name/").setValue(cafename);
                    postFirebaseDatabaseCafeInfo(true);
                }
            }
        });

        return view;
    }

    private void postFirebaseDatabaseCafeInfo(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            CafeInfo post = new CafeInfo(cafename, longitudeS, latitudeS);
            postValues = post.toMap();
        }
        childUpdates.put("/cafe_list/" + cafename + "/cafe_info/", postValues);
        ((ManagerActivity)getActivity()).mPostReference.updateChildren(childUpdates);
        clearET();
        ((ManagerActivity)getActivity()).getFirebaseDatabaseCheckMyCafe();
    }

    private void clearET() {
        nameE.setText("");
        longitudeE.setText("");
        latitudeE.setText("");

        cafename = "";
        longitudeS = "";
        latitudeS = "";
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
        if (context instanceof RegisterFragment.OnFragmentInteractionListener) {
            mListener = (RegisterFragment.OnFragmentInteractionListener) context;
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
