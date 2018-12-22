package com.uki121.hguidetemplate;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Target {
    private HashMap < Integer, Boolean > targetnodes;//< targetid, state >
    private String target_name = "no name";
    private String event_type;//event_type = {trigger_type, action_type}

    public Target() {
        targetnodes = new HashMap<>();
    }
    public Target(String _eventtype) {
        Log.d("- Target", "construction");
        targetnodes = new HashMap<>();
        this.event_type = _eventtype;
    }
    public Target(List < Integer > _tid, String _event) {
        Log.d("Target-constructor", "start ~");
        targetnodes = new HashMap<>();
        this.event_type = _event;
        for (int i = 0; i < _tid.size(); ++i) {
            targetnodes.put(_tid.get(i), false);
            Log.i("Target-element", "id :" + _tid.get(i));
        }
        Log.d("Target-constructor", "~ success");
    }
    public Target(List < Integer > _tid, List < Boolean> _state, String _event) {
        this.event_type = _event;
        if (_tid.size() == _state.size()) {
            for (int i = 0; i < _tid.size(); ++i) {
                targetnodes.put(_tid.get(i), _state.get(i));
            }
        } else { Log.d("- Target-constructor", "success");}
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
        Log.d("Target-info", "start");
        Iterator < Integer > it_key = targetnodes.keySet().iterator();
        while(it_key.hasNext()) {
            int key_val = it_key.next();
            Log.i("Target_set", "key : " + key_val + ", value : " + targetnodes.get(key_val));
        }
        Log.d("Target-info", "success");
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

    public Set< Integer > getElement() {return this.targetnodes.keySet();}
    public Integer getElement(int _index) {
        List < Integer > item = new ArrayList<>(this.targetnodes.keySet());
        return item.get(_index);
    }
}
