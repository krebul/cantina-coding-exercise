package com.fdoughty.cantina.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Label extends Element
{
	@JsonProperty("text")
	Text text;

	public Text getText()
	{
		return text;
	}

	public void setText(Text text)
	{
		this.text = text;
	}
	
	
}
