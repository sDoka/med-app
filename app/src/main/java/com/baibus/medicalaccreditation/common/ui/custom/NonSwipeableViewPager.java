package com.baibus.medicalaccreditation.common.ui.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 30.04.2017
 * Time: 19:16
 * To change this template use File | settings | File Templates.
 */
public class NonSwipeableViewPager extends ViewPager {

    public NonSwipeableViewPager(Context context) {
        super(context);
//        setMyScroller();
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setMyScroller();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

//    private void setMyScroller() {
//        try {
//            Class<?> viewpager = ViewPager.class;
//            Field scroller = viewpager.getDeclaredField("mScroller");
//            scroller.setAccessible(true);
//            scroller.set(this, new MyScroller(getContext()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public class MyScroller extends Scroller {
//        public MyScroller(Context context) {
//            super(context, new DecelerateInterpolator());
//        }
//
//        @Override
//        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
//            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/);
//        }
//    }

}
