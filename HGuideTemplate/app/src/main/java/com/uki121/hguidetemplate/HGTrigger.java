package com.uki121.hguidetemplate;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class HGTrigger {
    class Trigger_Set {
        final String DEFAULT_TYPE = "no type";
        Trigger_Set() { this.sibling_trigger = new ArrayList<>();}
        Trigger_Set(String _trigType) {
            sibling_trigger = new ArrayList<>();
            this.triggerType = _trigType;
        }
        Trigger_Set(String _trigType, int _sibling) {
            sibling_trigger = new ArrayList<>();
            this.triggerType = _trigType;
            this.sibling_trigger.add(_sibling);
        }
        public void add(int _newTrigger) {
            sibling_trigger.add(_newTrigger);
        }
        boolean find(int _sibling) {
            Iterator < Integer > it = sibling_trigger.iterator();
            while(it.hasNext()) {
                if (it.next() == _sibling){ return true;}
            }
            return false;
        }
        public List < Integer > getSibling() { return sibling_trigger;}
        public int getSize() { return sibling_trigger.size();}
        public String getType() { return this.triggerType;}
        public String getType(int _sibling) {
            Iterator< Integer > it = sibling_trigger.iterator();
            while(it.hasNext()) {
                if (it.next() == _sibling) {
                    return triggerType;}
            }
            return DEFAULT_TYPE;
        }
        private String triggerType;
        private List < Integer > sibling_trigger;
    };
    //
    enum TRIG_TYPE{Except, Empty_text, All_check, Scroll_bottom, Scroll_up};
    private HashMap < Integer, Target > trigTargets;//< Trigger_name, Source_views >
    private List < Trigger_Set > trigParentList;
    private static final int MAX_TOUCH_COUNT = 15;
    private static int count = 0;
    private int statistical_count = 0;//In a view //todo : more
    interface setCustom {
        boolean status_check(String[] _args);
    }
    public HGTrigger(){
        trigTargets = new HashMap<>();
        trigParentList = new ArrayList<>();
    };
    public HGTrigger(Integer _trigname, List < Integer > _src, String _trigtype) {
        trigTargets = new HashMap<>();
        trigTargets.put(_trigname, new Target(_trigtype, _src));
        trigParentList.add(new Trigger_Set(_trigtype, _trigname));
    }
    private static List < Boolean > getStatusFrom(List < Integer > tid, View _view) {
        List <Boolean> status = new ArrayList<>();
        try {
            for (int i = 0; i < tid.size(); ++i) {
                int srcID = tid.get(i);
                String srcClass = _view.findViewById(srcID).getAccessibilityClassName().toString();
                switch (srcClass) {
                    case "android.widget.CheckBox": {
                        CheckBox child_views;
                        child_views = (CheckBox)_view.findViewById(srcID);
                        status.add(child_views.isChecked());
                    }
                        break;
                    case "android.widget.EditText": {
                        EditText child_views;
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
    public boolean updateTrigger(int _trigname, View _view) {
        try {//todo
            boolean isUpdate = false;
            if (trigTargets.containsKey(_trigname)) {
                Target newTrig = trigTargets.get(_trigname);
                Target oldTrig = new Target(newTrig);
                makeTrigger(newTrig, _view);
                trigTargets.put(_trigname, newTrig);
                return !newTrig.compare(oldTrig);
            }
            Log.w("HGT-update Trigger", " no such trigger name found");
        } catch (Exception e) {
            Log.e("HGT-update Trigger", e.getMessage());
        }
        return false;
    }
    //todo : call by references test
    public boolean updateTrigger(Target _trigger, View _view) {
        Target oldTrig = new Target(_trigger);
        Target newTrig = makeTrigger(_trigger, _view);
        trigTargets.put(_trigger.getName(), newTrig);
        return !newTrig.compare(oldTrig);
    }
    public Target makeTrigger(Target _trigger, View _view) {
        Log.i("HGT","makeTrigger in on " + _trigger.getType());
        //view 상태 설정
        final List <Integer> viewID = _trigger.getViewId();
        final List <Boolean> status = new ArrayList<>(getStatusFrom(viewID, _view));
        switch (_trigger.getType()) {
            case "Except" :
                count %= MAX_TOUCH_COUNT;
            case "All_check" :
            case "Empty_text" :
                for (int i = 0; i < status.size(); ++i)
                    _trigger.setStatus(viewID.get(i), status.get(i));
                return _trigger;
            case "Scroll_up" : break;
            case "Scroll_down" : break;
            default :
                Log.e("HGT-makeTrigger", "no such trigger type.");
                break;
        }
        return null;
    }
    //HGTrigger 클래스 멤버 추가
    public void add(Target _trigger) {
        addTarget(_trigger.getName(), _trigger);
        addTrigger(_trigger.getType(), _trigger.getName());
    }
    public void add(String _trigtype, int _trigname, Target _src) {
        Log.w("HGT-add"," start ~");
        Log.d("trigtype", _trigtype);
        Log.d("trigname", "" + _trigname);
        addTarget(_trigname, _src);
        addTrigger(_trigtype, _trigname);
    }
    public void add(String _trigtype, int _trigname, List< Integer > _srcid) {
        addTarget(_trigtype, _trigname, _srcid);
        addTrigger(_trigtype, _trigname);
    }
    //< 트리거 아이디, 타겟 > 추가하기
    private void addTarget(String _trigtype, Integer _trigname, List< Integer > _srcid) { trigTargets.put(_trigname, new Target(_trigtype, _srcid)); }
    private void addTarget(Integer _trigname, Target _src) { trigTargets.put(_trigname, _src); }
    //< 트리거 타입, List 형 트리거 아이디 > 추가하기
    //@_trigType : 트리거 타입
    //@_siblingTrigger : 트리거 세트에 속한 트리거 ID
    private void addTrigger(String _trigType, int _siblingTrigger) {
        Log.w("AddTriger", " start~");
        //boolean IsParent = false;
        //parent는 있지만 child가 없다면
        for (int i = 0; i < trigParentList.size(); ++i) {
           //< parent, child >에 같은 parent가 존재 && child에 없으면
           if (trigParentList.get(i).getType() == _trigType) {
               //IsParent = true;
               if (!trigParentList.get(i).find(_siblingTrigger)) {
                   Log.d("New Child","Added!!!");
                   trigParentList.get(i).add(_siblingTrigger);
               }
               Log.d("Already Child","Here!!!");
               Log.w("AddTriger", " ~end");
               return;
           }
        }
        //parent도 없으면
        Log.d("New parent","Added!!!");
        //if (!IsParent) {
            trigParentList.add(new Trigger_Set(_trigType, _siblingTrigger));
        //}
        Log.w("AddTriger", " ~end");
    }
    public boolean find(String _trigname) {return trigTargets.containsKey(_trigname.hashCode());}
    public boolean find(int _trigname) {return trigTargets.containsKey(_trigname);}
    public Target getTrigger(int _trigname) {
        //todo
        Log.i("HGT", "GETTARGET");
        Target t = trigTargets.get(_trigname);
        if (t != null) {
            Log.d("- Target", "has elements.");
            return trigTargets.get(_trigname);
        }
        Log.w("- Target", "is null.");
        return null;
    }
    public List < Integer > getSibling(String _trigType) {
        for (int i = 0; i < trigParentList.size(); ++i) {
            if (trigParentList.get(i).getType() == _trigType) {//추가하려는 트리거 타입과 같은 타입이 있다면
                return trigParentList.get(i).getSibling();
            }
        }
        return null;
    }
    public String getType(int _trigname) { return trigTargets.get(_trigname).getType();}
    public int getCount() { return count;}
    public boolean IsMaxCount() { return (count++) % MAX_TOUCH_COUNT == 0;}
    public void downCount() { if (count > 2) {count /= 2;} else {count = 0;}}/* todo : more develop*/
    public void getInfo() {
        //1
        Log.w("TrigPair", "start ~");
        List < Integer > tid = new ArrayList<>(trigTargets.keySet());
        for (int i = 0; i < tid.size(); ++i) {
            Log.i("TrigPair-Target", " start~");
            Target tid_targt = trigTargets.get(tid.get(i));
            tid_targt.getInfo();
            Log.i("TrigPair-Target", " end~");
        }
        Log.w("TrigPair", "end ~");
        //2
        Log.w("TrigParent", " start ~");
        for (int i = 0; i < trigParentList.size(); ++i) {
            Log.d("trigParent", "type : " + trigParentList.get(i).getType());
            List < Integer > trigChild = trigParentList.get(i).getSibling();
            for (int j = 0; j < trigParentList.get(i).getSize(); ++j) {
                Log.d("trigChild", "id : " + trigChild.get(j));
            }
        }
        Log.w("TrigParent", " end ~");
    }
}
