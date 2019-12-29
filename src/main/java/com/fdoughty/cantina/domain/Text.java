package com.fdoughty.cantina.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Text extends Element
{
	@JsonProperty("text")
	private String text;

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
	
	
	
}
