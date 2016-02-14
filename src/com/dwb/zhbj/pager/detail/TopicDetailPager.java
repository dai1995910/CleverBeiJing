package com.dwb.zhbj.pager.detail;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

/**
 * ≤‡±ﬂ¿∏°∞ª∞Ã‚°±“≥√Ê
 * @author admin
 *
 */

public class TopicDetailPager extends BaseMenuDetailPager {

	public TopicDetailPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		TextView tView = new TextView(mActivity);
		tView.setText("topic newspager");
		return tView;
	}

}
