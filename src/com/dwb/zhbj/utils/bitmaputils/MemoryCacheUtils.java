package com.dwb.zhbj.utils.bitmaputils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


/**
 * �ڴ滺�湤��
 * @author admin
 *
 */
public class MemoryCacheUtils {
	
//	private HashMap<String, SoftReference<Bitmap>> cacheMap;
	private LruCache<String, Bitmap> cacheMap;

	
	public MemoryCacheUtils() {
//		cacheMap = new HashMap<String, SoftReference<Bitmap>>();
		long maxMemory = Runtime.getRuntime().maxMemory();
		cacheMap = new LruCache<String, Bitmap>((int) (maxMemory/8));
	}
	/**
	 * ��ȡBitmap
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromMemory(String url) {
		return cacheMap.get(url);
//		return cacheMap.get(url).get();
	}
	
	/**
	 * ����Bitmap
	 * @param url
	 * @param bitmap
	 */
	public void setBitmapFromMemory(String url , Bitmap bitmap) {
		//��������
		cacheMap.put(url, bitmap);
//		cacheMap.put(url, new SoftReference<Bitmap>(bitmap));
	}
}
