
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
 * 【用于下拉刷新和底部加载更多的ListView】
 * 
 * 用于替代NewsDetailTabPager中的header_news_list布局中的ListView
 * 本体为ListView通过添加Header和Footer实现。
 * 在NewsDetailTabPager中还会添加一个ViewPager作为Header，因为添加的时间顺序不同所以在这里添加的会在NewsDetailTabPager中添加的Header之上
 * @author admin
 *
 */
public class RefreshListView extends ListView implements OnScrollListener,android.widget.AdapterView.OnItemClickListener{
	private View headerRefresh;	//头布局
	private ImageView ivArrow;	//箭头
	private ProgressBar pbLoading;	//加载ProgressBar
	private TextView tvTips;	//下拉刷新文字提示
	private TextView tvDate;	//最后刷新时间
	private int startY;	//移动开始点Y
	private int headerHeight;	//头布局的高度
	
	private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
	private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
	private static final int STATE_REFRESHING = 2;// 正在刷新
	private int mCurrentState = STATE_PULL_REFRESH;	//当前状态(默认为下拉刷新状态)
	private int padding;
	
	private ImageView ivLoadMore;	//尾部局的加载图标
	private View footerView;
	
	OnRefreshListener onRefreshListener;
	private boolean isLoadingMore = false;
	private int footerHeight;
	
	private boolean flag = false;	//标示是否需要再移动中重置startY

	/**
	 * 
	 * 构造函数部分
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
	 * 初始化头布局
	 */
	private void initHeader() {
		headerRefresh = View.inflate(getContext(), R.layout.header_refresh, null);
		this.addHeaderView(headerRefresh);
		
		//获取Header中的各个组件
		ivArrow = (ImageView) headerRefresh.findViewById(R.id.iv_arrow);
		pbLoading = (ProgressBar) headerRefresh.findViewById(R.id.pb_loading);
		tvTips = (TextView) headerRefresh.findViewById(R.id.tv_tips);
		tvDate = (TextView) headerRefresh.findViewById(R.id.tv_date);
		
		//设置Header
		headerRefresh.measure(0, 0);
		headerHeight = headerRefresh.getMeasuredHeight();
		headerRefresh.setPadding(0, -headerHeight, 0, 0);
	}
	
	/**
	 * 初始化尾部局
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
	 * 重写触摸事件
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//判断触摸事件
		switch(ev.getAction()) {
			case MotionEvent.ACTION_DOWN :{
				//如果点下的时候可见的不是第一个则表示需要再中间重置startY
				if(getFirstVisiblePosition() != 0) {
					flag = true;
				}
				startY = (int) ev.getY();
				break;
			}
			case MotionEvent.ACTION_MOVE :{
				//保证获取正确的Y
				if(startY <= 0) {
					startY = (int) ev.getY();
				}
				//在拖动过程中重置startY
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
					//从其他状态变换为释放刷新
					mCurrentState = STATE_RELEASE_REFRESH;
					changeStateBehavior();
				}
				if(padding < 0 && mCurrentState != STATE_PULL_REFRESH) {
					//从其他状态变为下拉刷新状态
					mCurrentState = STATE_PULL_REFRESH;
					changeStateBehavior();
				}
				//拦截事件
				return true;
			}
			case MotionEvent.ACTION_UP :{
				//进行复原
				startY = -1;
				flag = false;
				if(mCurrentState == STATE_RELEASE_REFRESH) {
					//释放刷新
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
	 * 改变状态后的动作
	 */
	private void changeStateBehavior() {
		switch (mCurrentState) {
			//释放刷新装填
			case STATE_PULL_REFRESH:
				ivArrow.startAnimation(initAnimation(false));
				tvTips.setText("下拉刷新");
				ivArrow.setVisibility(View.VISIBLE);
				pbLoading.setVisibility(View.INVISIBLE);
				break;
			//正在刷新状态
			case STATE_REFRESHING:
				tvTips.setText("正在刷新");
				//必须要清除动画才能隐藏
				ivArrow.clearAnimation();
				ivArrow.setVisibility(View.INVISIBLE);
				pbLoading.setVisibility(View.VISIBLE);
				
//				//开始加载数据
//				onRefreshListener.onRefresh();
				break;
			//等待释放
			case STATE_RELEASE_REFRESH:
				ivArrow.startAnimation(initAnimation(true));
				ivArrow.setVisibility(View.VISIBLE);
				pbLoading.setVisibility(View.INVISIBLE);
				tvTips.setText("释放刷新");
				break;
			}
	}

	/**
	 * 生成动画
	 * @param flag 如果flag为true表示从0到-180；如果flag为false表示从-180到-360
	 * @return 返回一个动画
	 * 
	 */
	private RotateAnimation initAnimation(boolean flag) {
		RotateAnimation animation;
		if(flag) {
			//从0到-180
			animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		} else {
			//从-180到-360
			animation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		}
		animation.setDuration(200);
		animation.setFillAfter(true);
		return animation;
	}

	/**
	 * onScrollListener的方法
	 * @param view
	 * @param scrollState
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE 
				|| scrollState == OnScrollListener.SCROLL_STATE_FLING) {
			//滑动完毕的时候检测到底没有
			if(getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {
				//到了最后一个的时候
				Log.d("RefreshListView", "到底了");
				isLoadingMore = true;	
				footerView.setPadding(0, 0, 0, 0);	//显示footer
				setSelection(getCount());
				
				//调用加载更多的对应逻辑
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
	 * 接口
	 * @author admin
	 *
	 */
	public interface OnRefreshListener {
		public void onRefresh();

		public void onLoadMore();// 加载下一页数据
	}
	
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}
	
	/**
	 * 刷新完毕
	 * @param success
	 */
	public void onRefreshComplete(boolean success) {
		Log.d("RefreshListView", "============ inner : " + isLoadingMore);
		if(isLoadingMore) {
			footerView.setPadding(0, -footerHeight, 0, 0);
			isLoadingMore = false;
		} else {
			mCurrentState = STATE_PULL_REFRESH;
			tvTips.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbLoading.setVisibility(View.INVISIBLE);
			
			headerRefresh.setPadding(0, -headerHeight, 0, 0);
			
			if(success) {
				tvDate.setText("最后刷新时间：" + getCurrentTime());
			}
		}
	}
	
	/**
	 * 获取当前时间
	 * @return 当前时间的字符串
	 */
	private String getCurrentTime() {
		//HH表示24小时计时，MM表示从1月开始而不是0
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}
	
	
	
	OnItemClickListener mListener;
	/**
	 * 重写监听器设置事件来截获数据
	 */
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(this);
		//获取到用户所重写的listener
		mListener = listener;
	}

	/**
	 * 实现OnItemClickListener所重写的方法
	 * 当发生单击事件后会调用这里，所以在这里修改数据后调用用户所写的Listener并传入数据
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

