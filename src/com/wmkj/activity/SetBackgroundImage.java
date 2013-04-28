package com.wmkj.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

/**
 * 
 * @Name: com.wmkj.activity.SplashActivity SetBackgroundImage.java
 * @Author: nylqd
 * @Date: 2012-11-3
 * @Description:
 * 
 */
public class SetBackgroundImage {
	
	public static void changeBackgroundImage(int i) {

	}

	/**
	 * 设置背景图片
	 * 
	 * @param activity
	 * @param view
	 *            
	 */
	public static void setBackGround(Activity activity, View view) {
		String bg = getBG(activity);
		if (bg != null) {
			if ("bg1".equals(bg)) {
				view.setBackgroundResource(R.drawable.bg01);
				
			}
			if ("bg2".equals(bg)) {
				view.setBackgroundResource(R.drawable.bg02);
				
			}
			if ("bg3".equals(bg)) {
				view.setBackgroundResource(R.drawable.bg03);
				
			}
		} else {
			saveBackground(activity, "bg1");
			view.setBackgroundResource(R.drawable.bg01);
		}

	}

	/**
	 * 更改默认背景图片
	 * 
	 * @param activity
	 * @param imageTag
	 */
	public static void saveBackground(Activity activity, String imageTag) {
		SharedPreferences preferences = activity.getSharedPreferences("bg",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("background", imageTag);
		editor.commit();
	}

	/**
	 * 得到默认图片标识
	 * 
	 * @param activity
	 * @return
	 */
	private static String getBG(Activity activity) {
		SharedPreferences preferences = activity.getSharedPreferences("bg",
				Activity.MODE_PRIVATE);
		return preferences.getString("background", null);

	}
}
