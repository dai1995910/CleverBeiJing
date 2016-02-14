package com.dwb.zhbj.pager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class GoverPager extends BasePager {

	public GoverPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initView() {
		TextView textView = new TextView(mActivity);
		textView.setText("Gover");
		textView.setTextColor(Color.BLUE);
		textView.setTextSize(20);
		flContent.addView(textView);
		
		ivTitle.setText("ÕþÎñ");
		iv_open_slide_menu.setVisibility(View.VISIBLE);
		
	}

	@Override
	public void initData() {
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

}
