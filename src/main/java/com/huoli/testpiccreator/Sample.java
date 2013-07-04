/*******************************************************
 * Copyright 2011 - 2013 HuoLi Tek Corp., Ltd.
 * All rights reserved.
 *
 * Description	:
 * History  	:
 * (ID, Date, Author, Description)
 *
 *******************************************************/
package com.huoli.testpiccreator;

import gui.ava.html.image.generator.HtmlImageGenerator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.huoli.testpiccreator.model.TestModel;
import com.huoli.testpiccreator.model.TestModel.SubModel;

/**
 * @author LuoJ@huoLi.com
 * 
 */
public class Sample
{
	public static void main(String[] args) throws ClassNotFoundException
	{
		// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;

		for (int i = 0; i < 4090; i++) {
			try
			{
				// create a database connection
				connection = DriverManager.getConnection("jdbc:sqlite:hairstyle_db");
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(30); // set timeout to 30 sec.

				ResultSet rs = statement.executeQuery("select * from hair_style limit " + i + ",1");
				while (rs.next())
				{
					int id = rs.getInt("_id");
					String title = rs.getString("title");

					Statement s = connection.createStatement();
					s.setQueryTimeout(30); // set timeout to 30 sec.
					ResultSet r = statement.executeQuery("select * from hair_style_content where style_id = " + id);
					List<SubModel> list = new ArrayList<SubModel>();
					while (r.next()) {
						SubModel sm = new SubModel();
						String img = r.getString("pic_url");
						if (img == null || "".equals(img)) {
							sm.setContent(r.getString("content_desc"));
						} else {
							sm.setContent("http://www.faxingw.cn" + img);
							sm.setPic(true);
						}
						list.add(sm);
					}
					TestModel t = new TestModel();
					t.setList(list);
					t.setTitle(title);
					createImg(t);
				}
			} catch (SQLException e)
			{
				// if the error message is "out of memory",
				// it probably means no database file is found
				System.err.println(e.getMessage());
			} finally
			{
				try
				{
					if (connection != null)
						connection.close();
				} catch (SQLException e)
				{
					// connection close failed.
					System.err.println(e);
				}
			}
		}
	}

	public static final String START = "<body width='480px'><table width='480px' align='center' style='background-color: #FFFFFF'>";
	public static final String TITLE_START = "<tr align='center'><td><p color='#8c7415' style='max-width: 460px; font-size :27px'>";
	public static final String TITLE_END = "</p></td></tr>";
	public static final String CONTENT_START = "<tr align='left'><td><p style='text-indent: 40px; max-width: 460px; font-size :20px'; padding: 10px 0 10px 5px'>";
	public static final String CONTENT_END = "</p></td></tr>";
	public static final String IMG_START = "<tr align='center'><td><img style='max-width: 460px;' src='";
	public static final String IMG_END = "'/></td></tr>";
	public static final String END = "</table></body>";

	/**
	 * @param t
	 */
	private static void createImg(TestModel t) {
		System.out.println(t);
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		StringBuilder sb = new StringBuilder(START);
		sb.append(TITLE_START).append(t.getTitle()).append(TITLE_END);
		for (SubModel sm : t.getList()) {
			if (sm.isPic()) {
				sb.append(IMG_START).append(sm.getContent()).append(IMG_END);
			} else {
				if (sm.getContent().startsWith("发型屋小编猜")
						|| sm.getContent().startsWith("发型屋编辑")
						|| sm.getContent().contains("猜你喜欢")) {
					break;
				}
				sb.append(CONTENT_START).append(sm.getContent()).append(CONTENT_END);
			}
		}
		sb.append(END);
		imageGenerator.loadHtml(sb.toString());
		System.out.println(sb.toString());
		imageGenerator.getBufferedImage();
		imageGenerator.saveAsImage("d:/" + t.getTitle() + ".png");
	}
}