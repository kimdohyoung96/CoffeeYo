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
    Button addMenuB;
    Button deleteMenuB;
    EditText nameE;
    EditText longitudeE;
    EditText latitudeE;
    EditText menuE;
    String  cafeid;
    String nameS;
    String longitudeS;
    String latitudeS;
    String menuS;
    String uid;
    long cafeID;
    int flag;
    int menuCnt = 0;

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
        if(flag == 0) cafeID = ((ManagerActivity)getActivity()).getNewCafeID();
        else cafeID = ((ManagerActivity)getActivity()).getCurrentCafeID();
        uid = ((ManagerActivity)getActivity()).getUid();

        registerB = (Button) view.findViewById(R.id.registerB);
        addMenuB = (Button) view.findViewById(R.id.add_menu);
        deleteMenuB = (Button) view.findViewById(R.id.delete_menu);
        nameE = (EditText) view.findViewById(R.id.name);
        longitudeE = (EditText) view.findViewById(R.id.longitude);
        latitudeE = (EditText) view.findViewById(R.id.latitude);
        menuE = (EditText) view.findViewById(R.id.menu);

        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(contextRegister, "카페 등록/변경 완료", Toast.LENGTH_LONG).show();
                nameS = nameE.getText().toString();
                longitudeS = longitudeE.getText().toString();
                latitudeS = latitudeE.getText().toString();

                if ((nameS.length() * longitudeS.length() * latitudeS.length()) == 0) {
                    Toast.makeText(contextRegister, "Data is missing", Toast.LENGTH_SHORT).show();
                } else {
                    // set cafe_id
                    cafeid = Long.toString(cafeID);
                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("user_list/"+uid+"/cafe_id", cafeid);
                    ((ManagerActivity)getActivity()).mPostReference.updateChildren(taskMap);
                    postFirebaseDatabaseCafeInfo(true);
                }
            }
        });

        addMenuB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contextRegister, "메뉴 추가 완료", Toast.LENGTH_LONG).show();

                menuS = menuE.getText().toString();
                if(menuS.length() == 0){
                    Toast.makeText(contextRegister, "Data is missing", Toast.LENGTH_SHORT).show();
                } else {
                    menuCnt++;
                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("cafe_list/cafe"+cafeid+"/cafe_info/menu_cnt", Integer.toString(menuCnt));
                    ((ManagerActivity)getActivity()).mPostReference.updateChildren(taskMap);
                    postFirebaseDatabaseCafeMenu(true);
                }
            }
        });

        deleteMenuB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contextRegister, "메뉴 삭제 완료", Toast.LENGTH_LONG).show();

                menuS = menuE.getText().toString();
                if(menuS.length() == 0){
                    Toast.makeText(contextRegister, "Data is missing", Toast.LENGTH_SHORT).show();
                } else {
                    postFirebaseDatabaseCafeInfo(false);
                    menuCnt--;
                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("cafe_list/cafe"+cafeid+"/cafe_info/menu_cnt", Integer.toString(menuCnt));
                    ((ManagerActivity)getActivity()).mPostReference.updateChildren(taskMap);
                }
            }
        });

        return view;
    }

    private void postFirebaseDatabaseCafeMenu(boolean add){
        if(add == true) {
            ((ManagerActivity) getActivity()).mPostReference.child("/cafe_list/cafe" + cafeid + "/cafe_info/menu/menu" + Integer.toString(menuCnt)).setValue(menuS);
        } else {
            ((ManagerActivity) getActivity()).mPostReference.child("/cafe_list/cafe" + cafeid + "/cafe_info/menu/menu" + Integer.toString(menuCnt)).setValue(null);
        }
        clearET2();
    }

    private void postFirebaseDatabaseCafeInfo(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            CafeInfo post = new CafeInfo(nameS, longitudeS, latitudeS, Integer.toString(menuCnt));
            postValues = post.toMap();
        }
        childUpdates.put("/cafe_list/cafe" + cafeid + "/cafe_info/", postValues);
        ((ManagerActivity)getActivity()).mPostReference.updateChildren(childUpdates);
        clearET();
    }

    private void clearET2(){
        menuE.setText("");
        menuS = "";
    }

    private void clearET() {
        nameE.setText("");
        longitudeE.setText("");
        latitudeE.setText("");

        nameS = "";
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
