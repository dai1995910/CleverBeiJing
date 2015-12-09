package com.dwb.zhbj.activity;

import android.app.Activity;
import android.os.Bundle;

import com.dwb.zhbj.R;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_main);
	}
}
