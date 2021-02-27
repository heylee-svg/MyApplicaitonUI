package com.dhg.myapplicationui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * @author: Denghg  @createDate: 2020/11/24 下午6:22
 * @description
 **/
public class DrawableEditText extends AppCompatEditText {


    private Drawable mDrawable;
    private RightDrawableOnClickListener drawableOnClickListener;
    private boolean status = false;
    //拦截编辑框右侧图片区域以外的地方的点击事件
    private boolean isInterception = false;

    public DrawableEditText(Context context) {
        super(context);
        init();
    }

    public DrawableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDrawable = getCompoundDrawables()[2];

    }

    public void setDrawableOnClickListener(RightDrawableOnClickListener drawableOnClickListener) {
        this.drawableOnClickListener = drawableOnClickListener;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setInterception(boolean isInterception){
        this.isInterception = isInterception;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_UP:
                if (getCompoundDrawables()[2] != null) {
                    boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight() - 20);
                    if (touchable) {
                        status = !status;
                        if (drawableOnClickListener != null) {
                            drawableOnClickListener.onClickDrawable(status);
                        }
                        //return true;不能拦截，会造成焦点丢失的问题
                    }
                    if(isInterception){
                        return true;
                    }
                }
                break;
            default:
                if(isInterception){
                    return true;
                }
        }
        return super.onTouchEvent(event);
    }

    public void setRightDrawable(int resId) {
        if (resId < 0) {
            setCompoundDrawables(null, null, null, null);
            return;
        }
        mDrawable = getResources().getDrawable(resId);
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1]
                , mDrawable, getCompoundDrawables()[3]);
    }

    public interface RightDrawableOnClickListener {
        void onClickDrawable(boolean status);
    }
}
