package com.uki121.hguidetemplate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NavListFragment extends Fragment {

    @Override
    //현재 액티비티 내에 프레그먼트 레이아웃을 넣을면 onCreateView() 메서드를 오버라이딩한다.
    //- savedInstanceState는 프레그먼트가 다시 실행(resumed)되는 경우를 대비해 프레그먼트의 이전 인스턴스에 대한
    //  데이터를 전달하는 묶음이다.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_main, container, false);
        return view;
    }
    //특정 레이아웃 리소스 파일에서 새로운 뷰 계층 구조를 인플레이드 한다.
    //content.main.xml이 된다.
}
