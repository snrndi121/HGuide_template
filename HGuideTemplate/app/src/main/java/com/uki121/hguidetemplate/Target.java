package com.uki121.hguidetemplate;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class Target {
    private HashMap < Integer, Boolean > targetnodes;
    //private String tname;
    private String tevent;

    public Target(){}
    public Target(List < Integer > _tid, String _event) {
        for (int i = 0; i < _tid.size(); ++i) {
            if (targetnodes.containsKey(_tid.get(i))) {
                targetnodes.put(_tid.get(i), false);
            }
        }
        this.tevent = _event;
    }
    public String getEvent() { return this.tevent;}
    public void setStatus(int _tid, boolean _status) {
        if (targetnodes.containsKey(_tid)) {
            targetnodes.put(_tid, _status);
        } else {
            Log.e("Target add", "There is no element(" + _tid + ").");
        }
    }
    public boolean getStatus(int _tid) { return targetnodes.get(_tid);}
    public Set< Integer > getElement() { return this.targetnodes.keySet();}
}
