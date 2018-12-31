package com.uki121.hguidetemplate;

import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class HGIndicator {
    private HGTrigger triggers;
    private HGAction actions;
    private List < Pair < String, Integer > > triggers_count;//todo
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
            Log.i("HGI", "TRIGGER is updated."); }
        else {
            Log.i("HGI", "TRIGGER is already enrolled. It has source state modify."); }
        //1. check the state of source_target_list
        Target new_source = triggers.setTrigger(_trigtype, _srcid, baseview);
        new_source.setName(_trigname);
        triggers.add(_trigname, new_source);
        temp_trigger = _trigname;
        return this;
    }
    public HGIndicator Action(List< Integer > _dstid, String _actiontype) {
        //check trigger state
        Log.i("HGI","ACTION(1) is on.");
        actions.add(_dstid, _actiontype, temp_trigger);
        return this;
    }
    public HGIndicator Action(String _trigname, List< Integer > _dstid, String _actiontype) {
        //check trigger state
        temp_trigger = _trigname;
        Log.i("HGI","ACTION(2) is on.");
        //todo :del
        for (int i =0; i < _dstid.size(); ++i) {
            Log.d("tid", "" + _dstid.get(i));
        }
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
        Log.i("HGI","COMMIT is on.");
        try {
            if (baseview == null) { throw new Exception();}
            final Target triggerTars = triggers.getTarget(temp_trigger);
            final String trigType = triggerTars.getType();
            final String trigName = temp_trigger;
            Log.d("TriggerType", trigType);
            if (trigType.equals(HGTrigger.TRIG_TYPE.Except.toString())) {
                Log.d("OK","IN");
                baseview.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent e) {
                        Trigger(temp_trigger, new ArrayList<Integer>(triggerTars.getElement()), trigType);
                        Target newTrigTars = triggers.getTarget(trigName);
                        if (triggers.IsMaxCount()) {
                            actions.commit(baseview, newTrigTars);
                        }
                        return true;
                    }
                });
            } else {
                Log.d("NOP","OUT");
                actions.commit(baseview, triggerTars);
            }
        } catch (Exception e) {
            Log.i("HGI","Commit has an error. main view is null.");
        }
    }
}
