package com.dwb.zhbj.pager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class IndexPager extends BasePager {

	public IndexPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initView() {
		TextView textView = new TextView(mActivity);
		textView.setText("Index11");
		textView.setTextColor(Color.BLUE);
		textView.setTextSize(20);
		flContent.addView(textView);
		
		ivTitle.setText("Ê×Ò³");
		iv_open_slide_menu.setVisibility(View.INVISIBLE);
	}

	@Override
	public void initData() {
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}

}
