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
	
	public List<String> searchSystemView(String selector, JsonNode config) throws Exception
	{
		if (!selector.matches("^[a-zA-Z0-9_\\.\\#]*$"))
			throw new Exception("Invalid Selector: " + selector);
		Selector s = retrieveSelectorsFromString(selector);
		return findNodeFromView(config, s, null);
	}
	
	/* retrieveSelectorsFromString
	 * Description: Take a selector string and break it down into it's component selectors.
	 * Ex: CvarSelect#textureMode will be broken up into two selectors: CvarSelect and #textureMode
	 */
	public Selector retrieveSelectorsFromString(String inputStr) throws Exception {
		Selector s = new Selector();
		if (inputStr == null)
			throw new Exception("Invalid selector: Must not be NULL");
		
		while (true) {
			String modifier = "";
			String selector = "";
			if (inputStr.indexOf(".") > 0 || inputStr.indexOf("#") > 0) {
				if (inputStr.indexOf(".") > 0)
					modifier = ".";
				if (inputStr.indexOf("#") > 0)
					if (inputStr.indexOf(".") <= 0 || inputStr.indexOf("#") < inputStr.indexOf("."))
					modifier = "#";
			
				selector = inputStr.substring(0, inputStr.indexOf(modifier));
				inputStr = inputStr.substring(inputStr.indexOf(modifier));
			}
			else
				selector = inputStr; 
				
			if (findSelectorType(selector) == SelectorType.CLASS) {
				s.setClazz(selector);
				if (selector.length() <= 0)
					throw new Exception("Invalid selector: " + selector);
			}
			else if (findSelectorType(selector) == SelectorType.IDENTIFIER) {
				s.setIdentifier(selector.substring(1));
				if (selector.length() <= 1)
					throw new Exception("Invalid selector: " + selector);
			}
			else if (findSelectorType(selector) == SelectorType.CLASSNAME) {
				s.setClassName(selector.substring(1));
				if (selector.length() <= 1)
					throw new Exception("Invalid selector: " + selector);
			}
				
			if (modifier.equals(""))
				break;
		}
		return s;
		
	}
	
	/* parseSelector
	 * Input: String containing search selector
	 * Output: Selector object containing data parsed from selector string
	 */
	private SelectorType findSelectorType(String selectorStr) throws Exception {
		logger.debug("Processing selector [{}]", selectorStr);
		
		
		// figure out the type of selector based on the first character:
		// "selector" = class
		// ".selector" = className
		// "#selector" = identifier
		if (selectorStr.matches("^[a-zA-Z0-9_]*$"))
			return SelectorType.CLASS;
		else if (selectorStr.matches("^\\..*$"))
			return SelectorType.CLASSNAME;
		else if (selectorStr.matches("^\\#.*$"))
			return SelectorType.IDENTIFIER;
		else
			throw new Exception("Invalid Selector: " + selectorStr);
		
	}
	
	/* findNodeFromView
	 * Traverse the Json tree in search for nodes matching the selector
	 */
	private List<String> findNodeFromView(JsonNode rootNode, Selector selector, String path) throws Exception {
		List<String> results = new ArrayList<>();
		
		if (path == null)
			path = "/";
		
		logger.debug("Searching for node [{}] at [{}]", selector.toString(), path);
		// Check if this node matches the selector
		if (isMatch(rootNode, selector)) {
			results.add(path + selector.toString());
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
		
		logger.debug("Selector Class [{}], Node Class [{}]", selector.getClazz(), classStr);
		logger.debug("Selector Identifier [{}], Node Identifier [{}]", selector.getIdentifier(), identifierStr);
		
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
