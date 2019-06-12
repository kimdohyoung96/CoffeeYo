package mobileApp.project.CoffeeYo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    ListView currentOrderInfo;
    TextView myCafeInfo;
    TextView myCafeCongestion;
    ListView myCafeMenu;
    String cafename;
    int flag;
    ArrayList<String> currentOrderList;
    ArrayAdapter<String> orderArrayAdapter;
    ArrayList<String> menuList;
    ArrayAdapter<String> menuArrayAdapter;
    String clientS;
    String orderNumS;
    Context contextRegister;
    String uid;

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
        contextRegister = container.getContext();

        myCafeInfo = (TextView)view.findViewById(R.id.myCafe);
        myCafeCongestion = (TextView)view.findViewById(R.id.myCafeCongestion);
        myCafeMenu = (ListView) view.findViewById(R.id.myCafeMenu);
        currentOrderInfo = (ListView)view.findViewById(R.id.listView);

        menuList = new ArrayList<String>();
        menuArrayAdapter = new ArrayAdapter<String>(contextRegister, android.R.layout.simple_list_item_1);
        myCafeMenu.setAdapter(menuArrayAdapter);

        currentOrderList = new ArrayList<String>();
        orderArrayAdapter = new ArrayAdapter<String>(contextRegister, android.R.layout.simple_list_item_1);
        currentOrderInfo.setAdapter(orderArrayAdapter);

        flag = ((ManagerActivity)getActivity()).getFlag();
        uid = ((ManagerActivity)getActivity()).getUid();
        if(flag == 0){
            myCafeInfo.setText("Not registered yet");
            Toast.makeText(contextRegister, "등록한 카페가 없습니다", Toast.LENGTH_SHORT).show();
        }
        else{
            cafename = ((ManagerActivity)getActivity()).getCurrentCafeName();
            Log.d("HERE!!!!!", "cafename: "+cafename);
            getFirebaseDatabaseCafeInfo();
            getFirebaseDatabaseCafeMenu();
            getFirebaseDatabaseCurrentOrderInfo();
            getFirebaseDatabaseCongestion();
        }

        currentOrderInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = (String) adapterView.getItemAtPosition(position);
                int num = item.indexOf("Client: ");
                clientS = item.substring(num+8, (item.substring(num).indexOf("\n")+num));
                num = item.indexOf("Order number: ");
                orderNumS = item.substring(num+14, (item.substring(num).indexOf("\n")+num));

                Log.d("currentOrderInfo", "client:"+clientS);
                Log.d("currentOrderInfo", "orderNum:"+orderNumS);

                changeFirebaseDatabaseOrder();
                Toast.makeText(contextRegister,"주문을 완료했습니다",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void changeFirebaseDatabaseOrder(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(clientS).exists()){
                    for(DataSnapshot snapshot : dataSnapshot.child(clientS+"/order").getChildren()){
                        String orderNum = snapshot.getKey();
                        if(orderNum.equals(orderNumS)){
                            ((ManagerActivity)getActivity()).mPostReference.child("user_list/"+clientS+"/order/"+orderNumS+"/state").setValue("old");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        ((ManagerActivity)getActivity()).mPostReference.child("user_list").addValueEventListener(postListener);
    }

    public void getFirebaseDatabaseCongestion(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(cafename).exists()){
                    myCafeCongestion.setText("");
                    CafeCongestion get = dataSnapshot.child(cafename).getValue(CafeCongestion.class);
                    String info = get.congestion;
                    String result = "Current congestion: " + info;
                    myCafeCongestion.setText(result);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        ((ManagerActivity)getActivity()).mPostReference.child("cafe_list").addValueEventListener(postListener);
    }

    public void getFirebaseDatabaseCafeInfo(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(cafename).exists()){
                    myCafeInfo.setText("");
                    CafeInfo get = dataSnapshot.child("/"+cafename+"/cafe_info").getValue(CafeInfo.class);
                    String[] info = {get.cafe_name, get.cafe_longitude, get.cafe_latitude};
                    String result = "Cafe name: " + info[0] + "\nlocation: (" + info[1] + ", " + info[2] + ")";
                    myCafeInfo.setText(result);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {         }
        };
        ((ManagerActivity)getActivity()).mPostReference.child("cafe_list").addValueEventListener(postListener);
    }

    public void getFirebaseDatabaseCafeMenu(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menuList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    CafeMenu get = snapshot.getValue(CafeMenu.class);
                    String[] menu = {get.menu_name, get.price};
                    String result = "Menu: " + menu[0] + "\nPrice: " + menu[1];
                    menuList.add(result);
                }
                menuArrayAdapter.clear();
                menuArrayAdapter.addAll(menuList);
                menuArrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        ((ManagerActivity)getActivity()).mPostReference.child("cafe_list/"+cafename+"/menu").addValueEventListener(postListener);
    }

    public void getFirebaseDatabaseCurrentOrderInfo(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentOrderList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String client = snapshot.getKey();
                    for(DataSnapshot snapshot1 : snapshot.child("order").getChildren()){
                        String orderNum = snapshot1.getKey();
                        Orderfirebase get = snapshot1.getValue(Orderfirebase.class);
                        String[] order = {get.cafe_name, get.state, get.take};
                        if(order[0].equals(cafename) && order[1].equals("current")){
                            String menu_list = "";
                            for (DataSnapshot snapshot2 : snapshot1.child("menu").getChildren()){
                                CafemenuCount get1 = snapshot2.getValue(CafemenuCount.class);
                                String[] menu = {get1.cafe_menu, get1.count};
                                menu_list = menu_list + menu[0] + ": " + menu[1] + "개 ";
                            }
                            String result = "Client: " + client + "\nOrder number: " + orderNum + "\n" + menu_list + "\n" + order[2];
                            currentOrderList.add(result);
                        }
                    }
                    orderArrayAdapter.clear();
                    orderArrayAdapter.addAll(currentOrderList);
                    orderArrayAdapter.notifyDataSetChanged();
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
