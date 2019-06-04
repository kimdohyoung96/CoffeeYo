package mobileApp.project.CoffeeYo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CafeMenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CafeMenuFragment.OnFragmentInteractionListener mListener;

    Button addMenuB;
    Button deleteMenuB;
    EditText menuE;
    String  cafename;
    String menuS;
    int flag;

    public CafeMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CafeMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CafeMenuFragment newInstance(String param1, String param2) {
        CafeMenuFragment fragment = new CafeMenuFragment();
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
        View view = inflater.inflate(R.layout.fragment_cafemenu, container, false);
        final Context contextRegister = container.getContext();

        flag = ((ManagerActivity)getActivity()).getFlag();

        addMenuB = (Button) view.findViewById(R.id.add_menu);
        deleteMenuB = (Button) view.findViewById(R.id.delete_menu);
        menuE = (EditText) view.findViewById(R.id.menu);

        addMenuB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == 0){
                    Toast.makeText(contextRegister, "Cafe is not registered yet.", Toast.LENGTH_SHORT).show();
                }
                else{
                    cafename = ((ManagerActivity)getActivity()).getCurrentCafeName();
                    menuS = menuE.getText().toString();
                    if(menuS.length() == 0){
                        Toast.makeText(contextRegister, "Data is missing", Toast.LENGTH_SHORT).show();
                    } else {
                        postFirebaseDatabaseCafeMenu(true);
                        Toast.makeText(contextRegister, "메뉴 추가 완료", Toast.LENGTH_SHORT).show();
                    }
                }
                clearET();
            }
        });

        deleteMenuB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0){
                    Toast.makeText(contextRegister, "Cafe is not registered yet.", Toast.LENGTH_SHORT).show();
                }
                else{
                    cafename = ((ManagerActivity)getActivity()).getCurrentCafeName();
                    menuS = menuE.getText().toString();
                    if(menuS.length() == 0){
                        Toast.makeText(contextRegister, "Data is missing", Toast.LENGTH_SHORT).show();
                    } else {
                        postFirebaseDatabaseCafeMenu(false);
                        Toast.makeText(contextRegister, "메뉴 삭제 완료", Toast.LENGTH_SHORT).show();
                    }
                }
                clearET();
            }
        });

        return view;
    }

    private void postFirebaseDatabaseCafeMenu(boolean add){
        if(add) {
            ((ManagerActivity)getActivity()).mPostReference.child("/cafe_list/"+cafename+"/menu/"+menuS).setValue(menuS);
        } else {
            ((ManagerActivity) getActivity()).mPostReference.child("/cafe_list/"+cafename+"/menu/"+menuS).setValue(null);
        }
    }

    private void clearET(){
        menuE.setText("");
        menuS = "";
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
        if (context instanceof CafeMenuFragment.OnFragmentInteractionListener) {
            mListener = (CafeMenuFragment.OnFragmentInteractionListener) context;
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
