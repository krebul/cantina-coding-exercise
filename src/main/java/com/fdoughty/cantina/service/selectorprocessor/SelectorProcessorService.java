package com.fdoughty.cantina.service.selectorprocessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;



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
			selector.setClazz(selectorStr);
		else if (selectorStr.matches("^\\..*$"))
			selector.setClassName(selectorStr.substring(1));
		else if (selectorStr.matches("^\\#.*$"))
			selector.setIdentifier(selectorStr.substring(1));
		else
			throw new Exception("Invalid Selector: " + selectorStr);
		
		
		return selector;
	}
	
	public List<String> findNodeFromView(JsonNode rootNode, Selector selector, String path) throws Exception {
		List<String> results = new ArrayList<>();
		
		if (path == null)
			path = "/";
		
		//logger.debug("Searching for node [{}] at [{}]", selector.toString(), path);
		// Check if this node matches the selector
		if (isMatch(rootNode, selector)) {
			results.add(path);
		}
		
		
		// Iterate through all this node's children, and check them as well
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
	
	private boolean isMatch(JsonNode node, Selector selector) {
		String classStr = "";
		String identifierStr = "";
		boolean match = true;
		
		if (!node.path("class").isMissingNode())
			classStr = node.path("class").textValue();
		if (!node.path("identifier").isMissingNode())
			identifierStr = node.path("identifier").textValue();
		
		//logger.debug("Selector Class [{}], Node Class [{}]", selector.getClazz(), classStr);
		//logger.debug("Selector Identifier [{}], Node Identifier [{}]", selector.getIdentifier(), identifierStr);
		
		if (selector.getClassName() != null) {
			if (node.path("classNames").isMissingNode())
				match = false;
			else
			{
				ArrayNode classArr = (ArrayNode) node.path("classNames");
				if (classArr != null) {
					int index; 
					for (index = 0; index < classArr.size(); index++)
					{
						if (classArr.get(index).textValue().equalsIgnoreCase(selector.getClassName()))
							break;
					}
					if (index >= classArr.size())
						match = false;
				}
			}
		}
		
		if (selector.getClazz() != null && !classStr.equalsIgnoreCase(selector.getClazz()))
			match = false;
		if (selector.getIdentifier() != null && !identifierStr.equalsIgnoreCase(selector.getIdentifier()))
			match = false;
	
		
		return match;
	}
}
