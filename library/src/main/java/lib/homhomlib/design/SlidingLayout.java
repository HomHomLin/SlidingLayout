package lib.homhomlib.design;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by Linhh on 16/4/12.
 */
public class SlidingLayout extends FrameLayout{

    private int mTouchSlop;//系统允许最小的滑动判断值

    private View mViewBack;//背景View
    private View mViewFront;//正面View

    private static final int UN_SLIDING = 0;

    private boolean mIsBeingDragged;
    private float mInitialDownY;
    private float mInitialMotionY;
    private int mActivePointerId = INVALID_POINTER;


    private static final int INVALID_POINTER = -1;

    public SlidingLayout(Context context) {
        this(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void setBackView(View view){
        mViewBack = view;
        this.addView(view,0);
    }

    public void setFrontView(View view){
        mViewFront = view;
        this.addView(view);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = MotionEventCompat.getActionMasked(ev);

        //判断拦截

        //子控件能否滑动
        if (canChildScrollUp()) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                final float initialDownY = getMotionEventY(ev, mActivePointerId);
                if (initialDownY == -1) {
                    return false;
                }
                mInitialDownY = initialDownY;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
//                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) {
                    return false;
                }
                final float yDiff = y - mInitialDownY;
                if (yDiff > mTouchSlop && !mIsBeingDragged) {
                    mInitialMotionY = mInitialDownY + mTouchSlop;
                    mIsBeingDragged = true;
                }
                break;

            case MotionEventCompat.ACTION_POINTER_UP:
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return mIsBeingDragged;
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mViewFront instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mViewFront;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mViewFront, -1) || mViewFront.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mViewFront, -1);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("onTouchEvent", "move");
                float delta = (event.getY() - mInitialMotionY) / 3.0F;
                Instrument.getInstance().slidingByDelta(mViewFront, delta);
//                if(delta > 0 ){
//                    //向下滑动
//                    Instrument.getInstance().slidingByDelta(mViewFront, delta);
//                }else{
//                    //向上滑动
//                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Instrument.getInstance().slidingToY(mViewFront,0);
                break;
        }
        //消费触摸
        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    static class Instrument {
        private static Instrument mInstrument;
        public static Instrument getInstance(){
            if(mInstrument == null){
                mInstrument = new Instrument();
            }
            return mInstrument;
        }

        public void slidingByDelta(final View view ,final float delta){
            view.post(new Runnable() {
                @Override
                public void run() {
                    view.setTranslationY((int)delta);
                }
            });
        }

        public void slidingToY(final View view ,final float y){
            view.post(new Runnable() {
                @Override
                public void run() {
                    view.setY((int)y);
                }
            });
        }
    }
}
