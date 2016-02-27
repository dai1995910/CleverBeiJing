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
 * ����ͼƬ����
 * 
 * @author admin
 *
 */
public class LocalCacheUtils {

	private static String CACHE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/zhihuibj";
	private static String debugTag = "LocalCacheUtils";

	/**
	 * �ӱ��ػ�ȡBitmap����
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
	 * ���ñ���ͼƬ����
	 * 
	 * @param bitmap
	 * @param url
	 */
	public void setBitmapToLocal(Bitmap bitmap, String url) {
		if (bitmap != null) {
			try {
				// ���������ļ���·��
				String fileName = MD5Encoder.encode(url);
				File file = new File(CACHE_PATH, fileName);
				File parentFile = file.getParentFile();
				if (!parentFile.exists()) {
					// ���Ŀ¼�������򴴽�
					parentFile.mkdirs();
				}
				// ����ͼƬ
				Log.d(debugTag, file.toString());
				bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(
						file));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
