package com.uki121.hguidetemplate;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class HGAction {
    enum ACT_TYPE {HIGHLIGHT, FOCUS, POINTER}
    class Action {
        private ArrayList < Target > targets;//todo : relation : trigger(1) - action(*)
        private int trigger_hash_val;
        private int action_point = 0;
        protected Action() {
            this.targets = new ArrayList<>();
            this.trigger_hash_val = 0;
        }
        protected Action(List < Integer > _dest, String _actiontype, int _trihash_val) {
            this.targets = new ArrayList<>(Arrays.asList(new Target(_dest, _actiontype)));
            this.trigger_hash_val = _trihash_val;
        }
        protected void add(Target _element) { targets.add(_element);}
        protected ArrayList < Target > getTarget() { return this.targets;}
        protected List < Target > getUndo() { return action_point != targets.size()? targets.subList(action_point, targets.size() - 1) : null; }
        protected void count() {this.action_point++;}
        protected int getId() { return this.trigger_hash_val;}
        protected int getSize() { return this.targets.size();}
        protected int getLoc() { return this.action_point;}
    };
    private HashMap<Integer,  Action > mActions;
    public HGAction() {
        mActions = new HashMap<>();
    };
    /*
    public void commit(View _mainiview, Target _trigger) {
        //null check
        if (_mainiview == null || _trigger == null) {
            Log.e("HGA","There is no main view or sources.");
            if (_trigger == null) {
                Log.d("- Sources", "commit - source target is null.");
            } else {
                Log.d("- Main view", "commit - main view is null.");
            }
            Log.d("HGA", "no commit is occured.");
            return ;
        }
        Log.d("HGA","method(commit) is on");
        //1. ?��?��?�� action 리스?�� 찾아????��?��.(Find which actinos are executed)
        int trigger_id = _trigger.getName().hashCode();
        Log.d("HGA","Trigger name : " + _trigger.getName() + ", hash : " + trigger_id);
        Iterator <Target> it_actions = getUndoAction(trigger_id).iterator();
        if (!it_actions.hasNext()) {
            Log.d("HGA","source has no actions undo.");
            return;
        }
        //2. action ?��?��
        while(it_actions.hasNext()) {
            //3. source?�� 조건?��?�� ?��?��?��?�� ?��?��?�� ?��?���? 고른?��.
            //(They will consider about where to be executed)
            Iterator < Integer > src_it = _trigger.getElement().iterator();
            //String act_type = dst_it.next().getType();
            Target action = it_actions.next();
            this.mActions.get(trigger_id).count();//interaction with action_point in Action class
            while (src_it.hasNext()) {
                //The state of Target view is true, then execute
                int src_item = src_it.next();
                if (!_trigger.getStatus(src_item)) {
                    act(_mainiview, action, src_item);//?��?��?�� ?��?���? ?��?��?���? ?��?��?��?��.
                }
            }
        }
        return;
    }
    */
    public void commit(View _mainiview, Target _trigger) {
        try {
            //0. null check
            if (_mainiview == null || _trigger == null) {
                Log.e("HGA","There is no main view or sources.");
                throw new Exception();
            }
            Log.d("HGA", "method(commit) is on");

            //1. Find which actinos are executed
            int trigger_id = _trigger.getName().hashCode(); //get trigger id
            Log.d("HGA", "Trigger name : " + _trigger.getName() + ", hash : " + trigger_id);    //get action
            Iterator<Target> it_actions = getUndoAction(trigger_id).iterator(); //get action by trigger id
            if (!it_actions.hasNext()) {    //size check
                Log.e("HGA", "source has no actions matched.");
                throw new Exception();
            }
            //2. execute actions
            List < Integer > source_target = new ArrayList<>(_trigger.getElement()); //get stat of sources
            while (it_actions.hasNext()) {
                
            }
        } catch(Exception e)
        {
            Log.e("HGA-commit",e.getMessage());
        }
        return;
    }
    public void act(View _main, Target _action, int _child) {
        Log.d("HGA","Method(act) is on.");
        try {
            switch (_action.getType()) {
                case "HIGHLIGHT":
                    manageBlinkEffect((TextView) _main.findViewById(_child));
                    Log.d("Action : ", "HIGHLIGHT");
                    break;
                case "FOCUS":
                    Log.d("Action : ", "FOCUS");
                    int scroll_id = _action.getElement(0);
                    Log.i("Action-id", "" + scroll_id);
                    if (scroll_id != 0)
                        scrollToView((CheckBox) _main.findViewById(_child), (ScrollView) _main.findViewById(scroll_id), 0);
                    else throw new Exception();
                    break;
                case "POINTER":
                    Log.d("Action : ", "POINTER");
                    break;
                default:
                    Log.d("Action : ", "There is no such action name(" + _action + ")");
                    break;
            }
        } catch (Exception e) {
            Log.e("HGA-act", e.getMessage());
        }
        return ;
    }
    public void add(List < Integer > _dstid, String _actiontype, String _trigname) {
        int trig_hash_val = _trigname.hashCode();
        Log.d("\nHGA","method(add) is on.");
        //if there is an element already, then add into the back
        if (!this.mActions.containsKey(trig_hash_val)) {
                Log.d("HGA", "first actions are enrolled.");
                this.mActions.put(trig_hash_val, new Action(_dstid, _actiontype, trig_hash_val));
        } else { //add into the back
            Action new_actionsList = mActions.get(trig_hash_val);
            new_actionsList.add(new Target(_dstid, _actiontype));
            //
            Iterator <Target> it = new_actionsList.getTarget().iterator();
            Log.i("\nTarget List", "which has a trigger, '" + _trigname + "'.");
            while(it.hasNext())
            {
                it.next().getInfo();
            }
            //
            this.mActions.put(trig_hash_val, new_actionsList);
            Log.d("HGA", "new actions are added into existing action list.");
        }

    }
    public List < Target > getUndoAction(int _trigid) {
        Action temp = mActions.get(_trigid);
        return temp.getTarget();
    }
    //method
    private void manageBlinkEffect(TextView _target) {
        ObjectAnimator anim = ObjectAnimator.ofInt(_target, "backgroundColor", Color.WHITE, Color.BLUE,
                Color.WHITE);
        anim.setDuration(3000);
        anim.setEvaluator(new ArgbEvaluator());
        //anim.setRepeatMode(ValueAnimator.REVERSE);
        //anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }
    public static void scrollToView(View view, final ScrollView scrollView, int count) {
        if (view != null && view != scrollView) {
            count += view.getTop();
            scrollToView((View) view.getParent(), scrollView, count);
        } else if (scrollView != null) {
            final int finalCount = count;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, finalCount);
                }
            }, 200);
        }
    }
}
