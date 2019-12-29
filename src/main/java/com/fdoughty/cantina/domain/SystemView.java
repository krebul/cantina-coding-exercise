package com.fdoughty.cantina.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemView extends Element
{
	private List<View> subViews;

	@JsonProperty("subviews")
	public List<View> getSubViews()
	{
		return subViews;
	}

	public void setSubViews(List<View> subViews)
	{
		this.subViews = subViews;
	}
	
	
}
