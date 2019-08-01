package com.example.jett.dn_sqliteframework.update;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: UpdateStep
 * @Description: 数据库升级脚本信息
 */
public class UpdateStep
{
	/**
	 * 旧版本
	 */
	private String versionFrom;

	/**
	 * 新版本
	 */
	private String versionTo;

	/**
	 * 更新数据库脚本
	 */
	private List<UpdateDb> updateDbs;

	// ==================================================

	public UpdateStep(Element ele)
	{
		versionFrom = ele.getAttribute("versionFrom");
		versionTo = ele.getAttribute("versionTo");
		updateDbs = new ArrayList<UpdateDb>();

		NodeList dbs = ele.getElementsByTagName("updateDb");
		for (int i = 0; i < dbs.getLength(); i++)
		{
			Element db = (Element) (dbs.item(i));
			UpdateDb updateDb = new UpdateDb(db);
			this.updateDbs.add(updateDb);
		}
	}

	public List<UpdateDb> getUpdateDbs()
	{
		return updateDbs;
	}

	public void setUpdateDbs(List<UpdateDb> updateDbs)
	{
		this.updateDbs = updateDbs;
	}

	public String getVersionFrom()
	{
		return versionFrom;
	}

	public void setVersionFrom(String versionFrom)
	{
		this.versionFrom = versionFrom;
	}

	public String getVersionTo()
	{
		return versionTo;
	}

	public void setVersionTo(String versionTo)
	{
		this.versionTo = versionTo;
	}

}
