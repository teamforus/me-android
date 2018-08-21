package io.forus.me.android.presentation.view.component.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonSwipeableViewPager extends ViewPager {
    
    private Boolean disable = true;
    
    public NonSwipeableViewPager(Context context) {
        super(context);
    }
    public NonSwipeableViewPager(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return disable ? false : super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return disable ? false : super.onTouchEvent(event);
    }

    public void disableScroll(Boolean disable){
        //When disable = true not work the scroll and when disble = false work the scroll
        this.disable = disable;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
}
