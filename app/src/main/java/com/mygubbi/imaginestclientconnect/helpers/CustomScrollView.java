package com.mygubbi.imaginestclientconnect.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    private boolean enableScrolling = true;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scrollingEnabled() && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return scrollingEnabled() && super.onTouchEvent(ev);
    }

    private boolean scrollingEnabled() {
        return enableScrolling;
    }

    public void setScrolling(boolean enableScrolling) {
        this.enableScrolling = enableScrolling;
    }
}