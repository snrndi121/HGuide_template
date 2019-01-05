package com.uki121.hguidetemplate;

import android.util.Log;

import java.util.ArrayList;
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
    }
    //constructor
    public HGSensor(){
        this.viewSenseLevel = new HashMap<>();
        this.meanSenseLevel = SenseType.NO_HELP;
    };
    //method
    //set
    public void setMeanSense() {
        List < Integer > views = new ArrayList<>(viewSenseLevel.keySet());
        //calculate the sume of each touch and wait.
        int sumTime = 0, sumWait = 0;
        for (int i = 0; i < views.size(); ++i) {
            sumTime += viewSenseLevel.get(views.get(i)).getTouch();
            sumWait += viewSenseLevel.get(views.get(i)).getTouch();
        }
        //calculate mean
        int sz = views.size();
        try {
            float meanTime = sumTime / sz, meanWait = sumWait / sz;
            //todo : 평균값 구해서, 가까운 쪽을 따라가도록 해야함.
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
