package com.uki121.hguidetemplate;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class HGAction {
    enum ACT_TYPE {HIGHLIGHT, FOCUS, POINTER}
    class Action {
        private ArrayList < Target > targets;
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
    //Execute actions matched their trigger on main view
    //@_mainview : the main view from HGIndicator's 'baseview'
    //@_trigger : a specific source's Target
    public void commit(View _mainview, Target _trigger) {
        try {
            //0. 메인뷰와 트리거가 등록 안된 상태라면 예외발생(null check)
            if (_mainview == null || _trigger == null) {
                Log.e("HGA","There is no main view or sources.");
                throw new Exception();
            }
            Log.d("HGA", "method(commit) is on");

            //1. 트리거 이름과 연관된 액션 리스트 불러옴(Find which actions are executed)
            int trigger_id = _trigger.getName().hashCode(); //get trigger id
            Log.d("HGA", "Trigger name : " + _trigger.getName() + ", hash : " + trigger_id);    //get action
            Iterator<Target> action_target = getUndoAction(trigger_id).iterator(); //get action by trigger id
            if (!action_target.hasNext()) {    //size check
                Log.e("HGA", "source has no actions matched.");
                throw new Exception();
            }
            //2. 트리거의 상태 불러 오기(execute actions by states of sources)
            List < Boolean > source_target = new ArrayList<>(_trigger.getStatus()); //get stat of sources
            /*todo*///Iterator <Integer> source_target = _trigger.getElement().iterator();
            //3. 트리거 상태에 따라 액션 반응 시키기
            while(action_target.hasNext()) {
                Target action = action_target.next();
                act(_mainview, action, source_target);
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
    public void act(final View _main, Target _action, List <Boolean> _states) {
        Log.d("HGA","Method(act) is on.");
        try {
            //dst
            List < Integer > dst = new ArrayList<>(_action.getElement());
            String _actiontype = _action.getType();
            switch (_actiontype) {
                case "HIGHLIGHT":
                    for (int i = 0; i < dst.size(); ++i) {
                        if (!_states.get(i))
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
                        Log.i("Child-name", "" + children[id].getAccessibilityClassName());
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
                    for (int index = 0; index < dst.size(); ++index) {
                        if (!_states.get(index))
                            scrollToView(_main.findViewById(dst.get(index)), (ScrollView) children[id], 0);
                            break;
                    }
                    Log.d("Action : ", "FOCUS");
                     break;
                case "POINTER":
                    Log.d("Action : ", "POINTER");
                    final EditText editText;
                    int tarID = 0;
                    for (int i = 0; i < dst.size(); ++i) {
                        Log.d("state", "" + _states.get(i));
                        Log.d("name", "" + _main.findViewById(dst.get(i)).getAccessibilityClassName().toString());
                        if (!_states.get(i) && _main.findViewById(dst.get(i)).getAccessibilityClassName().toString() == "android.widget.EditText") {
                            tarID = dst.get(i);
                            break;
                        }
                    }
                    Log.d("ID", ""+tarID);
                    if (tarID == R.id.edit_1) {
                        Log.d("ID", "A");
                    } else if (tarID == R.id.edit_2) {
                        Log.d("ID", "B");
                    } else if (tarID == R.id.edit_3) {
                        Log.d("ID", "C");
                    }
                    if (tarID != 0) {
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
                    Log.d("Action : ", "There is no such action name(" + _actiontype + ")");
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
            Log.i("\n\nTarget List", "which has a trigger, '" + _trigname + "'.");
            while(it.hasNext())
            {
                it.next().getInfo();
            }

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
