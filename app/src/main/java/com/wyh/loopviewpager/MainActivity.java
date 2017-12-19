package com.wyh.loopviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    protected LoopViewPager loopViewPager1, loopViewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        List<View> views1 = new ArrayList<>();
        loopViewPager1 = (LoopViewPager) findViewById(R.id.loop_view_pager_1);
        LoopPageIndicator pageIndicator1 = (LoopPageIndicator) findViewById(R.id.page_indicator_1);

        loopViewPager2 = (LoopViewPager) findViewById(R.id.loop_view_pager_2);
        LoopPageIndicator pageIndicator2 = (LoopPageIndicator) findViewById(R.id.page_indicator_2);

        ViewPager viewPager3 = (ViewPager) findViewById(R.id.view_pager_3);
        LoopPageIndicator pageIndicator3 = (LoopPageIndicator) findViewById(R.id.page_indicator_3);

        for (int i = 1; i <= 3; i++) {
            TextView textView = getTextView(i);
            textView.append("LoopViewPager");
            views1.add(textView);
        }
        addFirstAndLast(views1);
        loopViewPager1.setScrollerDuration(500);
        loopViewPager1.setAdapter(new ViewPagerAdapter<>(views1));
        pageIndicator1.setViewPager(loopViewPager1);
        loopViewPager1.startLoop();

        List<View> views2 = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            TextView textView = getTextView(i);
            textView.append("LoopViewPager");
            views2.add(textView);
        }
        addFirstAndLast(views2);
        loopViewPager2.setScrollerDuration(800);
        loopViewPager2.setAdapter(new ViewPagerAdapter<>(views2));
        pageIndicator2.setViewPager(loopViewPager2);
        loopViewPager2.startLoop();


        List<View> views3 = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            TextView textView = getTextView(i);
            textView.append("ViewPager");
            views3.add(textView);
        }
        viewPager3.setAdapter(new ViewPagerAdapter<>(views3));
        pageIndicator3.setViewPager(viewPager3);
    }

    private void addFirstAndLast(List<View> views) {
        //大于1时才能开启滚动
        if (views != null && views.size() > 1) {
            TextView last = getTextView(views.size());
            last.append("LoopViewPager");
            views.add(0, getTextView(views.size()));//在开头位置添加最后一页
            TextView first = getTextView(1);
            first.append("LoopViewPager");
            views.add(first);//在结尾添加第一页
        }
    }

    @NonNull
    private TextView getTextView(int i) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText("第" + i + "页");
        return textView;
    }
}
