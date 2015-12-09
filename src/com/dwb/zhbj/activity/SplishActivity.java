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
 * ��ʼ������ҳ��
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
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_splish);
		rlSplishRoot = (RelativeLayout) findViewById(R.id.rl_splish_root);
	}

	/**
	 * ��ʼ������
	 */
	private void initAnimation() {
		//������ʾ�����еĶ����Ƿ���һ���岹��
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
	 * ��Ӷ�������������֤ʲôʱ�������ת
	 */
	private void initListener() {
		animationSet.setAnimationListener(new AnimationListener() {
			
			//��������ʼ��ʱ��
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			//�������ظ���ʱ��
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			//������������ʱ��,��ת����һ������
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
