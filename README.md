# LoopViewPager
无限循环轮播图 (包含指示器)

LoopViewPager loopViewPager1 =  findViewById(R.id.loop_view_pager_1);

LoopPageIndicator pageIndicator1 = findViewByI(R.id.page_indicator_1);

List<View> views1 = new ArrayList<>();
     
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
