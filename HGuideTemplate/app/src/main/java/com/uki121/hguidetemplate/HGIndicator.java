package com.uki121.hguidetemplate;

import android.view.View;

import java.util.List;

public class HGIndicator {
    //A에 B가 발생하면 C에 D를 해라.
    private HGTrigger triggers;
    private HGAction actions;
    private View baseview;

    public HGIndicator(View _baseview){
        this.triggers = new HGTrigger();
        this.actions = new HGAction();
        this.baseview = _baseview;
    };
    /*
    ver1.0
    public HGIndicator Trigger(String _trigname, List< Integer > _srcid, String _trigtype) {
        //todo : 상태 체크를 먼저하고 Target을 정하도록 하는 것이 훨씬 나을 것임.
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
        //1. check the state of source_target_list
        Target new_source = triggers.checkStatus(_trigtype, _srcid, baseview);
        //2. Make class HGTrigger which have Target made above.
        triggers.add(_trigname, new_source);
        return this;
    }
    //
    public HGIndicator Action(List< Integer > _dstid, String _eventtype) {

        return this;
    }
    public void Commit() {
        //triggers로부터 status가 true인 녀석들만 확인해서 actions의 commit 호출하면 될것
    }
}
