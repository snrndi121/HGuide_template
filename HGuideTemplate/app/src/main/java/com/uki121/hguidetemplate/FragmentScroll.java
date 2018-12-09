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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentScroll extends Fragment {
    private ScrollView mScrollview;
    private TextView textViewTos1,textViewTos2,textViewTos3;
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
        //Initializing variables
        textViewTos1 = (TextView) view.findViewById(R.id.content_tos1);
        textViewTos2 = (TextView) view.findViewById(R.id.content_tos2);
        textViewTos3 = (TextView) view.findViewById(R.id.content_tos3);

        List <String> tosList = Arrays.asList(getResources().getStringArray(R.array.tos));
        textViewTos1.setText(tosList.get(0));
        textViewTos2.setText(tosList.get(1));
        textViewTos3.setText(tosList.get(2));
        //mcontext = getActivity();

        return view;
    }

}
