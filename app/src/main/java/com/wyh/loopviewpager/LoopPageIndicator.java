package com.wyh.loopviewpager;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * ============================================================
 * <p/>
 * 作 者 :    clude
 * 创建日期 : 2017/12/13
 * 描 述 :    可以画圆/矩形的PageIndicator
 * <p/>
 * ============================================================
 **/
public class LoopPageIndicator extends View {
    private static final int CIRCLE = 0;//圆形
    private static final int RECTANGLE = 1;//矩形
    private static final int CENTER = 0;
    private static final int START = 1;
    private static final int END = 2;
    //未选中默认白色 选中默认红色
    public int normalColor = 0XFFFFFFFF, selectedColor = 0XFFec3627;
    public int defRadius = dip2px(6);
    public int indicatorRadius, indicatorHeight, indicatorWidth, indicatorMargin;
    public Paint normalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public Paint selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ViewPager viewPager;
    private int type;//默认圆形
    private int gravity;//位置 默认中心
    private int realPage;
    public boolean isLoop;//是否首尾加 1的循环

    public LoopPageIndicator(Context context) {
        this(context, null);
    }

    public LoopPageIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopPageIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoopPageIndicator);
        normalColor = array.getColor(R.styleable.LoopPageIndicator_loop_indicator_normal_color, normalColor);
        selectedColor = array.getColor(R.styleable.LoopPageIndicator_loop_indicator_selected_color, selectedColor);
        indicatorHeight = array.getDimensionPixelSize(R.styleable.LoopPageIndicator_loop_indicator_height, defRadius);
        indicatorWidth = array.getDimensionPixelSize(R.styleable.LoopPageIndicator_loop_indicator_width, defRadius);
        indicatorRadius = array.getDimensionPixelSize(R.styleable.LoopPageIndicator_loop_indicator_radius, defRadius);
        indicatorMargin = array.getDimensionPixelSize(R.styleable.LoopPageIndicator_loop_indicator_margin, defRadius);//间距
        type = array.getInt(R.styleable.LoopPageIndicator_loop_indicator_type, 0);
        gravity = array.getInt(R.styleable.LoopPageIndicator_loop_indicator_gravity, CENTER);
        array.recycle();

        normalPaint.setStyle(Paint.Style.FILL);//默认填充
        normalPaint.setColor(normalColor);
        selectedPaint.setStyle(Paint.Style.FILL);//默认填充
        selectedPaint.setColor(selectedColor);//默认填充
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getCount() <= 1) return;//1个不画
        int count = getRealCount();
        int width = getWidth();

        //总宽  count*直径+中间所有间距
        int indicatorALLWidth = count * (type == CIRCLE ? (indicatorRadius * 2) : indicatorWidth) + (count - 1) * indicatorMargin;
        //最左边indicator中心
        float cx;
        float cy = getHeight() / 2;
        //第一个indicator中心
        float firstCX;
        if (gravity == CENTER) {
            firstCX = width / 2.0f - indicatorALLWidth / 2.0f + (type == CIRCLE ? indicatorRadius : indicatorWidth / 2);
        } else if (gravity == START) { //左边
            firstCX = getPaddingLeft() + (type == CIRCLE ? indicatorRadius : indicatorWidth / 2);
        } else {
            firstCX = width - indicatorALLWidth - getPaddingRight() + (type == CIRCLE ? indicatorRadius : indicatorWidth / 2);
        }
        for (int i = 0; i < count; i++) {
            cx = firstCX + i * (type == CIRCLE ? (indicatorRadius * 2) : indicatorWidth) + i * indicatorMargin;
            if (type == CIRCLE) canvas.drawCircle(cx, cy, indicatorRadius, normalPaint);
            else {
                canvas.drawRect(cx - indicatorWidth / 2, cy - indicatorHeight / 2, cx + indicatorWidth / 2, cy + indicatorHeight / 2, normalPaint);
            }
        }
        //画选中的颜色
        cx = firstCX + realPage * (type == CIRCLE ? (indicatorRadius * 2) : indicatorWidth) + realPage * indicatorMargin;
        if (type == CIRCLE) canvas.drawCircle(cx, cy, indicatorRadius, selectedPaint);
        else {
            canvas.drawRect(cx - indicatorWidth / 2, cy - indicatorHeight / 2, cx + indicatorWidth / 2, cy + indicatorHeight / 2, selectedPaint);
        }
    }

    public int getCount() {
        if (viewPager != null && viewPager.getAdapter() != null)
            return viewPager.getAdapter().getCount();
        return 0;
    }

    public int getRealCount() {
        int count = getCount();
        if (isLoop && count > 1) count -= 2;//大于1时 循环前后各加1 所以真正的count需减2
        return count;
    }

    public int getRealPosition(int position) {
        int realConut = getRealCount();

        if (realConut == 0) return 0;
        int realPosition = (position - 1) % realConut;
        if (realPosition < 0) realPosition += realConut;

        return realPosition;
    }

    public void setViewPager(final ViewPager view) {
        if (viewPager == view) return;

        if (viewPager != null) viewPager.clearOnPageChangeListeners();
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        viewPager = view;
        isLoop = (view instanceof LoopViewPager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (viewPager != null && viewPager.getAdapter() != null) {
                    realPage = isLoop ? getRealPosition(position) : position;
                    invalidate();
                }
            }
        });
        invalidate();
    }

    public void setViewPager(final ViewPager view, int position) {
        realPage = (view instanceof LoopViewPager) ? getRealPosition(position) : position;
        setViewPager(view);
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        normalPaint.setColor(normalColor);
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
        selectedPaint.setColor(normalColor);
    }

    public int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
