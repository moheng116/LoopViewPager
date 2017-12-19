package com.wyh.loopviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

        for (int i = 1; i <= 3; i++) {
            TextView textView = getTextView(i);
            views1.add(textView);
        }
        addFirstAndLast(views1);
        loopViewPager1.setAdapter(new ViewPagerAdapter<>(views1));
        pageIndicator1.setViewPager(loopViewPager1);
        loopViewPager1.startLoop();

        List<View> views2 = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            TextView textView = getTextView(i);
            views2.add(textView);
        }
        addFirstAndLast(views2);
        loopViewPager2.setAdapter(new ViewPagerAdapter<>(views2));
        pageIndicator2.setViewPager(loopViewPager2);
        loopViewPager2.startLoop();
    }

    private void addFirstAndLast(List<View> views) {
        //大于1时才能开启滚动
        if (views != null && views.size() > 1) {
            views.add(0, getTextView(views.size()));//在开头位置添加最后一页
            views.add(getTextView(1));//在结尾添加第一页
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
