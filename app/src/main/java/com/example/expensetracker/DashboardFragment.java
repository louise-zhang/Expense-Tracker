package com.example.expensetracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DashboardFragment extends Fragment {


    private FloatingActionButton btnAdd;

    private ArrayList<HashMap<String, String>> transList;
    private DbHandler db;
    static SwipeMenuListView listView;
    private SimpleAdapter adapter;
    private boolean isFirst = false;

//    private EditText edFilter;

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
//        listViewFilter();
        setHasOptionsMenu(true);
    }

    private void findView(View view) {
//        edFilter = view.findViewById(R.id.search_filter);
        listView = view.findViewById(R.id.trans_list);

    }

    private void initListener() {

    }

    private void getTransListFromDb() {

        db = new DbHandler(getActivity());
        transList = new ArrayList<>();
        transList.addAll(db.getTrans());
        Collections.reverse(transList);  // reverse list
    }

    private void initListView() {
        adapter = new SimpleAdapter(getActivity(),
                transList, R.layout.list_row,
                new String[]{"amount", "category", "date_pick"},
                new int[]{R.id.tvAmount, R.id.tvCategory, R.id.tvDatePick});

        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0xE4,
                        0xB2)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setTitle("Edit");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_forever_black_24dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Intent editIntent = new Intent(getActivity(), AddExpenseActivity.class);
                        startActivity(editIntent);// Edit
                        break;
                    case 1:

                        Intent intent = getActivity().getIntent();
                        final int key = intent.getIntExtra(IntentString.KEY_ID, -1);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Delete Dialog")
                                .setMessage("Are you sure you want to delete?")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //do anything you want
                                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                        DbHandler db = new DbHandler(getActivity());
                                        db.deleteList(key);
//                                        getActivity().finish();
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        builder.create().show();

                        transList.remove(position);
                        adapter.notifyDataSetChanged(); // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(getActivity(), ExpenseDetailActivity.class);

                detailIntent.putExtra(IntentString.KEY_ID, Integer.parseInt(transList.get(position).get(IntentString.KEY_ID)));
                detailIntent.putExtra(IntentString.AMOUNT, transList.get(position).get(IntentString.AMOUNT));
                detailIntent.putExtra(IntentString.CATEGORY, transList.get(position).get(IntentString.CATEGORY));
                detailIntent.putExtra(IntentString.DATEPICKER, transList.get(position).get(IntentString.DATEPICKER));
                startActivity(detailIntent);
            }
        });


    }


//    private void listViewFilter() {
//        edFilter.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                (DashboardFragment.this).adapter.getFilter().filter(s);
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst) {
            transList.clear();
            transList.addAll(db.getTrans());
            adapter.notifyDataSetChanged();
        } else {
            isFirst = false;
        }

    }

}
