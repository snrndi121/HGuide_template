package com.uki121.hguidetemplate;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Target {
    private HashMap < Integer, Boolean > targetnodes;//< targetid, state >
    private String target_name = "no name";
    private String event_type;

    public Target() {
        targetnodes = new HashMap<>();
    }
    public Target(String _eventtype) {
        //Log.d("Target-construction(1)", "start ~");
        Log.d("Target-construction(1)", "success");
        targetnodes = new HashMap<>();
        this.event_type = _eventtype;
        //Log.d("Target-constructor(1)", "~ success");
    }
    public Target(List < Integer > _tid, String _event) {
        //Log.d("Target-constructor(2)", "start ~");
        Log.d("Target-construction(2)", "success");
        targetnodes = new HashMap<>();
        this.event_type = _event;
        for (int i = 0; i < _tid.size(); ++i) {
            targetnodes.put(_tid.get(i), false);
            /*todo : delete it*///Log.i("Target-element", "id :" + _tid.get(i));
        }
        //Log.d("Target-constructor(2)", "~ success");
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
        List < Integer > it_key = new ArrayList<>(targetnodes.keySet());
        for (int i = 0; i < it_key.size(); ++i) {
            int key_val = it_key.get(i);
            Log.i("Target_getInfo", "key : " + key_val + ", value : " + targetnodes.get(key_val));
        }
        Log.d("Target-info", "success");
        /*
        Log.d("Target-info2", "start");
        Set <Map.Entry< Integer, Boolean > > it2_key = targetnodes.entrySet();
        Iterator <Map.Entry<Integer, Boolean > > itr = it2_key.iterator();
        while(itr.hasNext()) {
            Map.Entry < Integer, Boolean > e= (Map.Entry <Integer, Boolean>) itr.next();
            Log.i("Target_getInfo", "key : " + e.getKey() + ", value : " + e.getValue());
        }
        Log.d("Target-info2", "success");
        */
    }
    public String getName() { return this.target_name;}
    public String getType() { return this.event_type;}
    public boolean getStatusAll() {
        List < Boolean > tarState = new ArrayList<>(targetnodes.values());
        for (int i = 0; i < tarState.size(); ++i)
            if (!tarState.get(i)) { return false;}
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
