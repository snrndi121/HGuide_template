package com.uki121.hguidetemplate;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class HGAction {
    enum ACT_TYPE {HIGHLIGHT, FOCUS, POINTER}
    class Action {
        private List < Target > targets;
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
        protected List < Target > getTarget() { return this.targets;}
        protected List < Target > getUndo() { return action_point != targets.size()? targets.subList(action_point, targets.size() - 1) : null; }
        protected void count() {this.action_point++;}
        protected int getId() { return this.trigger_hash_val;}
        protected int getSize() { return this.targets.size();}
        protected int getLoc() { return this.action_point;}
    };

    private HashMap<Integer,  Action > mActions; //< Hash_triggerName, Action >
    public HGAction() {
        mActions = new HashMap<>();
    };
    //Execute actions matched their trigger on main view
    //@_mainview : the main view from HGIndicator's 'baseview'
    //@_trigger : a specific source's Target
    public void commit(View _mainview, Target _trigger) {
        try {
            //0. 메인뷰와 트리거가 등록 안된 상태라면 예외발생(null check)
            if (_mainview == null || _trigger == null) {
                Log.w("HGA","There is no main view or sources.");
                throw new Exception();
            }
            Log.i("HGA", "COMMIT is on");
            //1. 트리거 이름과 연관된 액션 리스트 불러옴(Find which actions are executed)
            int trigger_id = _trigger.getName().hashCode(); //get trigger id
            //Log.d("HGA", "Trigger name : " + _trigger.getName() + ", hash : " + trigger_id);    //get action
            List <Target> action_target = new ArrayList<>(getUndoAction(trigger_id)); //get action by trigger id
            //size check
            if (action_target.size() <= 0) {
                Log.e("HGA", "source has no actions matched.");
                throw new Exception();
            }
            //2. 트리거에 따라 액션 반응 시키기
            for (int i = 0; i < action_target.size(); ++i) {
                Target action = action_target.get(i);
                act(_mainview, action, _trigger);
            }
        } catch(Exception e)
        {
            Log.e("HGA-commit",e.getMessage());
        }
        return;
    }
    //@_main : a main view of current views
    //@_action : a 'Target' which has action_type and destination target
    //@_child
    public void act(final View _main, Target _action, Target _trigger) {
        Log.i("HGA","ACT is on.");
        //0. 트리거의 상태 불러 오기(execute actions by states of sources)
        List < Boolean > trigStates = new ArrayList<>(_trigger.getStatus()); //get stat of sources
        try {
            //dst
            List < Integer > dst = new ArrayList<>(_action.getElement());
            String _actiontype = _action.getType();
            switch (_actiontype) {
                case "HIGHLIGHT":
                    for (int i = 0; i < dst.size(); ++i) {
                        if (!trigStates.get(i))
                            highlight((TextView) _main.findViewById(dst.get(i)));
                    }
                    Log.d("Action : ", "HIGHLIGHT");
                    break;
                case "FOCUS":
                    //find scrollview
                    int id = 0; boolean scrl_found = false;
                    LinearLayout root = (LinearLayout)_main;
                    View[] children = new View[root.getChildCount()];
                    for (id = 0; id < root.getChildCount(); ++id) {
                        children[id] = (View) root.getChildAt(id);
                        Log.d("Child-name", "" + children[id].getAccessibilityClassName());
                        if (children[id].getAccessibilityClassName().equals("android.widget.ScrollView")) {
                            scrl_found = true;
                            break;
                        }
                    }
                    if (!scrl_found) {
                        Log.e("HGA-act", "There is no scrollview found");
                        throw new Exception();
                    }
                    //focus
                    for (int i = 0; i< dst.size(); ++i) {
                        int curID = dst.get(i);
                        if (!trigStates.get(i))
                            scrollToView(_main.findViewById(curID), (ScrollView) children[id], 0);
                            break;
                    }
                    Log.d("Action : ", "FOCUS");
                     break;
                case "POINTER"://todo : animation is needed
                    Log.i("Action : ", "POINTER");
                    final EditText editText;
                    int tarID = Integer.MAX_VALUE;
                    for (int i = 0; i < dst.size(); ++i) {
                        int curID = dst.get(i);
                        if (!trigStates.get(i) && _main.findViewById(curID).getAccessibilityClassName().toString() == "android.widget.EditText") {
                            if (curID < tarID)
                                tarID = curID;
                        }/* todo : else if () */
                    }
                    if (tarID != Integer.MAX_VALUE) {
                        editText = (EditText) _main.findViewById(tarID);
                        editText.post(new Runnable() {
                            @Override
                            public void run() {
                                editText.setFocusableInTouchMode(true);
                                editText.requestFocus();

                                InputMethodManager imm = (InputMethodManager) _main.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(editText, 0);
                            }
                        });
                    }
                    break;
                case "POPUP":
                default:
                    Log.w("Action : ", "There is no such action name(" + _actiontype + ")");
                    break;
            }
        } catch (Exception e) {
            Log.e("HGA-act", e.getMessage());
        }
        return ;
    }
    public void add(List < Integer > _dstid, String _actiontype, String _trigname) {
        int trig_hash_val = _trigname.hashCode();
        Log.i("\nHGA","ADD is on.");
        //if there is an element already, then add into the back
        if (!this.mActions.containsKey(trig_hash_val)) {
                Log.d("HGA", "first actions are enrolled.");
                this.mActions.put(trig_hash_val, new Action(_dstid, _actiontype, trig_hash_val));
        } else { //add into the back
            Action new_actionsList = mActions.get(trig_hash_val);
            new_actionsList.add(new Target(_dstid, _actiontype));
            this.mActions.put(trig_hash_val, new_actionsList);
            Log.d("HGA", "new actions are added into existing action list.");
        }

    }
    public List < Target > getUndoAction(int _trigid) {
        Action temp = mActions.get(_trigid);
        return temp.getTarget();
    }
    //method
    private void highlight(TextView _target) {
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
