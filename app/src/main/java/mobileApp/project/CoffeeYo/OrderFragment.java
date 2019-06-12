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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView oldOrderInfo;
    ArrayList<String> oldOrderList;
    ArrayAdapter<String> arrayAdapter;
    int flag;
    String cafename;

    private OrderFragment.OnFragmentInteractionListener mListener;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        final Context contextRegister = container.getContext();

        oldOrderInfo = (ListView)view.findViewById(R.id.listview);
        oldOrderList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(contextRegister, android.R.layout.simple_list_item_1);
        oldOrderInfo.setAdapter(arrayAdapter);

        flag = ((ManagerActivity)getActivity()).getFlag();
        if(flag == 0){
            Toast.makeText(contextRegister, "등록된 카페가 없습니다", Toast.LENGTH_SHORT).show();
        }
        else{
            cafename = ((ManagerActivity)getActivity()).getCurrentCafeName();
            getFirebaseDatabaseOldOrderInfo();
        }

        return view;
    }

    public void getFirebaseDatabaseOldOrderInfo(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                oldOrderList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String client = snapshot.getKey();
                    for (DataSnapshot snapshot1 : snapshot.child("order").getChildren()){
                        String orderNum = snapshot1.getKey();
                        Orderfirebase get = snapshot1.getValue(Orderfirebase.class);
                        String[] order = {get.cafe_name, get.state, get.take};
                        if(order[0].equals(cafename) && order[1].equals("old")){
                            String menu_list = "";
                            for (DataSnapshot snapshot2 : snapshot1.child("menu").getChildren()){
                                CafemenuCount get1 = snapshot2.getValue(CafemenuCount.class);
                                String[] menu = {get1.cafe_menu, get1.count};
                                menu_list = menu_list + menu[0] + " " + menu[1] + "개 ";
                            }
                            String result = "Client: " + client + "\nOrder number: " + orderNum + "\n" + menu_list + "\n" + order[2];
                            oldOrderList.add(result);
                        }
                    }
                    arrayAdapter.clear();
                    arrayAdapter.addAll(oldOrderList);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        ((ManagerActivity)getActivity()).mPostReference.child("user_list").addValueEventListener(postListener);
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
        if (context instanceof OrderFragment.OnFragmentInteractionListener) {
            mListener = (OrderFragment.OnFragmentInteractionListener) context;
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
