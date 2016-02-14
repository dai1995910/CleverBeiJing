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
	 * �ַ��¼�
	 * 
	 * ����Ϊfalse
	 * 	|--�󻬣��ҵ�ǰΪ���һ����ʱ��
	 * 	|--�һ�����ǰΪ��һ����ʱ��
	 * 	|--���»�����ʱ��
	 * 
	 * �����ж��Ƿ�Ϊ����
	 * ���ж��Ƿ�Ϊ����
	 * �ٸ������ҵľ������������
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//�����Ի�ȡ�¼����з���
			getParent().requestDisallowInterceptTouchEvent(true);
			
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
			
		case MotionEvent.ACTION_MOVE :
			int endX = (int) ev.getX();
			int endY = (int) ev.getY();
			if((Math.abs(startX - endX) - Math.abs(startY - endY)) > 0) {
				//˵��Ϊ����
				int position = getCurrentItem();
				int count = getAdapter().getCount();
				
				if( (endX - startX) > 0 && position == 0){
//					Log.d("TopNewsViewPager", "false1");
					//��Ϊ��һ������Ϊ�һ�,�ų��໬��
					getParent().requestDisallowInterceptTouchEvent(false);
				}
				if((endX - startX) <= 0 && position == count-1) {
//					Log.d("TopNewsViewPager", "false2");
					getParent().requestDisallowInterceptTouchEvent(false);
				}
			} else {
				//Ϊ����
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
