package com.dwb.zhbj.bean;

import java.util.ArrayList;

/**
 * NewsCenterTab�µ�ÿһ����ϸ������ ���磺����tab�µ�����
 * 
 * @author admin
 */
public class NewsCenterTabData {
	public TabData data;
	public int retcode; // ������

	public class TabData {
		public ArrayList<TabNewsData> news;
		public ArrayList<TopicNewsData> topic;
		public ArrayList<TopNewsData> topnews;
		public String more; // ��ȡ�������ݵ�url
		public String countcommenturl;
		@Override
		public String toString() {
			return "TabData [news=" + news + ", topic=" + topic + ", topnews="
					+ topnews + "]";
		}
		
		
	}

	/**
	 * json��news���� Ҳ����������Ϣ��bean
	 */
	public class TabNewsData {
		public boolean comment;
		public String commentlist;
		public String commenturl;
		public String id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;

		@Override
		public String toString() {
			return "TabNewsDetail [title=" + title + "]";
		}
	}

	/**
	 * �����topic���������
	 */
	public class TopicNewsData {
		public String description;
		public String id;
		public String listiamge;
		public int sort;
		public String title;
		public String url;

		@Override
		public String toString() {
			return "TopicNewsData [title=" + title + "]";
		}
	}

	/**
	 * topnews��bean
	 */
	public class TopNewsData {
		public boolean comment;
		public String commentlist;
		public String commenturl;
		public String id;
		public String pubdate;
		public String title;
		public String topimage;
		public String type;
		public String url;

		@Override
		public String toString() {
			return "TopNewsData [title=" + title + "]";
		}
	}
}
