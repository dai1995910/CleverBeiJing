package com.dwb.zhbj.pager.detail;

import android.app.Activity;
import android.view.View;

/**
 * ��ϸҳ��Ļ���
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
