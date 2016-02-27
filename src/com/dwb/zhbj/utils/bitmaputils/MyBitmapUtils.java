package com.dwb.zhbj.utils.bitmaputils;

import com.dwb.zhbj.R;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * �������Xutils��ͼƬ���ߵ�BitmapUtils����
 * 
 * @author admin
 *
 */
public class MyBitmapUtils {
	private static String DEBUGTAG = "MyBitmapUtils";
	private LocalCacheUtils localCacheUtils;
	private MemoryCacheUtils memoryCacheUtils;
	private NetCacheUtils netCacheUtils;
	
	public MyBitmapUtils() {
		localCacheUtils = new LocalCacheUtils();
		memoryCacheUtils = new MemoryCacheUtils();
		netCacheUtils = new NetCacheUtils(localCacheUtils , memoryCacheUtils);
	}
	
	
	/**
	 * ��ʾͼƬ
	 * 
	 * @param iv
	 * @param url
	 */
	public void display(ImageView iv, String url) {
		
		// Ϊ�˷�ֹImageView�����õ�ʱ����ͼƬ�ظ�������һ��������ΪĬ��ͼƬ
		iv.setImageResource(R.drawable.news_pic_default);
		Bitmap bitmap = null;
		
		// ���ڴ��л�ȡͼƬ
		bitmap = memoryCacheUtils.getBitmapFromMemory(url);
		if(bitmap != null) {
			//������
			iv.setImageBitmap(bitmap);
			Log.d(DEBUGTAG, "���ڴ��л�ȡ����");
			return;
		}
		
		// �ӱ��ػ�ȡͼƬ
		bitmap = localCacheUtils.getBitmapFromLocal(url);
		if (bitmap != null) {
			// ������,����ͼƬ�����������ϻ�������
			iv.setImageBitmap(bitmap);
			Log.d(DEBUGTAG, "��ȡ���ݴӱ���");
			//�������ݵ��ڴ���
			memoryCacheUtils.setBitmapFromMemory(url, bitmap);
			return;
		}

		// �������ȡͼƬ
		netCacheUtils.getBitmapFromNet(iv, url);
	}
}
