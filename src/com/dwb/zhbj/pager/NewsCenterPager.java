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
	//��ȡ��������
	private String result;
	//��������Ϊ����
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
		
		ivTitle.setText("����");
		iv_open_slide_menu.setVisibility(View.VISIBLE);
	}

	@Override
	public void initData() {
		//ÿ�ν����ʱ������һ�£���ֹ�л�ҳ�����࣬�����ص�
		flContent.removeAllViews();
//		Log.d("NewsCenterPager", "initData()");
		//���������
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		//��ȡ���ϵ�����
		getDataFromService();
		
		//��ȡ4��Ԕ�����
		detailList = new ArrayList<BaseMenuDetailPager>();
		//���ｫ���ݴ��ݹ�ȥ������������tab��ǩ
		
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
	 * �ӷ�������ȡ����
	 * ��������XUtils�е�api����ȡ����
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
				//�����ȡʧ��
				arg0.printStackTrace();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				//��ȡ�ɹ�
				result = arg0.result;
//				Log.d("NewsCenterPager", "��õ��������ǣ�" + result);
				parseResult(result);
				//�������ݸ�newDetailPager
				((NewsDetaliPager)detailList.get(0)).setData(parseResult.data.get(0).children);
				// �����ݼ�������֮�󣬼���Ĭ�Ͻ���
				flContent.addView(detailList.get(0).rootView);
				SharePrefenceUtils.setString(mActivity, GlobalContants.SLIDING_CATEGORIES_URL, result);
			}       
		} );
	}                         
	
	/**
	 * ������õ������ݣ������ò����
	 * @param result
	 */
	private void parseResult(String result) {
		Gson gson = new Gson();
		parseResult = gson.fromJson(result, NewsCenterData.class);
		
		//���ò����
		SlideMenuFragment slideMenuFragment = ((MainActivity)mActivity).getSlideMenuFragment();
		slideMenuFragment.setSldingMenuData(parseResult);
	}
	
	/**
	 * ��¶������ȡ�������������
	 * @return
	 */
	public NewsCenterData getParseResult() {
		return parseResult;
	}
	
	/**
	 * ���ý���
	 * @param chosedPosition
	 */
	public void setDetailPager(int chosedPosition , String currentTitle) {
		ivTitle.setText(currentTitle);
		//�Ƚ������
		flContent.removeAllViews();
		//��ʼ������
		detailList.get(chosedPosition).initData();
		flContent.addView(detailList.get(chosedPosition).rootView);
	}
}
