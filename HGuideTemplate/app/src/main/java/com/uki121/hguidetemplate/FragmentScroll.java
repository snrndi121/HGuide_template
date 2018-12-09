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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentScroll extends Fragment {
    private ScrollView mScrollview;
    private TextView textViewTos1,textViewTos2,textViewTos3;
    private Button btnConfirm;
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
        //checkbox
        final CheckBox checkTos1 = (CheckBox) view.findViewById(R.id.check_tos1);
        final CheckBox checkTos2 = (CheckBox) view.findViewById(R.id.check_tos2);
        final CheckBox checkTos3 = (CheckBox) view.findViewById(R.id.check_tos3);

        checkTos1.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        mcontext,
                        "Clicked : " + v.getId(),
                        Toast.LENGTH_SHORT
                ).show();
            }
         });
        btnConfirm = (Button) view.findViewById(R.id.btn_scroll_cofirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = " Please, check all the agreements ";
                //check whether checkboxes are checked all
                if (checkTos1.isChecked() && checkTos2.isChecked() && checkTos3.isChecked()) {
                    msg = "Success! ";
                }
                Toast.makeText(
                        mcontext, msg,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
        mcontext = getActivity();

        return view;
    }
}
