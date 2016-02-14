
package com.dwb.zhbj.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.dwb.zhbj.R;

/**
 * ����������ˢ�º͵ײ����ظ����ListView��
 * 
 * �������NewsDetailTabPager�е�header_news_list�����е�ListView
 * ����ΪListViewͨ�����Header��Footerʵ�֡�
 * ��NewsDetailTabPager�л������һ��ViewPager��ΪHeader����Ϊ��ӵ�ʱ��˳��ͬ������������ӵĻ���NewsDetailTabPager����ӵ�Header֮��
 * @author admin
 *
 */
public class RefreshListView extends ListView implements OnScrollListener,android.widget.AdapterView.OnItemClickListener{
	private View headerRefresh;	//ͷ����
	private ImageView ivArrow;	//��ͷ
	private ProgressBar pbLoading;	//����ProgressBar
	private TextView tvTips;	//����ˢ��������ʾ
	private TextView tvDate;	//���ˢ��ʱ��
	private int startY;	//�ƶ���ʼ��Y
	private int headerHeight;	//ͷ���ֵĸ߶�
	
	private static final int STATE_PULL_REFRESH = 0;// ����ˢ��
	private static final int STATE_RELEASE_REFRESH = 1;// �ɿ�ˢ��
	private static final int STATE_REFRESHING = 2;// ����ˢ��
	private int mCurrentState = STATE_PULL_REFRESH;	//��ǰ״̬(Ĭ��Ϊ����ˢ��״̬)
	private int padding;
	
	private ImageView ivLoadMore;	//β���ֵļ���ͼ��
	private View footerView;
	
	OnRefreshListener onRefreshListener;
	private boolean isLoadingMore = false;
	private int footerHeight;
	
	private boolean flag = false;	//��ʾ�Ƿ���Ҫ���ƶ�������startY

