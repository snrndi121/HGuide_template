package com.uki121.hguidetemplate;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/* todo : 우선순위 1위
   HGSensor는 각 뷰가 끝날때마다의 Touch 횟수와 Time을 뷰 별로 저장을 해야함.
   시작과 끝을 만들어줘야하는데...
   if (저 단계가 해결), 평균값 계산하고 HGS를 핸드폰 내부에 저장할 수도 있고, 서버로 넘겨줄수도 있겠지.
   2019. 01. 13
   - 일단 HGSensor가 Touch와 Time을 저장하는 용도로 사용하고 추후에 동기화하고, 데이터는 넘기는 부분은
     이 프로젝트의 목적을 벗어나는 일. 개별적으로 구현하면 되는 부분.
   - HGSensor 내부에서 Weight 값을 설정하는 부분이 있는데, 이것을 interface로 돌려야할 것 같은데
     구체적으로 어떻게 해야할지 감이 잘 오지 않음. interface WeightSensor 라고 하고 내부에.
   - 그럼 결론적으로 해야할 부분은, 각 동작을 마치면서 HGIndicator 안의 HGSensor를 업데이트 하면 되는 부분
     그리고 업데이트 되면 중앙값을 다시 조정하고
*/
public class HGSensor {
    enum SenseType {
        DESPERATE("DESPERATE", 40, 30000),
        NEED_ASSIST("NEED_ASSIST", 30, 20000),
        SELF_SUFFICEINT("SELF_SUFFICIENT", 20, 10000),
        NO_HELP("NO_HELP", -1, -1);
        //constructor
        SenseType(String _type, int _touch, int _wait) {
            this.Stype =_type;
            this.TouchCount =_touch;
            this.WaitCount =_wait;
        }
        //method
        //todo : 추후에 이 부분이 custom화 될수 있도록
        public static SenseType calculateTypeBy(int _meantouch, int _meanwait) {
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
        public void setType(String _type) { this.Stype =_type;}
        public void setTouch(int _touch) { this.TouchCount =_touch;}
        public void setWait(int _wait) { this.WaitCount =_wait;}
        //get
        public String getType() { return this.Stype;}
        public int getTouch() { return this.TouchCount;}
        public int getWait() { return this.WaitCount;}
        //var
        private String Stype;
        private int TouchCount = 0, WaitCount = 0;//sensorLevel 판단 기준
    };
    //constructor
    public HGSensor(){
        //need to be synchronized from own databases
        this.viewSenseLevel = new HashMap<>();
        this.meanSenseLevel = SenseType.NO_HELP;
    };
    //method
    //- checkState() return meanStatus
    public SenseType getSenseLevel() {
        SenseType res = SenseType.NO_HELP;
        res.setType(meanSenseLevel.getType());
        res.setTouch(meanSenseLevel.getTouch());
        res.setWait(meanSenseLevel.getWait());
        return res;
    }
    // 현재 설정된 평균 상태를 반환
    //
    //- reset() return ;
    // 설정에서 캐시값을 버리라고 하거나 초기화시 동작
    public void flushSenseValByid(int _viewid) {
        try {
            if (viewSenseLevel.containsKey(_viewid)) {
                viewSenseLevel.put(_viewid, null);
                Log.d("HGS-flush", " meanSenseLevel is updated.");
                setMeanSense();
                return;
            }
            Log.w("HGS-flush", " There is no such a view");
        } catch(Exception e) {
            Log.e("HGS-flush", e.getMessage());
        }
    }
    //todo : 다 지우는 거라서 private 아니면 일다는 protectd라고 뒀음
    protected void flushSenseAll() {
        viewSenseLevel.clear();
        meanSenseLevel = null;
    }
    //- assign() return ;
    // 사용자가 직접 입력하는 값을 받아서 설정함.
    //특정 뷰에 대하여 직접 sense값 변경
    //todo : 나중에 sentype으로 변경을 할 수 있도록 자세한 변수를 안 나오도록 할 것임
    private void assignByid(int _viewid, SenseType _src) {
        if (viewSenseLevel.containsKey(_viewid)) {
            Log.d("HGS-assign", "alreay in ");
        }
        viewSenseLevel.put(_viewid, _src);
        Log.w("HGS-assign", "first enrolled");
    }
    //
    public void updateSensor(int _viewid, int _touch, int _wait) {
        assignByid(_viewid, SenseType.calculateTypeBy(_touch, _wait));
        setMeanSense();
    }
    //set
    //각 뷰마다 설정된 sense 값을 바탕으로 평균값을 형성하여 종합적인 값을 도출함.
    public void setMeanSense() {
        List < Integer > views = new ArrayList<>(viewSenseLevel.keySet());
        //모든 뷰들에 대한 sense값 더하기(calculate the sume of each touch and wait.)
        float sumTime = 0, sumWait = 0;
        for (int i = 0; i < views.size(); ++i) {
            SenseType target = viewSenseLevel.get(views.get(i));
            if (target != null) {
                sumTime += viewSenseLevel.get(views.get(i)).getTouch();
                sumWait += viewSenseLevel.get(views.get(i)).getTouch();
            }
        }
        Log.d("sumTime", " " + sumTime);
        Log.d("sumWait", " " + sumWait);
        //평균값 계산(calculate mean)
        int sz = views.size();
        try {
            int meanTime = Math.round(sumTime) / sz, meanWait = Math.round(sumWait / sz);
            meanSenseLevel = SenseType.calculateTypeBy(meanTime, meanWait);
        } catch(Exception e) {
            Log.e("HGS", e.getMessage());
        }
    }
    //get
    public void getSenseInfoAll() {
        List < Integer > viewid = new ArrayList<>(viewSenseLevel.keySet());
        for (int i = 0; i < viewid.size(); ++i) {
            SenseType s = viewSenseLevel.get(viewid.get(i));
            Log.d("ViewId", " " + viewid.get(i));
            if (s != null) {
                Log.d("Sense-contents", " type : " + s.getType() + ", touch : " + s.getTouch() + ", wait : " + s.getWait());
            } else {
                Log.d("Sense-null", " ");
            }
        }
    }
    //var
    //viewID에 따라서 적용되는 senseType이 다르다
    //viewID 아니면 fragment 아이디에 따라서
    private HashMap < Integer, SenseType > viewSenseLevel;
    //모든 ViewID에 대한 평균값
    private SenseType meanSenseLevel;
}
