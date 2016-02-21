package com.dwb.zhbj.pager.detail;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dwb.zhbj.R;
import com.dwb.zhbj.activity.MainActivity;
import com.dwb.zhbj.bean.NewsCenterData.NewsTabData;
import com.dwb.zhbj.pager.detail.tab.NewsDetailTabPager;
import com.dwb.zhbj.view.HorizontalViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

/**
 * ���������ҳ��ľ���ʵ��
 * @author admin
 */

public class NewsDetaliPager extends BaseMenuDetailPager {

	private ViewPager vpContent;
	//tab��ǩ������	
	private ArrayList<NewsTabData> tabDatas;
	private ArrayList<NewsDetailTabPager> tabPagers;
	private TabPageIndicator tabIndicator;
	private NewsTabViewPagerAdapter adapter;

	public NewsDetaliPager(Activity activity) {
		super(activity);
	}

	/**
	 * ��ʼ��UI
	 */
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.news_detail_pager, null);
		vpContent = (ViewPager) view.findViewById(R.id.vp_detail_tab_content);
		tabIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		ViewUtils.inject(this, view);
		return view;
	}
	
	/**
	 * ��ʼ������
	 * �����һЩ��ʱ������������������Ĳ���
	 */
	@Override
	public void initData() {
		//����tab�Ķ���
		tabPagers = new ArrayList<NewsDetailTabPager>();
		for(int count = 0; count < tabDatas.size(); count++) {
			NewsDetailTabPager tab = new NewsDetailTabPager(mActivity, tabDatas.get(count));
			tabPagers.add(tab);
		}
		adapter = new NewsTabViewPagerAdapter();
		vpContent.setAdapter(adapter);
		tabIndicator.setViewPager(vpContent);
		/*
		 * ��Ӽ������ﵽ��̬���ݣ����������˷�,ע������Ҫ��������������Indicator��
		 * ��Page�仯���м�������ʱ��һҳ��ʱ���ò��������ʹ�ã�������ǵ�һҳ�򲻿����ϳ������
		 */
		tabIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
//				Log.d("NewsDetaliPager", "onPagerChangerListener");
//				System.out.println("onPageSelected-----------" + position);
				tabPagers.get(position).initData();
				//����UI
				adapter.notifyDataSetChanged();
				
				SlidingMenu slidingMenu = ((MainActivity)mActivity).getSlidingMenu();
				//���ò����
				if(position == 0) {
					//Ϊ��һ����ʱ������ϳ������
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				} else {
					//�������ϳ������
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
//				System.out.println("onPageScrolled-----------" + position);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		//Ĭ�ϳ�ʼ����һ��������
		tabPagers.get(0).initData();
	}
	
	
	class NewsTabViewPagerAdapter extends PagerAdapter {

		/**
		 * ���ص�ǰҳ������
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return tabDatas.get(position).title;
		}
		
		@Override
		public int getCount() {
			return tabPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = tabPagers.get(position).rootView;
//			tabPagers.get(position).initData();
			container.addView(view);
//			Log.d("NewsDetailPager", "instantiateItem : " + position);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * �����ݻ�ȡ��֮����ô˷��������Ѿ����������ݣ�Ȼ���ٸ�vp����adapter
	 * ��ֹ��������û�л�ȡ�Ϳ�ʼ��ʼ�������ֿ�ָ������
	 */
	public void setData(ArrayList<NewsTabData> children) {
		//��ȡ�������ϵ�����
		tabDatas = children;
		initData();
	}
	
	/**
	 * ��ת����һ��ҳ��
	 * @param v
	 */
	@OnClick(R.id.btn_tab_next)
	public void showNext(View v) {
		//�߽��ж�����ֹ���ֺ�ɫ�ĵ�����ʧ�����
		int positon = vpContent.getCurrentItem();
//		Log.d("NewsDetaliPager", positon + ":" + vpContent.getCurrentItem() + ":" + vpContent.getChildCount());
		if( positon == tabDatas.size() - 1) {
			tabIndicator.setCurrentItem(positon);
		} else {
			tabIndicator.setCurrentItem(++positon);
		}
	}
}
