package com.dwb.zhbj.pager.detail;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

/**
 * 侧边栏“互动”页面实现
 * @author admin
 *
 */

public class InteractDetailPager extends BaseMenuDetailPager {

	public InteractDetailPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		TextView tView = new TextView(mActivity);
		tView.setText("interact newspager");
		return tView;
	}

}
