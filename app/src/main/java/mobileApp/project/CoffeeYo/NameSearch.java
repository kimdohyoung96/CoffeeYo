package mobileApp.project.CoffeeYo;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.text.TextWatcher;
import android.text.Editable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class NameSearch extends Fragment {


    private NameSearch.OnFragmentInteractionListener mListener;
    private DatabaseReference mPostReference;

    private ListView listView;
    private ArrayList<String> data;
    private ArrayList<String> list; // 데이터를 넣은 리스트변수
    private ArrayAdapter<String> arrayAdapter;

    private EditText editSearch;




    public NameSearch() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.name_search, container, false);

        editSearch = (EditText) view.findViewById(R.id.editText);
        listView = (ListView) view.findViewById(R.id.cafenamelist);
        data = new ArrayList<String>();
        list = new ArrayList<String>();

        mPostReference = FirebaseDatabase.getInstance().getReference();

        arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);

        getFirebaseDatabaseCafeName();


        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editSearch.getText().toString();
                search(text);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    public void search(String charText) {

        list.clear();

        if (charText.length() == 0) {
            System.arraycopy(data, 0, list, 0, data.size());
            arrayAdapter.clear();
            arrayAdapter.addAll(list);
            arrayAdapter.notifyDataSetChanged();
        }

        else {

            for (int i = 0; i < data.size(); i++) {

                list.clear();

                if (data.get(i).toLowerCase().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(data.get(i));
                    arrayAdapter.clear();
                    arrayAdapter.addAll(list);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        arrayAdapter.notifyDataSetChanged();

    }

    public void getFirebaseDatabaseCafeName(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is updated");

                data.clear();

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    String id_key =  postSnapshot.getKey();
                    String value = (String) postSnapshot.getValue();

                    Cafenamedatabase get = dataSnapshot.child(id_key).child("cafe_info").getValue(Cafenamedatabase.class);
                    String[] info = {get.cafe_id, get.cafe_name};
                    String result = info[1];
                    data.add(result);
                    Log.d("getFirebaseDatabase", "key: " + id_key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1]);

                }
;
                arrayAdapter.clear();
                arrayAdapter.addAll(data);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mPostReference.child("cafe_list").addValueEventListener(postListener);
    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}