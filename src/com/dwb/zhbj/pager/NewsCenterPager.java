package com.dwb.zhbj.pager;

import java.util.ArrayList;

import com.dwb.zhbj.activity.MainActivity;
import com.dwb.zhbj.bean.NewsCenterData;
import com.dwb.zhbj.fragment.SlideMenuFragment;
import com.dwb.zhbj.global.GlobalContants;
import com.dwb.zhbj.pager.detail.BaseMenuDetailPager;
import com.dwb.zhbj.pager.detail.InteractDetailPager;
import com.dwb.zhbj.pager.detail.NewsDetaliPager;
import com.dwb.zhbj.pager.detail.PhotoDetailPager;
import com.dwb.zhbj.pager.detail.TopicDetailPager;
import com.dwb.zhbj.utils.SharePrefenceUtils;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class NewsCenterPager extends BasePager {
	//获取到的数据
	private String result;
	//解析数据为对象
	private NewsCenterData parseResult;
	private ArrayList<BaseMenuDetailPager> detailList;
	private NewsDetaliPager newsDetaliPager;

	public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initView() {
//		TextView textView = new TextView(mActivity);
//		textView.setText("NewsPager");
//		textView.setTextColor(Color.BLUE);
//		textView.setTextSize(20);
//		flContent.addView(textView);
		
		ivTitle.setText("新闻");
		iv_open_slide_menu.setVisibility(View.VISIBLE);
	}

	@Override
	public void initData() {
		//每次进入的时候，清理一下，防止切换页面后残余，导致重叠
		flContent.removeAllViews();
//		Log.d("NewsCenterPager", "initData()");
		//开启侧边栏
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		//获取网上的数据
		getDataFromService();
		
		//获取4個詳情界面
		detailList = new ArrayList<BaseMenuDetailPager>();
		//这里将数据传递过去，方便其生成tab标签
		
		newsDetaliPager = new NewsDetaliPager(mActivity);
		TopicDetailPager topicDetailPager = new TopicDetailPager(mActivity);
		PhotoDetailPager photoDetailPager = new PhotoDetailPager(mActivity);
		InteractDetailPager interactDetailPager = new InteractDetailPager(mActivity);
		detailList.add(newsDetaliPager);
		detailList.add(topicDetailPager);
		detailList.add(photoDetailPager);
		detailList.add(interactDetailPager);
	}

	/**
	 * 从服务器获取数据
	 * 这里是用XUtils中的api来获取数据
	 */
	private void getDataFromService() {
		String cacheResult = SharePrefenceUtils.getString(mActivity, GlobalContants.SLIDING_CATEGORIES_URL, "");
		if(!TextUtils.isEmpty(result)) {
			parseResult(cacheResult);
		}
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET,GlobalContants.SLIDING_CATEGORIES_URL, new RequestCallBack<String>() {
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				//如果获取失败
				arg0.printStackTrace();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				//获取成功
				result = arg0.result;
//				Log.d("NewsCenterPager", "获得到的数据是：" + result);
				parseResult(result);
				//传递数据给newDetailPager
				((NewsDetaliPager)detailList.get(0)).setData(parseResult.data.get(0).children);
				// 当数据加载完了之后，加载默认界面
				flContent.addView(detailList.get(0).rootView);
				SharePrefenceUtils.setString(mActivity, GlobalContants.SLIDING_CATEGORIES_URL, result);
			}       
		} );
	}                         
	
	/**
	 * 解析获得到的数据，并设置侧边栏
	 * @param result
	 */
	private void parseResult(String result) {
		Gson gson = new Gson();
		parseResult = gson.fromJson(result, NewsCenterData.class);
		
		//设置侧边栏
		SlideMenuFragment slideMenuFragment = ((MainActivity)mActivity).getSlideMenuFragment();
		slideMenuFragment.setSldingMenuData(parseResult);
	}
	
	/**
	 * 暴露给外界获取到解析后的数据
	 * @return
	 */
	public NewsCenterData getParseResult() {
		return parseResult;
	}
	
	/**
	 * 设置界面
	 * @param chosedPosition
	 */
	public void setDetailPager(int chosedPosition , String currentTitle) {
		ivTitle.setText(currentTitle);
		//先进行清除
		flContent.removeAllViews();
		//初始化数据
		detailList.get(chosedPosition).initData();
		flContent.addView(detailList.get(chosedPosition).rootView);
	}
}
