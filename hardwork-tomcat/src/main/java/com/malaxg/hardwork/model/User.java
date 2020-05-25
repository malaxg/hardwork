package com.malaxg.hardwork.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.malaxg.hardwork.annotation.NonRepeat;

/**
 * @author wangrong
 */
public class User extends Person
{
	
	private String id;
	@NonRepeat
	private String name;
	@Min(18)
	private int age;
	@NotBlank(message = "用户名不能为空啊")
	private String userName;
	private String userEmail;
	private String userTelephone;
	private String information;
	private int isFromIT;
	private String englishname;
	private String chinesename;
	private String departName;
	
	public String getName()
	{
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge()
	{
		return age;
	}
	
	public void setAge(int age)
	{
		this.age = age;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public String getUserEmail()
	{
		return userEmail;
	}
	
	public void setUserEmail(String userEmail)
	{
		this.userEmail = userEmail;
	}
	
	public String getUserTelephone()
	{
		return userTelephone;
	}
	
	public void setUserTelephone(String userTelephone)
	{
		this.userTelephone = userTelephone;
	}
	
	public String getInformation()
	{
		return information;
	}
	
	public void setInformation(String information)
	{
		this.information = information;
	}
	
	public int getIsFromIT()
	{
		return isFromIT;
	}
	
	public void setIsFromIT(int isFromIT)
	{
		this.isFromIT = isFromIT;
	}
	
	public String getEnglishname()
	{
		return englishname;
	}
	
	public void setEnglishname(String englishname)
	{
		this.englishname = englishname;
	}
	
	public String getChinesename()
	{
		return chinesename;
	}
	
	public void setChinesename(String chinesename)
	{
		this.chinesename = chinesename;
	}
	
	public String getDepartName()
	{
		return departName;
	}
	
	public void setDepartName(String departName)
	{
		this.departName = departName;
	}
	
	@Override public String toString()
	{
		final StringBuilder sb = new StringBuilder("User{");
		sb.append("id='").append(id).append('\'');
		sb.append(", name='").append(name).append('\'');
		sb.append(", age=").append(age);
		sb.append(", userName='").append(userName).append('\'');
		sb.append(", userEmail='").append(userEmail).append('\'');
		sb.append(", userTelephone='").append(userTelephone).append('\'');
		sb.append(", information='").append(information).append('\'');
		sb.append(", isFromIT=").append(isFromIT);
		sb.append(", englishname='").append(englishname).append('\'');
		sb.append(", chinesename='").append(chinesename).append('\'');
		sb.append(", departName='").append(departName).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
