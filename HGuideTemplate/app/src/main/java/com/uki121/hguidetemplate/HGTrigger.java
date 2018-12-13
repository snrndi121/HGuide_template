package com.uki121.hguidetemplate;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class HGTrigger {
    enum TRIG_TYPE{Except, Empty_text, All_check, Scroll_bottom, Scroll_up};
    private HashMap< String, Target > targets;

    interface setCustom {
        boolean status_check(String[] _args);
    }
    public HGTrigger(){};
    public HGTrigger(String _trigname, List < Integer > _src, String _trigtype) {
        //statuc check_first -> target에 넣기
        targets.put(_trigname, new Target(_src, _trigtype));
    }
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
    public void setStatusArg(String _triName, String ... args) {

    }
    /*
    private void statusCheck(View[]_views) {
        //todo : set status algorithm
        if ( )
        if (_views[0].getClass().getName().equalsIgnoreCase("android.widget.CheckBox")) {

            this.tstatus = true;
        } else if (_views[0].getClass().getName().equalsIgnoreCase("android.widget.EditText")) {
            this.tstatus = true;
        } else if (_views[0].getClass().getName().equalsIgnoreCase("android.widget.ScrollView")) {
            this.tstatus = true;
        }
    }
    */
    public void add(String _trigname, List< Integer > _srcid, String _triactions) { targets.put(_trigname, new Target(_srcid, _triactions));}
    public Target find(String _trigname) {return targets.get(_trigname);}
    public Target getStatus(String _trigname) {return targets.get(_trigname);}
}
