package com.dwb.zhbj.utils.bitmaputils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


/**
 * 内存缓存工具
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
	 * 获取Bitmap
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromMemory(String url) {
		return cacheMap.get(url);
//		return cacheMap.get(url).get();
	}
	
	/**
	 * 设置Bitmap
	 * @param url
	 * @param bitmap
	 */
	public void setBitmapFromMemory(String url , Bitmap bitmap) {
		//保存数据
		cacheMap.put(url, bitmap);
//		cacheMap.put(url, new SoftReference<Bitmap>(bitmap));
	}
}
