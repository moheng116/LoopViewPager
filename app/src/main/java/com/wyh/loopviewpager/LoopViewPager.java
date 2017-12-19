package com.wyh.loopviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * ============================================================
 * <p/>
 * 版 权 :    天厚投资 版权所有 (c)
 * 作 者 :    clude
 * 创建日期 : 2017/12/12
 * 描 述 :    循环ViewPager(只需手动开启一次即可 不可见 熄屏 亮屏等 已自动处理 无需再次处理)
 * <p/>
 * ============================================================
 **/
public class LoopViewPager extends ViewPager {
    private Runnable loopRunnable;
    private int action;
    private long delayMillis = 3000;//默认3s
    private boolean startedLoop;///是否开启过轮播
    private boolean noCallBack = true;//有无callBack
    private float offset;
    private int scrollState;
    private int currentPosition;

    public LoopViewPager(Context context) {
        this(context, null);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScrollerDuration(this, 500);
        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                scrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (getPageMargin() > 0) {
                        toRealPosition(currentPosition);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                offset = positionOffset;
                //同时设置 PageMargin和PageTransformer时 offset因精度问题可能不会为0
                if (getPageMargin() == 0) {
                    toRealPosition(position);
                }
            }
        });
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter.getCount() > 1) setCurrentItem(1);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        action = ev.getAction();
        //已开启轮播 按压时暂取消自动轮播
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (startedLoop) stopLoop();
                break;
            case MotionEvent.ACTION_UP:
                if (startedLoop && (offset == 0 || scrollState == ViewPager.SCROLL_STATE_IDLE))
                    startLoop();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void startLoop() {
        if (getAdapter() != null && getAdapter().getCount() > 1 && noCallBack) {
            startedLoop = true;
            noCallBack = false;
            if (loopRunnable == null)
                loopRunnable = new Runnable() {
                    @Override
                    public void run() {
                        setCurrentItem(getCurrentItem() + 1);
                        postDelayed(this, delayMillis);
                    }
                };
            postDelayed(loopRunnable, delayMillis);
        }
    }

    public void stopLoop() {
        if (startedLoop) removeCallbacks();
    }

    /**
     * 设置滚动间隔时间
     */
    public void setLoopDelay(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    /**
     * @param time 设置滚动动画时间
     */
    public void setScrollerDuration(ViewPager viewPager, final int time) {
        try {
            Field viewPagerField = ViewPager.class.getDeclaredField("mScroller");
            viewPagerField.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);
            Scroller scroller = new Scroller(getContext(), (Interpolator) interpolator.get(null)) {
                @Override
                public void startScroll(int startX, int startY, int dx, int dy, int duration) {
                    super.startScroll(startX, startY, dx, dy, time);
                }
            };
            viewPagerField.set(viewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        if (getAdapter() != null) return getAdapter().getCount();
        return 0;
    }

    public int getRealCount() {
        int count = getCount();
        if (count > 1) count -= 2;//大于1时 循环前后各加1 所以真正的count需减2
        return count;
    }

    public int getRealPosition(int position) {
        int realConut = getRealCount();

        if (realConut == 0) return 0;
        int realPosition = (position - 1) % realConut;
        if (realPosition < 0) realPosition += realConut;

        return realPosition;
    }

    private void toRealPosition(int position) {
        int count = getCount();
        if (count > 1) {
            int realPosition = getRealPosition(position);
            if (offset == 0 || scrollState == ViewPager.SCROLL_STATE_IDLE) {
                if (position == 0 || position == count - 1) {
                    setCurrentItem(realPosition + 1, false);
                }
                //手指触摸松开后 重新开启
                if (startedLoop && action == MotionEvent.ACTION_UP) startLoop();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            if (startedLoop) startLoop();
        } else {
            if (startedLoop) stopLoop();
        }
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        //熄屏0 亮屏1
        if (screenState == 0) {
            if (startedLoop) stopLoop();
        } else if (screenState == 1) {
            if (startedLoop) startLoop();
        }
    }

    public void removeCallbacks() {
        noCallBack = true;
        if (loopRunnable != null) removeCallbacks(loopRunnable);
    }
}
