package com.uki121.hguidetemplate;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class HGTrigger {
    enum TRIG_TYPE{Except, Empty_text, All_check, Scroll_bottom, Scroll_up};
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
    public static List < Boolean > getStatusFrom(List < Integer > tid, View _view) {
        Iterator <Integer> tid_iterator = tid.iterator();
        int index = 0, tsize = tid.size();
        List <Boolean> status = new ArrayList<>();
        try {
            while (tid_iterator.hasNext()) {
                switch (_view.findViewById(tid_iterator.next()).getAccessibilityClassName().toString()) {
                    case "android.widget.CheckBox": {
                        CheckBox[] child_views = new CheckBox[tsize];
                        while (tid_iterator.hasNext()) {
                            child_views[index++] = _view.findViewById(tid_iterator.next());
                        }
                        for (int i = 0; i < tsize; ++i) {
                            status.add(child_views[i].isChecked());
                        }
                    }
                        break;
                    case "android.widget.EditText": {
                        EditText[] child_views = new EditText[tsize];
                        while (tid_iterator.hasNext()) {
                            child_views[index++] = _view.findViewById(tid_iterator.next());
                        }
                        for (int i = 0; i < tsize; ++i) {
                            status.add(!child_views[i].getText().toString().isEmpty());
                        }
                    }
                        break;
                    case "android.widget.Button":{}
                        break;
                    case "android.widget.ScrollView":{}
                        break;
                    default: {
                        Log.e("HGT-getStatusFrom", "The name of sources' class is not found.");
                        throw new Exception();
                    }
                }
            }
            return status;
        } catch(Exception e) {
            Log.e("HGT-getStatusFrom", e.getMessage());
        }
        return null;
    }
    public static Target checkStatus(String _trig_type, List < Integer > tid, View view) {
        //1. View initializing
        Iterator < Integer > tid_iterator = tid.iterator();
        int index = 0, tsize = tid.size();
        //1.switch
        //어떤 프로세스 단계를 실행할 때 사용가능
        List <Boolean> status = new ArrayList<>(getStatusFrom(tid, view));
        if (_trig_type.equalsIgnoreCase("Except")) {
            Log.d("HGT","method(chekcstatus) in on 'Except'");
            //todo:time이랑 button 카운터가 필요해질것임
            //일단 타겟을 분류하는 작업이 이뤄짐
            //타겟이 EditText면 비어있는지 확인하고
            //타겟이 CheckBox면 비어있는지 확인
        } else if (_trig_type.equalsIgnoreCase("Empty_text")) {
            Log.d("HGT","method(chekcstatus) in on 'Empty_text'");
            Target checked_tar = new Target(_trig_type);
            for (int i = 0; i < tsize; ++i) {
                checked_tar.setStatus(tid.get(i), status.get(i));
            }
            return checked_tar;
        } else if (_trig_type.equalsIgnoreCase("All_check")) {
            Log.d("HGT","method(chekcstatus) in on 'All_check'");
            Target checked_tar = new Target(_trig_type);
            for (int i = 0; i < tsize; ++i) {
                checked_tar.setStatus(tid.get(i), status.get(i));
            }
            return checked_tar;

        } else if (_trig_type.equalsIgnoreCase("Scroll_bottom")) {
            //todo : usless
        } else if (_trig_type.equalsIgnoreCase("Scroll_up")) {
            //todo : usless
        }
        Log.d("HGT","method(chekcstatus) has an unvalid trigger type.");
        return null;
    }
    public void add(String _trigname, List< Integer > _srcid, String _trigtype) { targets.put(_trigname, new Target(_srcid, _trigtype));}
    public void add(String _trigname, Target _src) { targets.put(_trigname, _src);}
    public boolean find(String _trigname) {return targets.containsKey(_trigname);}
    public boolean getStatusAll(String _trigname) { return this.targets.get(_trigname).getStatusAll();}
    public Target getTarget(String _trigname) {
        //todo
        Log.d("HGT", "getTarget()");
        Target t = targets.get(_trigname);
        if (t != null) {
            Log.d("- Target", "has elements.");
            return targets.get(_trigname);
        }
        Log.d("- Target", "is null.");
        return null;
    }
}
