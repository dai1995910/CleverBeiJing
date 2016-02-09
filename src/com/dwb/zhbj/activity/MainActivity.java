package com.dwb.zhbj.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Slide;
import android.view.Window;
import android.widget.FrameLayout;

import com.dwb.zhbj.R;
import com.dwb.zhbj.fragment.ContentFragment;
import com.dwb.zhbj.fragment.SlideMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
	private FrameLayout flContent;
	public static String CONTENT_FRAGMENT = "content_fragment_tag";
	public static String SLINGMENU_FRAGMENT = "slingmenu_fragment_tag";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//���ò����
		setBehindContentView(R.layout.slide_menu);
		//��ȡ�����
		SlidingMenu slidingMenu = getSlidingMenu();
		//���ô�����ʽ
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setBehindOffset(300);
		initFragment();
	}

	private void initFragment() {
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		//�����滻,�����ϱ�ǩ
		transaction.replace(R.id.fl_content, new ContentFragment(),CONTENT_FRAGMENT);
		transaction.replace(R.id.fl_slide_menu, new SlideMenuFragment(),SLINGMENU_FRAGMENT);
		
		transaction.commit();
	}
	
	/**
	 * ��ȡ�������Fragment
	 * @return
	 */
	public SlideMenuFragment getSlideMenuFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentByTag(SLINGMENU_FRAGMENT);
		return (SlideMenuFragment)fragment;
	}
	/**
	 * ��ȡ��CONTENT_FRAGMENT
	 * @return
	 */
	public ContentFragment getContentFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentByTag(CONTENT_FRAGMENT);
		return (ContentFragment)fragment;
	}


}
