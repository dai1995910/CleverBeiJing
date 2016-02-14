package com.dwb.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

	private int startX;
	private int startY;

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopNewsViewPager(Context context) {
		super(context);
	}
	
	/**
	 * 分发事件
	 * 
	 * 请求为false
	 * 	|--左滑，且当前为最后一个的时候
	 * 	|--右滑，当前为第一个的时候
	 * 	|--上下滑动的时候
	 * 
	 * 首先判断是否为上下
	 * 再判断是否为左右
	 * 再根据左右的具体情况来分析
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//请求以获取事件进行分析
			getParent().requestDisallowInterceptTouchEvent(true);
			
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
			
		case MotionEvent.ACTION_MOVE :
			int endX = (int) ev.getX();
			int endY = (int) ev.getY();
			if((Math.abs(startX - endX) - Math.abs(startY - endY)) > 0) {
				//说明为左右
				int position = getCurrentItem();
				int count = getAdapter().getCount();
				
				if( (endX - startX) > 0 && position == 0){
//					Log.d("TopNewsViewPager", "false1");
					//当为第一个并且为右滑,放出侧滑栏
					getParent().requestDisallowInterceptTouchEvent(false);
				}
				if((endX - startX) <= 0 && position == count-1) {
//					Log.d("TopNewsViewPager", "false2");
					getParent().requestDisallowInterceptTouchEvent(false);
				}
			} else {
				//为上下
//				Log.d("TopNewsViewPager", "false3");
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
