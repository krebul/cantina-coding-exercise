package com.fdoughty.cantina.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Element
{
	
	private String _class;
	private List<String> classNames;
	private String identifier;

	@JsonProperty("class")
	public String getClazz()
	{
		return _class;
	}

	public void setClazz(String _class)
	{
		this._class = _class;
	}

	@JsonProperty("classNames")
	public List<String> getClassNames()
	{
		return classNames;
	}

	public void setClassNames(List<String> classNames)
	{
		this.classNames = classNames;
	}

	@JsonProperty("identifier")
	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}
	
	
}
