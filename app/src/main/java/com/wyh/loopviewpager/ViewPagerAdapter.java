package com.wyh.loopviewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * ============================================================
 * <p/>
 * 作 者 :    clude
 * 创建日期 : 2017/12/12
 * 描 述 :    View PagerAdapter
 * <p/>
 * ============================================================
 **/
@SuppressWarnings("WeakerAccess")
public class ViewPagerAdapter<T extends View> extends PagerAdapter {
    private List<T> list;

    public ViewPagerAdapter(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = list.get(position);
        container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = list.get(position);
        container.addView(view);
        return view;
    }
}
