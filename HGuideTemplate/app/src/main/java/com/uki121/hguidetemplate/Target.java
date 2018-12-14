package com.uki121.hguidetemplate;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class Target {
    private HashMap < Integer, Boolean > targetnodes;
    //private String tname;
    private String trigger_type;

    public Target(){}
    public Target(String _ttype) { this.trigger_type = _ttype;}
    public Target(List < Integer > _tid, String _event) {
        for (int i = 0; i < _tid.size(); ++i) {
            if (targetnodes.containsKey(_tid.get(i))) {
                targetnodes.put(_tid.get(i), false);
            }
        }
        this.trigger_type = _event;
    }
    public Target(List < Integer > _tid, List < Boolean> _tstate, String _event) {
        this.trigger_type = _event;
        if (_tid.size() == _tstate.size()) {
            for (int i = 0; i < _tid.size(); ++i) {
                targetnodes.put(_tid.get(i), _tstate.get(i));
            }
        } else { Log.e("Target - constructor", "(2)");}
    }
    public String getType() { return this.trigger_type;}
    public void setStatus(int _tid, boolean _status) {
       // if (targetnodes.containsKey(_tid)) {
            targetnodes.put(_tid, _status);
       // } else {
       //     Log.e("Target add", "There is no element(" + _tid + ").");
       // }
    }
    //print all about a information of a target
    public void getInfo() {
        //todo;
    }
    public boolean getStatus(int _tid) { return targetnodes.get(_tid);}
    public Set< Integer > getElement() { return this.targetnodes.keySet();}
}
