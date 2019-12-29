package com.fdoughty.cantina.service.selectorprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fdoughty.cantina.service.selectorprocessor.Selector.SelectorType;


public class SelectorProcessorService
{
	private static SelectorProcessorService singleton = null;
	private static final Logger logger = LoggerFactory.getLogger(SelectorProcessorService.class);

	private SelectorProcessorService()
	{
		
	}
	
	public static SelectorProcessorService getInstance() {
		if (singleton == null)
			singleton = new SelectorProcessorService();
		return singleton;
	}
	
	
	public Selector parseSelector(String selectorStr) throws Exception {
		Selector selector = new Selector();
		logger.debug("processing selector [{}]", selectorStr);
		if (selectorStr == null || selectorStr.length() <= 0)
			throw new Exception("Invalid Selector: " + selectorStr);
		
		// figure out the type of selector based on the first character:
		// "selector" = class
		// ".selector" = className
		// "#selector" = identifier
		if (selectorStr.matches("^[a-zA-Z0-9_]*$"))
		{
			selector.setType(SelectorType.CLASS);
			selector.setSelectorName(selectorStr);
		}
		else if (selectorStr.matches("^\\..*$"))
		{
			selector.setType(SelectorType.CLASSNAME);
			selector.setSelectorName(selectorStr.substring(1));
		}
		else if (selectorStr.matches("^\\#.*$"))
		{
			selector.setType(SelectorType.IDENTIFIER);
			selector.setSelectorName(selectorStr.substring(1));
		}
		else
			throw new Exception("Invalid Selector: " + selectorStr);
		
		logger.debug("SelectorType [{}]", selector.getType());
		logger.debug("SelectorName [{}]", selector.getSelectorName());
		
		return selector;
	}
}
