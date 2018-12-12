package com.uki121.hguidetemplate;

import android.util.Log;
import android.view.View;

class HGTrigger {
    private int tid;
    private String tname;
    private boolean tstatus;

    public HGTrigger(){};
    public HGTrigger(int _srctarget, String _whattrigger) {
        this.tid = _srctarget;
        this.tname = _whattrigger;
    }
    public void statusCheck(View[]_views) {
        //todo : set status algorithm
        if (_views[0].getClass().getName().equalsIgnoreCase("android.widget.CheckBox")) {

            this.tstatus = true;
        } else if (_views[0].getClass().getName().equalsIgnoreCase("android.widget.EditText")) {
            this.tstatus = true;
        }


    }
    public void setStatus(boolean _status) {this.tstatus = _status;}
    public boolean getStatus() { return this.tstatus;}

}
