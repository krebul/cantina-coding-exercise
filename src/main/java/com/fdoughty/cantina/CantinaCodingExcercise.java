package com.fdoughty.cantina;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdoughty.cantina.domain.SystemView;
import com.fdoughty.cantina.service.selectorprocessor.SelectorProcessorService;

/**
 * Hello world!
 *
 */
public class CantinaCodingExcercise 
{
	public static final String FILE_URL = "https://raw.githubusercontent.com/jdolan/quetoo/master/src/cgame/default/ui/settings/SystemViewController.json";
	private static SelectorProcessorService svc;
	
	
    public static void main( String[] args )
    {
    	svc = SelectorProcessorService.getInstance();
        System.out.println( FILE_URL );
        try
		{
			loadInputData();
			findElement(".Input");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
    }
    
    private static List<String> findElement(String selector) throws Exception
    {
    	List<String> results = new ArrayList<>();
    	svc.parseSelector(selector);
    	return results;
    }
    
    
    private static SystemView loadInputData() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
    	ObjectMapper mapper = new ObjectMapper();

    	//JSON URL to Java object
    	SystemView obj = mapper.readValue(new URL(FILE_URL), SystemView.class);
    	return obj;
    }
}
