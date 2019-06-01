package mobileApp.project.CoffeeYo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    EditText IDE;
    EditText nameE;
    EditText longitudeE;
    EditText latitudeE;
    int cafeID;
    String cafeIDS;
    String nameS;
    String longitudeS;
    String latitudeS;
    String menuS;
    String menu1;
    String menu2;
    String menu3;
    String menu4;
    String menu5;

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

        registerB = (Button) view.findViewById(R.id.register);
        addMenuB = (Button) view.findViewById(R.id.add_menu);
        deleteMenuB = (Button) view.findViewById(R.id.delete_menu);
        IDE = (EditText) view.findViewById(R.id.id);
        nameE = (EditText) view.findViewById(R.id.name);
        longitudeE = (EditText) view.findViewById(R.id.longitude);
        latitudeE = (EditText) view.findViewById(R.id.latitude);
        final EditText menuE = (EditText) view.findViewById(R.id.menu);

        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contextRegister, "카페 등록/변경", Toast.LENGTH_LONG).show();
                cafeIDS = IDE.getText().toString();
                nameS = nameE.getText().toString();
                longitudeS = longitudeE.getText().toString();
                latitudeS = latitudeE.getText().toString();
                menu1 = "Unknown";
                menu2 = "Unknown";
                menu3 = "Unknown";
                menu4 = "Unknown";
                menu5 = "Unknown";

                if ((cafeIDS.length() * nameS.length() * longitudeS.length() * latitudeS.length()) == 0) {
                    Toast.makeText(contextRegister, "Data is missing", Toast.LENGTH_SHORT).show();
                } else {
                    cafeID = Integer.parseInt(cafeIDS);
                    postFirebaseDatabaseCafeInfo(true);
                }
            }
        });

        addMenuB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contextRegister, "메뉴 추가", Toast.LENGTH_LONG).show();
            }
        });

        deleteMenuB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contextRegister, "메뉴 삭제", Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

    private void postFirebaseDatabaseCafeInfo(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            CafeInfo post = new CafeInfo();
            postValues = post.toMap();
        }
        childUpdates.put("/cafe_list/" + cafeIDS + "/cafe_info/", postValues);  //user_id로 cafe_id 찾는거로 고치기 + query join
        ((ManagerActivity)getActivity()).mPostReference_cafeInfo.updateChildren(childUpdates);
        clearET();
    }

    private void clearET() {
        IDE.setText("");
        nameE.setText("");
        longitudeE.setText("");
        latitudeE.setText("");

        cafeID = 0;
        cafeIDS = "";
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
