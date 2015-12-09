package com.dwb.zhbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.dwb.zhbj.R;
import com.dwb.zhbj.utils.SharePrefenceUtils;

/**
 * 开始的闪屏页面
 * 
 * @author admin
 *
 */
public class SplishActivity extends Activity {

	private RelativeLayout rlSplishRoot;
	private AnimationSet animationSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initAnimation();
		initListener();
	}
	
	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_splish);
		rlSplishRoot = (RelativeLayout) findViewById(R.id.rl_splish_root);
	}

	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		//参数表示集合中的动画是否公用一个插补器
		animationSet = new AnimationSet(false);
		
		RotateAnimation rotate = new RotateAnimation(0, -360,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(1000);

		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(1000);

		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		scale.setDuration(1000);
		
		animationSet.addAnimation(rotate);
		animationSet.addAnimation(alpha);
		animationSet.addAnimation(scale);
		
		rlSplishRoot.startAnimation(animationSet);
		
	}
	
	/**
	 * 添加动画监听器，保证什么时候进行跳转
	 */
	private void initListener() {
		animationSet.setAnimationListener(new AnimationListener() {
			
			//当动画开始的时候
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			//当动画重复的时候
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			//当动画结束的时候,跳转到下一个界面
			@Override
			public void onAnimationEnd(Animation animation) {
				boolean flag = SharePrefenceUtils.getBooleanValue(SplishActivity.this, "is_user_guide_showed", false);
				if(flag) {
					startActivity(new Intent(SplishActivity.this , MainActivity.class));
				} else {
					startActivity(new Intent(SplishActivity.this, GuideActivity.class));
				}
				finish();
			}
		});
	}
}
