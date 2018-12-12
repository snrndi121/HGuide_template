package com.uki121.hguidetemplate;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

class HGAction {
    enum ACTION {HIGHLIGHT, FOCUS, POINTER}
    private int tid;
    private String aname;
    private boolean actionswitch = false;
    public HGAction() {};
    public HGAction(int _dstname, String _action) {
        this.tid = _dstname;
        this.aname = _action;

    }
    public void setAction(int _dstname, String _action) {
        this.tid = _dstname;
        this.aname = _action;
    }
    public void commit(View base, boolean _switch) {
        if (_switch) {
            if (aname == "HIGHLIGHT") {
                manageBlinkEffect((TextView) base.findViewById(tid));
                Log.d("Action : ", "HIGHLIGHT");
            }else if (aname == "FOCUS") {
                scrollToView(base, (ScrollView) base.findViewById(tid), 0);
                Log.d("Action : ", "FOCUS");
            } else if (aname == "POINTER") {
                //
                Log.d("Action : ", "POINTER");
            }
            return;
        }
        Log.d("Action : ", "no action!");
    }
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
