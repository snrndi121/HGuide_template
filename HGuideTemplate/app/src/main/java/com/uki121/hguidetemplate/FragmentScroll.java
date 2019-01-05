package com.uki121.hguidetemplate;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;

public class FragmentScroll extends Fragment {
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

        //HGI
        final HGIndicator hgIndicator = new HGIndicator(view);
        //todo : get rid of
        List <String> tosList = Arrays.asList(getResources().getStringArray(R.array.tos));
        textViewTos1.setText(tosList.get(0));
        textViewTos2.setText(tosList.get(1));
        textViewTos3.setText(tosList.get(2));
        //checkbox
        final CheckBox checkTos1 = (CheckBox) view.findViewById(R.id.check_tos1);
        final CheckBox checkTos2 = (CheckBox) view.findViewById(R.id.check_tos2);
        final CheckBox checkTos3 = (CheckBox) view.findViewById(R.id.check_tos3);

        btnConfirm = (Button) view.findViewById(R.id.btn_scroll_cofirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 새로운 코딩방식*/
                List<Integer> srcview_id = Arrays.asList(R.id.check_tos1, R.id.check_tos2, R.id.check_tos3);
                List<Integer> dstview_id = Arrays.asList(R.id.content_tos1, R.id.content_tos2, R.id.content_tos3);
                hgIndicator.Trigger("confirm_checkbox", srcview_id, "All_check")
                            .Action(dstview_id, "HIGHLIGHT")
                            .AddAction(dstview_id, "FOCUS")
                            .Commit();
                //

                }
        });
        return view;
    }
}
