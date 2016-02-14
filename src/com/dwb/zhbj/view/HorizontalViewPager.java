package com.dwb.zhbj.view;

import android.content.Context;
import android.database.CursorJoiner.Result;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * (废弃)
 * 当滑动到第一个的时候可以滑动出来侧边栏
 * 
 * 
 * @author admin
 *
 */
public class HorizontalViewPager extends ViewPager {

	private int xDown;
	private int yDown;

	public HorizontalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizontalViewPager(Context context) {
		super(context);
	}

 
	/**
	 * 是否继续下发事件
	 * 是第一个接收到事件的方法
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
//		if(ev.getAction() == MotionEvent.ACTION_DOWN) {
//			xDown = (int) ev.getX();
//		}
//		if(ev.getAction() == MotionEvent.ACTION_MOVE) {
//			int xMove = (int) ev.getX();
//			int result = xMove - xDown;
//			
//			int currentItem = getCurrentItem();
//			Log.d("HorizontalViewPager", currentItem + "");
//			
//			if (currentItem == 0 && result > 0) {
//				Log.d("debyug", "return false ");
//				// 如果是第0个则不需要请求事件，让侧边栏正常的滑动出来
//				getParent().requestDisallowInterceptTouchEvent(false);
//			} else {
//				Log.d("debyug", "return true ");
//				getParent().requestDisallowInterceptTouchEvent(true);
//			}
//		}
		
//		int currentItem = getCurrentItem();
//		System.out.println(currentItem);
//		if(currentItem == 1) {
//			getParent().requestDisallowInterceptTouchEvent(false);
//		} else {
//			getParent().requestDisallowInterceptTouchEvent(true);
//		}
//		
//		return super.dispatchTouchEvent(ev);
		if(getCurrentItem() != 0) {
			getParent().requestDisallowInterceptTouchEvent(true);
		} else {
			getParent().requestDisallowInterceptTouchEvent(false);
		}
		return super.dispatchTouchEvent(ev);
	}

}
