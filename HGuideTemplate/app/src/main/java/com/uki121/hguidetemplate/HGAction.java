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
        private int trigger_name;
        private int action_point = 0;
        protected Action() {
            this.targets = new ArrayList<>();
            this.trigger_name = 0;
        }
        protected Action(String _actiontype, int _trigname, List < Integer > _dest) {
            this.targets = new ArrayList<>(Arrays.asList(new Target(_actiontype, _dest)));
            this.trigger_name = _trigname;
        }
        protected void add(Target _element) { targets.add(_element);}
        protected List < Target > getTarget() { return this.targets;}
        protected List < Target > getUndo() { return action_point != targets.size()? targets.subList(action_point, targets.size() - 1) : null; }
        protected void count() {this.action_point++;}
        protected int getId() { return this.trigger_name;}
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
            //0. 메인뷰�?? ?��리거��? ?����? ?��?�� ?��?��?����? ?��?��발생(null check)
            if (_mainview == null || _trigger == null) {
                Log.w("HGA","There is no main view or sources.");
                throw new Exception();
            }
            Log.i("HGA", "COMMIT is on");
            //1. ?��리거 ?��름과 ?����??�� ?��?�� 리스?�� 불러?��(Find which actions are executed)
            int trigger_id = _trigger.getName(); //get trigger id
            Log.d("HGA", "Trigger name : " + _trigger.getName() + ", hash : " + trigger_id);    //get action
            List <Target> action_target = new ArrayList<>(getUndoAction(trigger_id)); //get action by trigger id
            //size check
            if (action_target.size() <= 0) {
                Log.e("HGA", "source has no actions matched.");
                throw new Exception();
            }
            //2. ?��리거?�� ?��?�� ?��?�� 반응 ?��?����?
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
    /*
    public void act(final View _view, Target _action, Target _trigger) {
        Log.i("HGA","ACT is on.");
        //0. ?��리거?�� ?��?�� 불러 ?����?(execute actions by states of sources)
        List < Boolean > trigStates = new ArrayList<>(_trigger.getStatus()); //get stat of sources
        try {
            //dst
            List < Integer > dst = _action.getViewId();
            String _actiontype = _action.getType();
            switch (_actiontype) {
                case "HIGHLIGHT":
                    for (int i = 0; i < dst.size(); ++i) {
                        if (!trigStates.get(i))
                            highlight((TextView) _view.findViewById(dst.get(i)));
                    }
                    Log.d("Action : ", "HIGHLIGHT");
                    break;
                case "FOCUS":
                    //find scrollview
                    LinearLayout root = (LinearLayout)_view;
                    View[] children = new View[root.getChildCount()];
                    int scroll_id = findScrollView(_view);
                    if (scroll_id < 0) {
                        Log.e("HGA-act", "There is no scrollview found");
                        throw new Exception();
                    }
                    //focus
                    children[scroll_id] = (View) root.getChildAt(scroll_id);
                    for (int i = 0; i< dst.size(); ++i) {
                        int curID = dst.get(i);
                        if (!trigStates.get(i))
                            scrollToView(_view.findViewById(curID), (ScrollView) children[scroll_id], 0);
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
                        if (!trigStates.get(i) && _view.findViewById(curID).getAccessibilityClassName().toString() == "android.widget.EditText") {
                            if (curID < tarID)
                                tarID = curID;
                        }// todo : else if ()
                    }
                    if (tarID != Integer.MAX_VALUE) {
                        editText = (EditText) _view.findViewById(tarID);
                        editText.post(new Runnable() {
                            @Override
                            public void run() {
                                editText.setFocusableInTouchMode(true);
                                editText.requestFocus();
                                InputMethodManager imm = (InputMethodManager) _view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
    }*/
    public void act(final View _view, Target _action, Target _trigger) {
            Log.i("HGA","ACT is on.");
            //0. ?��리거?�� ?��?�� 불러 ?����?(execute actions by states of sources)
            List < Boolean > trigStates = new ArrayList<>(_trigger.getStatus()); //get stat of sources
            try {
                //dst
                List < Integer > dst = _action.getViewId();
                String _actiontype = _action.getType();
                switch (_actiontype) {
                case "HIGHLIGHT":
                    for (int i = 0; i < dst.size(); ++i) {
                        if (!trigStates.get(i))
                        highlight((TextView) _view.findViewById(dst.get(i)));
                    }
                    Log.d("Action : ", "HIGHLIGHT");
                    break;
                case "FOCUS":
                    //find scrollview
                    LinearLayout root = (LinearLayout)_view;
                    View scrollview = findScrollView(_view);
                    if (scrollview == null) {
                        Log.e("HGA-act", "There is no scrollview found");
                        throw new Exception();
                    }
                    //focus
                    for (int i = 0; i< dst.size(); ++i) {
                        int curID = dst.get(i);
                        if (!trigStates.get(i))
                            scrollToView(_view.findViewById(curID), (ScrollView) scrollview, 0);
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
                        if (!trigStates.get(i) && _view.findViewById(curID).getAccessibilityClassName().toString() == "android.widget.EditText") {
                            if (curID < tarID)
                             tarID = curID;
                        }/* todo : else if () */
                    }
                    if (tarID != Integer.MAX_VALUE) {
                        editText = (EditText) _view.findViewById(tarID);
                        editText.post(new Runnable() {
                            @Override
                            public void run() {
                                    editText.setFocusableInTouchMode(true);
                                    editText.requestFocus();
                                    InputMethodManager imm = (InputMethodManager) _view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
public void add(String _type, int _name, List < Integer > _viewid) {
        Log.i("\nHGA","ADD is on.");
        //if there is an element already, then add into the back
        if (!this.mActions.containsKey(_name)) {
            Log.d("HGA", "first actions are enrolled.");
            this.mActions.put(_name, new Action(_type, _name, _viewid));
        } else { //add into the back
            Action new_actionsList = mActions.get(_name);
            new_actionsList.add(new Target(_type, _viewid));
            this.mActions.put(_name, new_actionsList);
            Log.d("HGA", "new actions are added into existing action list.");
        }

    }
    public List < Target > getUndoAction(int _trigid) {
        Action temp = mActions.get(_trigid);
        return temp.getTarget();
    }
    //method
    /*
    public static int findScrollView(View _view) {
        int id;
        LinearLayout root = (LinearLayout)_view;
        View[] children = new View[root.getChildCount()];
        //
        for (id = 0; id < root.getChildCount(); ++id) {
            children[id] = (View) root.getChildAt(id);
            Log.d("Child-name", "" + children[id].getAccessibilityClassName());
            if (children[id].getAccessibilityClassName().equals("android.widget.ScrollView")) {
                return id;
            }
        }
        return -1;
    }
    */
    public static View findScrollView(View _view) {
        int id;
        LinearLayout root = (LinearLayout)_view;
        View[] children = new View[root.getChildCount()];
        //
        for (id = 0; id < root.getChildCount(); ++id) {
            children[id] = (View) root.getChildAt(id);
            Log.d("Child-name", "" + children[id].getAccessibilityClassName());
            if (children[id].getAccessibilityClassName().equals("android.widget.ScrollView")) {
                return children[id];
            }
        }
        return null;
    }
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
