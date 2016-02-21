package com.dwb.zhbj.bean;

import java.util.ArrayList;

import android.app.LocalActivityManager;
import android.location.LocationManager;


/**
 * 组图部分的Gson，javabean
 * @author admin
 *
 */
public class PhotosData {
	int retcode;	//返回码
	public PhotoData data;	//组图界面数据
	/**
	 * 组图界面数据类
	 * @author admin
	 */
	public class PhotoData {
		public ArrayList<ItemData> news;	//item数据数组
		public String title;
		
		@Override
		public String toString() {
			return title;
		}
		
		/**
		 * 组图界面item数据
		 * @author admin
		 */
		public class ItemData {
			boolean comment;
			String commentlist;
			String commenturl;
			String id;
			String largeimage;
			String listimage;
			String pubdate;
			String smallimage;
			String title;
			String type;
			String url;
			
			public boolean isComment() {
				return comment;
			}

			public void setComment(boolean comment) {
				this.comment = comment;
			}

			public String getCommentlist() {
				return commentlist;
			}

			public void setCommentlist(String commentlist) {
				this.commentlist = commentlist;
			}

			public String getCommenturl() {
				return commenturl;
			}

			public void setCommenturl(String commenturl) {
				this.commenturl = commenturl;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getLargeimage() {
				return largeimage;
			}

			public void setLargeimage(String largeimage) {
				this.largeimage = largeimage;
			}

			public String getListimage() {
				return listimage;
			}

			public void setListimage(String listimage) {
				this.listimage = listimage;
			}

			public String getPubdate() {
				return pubdate;
			}

			public void setPubdate(String pubdate) {
				this.pubdate = pubdate;
			}

			public String getSmallimage() {
				return smallimage;
			}

			public void setSmallimage(String smallimage) {
				this.smallimage = smallimage;
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}

			@Override
			public String toString() {
				return title + ":" +  type;
			}
		}
	}
}
