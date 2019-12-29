package com.fdoughty.cantina.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class View extends Element
{
	@JsonProperty("subviews")
	private List<View> views;
	
	@JsonProperty("label")
	private Label label;
	
	@JsonProperty("title")
	private Text title;
	
	@JsonProperty("contentView")
	private View contentView;
	
	@JsonProperty("control")
	private Control control;

	public List<View> getViews()
	{
		return views;
	}

	public void setViews(List<View> views)
	{
		this.views = views;
	}

	public Label getLabel()
	{
		return label;
	}

	public void setLabel(Label label)
	{
		this.label = label;
	}

	public View getContentView()
	{
		return contentView;
	}

	public void setContentView(View contentView)
	{
		this.contentView = contentView;
	}

	public Control getControl()
	{
		return control;
	}

	public void setControl(Control control)
	{
		this.control = control;
	}

	public Text getTitle()
	{
		return title;
	}

	public void setTitle(Text title)
	{
		this.title = title;
	}
	
	
}
