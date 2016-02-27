package com.dwb.zhbj.utils.bitmaputils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import com.dwb.zhbj.R;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * 获取图片从网络
 * @author admin
 *
 */
public class NetCacheUtils {
	private String DEBUGTAG = "NetCacheUtils";
	private LocalCacheUtils localCacheUtils;
	private MemoryCacheUtils memoryCacheUtils;
	public NetCacheUtils(LocalCacheUtils localCacheUtils,
			MemoryCacheUtils memoryCacheUtils) {
		this.localCacheUtils = localCacheUtils; 
		this.memoryCacheUtils = memoryCacheUtils;
	}


	/**
	 *  从网络获取数据
	 * @param iv
	 * @param url
	 * @return
	 */
	public void getBitmapFromNet(ImageView iv, String url) {
		new BitMapTask().execute(iv , url);
	}
	
	
	/**
	 * 第一个泛型：参数类型
	 * 第二个泛型：更新进度的泛型
	 * 第三个泛型：是doInBackground的返回结果
	 * @author admin
	 *
	 */
	class BitMapTask extends AsyncTask<Object, Void, Bitmap> {

		private ImageView iv;
		private String url;

		/**
		 * 在后台进行逻辑，处于子线程
		 * 返回值发送到onPostExecute中
		 */
		@Override
		protected Bitmap doInBackground(Object... params) {
			url = (String) params[1];
			iv = (ImageView) params[0];
			HttpURLConnection conn = null;
			InputStream inputStream = null;
			try {
				conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setReadTimeout(5000);
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				conn.connect();
				
				if(conn.getResponseCode() == 200) {
					//连接正常
					inputStream = conn.getInputStream();
//					options.inSampleSize = 2;	//长宽各压缩一半
//					options.inPreferredConfig = Bitmap.Config.
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					return bitmap;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//关闭流，和连接
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				conn.disconnect();
				
			}
			return null;
		}
		
		/**
		 * 耗时方法结束后，执行，主线程
		 */
		@Override
		protected void onPostExecute(Bitmap result) {
			if(result != null) {
				iv.setImageBitmap(result);
				//保存数据
				localCacheUtils.setBitmapToLocal(result, url);
				memoryCacheUtils.setBitmapFromMemory(url, result);
			} else {
				//如果没下载成功，设置为默认图片
				iv.setImageResource(R.drawable.news_pic_default);
			}
		}
		
		/**
		 * 更新进度，主线程
		 */
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}
}
