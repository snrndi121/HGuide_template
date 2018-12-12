package com.uki121.hguidetemplate;

import android.view.View;

public class HGIndicator {
    private HGTrigger triggers;
    private HGAction actions;
    private View baseview;

    public HGIndicator(View _baseview){ this.baseview = _baseview;};
    public HGIndicator Trigger(int _srcid, String _triactions) {
        triggers = new HGTrigger(_srcid, _triactions);
        return this;
    }
    public HGIndicator Action(int _dstid, String _whataction) {
        actions = new HGAction(_dstid, _whataction);
        return this;
    }
    public HGIndicator StatusCheck(View _target[]) {
        triggers.statusCheck(_target);
        return this;
    }
    public void Commit() {
        actions.commit(baseview, getStatus());
    }
    public boolean getStatus() {
        return triggers.getStatus();
    }
}
