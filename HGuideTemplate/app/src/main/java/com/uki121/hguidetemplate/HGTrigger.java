package com.uki121.hguidetemplate;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class HGTrigger {
    enum TRIG_TYPE{Except, Empty_text, All_check, Process, Scroll_bottom, Scroll_up};
    private HashMap < String, Target > targets;//< Trigger_name, Source_views >

    interface setCustom {
        boolean status_check(String[] _args);
    }
    public HGTrigger(){
        targets = new HashMap<>();
    };
    public HGTrigger(String _trigname, List < Integer > _src, String _trigtype) {
        targets = new HashMap<>();
        targets.put(_trigname, new Target(_src, _trigtype));
    }
    private static List < Boolean > getStatusFrom(List < Integer > tid, View _view) {
        List <Boolean> status = new ArrayList<>();
        try {
            for (int i = 0; i < tid.size(); ++i) {
                int srcID = tid.get(i);
                String sam = _view.findViewById(srcID).getAccessibilityClassName().toString();
                Log.d("HGT", sam);
                switch (sam) {
                    case "android.widget.CheckBox": {
                        CheckBox child_views;
                        child_views = (CheckBox)_view.findViewById(srcID);
                        status.add(child_views.isChecked());
                    }
                        break;
                    case "android.widget.EditText": {
                        EditText child_views;
                        //todo : EditText가 모두 빈값이라면 제일 위의 녀석만 false 처리, 그 뒤는 그냥 무시,
                        child_views = _view.findViewById(srcID);
                        if (child_views.getText() != null) {
                            status.add(!child_views.getText().toString().isEmpty());
                        } else {
                            status.add(false);
                        }
                    }
                        break;
                    case "android.widget.Button":{;}
                        break;
                    case "android.widget.ScrollView":{;}
                        break;
                    default:
                        Log.e("HGT-getStatusFrom", "The name of sources' class is not found.");
                        throw new Exception();
                }
            }
            return status;
        } catch(Exception e) {
            Log.e("HGT-getStatusFrom", e.getMessage());
        }
        return null;
    }
    public static Target setTrigger(String _trigtype, final List < Integer > _tid, View _view) {
        //1. View initializing
        final int tsize = _tid.size();
        //Target checked_tar = new Target(_trigtype);
        final List <Boolean> status = new ArrayList<>(getStatusFrom(_tid, _view));
        Log.i("HGT","SETTRIGGER in on " + _trigtype);
        switch (_trigtype) {
            case "Except" :
                //* Except
                //* 목적 : 제한된 시간이랑 버튼 카운트를 넘어가면 해야될 작업들을 알려주는 작업
                //* 개요 : "등록된 소스 타겟이 조건안에 실행이 안되면 목적 타겟으로 알려주는 작업"
                //* 필요한 상황
                //1. 어떤 것들을 클릭을 해야하는데 뭘 해야될지 몰라서 헤매는 경우 (노인 경우 타겟으로함)
                //2. 작업들간에 일련의 순서를 가지게 될것임. 그러면 굳이 process란 트리거가 필요없음.
                //3. 소스를 등록할 떄부터 일련의 순서를 체크하게 만들고,
                //4. 등록된 소스의 상태를 아니야 의미가 다랄.
                //5. 내가 원래 하려고 했던거는, 뭔가의 작업이 필요한 작업들을 눌리는게 아니라 아무 액션도 안 일어
                //나는 녀석들을 클릭하고 잇는 상황에서 다음 상태들을 알려주는 의미였음.
                //그런데 판단해야될거는, 스크롤 이벤트를 할 수도 있고, 그렇기 때문에, 스크롤 이벤트와 이부분을
                //차이를 둬야함. 그래서 scroll 이벤트는 무시하되, 그외 엉뚱한 이벤트가 발생할 경우 다음 할 부분을
                //알려줘야함.
                //그럼, 일단 각 state들에 대한 상태를 파악하는 일이 필요할것
                //그 다음 터치이벤트가 스크롤인지 아니면 의미없는 이벤트인지 확인하는 작업이 필요함.
                //이 부분이 꽤 복잡할 듯
                //그럼 터치 이벤트가 발생할 때마다, 각각의 status 이벤트들이 재정의가 되야함.
                //소스를 에딧 테스트로 해놓은 상황에서
                //빈값 유무와 터치 유무
                //빈갑 o & 터치 o -> false;
                //빈값 o & 터치 x -> false;
                //빈값 x & 터치 o -> true;
                //빈값 x & 터치 x -> true;
                //빈값이 유지되는 상황에서 고쳐지지 않으면(status 체크가 매번 일어남, 초로 하게 되면 너무
                // 커짐. 그래서 터치 이벤트가 발생하면, status 체크를 하는 방식으로 바꿔야함)
                //타겟리스트가 EditText라면 비어있는 것을 감지할거고, 지정된 타겟의 상단을 적으라고 알려주면돼
                //타겟리스트가 EditText가 가득차있었는데, 그러면 다음 눌려야할 곳을 알려주면 그만 아닌가.\
                //그런데, 후자를 실행함에 있어서 꼬일 염려는 없을까?
                //액션을 하는데 있어서, Except라면 좀 다른식으로 boolean을 매겨야함.
                //액션과 트티거는 별개임.
                //터치이벤트가 동작을 하고 있음.
                //Except = process 이름이 될 수도
                //일정 순서대로 진행해야되는데 이를 src에 넣어두고 그렇게 행동을 하지 않으면 가리키게 함.
                /*
                _view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int count = 0;
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN :
                            case MotionEvent.ACTION_MOVE :
                            case MotionEvent.ACTION_UP :
                                for (int i = 0; i < tsize; ++i) {
                                    checked_tar.setStatus(_tid.get(i), status.get(i));
                                }
                                count ++;
                                break;
                            default:
                                Log.e("HGT-except", "no such motion event found");
                                break;
                        }
                        if (count % 3 == 0) {
                            //do Action
                        }
                        return false;
                    }
                });

                return checked_tar;
                */
            case "All_check" :
            case "Empty_text" :
                Target checked_tar = new Target(_trigtype);
                for (int i = 0; i < tsize; ++i) {
                    Log.i("HGT-setTrigger", "tid : " + _tid.get(i));
                    checked_tar.setStatus(_tid.get(i), status.get(i));
                }
                checked_tar.getInfo();
                return checked_tar;
            case "Process" :
                //각각의 상태들을 받아와서 어떤것부터 실행할지 알려주는 작업
                break;
            case "Scroll_up" :
                break;
            case "Scroll_down" :
                break;
            default :
                Log.e("HGT-setTrigger", "no such trigger type.");
                break;
        }
        return null;
    }
    public void add(String _trigname, List< Integer > _srcid, String _trigtype) { targets.put(_trigname, new Target(_srcid, _trigtype));}
    public void add(String _trigname, Target _src) { targets.put(_trigname, _src);}
    public boolean find(String _trigname) {return targets.containsKey(_trigname);}
    public boolean getStatusAll(String _trigname) { return this.targets.get(_trigname).getStatusAll();}
    public Target getTarget(String _trigname) {
        //todo
        Log.i("HGT", "GETTARGET");
        Target t = targets.get(_trigname);
        if (t != null) {
            Log.d("- Target", "has elements.");
            return targets.get(_trigname);
        }
        Log.w("- Target", "is null.");
        return null;
    }

}
