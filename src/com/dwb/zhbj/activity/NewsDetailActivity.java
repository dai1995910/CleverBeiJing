package com.dwb.zhbj.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.widget.ImageView;

import com.dwb.zhbj.R;

/**
 * 显示新闻详情的页面
 * @author admin
 *
 */
public class NewsDetailActivity extends Activity implements OnClickListener{
	private ImageView ivBack;
	private ImageView ivTextSize;
	private ImageView ivShare;
	private WebView wvNewsContents;
	private String url;
	private int mCurrentChosedTextSize = 2;
	private String debugTag = "NewsDetailActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);
		
		initView();
		initData();
		setData();
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivTextSize = (ImageView) findViewById(R.id.iv_text_size);
		ivShare = (ImageView) findViewById(R.id.iv_share);
		wvNewsContents = (WebView) findViewById(R.id.wv_news_contents);
		
		/**
		 * 设置监听事件
		 */
		
		ivBack.setOnClickListener(this);
		ivShare.setOnClickListener(this);
		ivTextSize.setOnClickListener(this);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
	}
	
	/**
	 * 设置数据
	 */
	private void setData() {
		setWebViewAttribute();
		wvNewsContents.loadUrl(url);
	}

	/**
	 * 设置WebView属性
	 */
	private void setWebViewAttribute() {
		WebSettings settings = wvNewsContents.getSettings();
		
		//设置支持js
		settings.setJavaScriptEnabled(true);
	}

	/**
	 * 单击事件
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			//返回
			case R.id.iv_back: {
				finish();
				break;
			}
			
			//字体设置
			case R.id.iv_text_size: {
				Log.d(debugTag, "单击了字体设置");
				showTextSizeDialog();
				break;
			}
			
			//分享
			case R.id.iv_share: {
				break;
			}
		}
	}

	/**
	 * 显示字体设置对话框
	 */
	private void showTextSizeDialog() {
		String[] items = {"超大号字体" , "大号字体" ,"正常字体" ,"小号字体" ,"超小号字体"};
		
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("字体大小设置");
		builder.setSingleChoiceItems(items, mCurrentChosedTextSize, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mCurrentChosedTextSize = which;
			}
		});
		
		//确认按钮
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				WebSettings settings = wvNewsContents.getSettings();
				switch (mCurrentChosedTextSize) {
				case 0:
					settings.setTextSize(TextSize.LARGEST);
					break;
				case 1:
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					settings.setTextSize(TextSize.SMALLEST);
					break;
				}
			}
		});
		
		//取消按钮
		builder.setNegativeButton("取消", null);
		builder.show();
	}

}
