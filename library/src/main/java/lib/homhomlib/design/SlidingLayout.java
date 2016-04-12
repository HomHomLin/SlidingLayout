package lib.homhomlib.design;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by Linhh on 16/4/12.
 */
public class SlidingLayout extends FrameLayout{

    private View mViewBack;//背景View
    private View mViewFront;//正面View

    private static final int UN_SLIDING = 0;

    private int mLastSlidingY;

    private long mLastSlidingTime = 0;

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
        mLastSlidingY = UN_SLIDING;
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

        //判断拦截
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastSlidingY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("onTouchEvent", "move");
                float delta = (event.getY() - mLastSlidingY) / 3.0F;
                if(delta > 0 ){
                    //向下滑动
                    Instrument.getInstance().slidingByDelta(mViewFront, delta);
                }else{
                    //向上滑动
                }

                break;
            case MotionEvent.ACTION_UP:
                mLastSlidingY = UN_SLIDING;
                mLastSlidingTime = 0;
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
