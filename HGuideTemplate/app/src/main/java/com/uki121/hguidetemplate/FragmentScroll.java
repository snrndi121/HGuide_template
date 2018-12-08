package com.uki121.hguidetemplate;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;

import java.util.Arrays;
import java.util.List;

public class FragmentScroll extends Fragment {
    private ScrollView mScrollview;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Context mcontext;

    public FragmentScroll() {};
    public static FragmentScroll newInstance() {
        FragmentScroll fragment = new FragmentScroll();
        return fragment;
    }
    @Override
    public void onCreate(Bundle SavedInstancState) {
        super.onCreate(SavedInstancState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_scroll, container, false);

        mcontext = getActivity();
        //Recycler
        //mRecyclerView = (RecyclerView) view.findViewById(R.id.termOfServices_list);
        //mRecyclerView.setHasFixedSize(true);
        // Specify a layout for RecyclerView
        // StaggeredGrid 레이아웃을 사용한다
        // mLayoutManager = new LinearLayoutManager(mcontext);
        // layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        // layoutManager = new GridLayoutManager(this,3);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(mcontext));

        // 리소스에서 name이 company인 array를 가져와서 String[] 배열에 대입
        List<String> termOfservcies = Arrays.asList(getResources().getStringArray(R.array.tos));

        // Initialize a new Adapter for RecyclerView
        //mAdapter = new TosAdapter(mcontext, termOfservcies);
        // Data bind RecyclerView with Adapter
        //mRecyclerView.setAdapter(mAdapter);

        return view;
    }

}
