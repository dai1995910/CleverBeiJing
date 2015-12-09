package com.dwb.zhbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.dwb.zhbj.R;
import com.dwb.zhbj.utils.SharePrefenceUtils;

/**
 * ����ҳ��
 * 
 * @author admin
 *
 */

public class GuideActivity extends Activity {
	// ������Դ
	int[] mImageResources = { R.drawable.guide_1, R.drawable.guide_2,
			R.drawable.guide_3 };

	// ��ʼ��view
	private ViewPager vpGuide;
	private LinearLayout llDotsContainer;
	private View redDot;

	private int distance; // ����dot��ľ���

	private Button btnStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initDots();
		countTwoDotsMargin();
		setListener();

	}

	/**
	 * ��ʼ��View
	 */
	private void initView() {
		setContentView(R.layout.activity_guide);

		vpGuide = (ViewPager) findViewById(R.id.vp_guide);
		vpGuide.setAdapter(new GuidePagerAdapter());

		llDotsContainer = (LinearLayout) findViewById(R.id.ll_dots_container);

		redDot = findViewById(R.id.red_dot);
		
		btnStart = (Button) findViewById(R.id.btn_start);
	}

	/**
	 * ���ڳ�ʼ��pagerview��adpater
	 * 
	 * @author admin
	 *
	 */
	class GuidePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageResources.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView iv = new ImageView(GuideActivity.this);
			iv.setBackgroundResource(mImageResources[position]);
			container.addView(iv);
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * ���ɵ��
	 */
	private void initDots() {
		LinearLayout.LayoutParams params;

		for (int i = 0; i < mImageResources.length; i++) {
			View dot = new View(this);
			params = new LinearLayout.LayoutParams(15, 15);

			if (i > 0) {
				params.leftMargin = 15;
			}
			dot.setLayoutParams(params);
			dot.setBackgroundResource(R.drawable.sharp_dot);

			llDotsContainer.addView(dot);
		}
	}

	/**
	 * ���ü�����
	 */
	private void setListener() {
		//���ð�ť�ļ�����
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(GuideActivity.this , MainActivity.class));
				SharePrefenceUtils.setBooleanValue(GuideActivity.this, "is_user_guide_showed", true);
				finish();
			}
		});
		//����ViewPager�ļ�����
		vpGuide.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// Log.d("debug", "onPageSelected --" + position + "");
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				RelativeLayout.LayoutParams redDotParams = (LayoutParams) redDot
						.getLayoutParams();
				int left = (int) (distance * positionOffset
						+ distance * position);
				redDotParams.leftMargin = left;
				redDot.setLayoutParams(redDotParams);
				
				if(position == mImageResources.length - 1) {
					btnStart.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// Log.d("debug", "state" + state);
			}
		});
	}

	/**
	 * ����������ľ���
	 */
	private void countTwoDotsMargin() {

		/*
		 * ֱ��ͨ��������ȡ�����ǻ�ȡ������ ԭ������Ϊ��oncreate��ʱ����û�н���onmeasure�ģ�����û�취��þ���
		 */

		// int distance = llDotsContainer.getChildAt(0).getLeft() -
		// llDotsContainer.getChildAt(1).getLeft();

		// ͨ�������������������ֽ�����ʱ��
		llDotsContainer.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Log.d("debug", "layout ����");
						distance = llDotsContainer.getChildAt(1).getLeft()
								- llDotsContainer.getChildAt(0).getLeft();
						Log.d("debug", distance + "");
						//�����������ֹ��ε���
						llDotsContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
				});
	}
}
