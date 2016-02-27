package com.dwb.zhbj.pager.detail;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwb.zhbj.R;
import com.dwb.zhbj.bean.PhotosData;
import com.dwb.zhbj.bean.PhotosData.PhotoData.ItemData;
import com.dwb.zhbj.global.GlobalContants;
import com.dwb.zhbj.utils.bitmaputils.MyBitmapUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 侧边栏“组图”页面的实现
 * 
 * @author admin
 *
 */
public class PhotoDetailPager extends BaseMenuDetailPager implements
		OnClickListener {

	private GridView gvPhotos;
	private ListView lvPhotos;
	private ImageView ivChangeListStyle;
	private PhotosData parsedData;
	private boolean LIST_VISIBLE = true;
	private boolean GRID_VISIBLE = false;
	private MyListAdapter adapter;

	public PhotoDetailPager(Activity activity, ImageView ivChangeList) {
		super(activity);
		this.ivChangeListStyle = ivChangeList;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.activity_photos, null);
		lvPhotos = (ListView) view.findViewById(R.id.lv_photos);
		gvPhotos = (GridView) view.findViewById(R.id.gv_photos);

		return view;
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void initData() {
		// 获取数据
		getDataFromService();

		ivChangeListStyle.setOnClickListener(this);
	}

	/**
	 * 获取数据从服务器
	 */
	private void getDataFromService() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalContants.Photos_URL,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(mActivity, "获取数据失败：" + arg1,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String result = arg0.result;
						parseData(result);
					}

				});
	}

	/**
	 * 解析数据
	 * 
	 * @param result
	 */
	private void parseData(String result) {
		Gson gson = new Gson();
		parsedData = gson.fromJson(result, PhotosData.class);

		if (parsedData.data.news != null) {
			adapter = new MyListAdapter();
			lvPhotos.setAdapter(adapter);
			gvPhotos.setAdapter(adapter);
		}
	}

	/**
	 * 使用的适配器
	 * 
	 * @author admin
	 */
	class MyListAdapter extends BaseAdapter {
		private BitmapUtils utils;
		private MyBitmapUtils myUtils;

		public MyListAdapter() {
			utils = new BitmapUtils(mActivity);
			myUtils = new MyBitmapUtils();
			utils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return parsedData.data.news.size();
		}

		@Override
		public Object getItem(int position) {
			return parsedData.data.news.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_photos,
						null);
				holder = new ViewHolder();
				holder.ivImage = (ImageView) convertView
						.findViewById(R.id.iv_image);
				holder.tvImageTitle = (TextView) convertView
						.findViewById(R.id.tv_image_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

				ItemData data = parsedData.data.news.get(position);
				myUtils.display(holder.ivImage, data.getListimage());

				holder.tvImageTitle.setText(data.getTitle());

			}
			return convertView;
		}

		class ViewHolder {
			ImageView ivImage;
			TextView tvImageTitle;
		}
	}

	/**
	 * 单击事件
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		/*
		 * 切换显示模式
		 */
		if (LIST_VISIBLE) {
			LIST_VISIBLE = false;
			GRID_VISIBLE = true;

			ivChangeListStyle.setImageResource(R.drawable.icon_pic_list_type);
			lvPhotos.setVisibility(View.GONE);
			gvPhotos.setVisibility(View.VISIBLE);
		} else if (GRID_VISIBLE) {
			LIST_VISIBLE = true;
			GRID_VISIBLE = false;

			ivChangeListStyle.setImageResource(R.drawable.icon_pic_grid_type);
			lvPhotos.setVisibility(View.VISIBLE);
			gvPhotos.setVisibility(View.GONE);
		}
	}

}
