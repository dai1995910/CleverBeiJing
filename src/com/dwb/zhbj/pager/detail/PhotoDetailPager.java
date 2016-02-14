package com.dwb.zhbj.pager.detail;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

/**
 * 侧边栏“组图”页面的实现
 * @author admin
 *
 */
public class PhotoDetailPager extends BaseMenuDetailPager {

	public PhotoDetailPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		TextView tView = new TextView(mActivity);
		tView.setText("photo newspager");
		return tView;
	}

}
