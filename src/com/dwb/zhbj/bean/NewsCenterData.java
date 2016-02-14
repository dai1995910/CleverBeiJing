package com.dwb.zhbj.bean;

import java.util.ArrayList;

public class NewsCenterData {
	public ArrayList<SlingMenuData> data;
	int retcode;

	@Override
	public String toString() {
		return "NewsCenterData [data=" + data + "]";
	}

	public class SlingMenuData {
		public String id;
		public String title;
		public int type;
		public String url;
		public ArrayList<NewsTabData> children;

		@Override
		public String toString() {
			return "SlingMenuData [id=" + id + ", title=" + title
					+ ", children=" + children + "]";
		}
	}
	public	class NewsTabData {
		public String id;
		public String title;
		public int type;
		public String url;
		
		@Override
		public String toString() {
			return "NewsData [title=" + title + ", type=" + type + "]";
		}
	}
}
