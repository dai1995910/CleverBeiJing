package com.dwb.zhbj.pager.detail.tab;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwb.zhbj.R;
import com.dwb.zhbj.activity.NewsDetailActivity;
import com.dwb.zhbj.bean.NewsCenterData.NewsTabData;
import com.dwb.zhbj.bean.NewsCenterTabData;
import com.dwb.zhbj.bean.NewsCenterTabData.TabNewsData;
import com.dwb.zhbj.global.GlobalContants;
import com.dwb.zhbj.utils.SharePrefenceUtils;
import com.dwb.zhbj.view.RefreshListView;
import com.dwb.zhbj.view.RefreshListView.OnRefreshListener;
import com.dwb.zhbj.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * NewsDetailPagerҳ
 * 
 * @author admin
 *
 */
public class NewsDetailTabPager implements OnPageChangeListener {
	// private String title;
	private Activity mActivity;
	public View rootView;
	private TopNewsViewPager vpTopNews;
	// ����
	private NewsTabData tabData;
	// �����������
	private NewsCenterTabData parsedData;
	private View view;
	private TextView topNewsTitle;
	private CirclePageIndicator topNewsIndicator; // ViewPager�ĵ�
	private RefreshListView lvNewsContent; // ������ŵ�ListView
	private View headerView;
	private String moreUrl; // ��ȡ��������url

	private String debugTag = "NewsDetailTabPager";

	private ArrayList<TabNewsData> newsDatas;
	private NewsListAdapter newsListAdapter;
	
	private Handler mHanlder;

	public NewsDetailTabPager(Activity activity, NewsTabData newsTabData) {
		mActivity = activity;
		tabData = newsTabData;
		// this.title = newsTabData.title;
		rootView = initView();
	}

	public View initView() {
		view = View.inflate(mActivity, R.layout.news_detail_tab_pager, null);
		headerView = View.inflate(mActivity, R.layout.header_news_list, null);
		vpTopNews = (TopNewsViewPager) headerView
				.findViewById(R.id.vp_top_news);
		topNewsTitle = (TextView) headerView.findViewById(R.id.tv_topNewsTitle);
		// TopNews�ļ�����
		topNewsIndicator = (CirclePageIndicator) headerView
				.findViewById(R.id.indicator);
		lvNewsContent = (RefreshListView) view
				.findViewById(R.id.lv_news_content);
		lvNewsContent.addHeaderView(headerView); // ���ͷ����
		topNewsIndicator.setSnap(true);
		topNewsIndicator.setOnPageChangeListener(this);

		lvNewsContent.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("NewsDetailTabPager", "setOnItemClickListener");
				// ���汻������itemID
				String readID = parsedData.data.news.get(position).id;
				String readedIDs = SharePrefenceUtils.getString(mActivity,
						"readed_ids", "");
				if (!readedIDs.contains(readID)) {
					readedIDs = readedIDs + "," + readID;
					SharePrefenceUtils.setString(mActivity, "readed_ids",
							readedIDs);
				}
				// �ı���ɫ
				changeTitleColor(view);

				Log.d("NewsDetailTabPager", "onclick");
				Intent intent = new Intent();
				intent.putExtra("url", parsedData.data.news.get(position).url);
				intent.setClass(mActivity, NewsDetailActivity.class);
				mActivity.startActivity(intent);
			}
		});

		lvNewsContent.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// ���»�ȡһ������
				getDataFromService(GlobalContants.SERVICE_URL + tabData.url);
