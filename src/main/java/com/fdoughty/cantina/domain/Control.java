package com.fdoughty.cantina.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Control extends Element
{
	@JsonProperty("min")
	private Float min;
	
	@JsonProperty("max")
	private Float max;
	
	@JsonProperty("var")
	private String var;
	
	@JsonProperty("step")
	private Integer step;
	
	@JsonProperty("expectsStringValue")
	private Boolean expectsStringValue;

	public Float getMin()
	{
		return min;
	}

	public void setMin(Float min)
	{
		this.min = min;
	}

	public Float getMax()
	{
		return max;
	}

	public void setMax(Float max)
	{
		this.max = max;
	}

	public String getVar()
	{
		return var;
	}

	public void setVar(String var)
	{
		this.var = var;
	}

	public Integer getStep()
	{
		return step;
	}

	public void setStep(Integer step)
	{
		this.step = step;
	}

	public Boolean getExpectsStringValue()
	{
		return expectsStringValue;
	}

	public void setExpectsStringValue(Boolean expectsStringValue)
	{
		this.expectsStringValue = expectsStringValue;
	}
	
	
	
}
