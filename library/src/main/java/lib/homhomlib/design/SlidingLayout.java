package lib.homhomlib.design;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Linhh on 16/4/12.
 */
public class SlidingLayout extends FrameLayout{

    private int mTouchSlop;//系统允许最小的滑动判断值
    private int mBackgroundViewLayoutId;

    private View mBackgroundView;//背景View
    private View mTargetView;//正面View

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
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingLayout);
        mBackgroundViewLayoutId = a.getResourceId(R.styleable.SlidingLayout_background_view, 0);
        if(mBackgroundViewLayoutId != 0){
            View view = View.inflate(context, mBackgroundViewLayoutId, null);
            setBackgroundView(view);
        }
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void setBackgroundView(View view){
        if(mBackgroundView != null){
            this.removeView(mBackgroundView);
        }
        mBackgroundView = view;
        this.addView(view, 0);
    }

    public View getBackgroundView(){
        return this.mBackgroundView;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //实际上整个layout只能存在一个背景和一个前景才有用途
//        if(getChildCount() > 2){
//
//        }
        if (getChildCount() == 0) {
            return;
        }
        if (mTargetView == null) {
            ensureTarget();
        }
        if (mTargetView == null) {
            return;
        }
    }

    private void ensureTarget() {
        if (mTargetView == null) {
            mTargetView = getChildAt(getChildCount() - 1);
        }
    }

    public void setTargetView(View view){
        if(mTargetView != null){
            this.removeView(mTargetView);
        }
        mTargetView = view;
        this.addView(view);
    }

    public View getTargetView(){
        return this.mTargetView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        ensureTarget();

        final int action = MotionEventCompat.getActionMasked(ev);

        //判断拦截
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

                if(y > mInitialDownY) {
                    //判断是否是上拉操作
                    final float yDiff = y - mInitialDownY;
                    if (yDiff > mTouchSlop && !mIsBeingDragged && !canChildScrollUp()) {
                        mInitialMotionY = mInitialDownY + mTouchSlop;
                        mIsBeingDragged = true;
                    }
                }else if(y < mInitialDownY){
                    //判断是否是下拉操作
                    final float yDiff = mInitialDownY - y;
                    if (yDiff > mTouchSlop && !mIsBeingDragged && !canChildScrollDown()) {
                        mInitialMotionY = mInitialDownY + mTouchSlop;
                        mIsBeingDragged = true;
                    }
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

    /**
     * 判断View是否可以上拉
     * @return
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (mTargetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTargetView, -1) || mTargetView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetView, -1);
        }
    }

    /**
     * 判断View是否可以下拉
     * @return
     */
    public boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (mTargetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetView;
                return absListView.getChildCount() > 0
                        && (absListView.getLastVisiblePosition() == absListView.getChildCount() - 1 || absListView.getChildAt(absListView.getChildCount() - 1)
                        .getBottom() < absListView.getPaddingBottom());
            } else {
                return ViewCompat.canScrollVertically(mTargetView, 1) || mTargetView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetView, 1);
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
                Instrument.getInstance().slidingByDelta(mTargetView, delta);
//                if(delta > 0 ){
//                    //向下滑动
//                    Instrument.getInstance().slidingByDelta(mViewFront, delta);
//                }else{
//                    //向上滑动
//                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Instrument.getInstance().slidingToY(mTargetView,0);
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
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                view.setTranslationY(delta);
            }else{
                ViewHelper.setTranslationY(view, delta);
            }
//            view.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//                        view.setTranslationY(delta);
//                    }else{
//                        ViewHelper.setTranslationY(view, delta);
//                    }
//                }
//            });
        }

        public void slidingToY(final View view ,final float y){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                view.setY(y);
            }else{
                ViewHelper.setY(view,y);
            }
//            view.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//                        view.setY(y);
//                    }else{
//                        ViewHelper.setY(view,y);
//                    }
//                }
//            });
        }
    }
}
