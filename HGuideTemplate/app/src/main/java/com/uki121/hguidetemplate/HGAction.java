package com.uki121.hguidetemplate;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    public void commit(View _main, Target _src) {
        //null check
        if (_main == null || _src == null) {
            Log.e("HGA","There is no main view or sources.");
            if (_src == null) {
                Log.d("- Sources", "commit - source target is null.");
            } else {
                Log.d("- Main view", "commit - main view is null.");
            }
            Log.d("HGA", "no commit is occured.");
            return ;
        }
        Log.d("HGA","method(commit) is on");
        //1. 실행할 action 리스트 찾아와야함.(Find which actinos are executed)
        int src_id = _src.getName().hashCode();
        Log.d("HGA","Trigger name : " + _src.getName() + ", hash : " + src_id);
        //Action dst_act = mActions.get(src_id);
        //Iterator <Target> dst_it = dst_act.getTarget().iterator();
        Iterator <Target> dst_it = getUndoAction(src_id).iterator();
        if (!dst_it.hasNext()) {
            Log.d("HGA","source has no actions undo.");
            return;
        }
        //2. action 실행
        while(dst_it.hasNext()) {
            //3. source의 조건들을 확인해서 실행할 장소를 고른다.
            //(They will consider about where to be executed)
            Iterator < Integer > src_it = _src.getElement().iterator();
            String act_type = dst_it.next().getType();
            this.mActions.get(src_id).count();//interaction with action_point in Action class
            while (src_it.hasNext()) {
                //The state of Target view is true, then execute
                int child_id = src_it.next();
                if (!_src.getStatus(child_id)) {
                    act(_main, act_type, child_id);
                }
            }
        }
        return;
    }
    public void act(View _main, String _action, int _child) {
        Log.d("HGA","Method(act) is on.");
        switch(_action) {
            case "HIGHLIGHT" :
                manageBlinkEffect((TextView) _main.findViewById(_child));
                Log.d("Action : ", "HIGHLIGHT");
                break;
            case "FOCUS" :
                scrollToView(_main, (ScrollView) _main.findViewById(_child), 0);
                Log.d("Action : ", "FOCUS");
                break;
            case "POINTER" :
                Log.d("Action : ", "POINTER");
                break;
            default:
                Log.d("Action : ", "There is no such action name(" + _action + ")");
                break;
        }
        return ;
    }
    public void add(List < Integer > _dstid, String _actiontype, String _trigname) {
        int trig_hash_val = _trigname.hashCode();
        Log.d("HGA","method(add) is on.");
        Log.d("- trigname",_trigname + ", hash : " + trig_hash_val);
        this.mActions.put(trig_hash_val, new Action(_dstid, _actiontype, trig_hash_val));
    }
    public List < Target > getUndoAction(int _trigid) {
        Action temp = mActions.get(_trigid);
        //return temp.getUndo();
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
