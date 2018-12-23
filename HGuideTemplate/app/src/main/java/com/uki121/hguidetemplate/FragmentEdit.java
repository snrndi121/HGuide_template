package com.uki121.hguidetemplate;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentEdit extends Fragment {
    EditText editText1, editText2, editText3;
    Button button_confm;
    @Override
    public void onCreate(Bundle SavedInstancState) {
        super.onCreate(SavedInstancState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_edittext, container, false);

        final HGIndicator hgi = new HGIndicator(view);
        editText1 = (EditText) view.findViewById(R.id.edit_1);
        editText2 = (EditText) view.findViewById(R.id.edit_2);
        editText3 = (EditText) view.findViewById(R.id.edit_3);
        button_confm = (Button) view.findViewById(R.id.btn_edittext);
        List < Integer > target_id = Arrays.asList(R.id.edit_1, R.id.edit_2, R.id.edit_3);
        //
        //* Except
        //* 목적 : 제한된 시간이랑 버튼 카운트를 넘어가면 해야될 작업들을 알려주는 작업
        //* 개요 : "등록된 소스 타겟이 조건안에 실행이 안되면 목적 타겟으로 알려주는 작업"
        // 일정 시간마다 계속해서 저거를 실행을 시켜줘야함.
        //
        hgi.Trigger("process_next", target_id, "Except")
           .Action(target_id, "HIGHLIGHT")
           .Commit();

        //todo : Except
        button_confm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List < Integer > target_id = Arrays.asList(R.id.edit_1, R.id.edit_2, R.id.edit_3);
                hgi.Trigger("process_edit", target_id, "Empty_text")
                    //.Action(target_id, "HIGHLIGHT")
                    .AddAction(target_id, "POINTER")
                    .Commit();
            }
        });
        return view;
    }
}
