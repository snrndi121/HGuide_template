package com.uki121.hguidetemplate;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Target {
    private HashMap < Integer, Boolean > targetnodes;//< targetid, state >
    private String target_name = "no name";
    private String event_type;//event_type = {trigger_type, action_type}

    public Target(){}
    public Target(String _ttype) { this.event_type = _ttype;}
    public Target(List < Integer > _tid, String _event) {
        for (int i = 0; i < _tid.size(); ++i) {
            if (targetnodes.containsKey(_tid.get(i))) {
                targetnodes.put(_tid.get(i), false);
            }
        }
        this.event_type = _event;
    }
    public Target(List < Integer > _tid, List < Boolean> _tstate, String _event) {
        this.event_type = _event;
        if (_tid.size() == _tstate.size()) {
            for (int i = 0; i < _tid.size(); ++i) {
                targetnodes.put(_tid.get(i), _tstate.get(i));
            }
        } else { Log.e("Target - constructor", "(2)");}
    }
    public void setName(String _targetname) { this.target_name = _targetname;}
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
    public String getName() { return this.target_name;}
    public String getType() { return this.event_type;}
    public boolean getStatusAll() {
        Iterator it = targetnodes.values().iterator();
        while(it.hasNext())
            if (!(Boolean) it.next()) { return false;}
        return true;
    }
    public Collection< Boolean > getStatus() { return targetnodes.values();}
    public boolean getStatus(int _tid) { return targetnodes.get(_tid);}
    public Set< Integer > getElement() { return this.targetnodes.keySet();}
}
