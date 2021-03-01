package com.cylan.smart.plugin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

/**
 * @author Lupy create on 19-1-14
 * @Description
 */

public class SmartTabLayout extends TabLayout {


    public SmartTabLayout(Context context) {
        super(context);
    }

    public SmartTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int tapDefineWidth = -1;
        ViewGroup vg = (ViewGroup) this.getChildAt(0);
        if (vg.getChildCount() == 4) {
            int defineWidth = vg.getChildAt(3).getWidth();
            tapDefineWidth = getWidth() - defineWidth;
        }


        if (ev.getAction() == MotionEvent.ACTION_DOWN &&
                tapDefineWidth != -1 && ev.getX() > tapDefineWidth &&
                onInterceptListener != null
        ) {
            return onInterceptListener.onIntercept();
        }
        return super.onInterceptTouchEvent(ev);
    }

    private OnInterceptListener onInterceptListener;

    public void setOnInterceptListener(OnInterceptListener onInterceptListener) {
        this.onInterceptListener = onInterceptListener;
    }

    public interface OnInterceptListener {
        boolean onIntercept();
    }
}