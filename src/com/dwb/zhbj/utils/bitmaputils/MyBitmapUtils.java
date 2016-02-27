package com.dwb.zhbj.utils.bitmaputils;

import com.dwb.zhbj.R;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * 用于替代Xutils的图片工具的BitmapUtils工具
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
	 * 显示图片
	 * 
	 * @param iv
	 * @param url
	 */
	public void display(ImageView iv, String url) {
		
		// 为了防止ImageView被重用的时候导致图片重复，所以一来就设置为默认图片
		iv.setImageResource(R.drawable.news_pic_default);
		Bitmap bitmap = null;
		
		// 从内存中获取图片
		bitmap = memoryCacheUtils.getBitmapFromMemory(url);
		if(bitmap != null) {
			//有数据
			iv.setImageBitmap(bitmap);
			Log.d(DEBUGTAG, "从内存中获取数据");
			return;
		}
		
		// 从本地获取图片
		bitmap = localCacheUtils.getBitmapFromLocal(url);
		if (bitmap != null) {
			// 有数据,设置图片并跳过从网上虎丘数据
			iv.setImageBitmap(bitmap);
			Log.d(DEBUGTAG, "获取数据从本地");
			//保存数据到内存中
			memoryCacheUtils.setBitmapFromMemory(url, bitmap);
			return;
		}

		// 从网络读取图片
		netCacheUtils.getBitmapFromNet(iv, url);
	}
}
