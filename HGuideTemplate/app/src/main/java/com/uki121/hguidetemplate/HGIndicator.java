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
    //
    public HGIndicator Action(List< Integer > _dstid, String _eventtype) {

        return this;
    }
    public void Commit() {
        //triggers로부터 status가 true인 녀석들만 확인해서 actions의 commit 호출하면 될것
    }
}
