package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class DashboardFragment extends Fragment {


    private FloatingActionButton btnAdd;

    private ArrayList<HashMap<String, String>> transList;
    private DbHandler db;
    private ListView listView;
    private SimpleAdapter adapter;
    private boolean isFirst = false;

        @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
            findView(view);
            return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getTransListFromDb();
        initListView();
        initListener();
        isFirst = true;
    }

    private void findView(View view){
        btnAdd = view.findViewById(R.id.btnAdd);
        listView = view.findViewById(R.id.trans_list);
    }

    private void initListener(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getActivity(), AddExpenseActivity.class);
                addIntent.putExtra(IntentString.IS_ADD,true);
                startActivity(addIntent);
            }
        });
    }

    private void getTransListFromDb(){

        db = new DbHandler(getActivity());
        transList = new ArrayList<>();
        transList.addAll(db.getTrans()) ;
    }

    private void initListView(){
        adapter = new SimpleAdapter(getActivity(),
                transList, R.layout.list_row,
                new String[] {"amount", "category", "date_pick"},
                new int[]{R.id.tvAmount, R.id.tvCategory, R.id.tvDatePick});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(getActivity(), ExpenseDetailActivity.class);

                detailIntent.putExtra(IntentString.KEY_ID,Integer.parseInt(transList.get(position).get(IntentString.KEY_ID)));
                detailIntent.putExtra(IntentString.AMOUNT,transList.get(position).get(IntentString.AMOUNT));
                detailIntent.putExtra(IntentString.CATEGORY,transList.get(position).get(IntentString.CATEGORY));
                detailIntent.putExtra(IntentString.DATEPICKER,transList.get(position).get(IntentString.DATEPICKER));
                startActivity(detailIntent);
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst){
            transList.clear();
            transList.addAll(db.getTrans());
            adapter.notifyDataSetChanged();
        }else{
            isFirst = false;
        }

    }

}
