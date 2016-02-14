package com.dwb.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * 不会接受到触摸事件的ViewPager
 * @author admin
 *
 */
public class NoScrollViewPager extends android.support.v4.view.ViewPager {

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollViewPager(Context context) {
		super(context);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

}
