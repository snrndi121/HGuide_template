package com.uki121.hguidetemplate;

import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class HGIndicator {
    private HGTrigger mtrigger;
    private HGAction maction;
    private List < Pair < String, Integer > > mtrigger_count;//todo
    private int temp_trigger = Integer.MAX_VALUE;  //hash value of the current trigger_name
    private View baseview;

    public HGIndicator(View _baseview){
        this.mtrigger = new HGTrigger();
        this.maction = new HGAction();
        this.baseview = _baseview;
    };
    public HGIndicator Trigger(String _trigname, List < Integer > _srcid, String _trigtype) {
        Target src = new Target(_trigname.hashCode(), _trigtype, _srcid);
        //check duplicated trig_name
        if (!mtrigger.find(_trigname)) {//처음 들어온 값이라면
            Log.i("HGI", "TRIGGER is updated.");
            Target newTrigger = mtrigger.makeTrigger(src, baseview);
            mtrigger.add(newTrigger);
        }
        else {//이미 존재하는 트리거의 상태만 갱신
            Log.i("HGI", "TRIGGER is already enrolled. It has source state modify.");
            mtrigger.updateTrigger(src, baseview);
        }
        temp_trigger = src.getName();
        return this;
    }
    //임시 트리거 변수에 특정 액션 추가
    public HGIndicator Action(List< Integer > _dstid, String _actiontype) {
        //check trigger state
        Log.i("HGI","ACTION(1) is on.");
        maction.add(_actiontype, temp_trigger, _dstid);
        return this;
    }
    //특정한 트리거에 액션을 추가
    public HGIndicator Action(String _trigname, List< Integer > _dstid, String _actiontype) {
        //check trigger state
        //temp_trigger = _trigname.hashCode();
        Log.i("HGI","ACTION(2) is on.");
        maction.add(_actiontype, _trigname.hashCode(), _dstid);
        return this;
    }
    public HGIndicator AddAction(String _trigname, List < Integer > _dstid, String _actiontype) {
        return Action(_trigname, _dstid, _actiontype);
    }
    public HGIndicator AddAction(List < Integer > _dstid, String _actiontype) {
        return Action(_dstid, _actiontype);
    }
    public void Commit() {
        Log.i("HGI","COMMIT is on.");
        try {
            //예외 처리
            if (baseview == null) { throw new Exception();}
            //액션에 필요한 트리거 요소 선언
            int trigName = temp_trigger;
            final Target tt;
            final Target trigger = mtrigger.getTrigger(trigName);
            String trigType = trigger.getType();
            Log.d("TriggerType", trigType);
            //case1_트리거 타입 : EXCEPT
            if (trigType.equals(HGTrigger.TRIG_TYPE.Except.toString())) {
                Log.d("OK","IN");
                //EXCEPT 판단을 할 터치리스너 등록
                baseview.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent e) {
                        //2. 트리거 정보 불러오기
                        //2-1. 현재 트리거 정보 가져오기
                        String curType = HGTrigger.TRIG_TYPE.Except.toString();
                        //2-2. 실행되어야할 curType의 트리거 가져오기
                        List < Integer > curTrigs = mtrigger.getSibling(curType);
                        //2-3 temp_trigger의 트리거 타입 가져오기
                        String oldType = mtrigger.getType(temp_trigger);
                        //3.현재 트리거와 임시 트리거 타입 비교
                        Log.d("curType/oldType", curType + "/ " + oldType);
                        if (curType != oldType) {
                            Log.w("Trigger state"," changed");
                        } else {
                            Log.w("Trigger state"," same before");
                        }
                        Log.d("Trigger silbing", " size : " + curTrigs.size());

                        //4. 현재 타입에 맞는 트리거 동작
                        for (int i = 0; i < curTrigs.size(); ++i) {
                               //이전에 기록된 트리거 정보 가져오기
                               int tid = curTrigs.get(i);
                               temp_trigger = tid;
                               boolean isUpdate = mtrigger.updateTrigger(tid, baseview);/*ver1.0*///Target t = mtrigger.getTrigger(curTrigs.get(i)); mtrigger.updateTrigger(t, baseview);
                                Log.w("update ", " >> " + isUpdate);
                               //업데이트가 생겼다면 카운트 조정
                               if (isUpdate) {
                                  mtrigger.downCount();
                               }
                               //업데이트가 없다면 count를 올려간다.
                               ///1. Commit 동작 condition : 특정 횟수 이상일 때 동작
                               else if (mtrigger.IsMaxCount()) {
                                  //action 클래스에 해당 트리거를 던져준다.
                                  maction.commit(baseview, mtrigger.getTrigger(tid));
                               }
                         }
                        Log.d("HGI-Touch", "" + mtrigger.getCount());
                        return true;
                    }
                });
            } else { //trigger_type : remain
                  maction.commit(baseview, trigger);
            }
        } catch (Exception e) {
            Log.i("HGI","Commit has an error. main view is null.");
        }
    }
}
