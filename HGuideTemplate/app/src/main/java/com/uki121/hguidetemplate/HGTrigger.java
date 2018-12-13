package com.uki121.hguidetemplate;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

class HGTrigger {
    enum TRIG_TYPE{Except, Empty_text, All_check, Scroll_bottom, Scroll_up};
    private HashMap< String, Target > targets;

    interface setCustom {
        boolean status_check(String[] _args);
    }
    public HGTrigger(){};
    public HGTrigger(String _trigname, List < Integer > _src, String _trigtype) {
        targets.put(_trigname, new Target(_src, _trigtype));
    }
    public void checkStatus(String _trigname, String _trigtype, View view) {
        Target src = targets.get(_trigname);
        List < Integer > src_id = src.getElement();
        if (_trigtype.equalsIgnoreCase("Except")) {
            //
        } else if (_trigtype.equalsIgnoreCase("Empty_text")) {
            //
        } else if (_trigtype.equalsIgnoreCase("All_check")) {
            //1. View initializing
            int src_size = src_id.size();
            CheckBox[] src_view = new CheckBox[src_size];
            for (int i = 0; src_id.size() < i; ++i) {
                src_view[i] = view.findViewById(src_id.get(i));
            }
            //2. Check status
            int checkboxFlag = 0;
            for (int i = 0; i < src_size; ++i) {
                checkboxFlag = src_view[i].isChecked() ? checkboxFlag + (int) Math.pow(2, i) : checkboxFlag;
            }
            else {
                //find the point where to hight and move
                TextView[] target = {textViewTos1, textViewTos2, textViewTos3};
                CheckBox[] target_box = {checkTos1, checkTos2, checkTos3};
                //operation_highlight
                for (int i = 0; i < target.length; ++i) {
                    //highlight
                    if (!target_box[i].isChecked()) {
                        manageBlinkEffect(target[i]);
                    }
                }
            }
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
    public boolean getStatus(String _trigname) {return targets.get(_trigname).getStatus();}
}
