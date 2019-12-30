package com.fdoughty.cantina;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fdoughty.cantina.service.selectorprocessor.Selector;
import com.fdoughty.cantina.service.selectorprocessor.SelectorProcessorService;

/**
 * Hello world!
 *
 */
public class CantinaCodingExcercise 
{
	public static final String FILE_URL = "https://raw.githubusercontent.com/jdolan/quetoo/master/src/cgame/default/ui/settings/SystemViewController.json";
	private static SelectorProcessorService svc;
	private static final Logger logger = LoggerFactory.getLogger(CantinaCodingExcercise.class);
	
	
    public static void main( String[] args )
    {
    	svc = SelectorProcessorService.getInstance();
        System.out.println( FILE_URL );
        Scanner scanner = new Scanner(System.in);
        try
		{
        	JsonNode rootNode = loadViewData();
        	
        	while (true) {
				
        		System.out.println("Please enter a selector.");
				String selectorStr = scanner.next();
				Selector selector = svc.parseSelector(selectorStr);
				List<String> results = svc.findNodeFromView(rootNode, selector, null);
				for (String result : results) {
					logger.info(result);
				}
				logger.info("{} result(s) found.", results.size());
        	}
        	
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
        scanner.close();
		
    }

    
    
    private static JsonNode loadViewData() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
    	ObjectMapper mapper = new ObjectMapper();

    	//JSON URL to Java object
    	//SystemView obj = mapper.readValue(new URL(FILE_URL), SystemView.class);
    	JsonNode root = mapper.readTree(new URL(FILE_URL));
    	
    	
    	return root;
    }
}
