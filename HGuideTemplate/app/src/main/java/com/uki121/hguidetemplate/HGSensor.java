package com.uki121.hguidetemplate;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HGSensor {
    enum SenseType {
        DESPERATE("DESPERATE", 40, 30000),
        NEED_ASSIST("NEED_ASSIST", 30, 20000),
        SELF_SUFFICEINT("SELF_SUFFICIENT", 20, 10000),
        NO_HELP("NO_HELP", -1, -1);
        //constructor
        SenseType(String _type, int _touch, int _wait) {
            this.stype =_type;
            this.touchCount =_touch;
            this.waitCount =_wait;
        }
        //method
        public static SenseType calculateSense(int _meantouch, int _meanwait) {
            int tindex = 0, diffweight = Integer.MAX_VALUE;
            List < SenseType > stypes= Arrays.asList(SenseType.values());
            //차이 구하면서 고르기
            //todo : 고도화 할것.지금은 단순하게 차이가 가장 작을때를 Mean으로 반환함
            for(int i = 0; i < stypes.size(); ++i) {
                int diffTouch = Math.abs(stypes.get(i).getTouch() -_meantouch),
                    diffWait = Math.abs(stypes.get(i).getWait() -_meanwait);
                if (diffTouch + diffWait < diffweight) {
                    tindex = i;
                }
            }
            return stypes.get(tindex);
        }
        //set
        public void setType(String _type) { this.stype =_type;}
        public void setTouch(int _touch) { this.touchCount =_touch;}
        public void setWait(int _wait) { this.waitCount =_wait;}
        //get
        public String getType() { return this.stype;}
        public int getTouch() { return this.touchCount;}
        public int getWait() { return this.waitCount;}
        //var
        private String stype;
        private int touchCount = 0, waitCount = 0;
    };
    //constructor
    public HGSensor(){
        //need to be synchronized from own databases
        this.viewSenseLevel = new HashMap<>();
        this.meanSenseLevel = SenseType.NO_HELP;
    };
    //method
    //- checkState() return meanStatus
    // 현재 설정된 평균 상태를 반환
    //
    //- reset() return ;
    // 설정에서 캐시값을 버리라고 하거나 초기화시 동작
    //
    //- assign() return ;
    // 사용자가 직접 입력하는 값을 받아서 설정함.
    //
    //- synchronize() return ;
    // 지정된 데이터 베이스를 받아와서 설정하는 부분
    //set
    //각 뷰마다 설정된 sense 값을 바탕으로 평균값을 형성하여 종합적인 값을 도출함.
    public void setMeanSense() {
        List < Integer > views = new ArrayList<>(viewSenseLevel.keySet());
        //모든 뷰들에 대한 sense값 더하기(calculate the sume of each touch and wait.)
        float sumTime = 0, sumWait = 0;
        for (int i = 0; i < views.size(); ++i) {
            sumTime += viewSenseLevel.get(views.get(i)).getTouch();
            sumWait += viewSenseLevel.get(views.get(i)).getTouch();
        }
        Log.d("sumTime", " " + sumTime);
        Log.d("sumWait", " " + sumWait);
        //평균값 계산(calculate mean)
        int sz = views.size();
        try {
            int meanTime = Math.round(sumTime) / sz, meanWait = Math.round(sumWait / sz);
            //todo : 평균값 구해서, 가까운 쪽을 따라가도록 해야함.
            meanSenseLevel = SenseType.calculateSense(meanTime, meanWait);
            //differ
        } catch(Exception e) {
            Log.e("HGS", e.getMessage());
        }
    }
    //get
    //var
    //viewID에 따라서 적용되는 senseType이 다르다
    //viewID 아니면 fragment 아이디에 따라서
    private HashMap < Integer, SenseType > viewSenseLevel;
    //모든 ViewID에 대한 평균값
    private SenseType meanSenseLevel;
}
