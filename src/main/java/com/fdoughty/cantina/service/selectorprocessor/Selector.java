package com.fdoughty.cantina.service.selectorprocessor;

public class Selector
{
	public enum SelectorType {
		CLASS, CLASSNAME, IDENTIFIER
	}
	
	private SelectorType type;
	private String selectorName;
	public SelectorType getType()
	{
		return type;
	}
	public void setType(SelectorType type)
	{
		this.type = type;
	}
	public String getSelectorName()
	{
		return selectorName;
	}
	public void setSelectorName(String selectorName)
	{
		this.selectorName = selectorName;
	}
	
	

}
