package com.dwb.zhbj.pager;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwb.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * ҳ�����
 * @author admin
 *
 */
public abstract class BasePager {
	public Activity mActivity;
	//����ҳ��Ĳ���
	public View view;
	//���Ͻǵİ�ť
	public ImageView iv_open_slide_menu;
	//���ݴ���
	public FrameLayout flContent;
	//���ⲿ��
	public TextView ivTitle;
	public SlidingMenu slidingMenu;
	public ImageView ivChangeList;
	
	public BasePager(Activity activity) {
		mActivity = activity;
		init();
		initView();
	}
	
	/**
	 * ��ȡ������ҳ��ĸ������
	 */
	private void init() {
		view = View.inflate(mActivity, R.layout.fragment_base_pager, null);
		iv_open_slide_menu = (ImageView) view.findViewById(R.id.iv_open_slide_menu);
		flContent = (FrameLayout) view.findViewById(R.id.fl_content);
		ivTitle = (TextView) view.findViewById(R.id.iv_title);
		ivChangeList = (ImageView) view.findViewById(R.id.iv_change_list); 
		
		slidingMenu = ((SlidingFragmentActivity)mActivity).getSlidingMenu();
		
		iv_open_slide_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SlidingMenu slidingMenu = ((SlidingFragmentActivity)mActivity).getSlidingMenu();
				slidingMenu.toggle();
			}
		});
	}

	public abstract void initView();
	
	public abstract void initData();
}