//				Toast.makeText(mActivity, "ˢ�����", Toast.LENGTH_SHORT).show();
//				lvNewsContent.onRefreshComplete(true);
			}

			@Override
			public void onLoadMore() {
				Log.d("NewsDetailTabPager", "-=================" + moreUrl);
				if (!TextUtils.isEmpty(moreUrl)) {
					// ��ȡ��������
					getMoreDataFromService(moreUrl);
				} else {
					Toast.makeText(mActivity, "�Ѿ�û�и����������", Toast.LENGTH_SHORT)
							.show();
					lvNewsContent.onRefreshComplete(false);
				}
				// lvNewsContent.hideFootView();
			}
		});
		return view;
	}

	private void changeTitleColor(View view) {
		TextView tvNewsTitle = (TextView) view.findViewById(R.id.tv_news_title);
		tvNewsTitle.setTextColor(Color.GRAY);
	}

	public void initData() {
		// Log.d("NewsDetailTabPager", "initData : " + tabData.title);
		// ��ȡ�����ݣ�parsedData
		getDataFromService(GlobalContants.SERVICE_URL + tabData.url);

	}

	
	/**
	 * ViewPager��Adapter
	 * @author admin
	 *
	 */
	class MyAdapter extends PagerAdapter {

		private BitmapUtils bitmapUtils;

		public MyAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
			bitmapUtils
					.configDefaultLoadFailedImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return parsedData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container,final int position) {
			// ����ImageView
			ImageView iv = new ImageView(mActivity);
			iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Log.d(debugTag, "onClick");
					Intent i = new Intent(mActivity, NewsDetailActivity.class);
					i.putExtra("url", parsedData.data.topnews.get(position).url);
					mActivity.startActivity(i);
				}
			});
			iv.setOnTouchListener(new MyTouchListener());
			
			// ʹ��XUtils�еĹ��߽�ͼƬ��down���������ý�iv
			bitmapUtils.display(iv,
					parsedData.data.topnews.get(position).topimage);
			container.addView(iv);
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * ���ڼ���ViewPager��ImageView�Ĵ����¼��ļ�����
	 * ���û�������ʱ�����Զ�����
	 * @author admin
	 *
	 */
	private class MyTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_CANCEL:
				mHanlder.removeCallbacksAndMessages(null);
				mHanlder.sendMessageDelayed(Message.obtain(), 3000);
				break;
			case MotionEvent.ACTION_DOWN:
				mHanlder.removeCallbacksAndMessages(null);
				break;
			case MotionEvent.ACTION_UP:
				mHanlder.removeCallbacksAndMessages(null);
				mHanlder.sendMessageDelayed(Message.obtain(), 3000);
				break;

			default:
				break;
			}
			return false;
		}
		
	}
	/**
	 * �ӷ�������ȡ����
	 * 
	 * @param url
	 */
	public void getDataFromService(final String url) {
		String cacheResult = SharePrefenceUtils.getString(mActivity, url, "");
		if (!TextUtils.isEmpty(cacheResult)) {
			Log.d(debugTag, "�������ݴӻ���");
			parseData(cacheResult, false);
		}
		Log.d(debugTag, "�������ݴ�����");
		// û�л���
		HttpUtils utils = new HttpUtils();

		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// �����ȡʧ��
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mActivity, "���ݻ�ȡʧ��", Toast.LENGTH_SHORT)
								.show();
					}
				});
				lvNewsContent.onRefreshComplete(false);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// �����ȡ�ɹ�
				String result = arg0.result;
				parseData(result, false);
				lvNewsContent.onRefreshComplete(true);
				Log.d(debugTag, "���滺��");
				// ���滺��
				SharePrefenceUtils.setString(mActivity, url, result);
			}
		});

	}

	/**
	 * ��ȡ��������ݴӷ�����
	 * 
	 * @param moreUrl
	 */
	public void getMoreDataFromService(final String moreUrl) {
		String cacheResult = SharePrefenceUtils.getString(mActivity, moreUrl,
				"");
		if (!TextUtils.isEmpty(cacheResult)) {
			Log.d(debugTag, "�������ݴӻ���");
			parseData(cacheResult, true);
//			lvNewsContent.onRefreshComplete(true);
		}
		
		Log.d(debugTag, "�������ݴ�����");
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, moreUrl, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(mActivity, arg1, Toast.LENGTH_SHORT).show();
				lvNewsContent.onRefreshComplete(false);
				Toast.makeText(mActivity, "��ȡ����ʧ��", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				parseData(result, true);
				lvNewsContent.onRefreshComplete(true);
				// ���滺��
				SharePrefenceUtils.setString(mActivity, moreUrl, result);
			}
		});

	}

	/**
	 * ��������
	 * 
	 * @param result
	 *            Ϊ�����ϻ�ȡ����json�������ַ���
	 */
	private void parseData(String result, boolean isLoadMore) {
		Gson gson = new Gson();
		if (!isLoadMore) {
			parsedData = gson.fromJson(result, NewsCenterTabData.class);
			// Log.d("NewsDetailTabPager", parsedData.data.toString());

			if (!TextUtils.isEmpty(parsedData.data.more)) {
				moreUrl = GlobalContants.SERVICE_URL + parsedData.data.more;
			} else {
				moreUrl = null;
			}

			// ����adapter
			MyAdapter adapter = new MyAdapter();
			vpTopNews.setAdapter(adapter);

			newsListAdapter = new NewsListAdapter();
			lvNewsContent.setAdapter(newsListAdapter);

			// ���ü�����
			topNewsIndicator.setViewPager(vpTopNews);

			// ���ó�ʼ����
			topNewsTitle.setText(parsedData.data.topnews.get(0).title);
			topNewsIndicator.onPageSelected(0);
		} else {
			NewsCenterTabData moreData = gson.fromJson(result,
					NewsCenterTabData.class);
			ArrayList<TabNewsData> moreNewsDatas = moreData.data.news;
			newsDatas.addAll(moreNewsDatas);

			if (!TextUtils.isEmpty(moreData.data.more)) {
				moreUrl = moreData.data.more;
			} else {
				moreUrl = null;
			}

			newsListAdapter.notifyDataSetChanged();
		}
		//����ViewPager���Զ�����
		if(mHanlder == null) {
			mHanlder = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					
					int currentItem = vpTopNews.getCurrentItem();
					int count = vpTopNews.getAdapter().getCount();
					currentItem = currentItem + 1;
					
					if(currentItem >= count) {
						currentItem =0;
					}
					vpTopNews.setCurrentItem(currentItem);
					
					mHanlder.sendMessageDelayed(Message.obtain(), 3000);
				}
			};
			mHanlder.sendMessageDelayed(Message.obtain(), 3000);
		};
		
	}

	/**
	 * lvNewsContent����������������ʾ����
	 * 
	 * @author admin
	 *
	 */
	class NewsListAdapter extends BaseAdapter {

		private BitmapUtils bitmapUtils;

		public NewsListAdapter() {
			newsDatas = parsedData.data.news;
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return newsDatas.size();
		}

		@Override
		public Object getItem(int position) {
			return newsDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_news, null);
				holder = new ViewHolder();
				// �������
				holder.tv_news_date = (TextView) convertView
						.findViewById(R.id.tv_news_date);
				holder.tv_news_title = (TextView) convertView
						.findViewById(R.id.tv_news_title);
				holder.iv_pic = (ImageView) convertView
						.findViewById(R.id.iv_pic);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final TabNewsData tabNewsData = newsDatas.get(position);
			holder.tv_news_date.setText(tabNewsData.pubdate);

			// ����Ƿ��Ѿ��Ķ���
			holder.tv_news_title.setText(tabNewsData.title);
			String id = newsDatas.get(position).id;
			String readedIDs = SharePrefenceUtils.getString(mActivity,
					"readed_ids", "");
			if (readedIDs.contains(id)) {
				holder.tv_news_title.setTextColor(Color.GRAY);
			} else {
				holder.tv_news_title.setTextColor(Color.BLACK);
			}

			bitmapUtils.display(holder.iv_pic, tabNewsData.listimage);

			return convertView;
		}

		class ViewHolder {
			public TextView tv_news_title;
			public TextView tv_news_date;
			public ImageView iv_pic;
		}
	}

	/**
	 * ʵ��OnPageChangeListener�����صķ���
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// Log.d("NewsDetailTabPager", "onPageScrolled : " + position + "");
	}

	@Override
	public void onPageSelected(int position) {
		// Log.d("NewsDetailTabPager", position + "");
		// ����TextView
		topNewsTitle.setText(parsedData.data.topnews.get(position).title);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

}
