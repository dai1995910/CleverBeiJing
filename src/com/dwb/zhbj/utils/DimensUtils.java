package com.dwb.zhbj.utils;

import android.content.Context;

/**
 * px和dp互换的方法
 * @author admin
 *
 */
public class DimensUtils {
	public static float px2dp(Context ctx , int px) {
		//获取到密度
		float density = ctx.getResources().getDisplayMetrics().density;
		
		return px/density;
	}
	
	public static int dp2px(Context ctx , float dp) {
		float desity = ctx.getResources().getDisplayMetrics().density;
		return (int) (dp * desity + 0.5f);
	}
}
