package com.uki121.hguidetemplate;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class TargetNode {
    int id;
    boolean status = false;
    public TargetNode(){};
    public TargetNode(int _id, boolean _status) {this.id = _id; this.status = _status;}
    public TargetNode(int _id) { this.id = _id;}
    public boolean getStatus() {return this.status;}
    public int getId() { return this.id;}
}

public class Target {
    private List < TargetNode > targetnodes;
    //private String tname;
    private String tevent;

    public Target(){}
    public Target(List < Integer > _tid, String _event) {
        //1. Conversion 'Integer' to 'TargetNode'
        List < TargetNode > s = new ArrayList<>();
        for (int i = 0; i < _tid.size(); ++i) {
            s.add(new TargetNode(_tid.get(i)));
        }
        this.targetnodes = s;
        this.tevent = _event;
    }
    public String getEvent() { return this.tevent;}
    public void setStatus(boolean _status) { this.tstatus = _status;}
    public boolean getStatus(int _tid) {
        r
    }
    public List < TargetNode > getElement() { return this.targetnodes;}
}
