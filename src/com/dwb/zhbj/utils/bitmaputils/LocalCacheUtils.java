package com.dwb.zhbj.utils.bitmaputils;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.dwb.zhbj.utils.MD5Encoder;

/**
 * 本地图片工具
 * 
 * @author admin
 *
 */
public class LocalCacheUtils {

	private static String CACHE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/zhihuibj";
	private static String debugTag = "LocalCacheUtils";

	/**
	 * 从本地获取Bitmap对象
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromLocal(String url) {
		try {
			File localFile = new File(CACHE_PATH, MD5Encoder.encode(url));
			if (localFile.exists()) {
				Bitmap bitmap = BitmapFactory.decodeFile(localFile.toString());
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 设置本地图片对象
	 * 
	 * @param bitmap
	 * @param url
	 */
	public void setBitmapToLocal(Bitmap bitmap, String url) {
		if (bitmap != null) {
			try {
				// 构建保存文件的路径
				String fileName = MD5Encoder.encode(url);
				File file = new File(CACHE_PATH, fileName);
				File parentFile = file.getParentFile();
				if (!parentFile.exists()) {
					// 如果目录不存在则创建
					parentFile.mkdirs();
				}
				// 保存图片
				Log.d(debugTag, file.toString());
				bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(
						file));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
