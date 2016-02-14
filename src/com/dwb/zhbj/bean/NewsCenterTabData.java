package com.dwb.zhbj.bean;

import java.util.ArrayList;

/**
 * NewsCenterTab下的每一个详细的数据 例如：北京tab下的数据
 * 
 * @author admin
 */
public class NewsCenterTabData {
	public TabData data;
	public int retcode; // 返回码

	public class TabData {
		public ArrayList<TabNewsData> news;
		public ArrayList<TopicNewsData> topic;
		public ArrayList<TopNewsData> topnews;
		public String more; // 获取更多数据的url
		public String countcommenturl;
		@Override
		public String toString() {
			return "TabData [news=" + news + ", topic=" + topic + ", topnews="
					+ topnews + "]";
		}
		
		
	}

	/**
	 * json下news的类 也就是新闻信息的bean
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
	 * 侧边栏topic下面的数据
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
	 * topnews的bean
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
