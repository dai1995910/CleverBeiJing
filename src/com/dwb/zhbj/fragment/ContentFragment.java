package com.dwb.zhbj.fragment;

import java.util.ArrayList;

import com.dwb.zhbj.R;
import com.dwb.zhbj.pager.BasePager;
import com.dwb.zhbj.pager.GoverPager;
import com.dwb.zhbj.pager.IndexPager;
import com.dwb.zhbj.pager.NewsCenterPager;
import com.dwb.zhbj.pager.SettingPager;
import com.dwb.zhbj.pager.SmartServicePager;
import com.dwb.zhbj.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContentFragment extends BaseFragment {

	private com.dwb.zhbj.view.NoScrollViewPager vp_content;
	private ArrayList<BasePager> pagerList;
	private RadioGroup rgGroup;
	private View view;

	public ContentFragment() {
		super();
	}

	@Override
	public View initView() {
		view = View.inflate(mActivity, R.layout.fragment_content, null);
		initUI();	//��ȡ���
		initData();	//��ʼ��view
		
		return view;
	}

	/*
	 * ��ȡ���
	 */
	public void initUI() {
		vp_content = (NoScrollViewPager) view.findViewById(R.id.vp_content);
		rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
	}
	private void initData() {
		//��ʼ��pager����
		pagerList = new ArrayList<BasePager>();
		IndexPager indexPager = new IndexPager(mActivity);
		GoverPager goverPager = new GoverPager(mActivity);
		NewsCenterPager newsCenterPagerPager = new NewsCenterPager(mActivity);
		SettingPager settingPager = new SettingPager(mActivity);
		SmartServicePager smartServicePager = new SmartServicePager(mActivity);

		pagerList.add(indexPager);
		pagerList.add(newsCenterPagerPager);
		pagerList.add(smartServicePager);
		pagerList.add(goverPager);
		pagerList.add(settingPager);

		vp_content.setAdapter(new MyPagerAdapter());
		rgGroup.setOnCheckedChangeListener(new MyOnCheckedChangedListener());
		rgGroup.check(R.id.rb_home); // ����Ĭ��ѡ��ҳ��

		vp_content.setOnPageChangeListener(new MyOnPagerChangerListener());
		//�ս���ҳ��ʱ�����ݽ��г�ʼ��
		pagerList.get(0).initData();
	}

	/**
	 * ���ײ���ť�ı��ʱ���л�����Ӧ�Ľ���
	 * @author admin
	 *
	 */
	class MyOnCheckedChangedListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rb_home:
				vp_content.setCurrentItem(0, false);
				break;
			case R.id.rb_news_center:
				vp_content.setCurrentItem(1, false);
				break;
			case R.id.rb_smart_service:
				vp_content.setCurrentItem(2, false);
				break;
			case R.id.rb_gover:
				vp_content.setCurrentItem(3, false);
				break;
			case R.id.rb_setting:
				vp_content.setCurrentItem(4, false);
				break;
			default:
				break;
			}
		}

	}

	/**
	 * ��ҳ���л���ʱ���ټ�������
	 * @author admin
	 *
	 */
	class MyOnPagerChangerListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			// SlidingMenu slidingMenu =
			// ((SlidingFragmentActivity)mActivity).getSlidingMenu();
			// if(position == 1 ||position == 2 ||position == 3) {
			// slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			// } else {
			// slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			// }
			// �������ʱ��ų�ʼ������
			pagerList.get(position).initData();
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = pagerList.get(position);
			// pager.initData(); //��ǰҳ��������ݵĳ�ʼ��
			container.addView(pager.view);
			return pager.view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	/**
	 * ��ȡ��NewsCenterPager
	 * @return 
	 */
	public NewsCenterPager getNewsCenterPager() {
		return (NewsCenterPager)pagerList.get(1);
	}
}
