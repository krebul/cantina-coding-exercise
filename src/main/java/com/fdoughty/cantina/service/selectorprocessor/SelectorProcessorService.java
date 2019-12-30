package com.fdoughty.cantina.service.selectorprocessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
	
	/* parseSelector
	 * Input: String containing search selector
	 * Output: Selector object containing data parsed from selector string
	 */
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
	
	public List<String> findNodeFromView(JsonNode rootNode, Selector selector, String path) throws Exception {
		List<String> results = new ArrayList<>();
		JsonNode theNode = null;
		if (path == null)
			path = "/";
		
		//logger.debug("Searching for node [{}] at [{}]", selector.getSelectorName(), path);
		switch (selector.getType()) {
			case CLASS:
				if (!rootNode.path("class").isMissingNode()) {
					if (rootNode.path("class").textValue().equalsIgnoreCase(selector.getSelectorName())) {
						results.add(path + rootNode.path("class").textValue());
						
					}
				}
				break;
			case CLASSNAME:
				if (!rootNode.path("classNames").isMissingNode()) {
					ArrayNode classArr = (ArrayNode) rootNode.path("classNames");
					if (classArr != null) {
						for (int index = 0; index < classArr.size(); index++)
						{
							if (classArr.get(index).textValue().equalsIgnoreCase(selector.getSelectorName()))
								results.add(path + "." + classArr.get(index).textValue());
						}
						
					}
				}
				break;
			case IDENTIFIER:
				if (!rootNode.path("identifier").isMissingNode()) {
					if (rootNode.path("identifier").textValue().equalsIgnoreCase(selector.getSelectorName())) {
						results.add(path + "#" + rootNode.path("identifier").textValue());
						
					}
				}
				break;
			default:
				throw new Exception("Unknown Selector Type: " + selector.getType());
		}
		
		Iterator<Entry<String, JsonNode>> i = rootNode.fields();
		while (i.hasNext()) {
			Entry<String, JsonNode> entry = i.next();
			JsonNode field = rootNode.get(entry.getKey());
			if (field.isObject()) {
				results.addAll(findNodeFromView(field, selector, path + entry.getKey() + "/"));
			}
			
			else if (field.isArray()) {
				for (JsonNode svNode : field){
					results.addAll(findNodeFromView(svNode, selector, path + entry.getKey() + "/"));
				}
			}
		}
		return results;
	}
}
