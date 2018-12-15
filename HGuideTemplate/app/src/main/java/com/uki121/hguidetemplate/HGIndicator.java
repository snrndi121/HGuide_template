package com.uki121.hguidetemplate;

import android.util.Log;
import android.util.Pair;
import android.view.View;

import java.util.HashMap;
import java.util.List;

public class HGIndicator {
    //A?�� B��?? 발생?����?? C?�� D��?? ?��?��.
    private HGTrigger triggers;
    private HGAction actions;
    private List < Pair < String, Integer > > triggers_count;//todo
    private boolean trigger_switch = false;
    private String temp_trigger = "no trigger";
    private View baseview;

    public HGIndicator(View _baseview){
        this.triggers = new HGTrigger();
        this.actions = new HGAction();
        this.baseview = _baseview;
    };
    /*
    ver1.0
    public HGIndicator Trigger(String _trigname, List< Integer > _srcid, String _trigtype) {
        //todo : ?��?�� 체크��?? 먼�???����?? Target?�� ?��?��?����?? ?��?�� 것이 ?��?�� ?��?�� 것임.
        //1.current trigger confirm
        this.baseid = _trigname;
        //2.trigger constructor
        if (triggers == null) {
            triggers = new HGTrigger(_trigname, _srcid, _trigtype);
        } else if (triggers.find(_trigname) == null) {
            triggers.add(_trigname, _srcid, _trigtype);
        }
        //3.check its status
        triggers.checkStatus(_trigname, _trigtype, baseview);
        return this;
    }
    *//*
    ** Trigger("Trigger_custom_name", "source_targets_list", "Trigger_event")
    */
    public HGIndicator Trigger(String _trigname, List < Integer > _srcid, String _trigtype) {
        //check duplicated trig_name
        if (!triggers.find(_trigname)) {
            //1. check the state of source_target_list
            Target new_source = triggers.checkStatus(_trigtype, _srcid, baseview);
            new_source.setName(_trigname);
            //2. Make class HGTrigger which have Target made above.
            triggers.add(_trigname, new_source);
            //3. set temporary variables
            temp_trigger = _trigname;
            trigger_switch = true;

            return this;
        }
        Log.e("HGTrigger", "found duplicated trigger_name!");
        return null;
    }
    //
    public HGIndicator Action(List< Integer > _dstid, String _eventtype) {
        //check trigger state
        if (trigger_switch) { //todo : ?��리거 ?��?��?�� 존재?��?����???�� ????�� ��??분을 ???체하?�� ��??��??(maybe)
            //1. actions ?�� ?��?�� ?��리거?�� ????�� action 추�???����??
            actions.add(_dstid, _eventtype, temp_trigger);
            return this;
        }
        Log.d("HGIndicator", "trigger_switch is off now.");
        return null;
    }
    //todo
    public HGIndicator AddAction(String _trigname, List < Integer > _dstid, String _eventtype) {
        //find trigger_name
        //(1) if it exists then, add
        //(2) else, no action occurs
        return null;
    }
    public void Commit() {
        //todo : may be this is the bug point
        //get the specific trigger's state
        //(1) if true, then Commit
        if (baseview != null) {
                actions.commit(baseview, triggers.getTarget(temp_trigger));
            trigger_switch = false;
        }
        //(2) else nothing
        Log.d("HGIndicator","Commit has an error. main view is null.");
        return;

    }
}
