package mobileApp.project.CoffeeYo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CongestionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RadioGroup group;
    RadioButton quietB;
    RadioButton commonB;
    RadioButton busyB;
    RadioButton fullB;
    Button button;
    String cafename;
    int flag;

    private CongestionFragment.OnFragmentInteractionListener mListener;

    public CongestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CongestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CongestionFragment newInstance(String param1, String param2) {
        CongestionFragment fragment = new CongestionFragment();
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
        View view = inflater.inflate(R.layout.fragment_congestion, container, false);
        final Context contextRegister = container.getContext();

        flag = ((ManagerActivity)getActivity()).getFlag();

        quietB = (RadioButton)view.findViewById(R.id.radio1);
        commonB = (RadioButton)view.findViewById(R.id.radio2);
        busyB = (RadioButton)view.findViewById(R.id.radio3);
        fullB = (RadioButton)view.findViewById(R.id.radio4);
        group = (RadioGroup)view.findViewById(R.id.radioGroup);
        button = (Button)view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == 0){
                    Toast.makeText(contextRegister, "Cafe is not registered yet", Toast.LENGTH_SHORT).show();
                }
                else {
                    cafename = ((ManagerActivity)getActivity()).getCurrentCafeName();
                    postFirebaseDatabaseCafeCongestion(true);
                    Toast.makeText(contextRegister, "혼잡도 설정 완료", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void postFirebaseDatabaseCafeCongestion(boolean add){
        if(add){
            if (quietB.isChecked()){
                ((ManagerActivity)getActivity()).mPostReference.child("/cafe_list/"+cafename+"/congestion").setValue("한산");
            } else if (commonB.isChecked()) {
                ((ManagerActivity)getActivity()).mPostReference.child("/cafe_list/"+cafename+"/congestion").setValue("보통");
            } else if (busyB.isChecked()) {
                ((ManagerActivity)getActivity()).mPostReference.child("/cafe_list/"+cafename+"/congestion").setValue("혼잡");
            } else if (fullB.isChecked()) {
                ((ManagerActivity)getActivity()).mPostReference.child("/cafe_list/"+cafename+"/congestion").setValue("만석");
            }
        }
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
        if (context instanceof CongestionFragment.OnFragmentInteractionListener) {
            mListener = (CongestionFragment.OnFragmentInteractionListener) context;
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
