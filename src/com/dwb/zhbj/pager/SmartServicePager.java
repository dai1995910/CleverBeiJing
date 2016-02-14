package com.dwb.zhbj.pager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initView() {
		TextView textView = new TextView(mActivity);
		textView.setText("SmartServicePager");
		textView.setTextColor(Color.BLUE);
		textView.setTextSize(20);
		flContent.addView(textView);
		
		ivTitle.setText("ÖÇÄÜ·þÎñ");
		iv_open_slide_menu.setVisibility(View.VISIBLE);
	}

	@Override
	public void initData() {
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

}
