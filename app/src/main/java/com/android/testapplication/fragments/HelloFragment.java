package com.android.testapplication.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.testapplication.MainActivity;
import com.android.testapplication.R;
import com.android.testapplication.adapters.RaitingRVAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelloFragment extends Fragment {


    private RecyclerView recyclerView;
    private RaitingRVAdapter rvAdapter;

    public HelloFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        //Log.d(LOG_TAG, "onStart()");
        ((MainActivity) getActivity()).setActivityTitle(R.string.hello_world);
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hello, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.new_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<Object> list = new ArrayList<>();
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        rvAdapter = new RaitingRVAdapter(getContext(),list);
        recyclerView.setAdapter(rvAdapter);
        // Inflate the layout for this fragment
        return view;
    }

}
