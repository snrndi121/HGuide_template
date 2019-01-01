package com.uki121.hguidetemplate;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Target {
    private HashMap < Integer, Boolean > viewState;//< targetid, state >
    private int event_name = Integer.MAX_VALUE;
    private String event_type = "no type";

    public Target() {
        viewState = new HashMap<>();
    }
    public Target(Target _src) {
        this.viewState = new HashMap<>(_src.getElement());
        this.event_type = _src.getType();
        this.event_name = _src.getName();
    }
    public Target( String _type, List < Integer > _viewid) {
        viewState = new HashMap<>();
        this.event_type =_type;
        for (int i = 0; i < _viewid.size(); ++i) {
            viewState.put(_viewid.get(i), false); }
        Log.d("Target-construction(2)", "success");
    }
    public Target(int _name, String _type, List < Integer > _viewid) {
        this.viewState = new HashMap<>();
        this.event_type =_type;
        this.event_name =_name;
        for (int i = 0; i < _viewid.size(); ++i) {
            viewState.put(_viewid.get(i), false); }
        Log.d("- Target-constructor(3)", "success");
    }
    //method
    public boolean compare(Target _dst) {
        if (event_type == _dst.getType() && event_name == _dst.getName()) {
              List < Integer > _dstid = new ArrayList<>(_dst.getViewId());
              for (int i = 0; i < _dstid.size(); ++i) {
                  int _curid = _dstid.get(i);
                  if (viewState.get(_curid) == null || viewState.get(_curid) != _dst.getStatus(_curid)){
                     return false;
                  }
              }
              return true;
        }
        return false;
    }
    //set
    public void setName(int _name) { this.event_name =_name;}
    public void setStatus(int _name, boolean _status) { viewState.put(_name, _status); }
    //get
    //print all about a information of a target
    public void getInfo() {
        Log.d("Target-info", "start");
        List < Integer > it_key = new ArrayList<>(viewState.keySet());
        for (int i = 0; i < it_key.size(); ++i) {
            int key_val = it_key.get(i);
            Log.i("- ", "key : " + key_val + ", value : " + viewState.get(key_val));
        }
        Log.d("Target-info", "success");
    }
    public HashMap <Integer, Boolean> getElement() { return this.viewState;}
    //public Set< Integer > getElement() {return this.viewID.keySet();}
    public boolean getStatusAll() {
        List < Boolean > tarState = new ArrayList<>(viewState.values());
        for (int i = 0; i < tarState.size(); ++i)
            if (!tarState.get(i)) { return false;}
        return true;
    }
    public boolean find(int _tid) { return viewState.containsKey(_tid);}
    public boolean getStatus(int _tid) { return viewState.get(_tid);}
    public Collection< Boolean > getStatus() { return viewState.values();}
    public List < Integer > getViewId() {return new ArrayList<>(this.viewState.keySet());}
    public int getName() { return this.event_name;}
    public String getType() { return this.event_type;}
    public int getSize() {return viewState.size();}
}
