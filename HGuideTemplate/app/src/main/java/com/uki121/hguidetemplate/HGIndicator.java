package com.uki121.hguidetemplate;

import android.util.Log;
import android.util.Pair;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HGIndicator {
    private HGTrigger triggers;
    private HGAction actions;
    private List < Pair < String, Integer > > triggers_count;//todo
    //private boolean trigger_switch = true;
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
    /* ver1.1
    public HGIndicator Trigger(String _trigname, List < Integer > _srcid, String _trigtype) {
        //check duplicated trig_name
        try {
            //1. check the state of source_target_list
            Target new_source = triggers.checkStatus(_trigtype, _srcid, baseview);
            new_source.setName(_trigname);
            if (!triggers.find(_trigname)) {
                Log.d("HGI", "Trigger is updated.");
                //2. Make class HGTrigger which have Target made above.
                triggers.add(_trigname, new_source);
                //3. set temporary variables
                temp_trigger = _trigname;
                return this;
            } else {
                Log.d("HGI","method(Trigger) is already enrolled.");
                //switch its state
                triggers.replace(_trigname, new_source);
                Log.d("HGI","method(Trigger) has source state modify.");
                return this;
            }
            //todo : 이미 있으면 그냥 연결된 action만 실행시킨다.
        } catch(Exception e) {
            Log.e("HGI", e.getMessage());
        }
        return null;
    }*/
    public HGIndicator Trigger(String _trigname, List < Integer > _srcid, String _trigtype) {
        //check duplicated trig_name
        if (!triggers.find(_trigname)) {
            Log.d("HGI", "Trigger is updated.");
        } else {
            Log.d("HGI","method(Trigger) is already enrolled.");
            Log.d("HGI","method(Trigger) has source state modify.");
        }
        //1. check the state of source_target_list
        Target new_source = triggers.checkStatus(_trigtype, _srcid, baseview);
        new_source.setName(_trigname);
        triggers.add(_trigname, new_source);
        temp_trigger = _trigname;
        return this;
    }
    //
    public HGIndicator Action(List< Integer > _dstid, String _actiontype) {
        //check trigger state
        Log.d("HGI","Action is on.");
        Log.d("HGI", "destination size : " + _dstid.size());
        actions.add(_dstid, _actiontype, temp_trigger);
        return this;
    }
    //todo
    public HGIndicator AddAction(String _trigname, List < Integer > _dstid, String _actiontype) {
        //find trigger_name
        //(1) if it exists then, add
        //(2) else, no action occurs
        return null;
    }
    /*
    public HGIndicator AddAction(List < Integer > _dstid, String _actiontype) {
        Log.d("HGI","AddAction is on.");
        Log.d("HGI", "destination size : " + _dstid.size());
        actions.add(_dstid, _actiontype, temp_trigger);
        return this;
    }*/
    public HGIndicator AddAction(List < Integer > _dstid, String _actiontype) {
        return Action(_dstid, _actiontype);
    }
    public void Commit() {
        //todo : may be this is the bug point
        //get the specific trigger's state
        //(1) if true, then Commit
        try {
            if (baseview == null) { throw new Exception();}
            Log.d("HGI","method(Commit) is on.");
            actions.commit(baseview, triggers.getTarget(temp_trigger));
            //trigger_switch = false;
        } catch (Exception e) { //(2) else nothing
            Log.e("HGI-commit","method(Commit) has an error. main view is null.");
        }
        return;
    }
}
