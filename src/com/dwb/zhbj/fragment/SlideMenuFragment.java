package com.dwb.zhbj.fragment;

import java.util.ArrayList;

import com.dwb.zhbj.R;
import com.dwb.zhbj.activity.MainActivity;
import com.dwb.zhbj.bean.NewsCenterData;
import com.dwb.zhbj.bean.NewsCenterData.SlingMenuData;
import com.dwb.zhbj.pager.NewsCenterPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.ComponentCallbacks;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * �����
 * @author admin
 *
 */
public class SlideMenuFragment extends BaseFragment{

	private View view;
	//�����չʾ���ݵ�ListView
	private ListView lvSlideMune;
	private ArrayList<SlingMenuData> dataList;
	//��ǰ��ѡ�е�item
	private int mChosedPosition;
	private MyAdapter adapter;

	
	@Override
	public View initView() {
		view = View.inflate(mActivity,R.layout.fragment_slide_menu ,null);
		return view;
	}
	
	/**
	 * ����������ϻ�ȡ��������,�����ò����
	 * @param parsedData
	 */
	public void setSldingMenuData(NewsCenterData parsedData) {
		dataList = parsedData.data;
		lvSlideMune = (ListView) view.findViewById(R.id.lv_slide_menu);
		adapter = new MyAdapter();
		lvSlideMune.setAdapter(adapter);
		lvSlideMune.setOnItemClickListener(new MyListener());
	}
	
	class MyListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mChosedPosition = position;
			adapter.notifyDataSetChanged();
			
			//�ı�NewsContentPager������
			ContentFragment contentFragment = ((MainActivity)mActivity).getContentFragment();
			NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
//			//����������Ĭ��Ϊ�������������Ҫ��������initData�н�������
//			((MainActivity)mActivity).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			newsCenterPager.setDetailPager(mChosedPosition , dataList.get(position).title);
			//�رղ����
			((MainActivity)mActivity).getSlidingMenu().toggle();
		}
	}
	/**
	 * ����ListView��Adapter
	 * @author admin
	 *
	 */
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.item_slide_menu, null);
			TextView item = (TextView) view.findViewById(R.id.tv_slidemenu_item);
			item.setText(dataList.get(position).title);
			if(position == mChosedPosition) {
				item.setEnabled(true);
			} else {
				item.setEnabled(false);
			}
			return view;
		}
	}
}