	/**
	 * 
	 * ���캯������
	 * 
	 */

	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initHeader();
		initFooter();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeader();
		initFooter();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeader();
		initFooter();
	}
	
	/**
	 * ��ʼ��ͷ����
	 */
	private void initHeader() {
		headerRefresh = View.inflate(getContext(), R.layout.header_refresh, null);
		this.addHeaderView(headerRefresh);
		
		//��ȡHeader�еĸ������
		ivArrow = (ImageView) headerRefresh.findViewById(R.id.iv_arrow);
		pbLoading = (ProgressBar) headerRefresh.findViewById(R.id.pb_loading);
		tvTips = (TextView) headerRefresh.findViewById(R.id.tv_tips);
		tvDate = (TextView) headerRefresh.findViewById(R.id.tv_date);
		
		//����Header
		headerRefresh.measure(0, 0);
		headerHeight = headerRefresh.getMeasuredHeight();
		headerRefresh.setPadding(0, -headerHeight, 0, 0);
	}
	
	/**
	 * ��ʼ��β����
	 */
	private void initFooter() {
		footerView = View.inflate(getContext(), R.layout.footer_load_more, null);
		this.addFooterView(footerView);
		
		ivLoadMore = (ImageView) footerView.findViewById(R.id.iv_load_more);
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(300);
		rotateAnimation.setFillAfter(true);
		rotateAnimation.setRepeatCount(-1);
		ivLoadMore.startAnimation(rotateAnimation);
		
		footerView.measure(0, 0);
		footerHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, -footerHeight, 0, 0);
		
		setOnScrollListener(this);
	}
	
	/**
	 * ��д�����¼�
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//�жϴ����¼�
		switch(ev.getAction()) {
			case MotionEvent.ACTION_DOWN :{
				//������µ�ʱ��ɼ��Ĳ��ǵ�һ�����ʾ��Ҫ���м�����startY
				if(getFirstVisiblePosition() != 0) {
					flag = true;
				}
				startY = (int) ev.getY();
				break;
			}
			case MotionEvent.ACTION_MOVE :{
				//��֤��ȡ��ȷ��Y
				if(startY <= 0) {
					startY = (int) ev.getY();
				}
				//���϶�����������startY
				if(getFirstVisiblePosition() == 0 && flag == true) {
					startY = (int) ev.getY();
					flag = false;
				}
				int dY = (int) (ev.getY() - startY);
				if(mCurrentState == STATE_REFRESHING) {
					break;
				}
				if(dY <= 0 || getFirstVisiblePosition() != 0) {
					break;
				}
				
				padding = dY - headerHeight;
				headerRefresh.setPadding(0, padding, 0, 0);
				if(padding >= 0 && mCurrentState != STATE_RELEASE_REFRESH) {
					//������״̬�任Ϊ�ͷ�ˢ��
					mCurrentState = STATE_RELEASE_REFRESH;
					changeStateBehavior();
				}
				if(padding < 0 && mCurrentState != STATE_PULL_REFRESH) {
					//������״̬��Ϊ����ˢ��״̬
					mCurrentState = STATE_PULL_REFRESH;
					changeStateBehavior();
				}
				//�����¼�
				return true;
			}
			case MotionEvent.ACTION_UP :{
				//���и�ԭ
				startY = -1;
				flag = false;
				if(mCurrentState == STATE_RELEASE_REFRESH) {
					//�ͷ�ˢ��
					mCurrentState = STATE_REFRESHING;
					headerRefresh.setPadding(0, 0, 0, 0);
					if(onRefreshListener != null) {
						onRefreshListener.onRefresh();
					}
					changeStateBehavior();
				} else if(mCurrentState == STATE_PULL_REFRESH) {
					headerRefresh.setPadding(0, -headerHeight, 0, 0);
				}
				break;
			}
		}
		return super.onTouchEvent(ev);
	}
	
	/**
	 * �ı�״̬��Ķ���
	 */
	private void changeStateBehavior() {
		switch (mCurrentState) {
			//�ͷ�ˢ��װ��
			case STATE_PULL_REFRESH:
				ivArrow.startAnimation(initAnimation(false));
				tvTips.setText("����ˢ��");
				ivArrow.setVisibility(View.VISIBLE);
				pbLoading.setVisibility(View.INVISIBLE);
				break;
			//����ˢ��״̬
			case STATE_REFRESHING:
				tvTips.setText("����ˢ��");
				//����Ҫ���������������
				ivArrow.clearAnimation();
				ivArrow.setVisibility(View.INVISIBLE);
				pbLoading.setVisibility(View.VISIBLE);
				
//				//��ʼ��������
//				onRefreshListener.onRefresh();
				break;
			//�ȴ��ͷ�
			case STATE_RELEASE_REFRESH:
				ivArrow.startAnimation(initAnimation(true));
				ivArrow.setVisibility(View.VISIBLE);
				pbLoading.setVisibility(View.INVISIBLE);
				tvTips.setText("�ͷ�ˢ��");
				break;
			}
	}

	/**
	 * ���ɶ���
	 * @param flag ���flagΪtrue��ʾ��0��-180�����flagΪfalse��ʾ��-180��-360
	 * @return ����һ������
	 * 
	 */
	private RotateAnimation initAnimation(boolean flag) {
		RotateAnimation animation;
		if(flag) {
			//��0��-180
			animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		} else {
			//��-180��-360
			animation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		}
		animation.setDuration(200);
		animation.setFillAfter(true);
		return animation;
	}

	/**
	 * onScrollListener�ķ���
	 * @param view
	 * @param scrollState
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE 
				|| scrollState == OnScrollListener.SCROLL_STATE_FLING) {
			//������ϵ�ʱ���⵽��û��
			if(getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {
				//�������һ����ʱ��
				Log.d("RefreshListView", "������");
				isLoadingMore = true;	
				footerView.setPadding(0, 0, 0, 0);	//��ʾfooter
				setSelection(getCount());
				
				//���ü��ظ���Ķ�Ӧ�߼�
				if(onRefreshListener != null) {
					Log.d("RefreshListView", "yes its there");
					onRefreshListener.onLoadMore();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	 
	/**
	 * �ӿ�
	 * @author admin
	 *
	 */
	public interface OnRefreshListener {
		public void onRefresh();

		public void onLoadMore();// ������һҳ����
	}
	
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}
	
	/**
	 * ˢ�����
	 * @param success
	 */
	public void onRefreshComplete(boolean success) {
		Log.d("RefreshListView", "============ inner : " + isLoadingMore);
		if(isLoadingMore) {
			footerView.setPadding(0, -footerHeight, 0, 0);
			isLoadingMore = false;
		} else {
			mCurrentState = STATE_PULL_REFRESH;
			tvTips.setText("����ˢ��");
			ivArrow.setVisibility(View.VISIBLE);
			pbLoading.setVisibility(View.INVISIBLE);
			
			headerRefresh.setPadding(0, -headerHeight, 0, 0);
			
			if(success) {
				tvDate.setText("���ˢ��ʱ�䣺" + getCurrentTime());
			}
		}
	}
	
	/**
	 * ��ȡ��ǰʱ��
	 * @return ��ǰʱ����ַ���
	 */
	private String getCurrentTime() {
		//HH��ʾ24Сʱ��ʱ��MM��ʾ��1�¿�ʼ������0
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}
	
	
	
	OnItemClickListener mListener;
	/**
	 * ��д�����������¼����ػ�����
	 */
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(this);
		//��ȡ���û�����д��listener
		mListener = listener;
	}

	/**
	 * ʵ��OnItemClickListener����д�ķ���
	 * �����������¼���������������������޸����ݺ�����û���д��Listener����������
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mListener.onItemClick(parent, view, position - getHeaderViewsCount(), id);
	}
}

