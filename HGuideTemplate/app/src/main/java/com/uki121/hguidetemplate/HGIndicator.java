package com.uki121.hguidetemplate;

import android.view.View;

import java.util.List;

public class HGIndicator {
    //A에 B가 발생하면 C에 D를 해라.
    private HGTrigger triggers;
    private HGAction actions;
    private String baseid;
    private View baseview;

    public HGIndicator(View _baseview){ this.baseview = _baseview;};
    public HGIndicator Trigger(String _trigname, List< Integer > _srcid, String _trigtype) {
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
    //
    public HGIndicator Action(List< Integer > _dstid, String _eventtype) {
        if (actions == null) {
            actions = new HGAction(_dstid, _eventtype);
        }
        return this;
    }
    public void Commit() {
        actions.commit(baseview, getStatus());
    }
}
