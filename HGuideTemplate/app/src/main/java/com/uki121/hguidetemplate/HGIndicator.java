package com.uki121.hguidetemplate;

import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import java.util.List;
import java.util.logging.Handler;

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
    public HGIndicator Trigger(String _trigname, List < Integer > _srcid, String _trigtype) {
        //check duplicated trig_name
        if (!triggers.find(_trigname)) {
            Log.d("HGI", "Trigger is updated.");
        } else {
            Log.d("HGI", "method(Trigger) is already enrolled.");
            Log.d("HGI", "method(Trigger) has source state modify.");
            //return null;
        }
        //1. check the state of source_target_list
        Target new_source = triggers.setTrigger(_trigtype, _srcid, baseview);
        new_source.setName(_trigname);
        triggers.add(_trigname, new_source);
        temp_trigger = _trigname;
        return this;
    }
    //@action_range :
    public HGIndicator Action(List< Integer > _dstid, String _actiontype) {//, String action_range) {
        //check trigger state
        Log.d("HGI","Action is on.");
        Log.d("HGI", "destination size : " + _dstid.size());
        actions.add(_dstid, _actiontype, temp_trigger);
        return this;
    }
    //@action_range :
    public HGIndicator Action(String _trigname, List< Integer > _dstid, String _actiontype) {//, String action_range) {
        //check trigger state
        temp_trigger = _trigname;
        Log.d("HGI","Action is on.");
        Log.d("HGI", "destination size : " + _dstid.size());
        actions.add(_dstid, _actiontype, temp_trigger);
        return this;
    }
    public HGIndicator AddAction(String _trigname, List < Integer > _dstid, String _actiontype) {
        return Action(_trigname, _dstid, _actiontype);
    }
    public HGIndicator AddAction(List < Integer > _dstid, String _actiontype) {
        return Action(_dstid, _actiontype);
    }
    public void Commit() {
        try {
            if (baseview == null) { throw new Exception();}
            Log.d("HGI","method(Commit) is on.");
            actions.commit(baseview, triggers.getTarget(temp_trigger));
        } catch (Exception e) { //(2) else nothing
            Log.e("HGI-commit","method(Commit) has an error. main view is null.");
        }
        return;
    }

}
