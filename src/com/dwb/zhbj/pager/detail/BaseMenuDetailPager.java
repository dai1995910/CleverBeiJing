package com.dwb.zhbj.pager.detail;

import android.app.Activity;
import android.view.View;

/**
 * 详细页面的基类
 * @author admin
 *
 */
public abstract class BaseMenuDetailPager {
	//View
	public View rootView;
	public Activity mActivity;
	public BaseMenuDetailPager(Activity activity) {
		mActivity = activity;
		rootView = initView();
	}
	public abstract View initView();
	
	public void initData() {
	}
}
