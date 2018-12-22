package com.uki121.hguidetemplate;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class HGTrigger {
    enum TRIG_TYPE{Except, Empty_text, All_check, Scroll_bottom, Scroll_up};
    private HashMap < String, Target > targets;//< Trigger_name, Source_views >

    interface setCustom {
        boolean status_check(String[] _args);
    }
    public HGTrigger(){
        targets = new HashMap<>();
    };
    public HGTrigger(String _trigname, List < Integer > _src, String _trigtype) {
        targets = new HashMap<>();
        targets.put(_trigname, new Target(_src, _trigtype));
    }
    /*
    //ver1.0
    public void checkStatus(String _trigname, String _trigtype, View view) {
        Target t = targets.get(_trigname);
        Iterator< Integer > tid_iterator = t.getElement().iterator();
        //1.switch
        if (_trigtype.equalsIgnoreCase("Except")) {
            //
        } else if (_trigtype.equalsIgnoreCase("Empty_text")) {
            //
        } else if (_trigtype.equalsIgnoreCase("All_check")) {
            //1. View initializing
            int index = 0, tsize = t.getElement().size();
            CheckBox[] child_views = new CheckBox[tsize];
            while(tid_iterator.hasNext())    {
                child_views[index++] = view.findViewById(tid_iterator.next());
            }
            //2. Check status
            Target new_t = t;
            Iterator< Integer > new_it= new_t.getElement().iterator();
            for (int i = 0; i < tsize; ++i) {
                if (child_views[i].isChecked()) {
                    new_t.setStatus(new_it.next(),true);
                } else {
                    new_t.setStatus(new_it.next(),false);
                }
            }
            targets.put(_trigname, new_t);

        } else if (_trigtype.equalsIgnoreCase("Scroll_bottom")) {
            //
        } else if (_trigtype.equalsIgnoreCase("Scroll_up")) {
            //
        }
    }
    */
    public static Target checkStatus(String _trig_type, List < Integer > tid, View view) {
        Iterator < Integer > tid_iterator = tid.iterator();
        //1.switch
        if (_trig_type.equalsIgnoreCase("Except")) {
            //
        } else if (_trig_type.equalsIgnoreCase("Empty_text")) {
            //
        } else if (_trig_type.equalsIgnoreCase("All_check")) {
            Log.d("HGT","method(chekcstatus) in on 'All_check'");
            //1. View initializing
            int index = 0, tsize = tid.size();
            CheckBox[] child_views = new CheckBox[tsize];
            while(tid_iterator.hasNext())    {
                child_views[index++] = view.findViewById(tid_iterator.next());
            }
            //2. Check status
            Target checked_tar = new Target(_trig_type);
            for (int i = 0; i < tsize; ++i) {
                boolean state = child_views[i].isChecked();
                checked_tar.setStatus(tid.get(i), state);
            }
            return checked_tar;

        } else if (_trig_type.equalsIgnoreCase("Scroll_bottom")) {
            //
        } else if (_trig_type.equalsIgnoreCase("Scroll_up")) {
            //
        }
        Log.d("HGT","method(chekcstatus) has an unvalid trigger type.");
        return null;
    }
    public void setStatusArg(String _triName, String ... args) {
            //todo;
    }
    public void add(String _trigname, List< Integer > _srcid, String _trigtype) { targets.put(_trigname, new Target(_srcid, _trigtype));}
    public void add(String _trigname, Target _src) { targets.put(_trigname, _src);}
    public void replace(String _trigname, Target _src) { targets.put(_trigname, _src);}
    public boolean find(String _trigname) {return targets.containsKey(_trigname);}
    public boolean getStatusAll(String _trigname) { return this.targets.get(_trigname).getStatusAll();}
    public Target getTarget(String _trigname) {
        //todo
        Log.d("HGT", "getTarget()");
        Target t = targets.get(_trigname);
        if (t != null) {
            Log.d("- Target", "has elements.");
            return targets.get(_trigname);
        }
        Log.d("- Target", "is null.");
        return null;
    }
}
