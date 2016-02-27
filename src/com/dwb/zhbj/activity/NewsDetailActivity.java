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
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.dwb.zhbj.R;

/**
 * ��ʾ���������ҳ��
 * 
 * @author admin
 *
 */
public class NewsDetailActivity extends Activity implements OnClickListener {
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
	 * ��ʼ��UI
	 */
	private void initView() {
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivTextSize = (ImageView) findViewById(R.id.iv_text_size);
		ivShare = (ImageView) findViewById(R.id.iv_share);
		wvNewsContents = (WebView) findViewById(R.id.wv_news_contents);

		/**
		 * ���ü����¼�
		 */

		ivBack.setOnClickListener(this);
		ivShare.setOnClickListener(this);
		ivTextSize.setOnClickListener(this);
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
	}

	/**
	 * ��������
	 */
	private void setData() {
		setWebViewAttribute();
		wvNewsContents.loadUrl(url);
	}

	/**
	 * ����WebView����
	 */
	private void setWebViewAttribute() {
		WebSettings settings = wvNewsContents.getSettings();

		// ����֧��js
		settings.setJavaScriptEnabled(true);
	}

	/**
	 * �����¼�
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ����
		case R.id.iv_back: {
			finish();
			break;
		}

		// ��������
		case R.id.iv_text_size: {
			Log.d(debugTag, "��������������");
			showTextSizeDialog();
			break;
		}

		// ����
		case R.id.iv_share: {
			showShare();
			break;
		}
		}
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();

		// ����ʱNotification��ͼ������� 2.5.9�Ժ�İ汾�����ô˷���
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(getString(R.string.share));
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		oks.setTitleUrl("http://sharesdk.cn");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("���Ƿ����ı�");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		oks.setImagePath("/sdcard/zhihuibj/test.jpg");// ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl("http://sharesdk.cn");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("���ǲ��������ı�");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl("http://sharesdk.cn");

		// ��������GUI
		oks.show(this);
	}

	/**
	 * ��ʾ�������öԻ���
	 */
	private void showTextSizeDialog() {
		String[] items = { "���������", "�������", "��������", "С������", "��С������" };

		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("�����С����");
		builder.setSingleChoiceItems(items, mCurrentChosedTextSize,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mCurrentChosedTextSize = which;
					}
				});

		// ȷ�ϰ�ť
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

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

		// ȡ����ť
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}

}
