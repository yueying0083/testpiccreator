/*******************************************************
 * Copyright 2011 - 2013 HuoLi Tek Corp., Ltd.
 * All rights reserved.
 *
 * Description	:
 * History  	:
 * (ID, Date, Author, Description)
 *
 *******************************************************/
package com.huoli.testpiccreator.model;

import java.util.List;

import com.google.gson.Gson;

/**
 * @author LuoJ@huoLi.com
 * 
 */
public class TestModel {
	private String title;
	private List<SubModel> list;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<SubModel> getList() {
		return list;
	}

	public void setList(List<SubModel> list) {
		this.list = list;
	}

	public static class SubModel {

		private String content;
		private boolean isPic;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public boolean isPic() {
			return isPic;
		}

		public void setPic(boolean isPic) {
			this.isPic = isPic;
		}

	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
