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
 * 侧边栏新闻页面的具体实现
 * @author admin
 */

public class NewsDetaliPager extends BaseMenuDetailPager {

	private ViewPager vpContent;
	//tab标签的数据	
	private ArrayList<NewsTabData> tabDatas;
	private ArrayList<NewsDetailTabPager> tabPagers;
	private TabPageIndicator tabIndicator;
	private NewsTabViewPagerAdapter adapter;

	public NewsDetaliPager(Activity activity) {
		super(activity);
	}

	/**
	 * 初始化UI
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
	 * 初始化数据
	 * 会放置一些耗时操作或者是请求网络的操作
	 */
	@Override
	public void initData() {
		//生成tab的对象
		tabPagers = new ArrayList<NewsDetailTabPager>();
		for(int count = 0; count < tabDatas.size(); count++) {
			NewsDetailTabPager tab = new NewsDetailTabPager(mActivity, tabDatas.get(count));
			tabPagers.add(tab);
		}
		adapter = new NewsTabViewPagerAdapter();
		vpContent.setAdapter(adapter);
		tabIndicator.setViewPager(vpContent);
		/*
		 * 添加监听器达到动态数据，减少流浪浪费,注意这里要将监听器设置在Indicator上
		 * 对Page变化进行监听，当时第一页的时候让侧边栏可以使用，如果不是第一页则不可以拖出侧边栏
		 */
		tabIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
//				Log.d("NewsDetaliPager", "onPagerChangerListener");
//				System.out.println("onPageSelected-----------" + position);
				tabPagers.get(position).initData();
				//更新UI
				adapter.notifyDataSetChanged();
				
				SlidingMenu slidingMenu = ((MainActivity)mActivity).getSlidingMenu();
				//设置侧边栏
				if(position == 0) {
					//为第一个的时候可以拖出侧边栏
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				} else {
					//否则不能拖出侧边啦
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
		//默认初始化第一个的数据
		tabPagers.get(0).initData();
	}
	
	
	class NewsTabViewPagerAdapter extends PagerAdapter {

		/**
		 * 返回当前页的名称
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
	 * 当数据获取到之后调用此方法传入已经解析的数据，然后再给vp设置adapter
	 * 防止出现数据没有获取就开始初始化，出现空指针的情况
	 */
	public void setData(ArrayList<NewsTabData> children) {
		//获取到网络上的数据
		tabDatas = children;
		initData();
	}
	
	/**
	 * 跳转到下一个页面
	 * @param v
	 */
	@OnClick(R.id.btn_tab_next)
	public void showNext(View v) {
		//边界判定，防止出现红色的底线消失的情况
		int positon = vpContent.getCurrentItem();
//		Log.d("NewsDetaliPager", positon + ":" + vpContent.getCurrentItem() + ":" + vpContent.getChildCount());
		if( positon == tabDatas.size() - 1) {
			tabIndicator.setCurrentItem(positon);
		} else {
			tabIndicator.setCurrentItem(++positon);
		}
	}
}
