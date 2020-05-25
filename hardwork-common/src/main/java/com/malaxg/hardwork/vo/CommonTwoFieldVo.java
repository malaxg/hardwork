package com.malaxg.hardwork.vo;


import com.malaxg.hardwork.annotation.NonRepeat;

public class CommonTwoFieldVo
{
	@NonRepeat(message = "default-exist-repeat-data")
	private String id;
	
	@NonRepeat
	private String label;
	
	public CommonTwoFieldVo()
	{
	}
	
	public CommonTwoFieldVo(String id, String label)
	{
		this.id = id;
		this.label = label;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("CommonTwoFieldVo{");
		sb.append("id='").append(id).append('\'');
		sb.append(", label='").append(label).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
