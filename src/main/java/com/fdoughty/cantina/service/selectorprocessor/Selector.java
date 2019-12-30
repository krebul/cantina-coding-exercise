package com.fdoughty.cantina.service.selectorprocessor;


public class Selector
{
	private String clazz;
	private String className;
	private String identifier;
	
	
	public String getClazz()
	{
		return clazz;
	}
	public void setClazz(String clazz)
	{
		this.clazz = clazz;
	}
	public String getClassName()
	{
		return className;
	}
	public void setClassName(String className)
	{
		this.className = className;
	}
	public String getIdentifier()
	{
		return identifier;
	}
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}
	
	@Override
	public String toString() {
		return (getClazz() == null ? "" : getClazz()) 
				+ (getClassName() == null ? "" : "." + getClassName())
				+ (getIdentifier() == null ? "" : "#" + getIdentifier()); 
	}
	

}
