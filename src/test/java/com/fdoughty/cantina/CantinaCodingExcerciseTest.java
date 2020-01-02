package com.fdoughty.cantina;

import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdoughty.cantina.service.selectorprocessor.Selector;
import com.fdoughty.cantina.service.selectorprocessor.SelectorProcessorService;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple CantinaCodingExcercise.
 */
public class CantinaCodingExcerciseTest 
    extends TestCase
{
	public static final String FILE_URL = "https://raw.githubusercontent.com/jdolan/quetoo/master/src/cgame/default/ui/settings/SystemViewController.json";
	private static SelectorProcessorService svc;
	private JsonNode config;
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CantinaCodingExcerciseTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( CantinaCodingExcerciseTest.class );
    }
    
    
    protected void setUp() throws Exception
    {
    	ObjectMapper mapper = new ObjectMapper();
    	config = mapper.readTree(new URL(FILE_URL));
    	svc = SelectorProcessorService.getInstance();
    }

    
    public void testRetrieveSelectorsFromString() throws Exception
    {
    	Selector s = svc.retrieveSelectorsFromString("CvarSelect#textureMode.column");
    	assertEquals("CvarSelect", s.getClazz());
    	assertEquals("textureMode", s.getIdentifier());
    	assertEquals("column", s.getClassName());
    	
    }
    
    public void testSearchSystemView() throws Exception
    {
    	List<String> results; 
    	
    	results = svc.searchSystemView("Input", config);
    	assertEquals(results.size(), 26);
    	results = svc.searchSystemView("StackView.column", config);
    	assertEquals(results.size(), 3);
    }
}
