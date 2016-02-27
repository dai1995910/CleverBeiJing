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
 * ��ȡͼƬ������
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
	 *  �������ȡ����
	 * @param iv
	 * @param url
	 * @return
	 */
	public void getBitmapFromNet(ImageView iv, String url) {
		new BitMapTask().execute(iv , url);
	}
	
	
	/**
	 * ��һ�����ͣ���������
	 * �ڶ������ͣ����½��ȵķ���
	 * ���������ͣ���doInBackground�ķ��ؽ��
	 * @author admin
	 *
	 */
	class BitMapTask extends AsyncTask<Object, Void, Bitmap> {

		private ImageView iv;
		private String url;

		/**
		 * �ں�̨�����߼����������߳�
		 * ����ֵ���͵�onPostExecute��
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
					//��������
					inputStream = conn.getInputStream();
//					options.inSampleSize = 2;	//�����ѹ��һ��
//					options.inPreferredConfig = Bitmap.Config.
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					return bitmap;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//�ر�����������
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
		 * ��ʱ����������ִ�У����߳�
		 */
		@Override
		protected void onPostExecute(Bitmap result) {
			if(result != null) {
				iv.setImageBitmap(result);
				//��������
				localCacheUtils.setBitmapToLocal(result, url);
				memoryCacheUtils.setBitmapFromMemory(url, result);
			} else {
				//���û���سɹ�������ΪĬ��ͼƬ
				iv.setImageResource(R.drawable.news_pic_default);
			}
		}
		
		/**
		 * ���½��ȣ����߳�
		 */
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}
}
