/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j;

import static org.junit.Assert.*;

import cz.muni.pb138.log4j.model.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.ImmutableDescriptor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jozef
 */
public class LoggerTest {
    private Logger patternLoggerXML;
    private Logger testLogger;
    private List<String> patternLoggerProp;

    public static Logger CreateLogger(
            String name, 
            Map<String, String> params, 
            List<String> apps, 
            String add, 
            String custClass, 
            boolean isRoot) {
        
        Logger l = new Logger();
        l.setName(name);
        
        if(params != null){
            for(Map.Entry<String, String> e : params.entrySet()) l.addParam(e.getKey(), e.getValue());
        }
        
        if(apps != null) {
            for(String a : apps) l.addAppenderName(a);
        }
        
        l.setAdditivity(add);
        
        l.setRootLogger(isRoot);
        
        l.setCustomClass(custClass);
        
        return l;
    }
    
    @Before
    public void setUp() {
        
        Map<String, String> params = new HashMap();
        params.put("DebugEnabled", "false");
        List<String> apps = new ArrayList<String>();
        apps.add("appendJedna");
        apps.add("appendDva");
        patternLoggerXML = CreateLogger("com.google.name",params,apps,"false","",false);
        patternLoggerXML.setLoggerLevel(patternLoggerXML.CreateLoggerLevel("WARN", null, ""));
        
        
        patternLoggerProp = new ArrayList<String>();
        patternLoggerProp.add("");
        patternLoggerProp.add("log4j.logger.com.google.name = WARN, appendJedna, appendDva");
        patternLoggerProp.add("log4j.additivity.com.google.name = false");
        patternLoggerProp.add("log4j.logger.com.google.name.DebugEnabled = false");
    }

    @Test
    public void setUpFromElementTest() {
        
        Logger readedLogger = new Logger();
                
        Document document = null;
        try {
            document = (new SAXReader()).read("unitTestFiles/patternLogger.xml");
        } catch (DocumentException ex) {
            fail();
        }
        
        Element rootElement = document.getRootElement();
        
        readedLogger.setUpFromElement(rootElement);
        
        assertEquals(patternLoggerXML, readedLogger);
    }
    
    @Test
    public void toPropertyTest() {
        
        List<String> ourOutput = patternLoggerXML.toProperty(new ArrayList<String>());
        
        Collections.sort(ourOutput);
        Collections.sort(patternLoggerProp);
        assertEquals(ourOutput.size(), patternLoggerProp.size());
        assertEquals(ourOutput, patternLoggerProp);
    }
    
    @Test
    public void verifyTest() {
        /*testLogger = new Logger();
        
        testLogger.setAdditivity("not-false");
        try{
            testLogger.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        
        
        //class name and name with space
        testLogger.setAdditivity("false");         
        testLogger.setName("com. google. name");
        try{
            testLogger.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        
        testLogger.setName("com. google.name");
        testLogger.setCustomClass("com. google. name");
        try{
            testLogger.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        } 
        testLogger.setCustomClass("com.google.name");
        
        testLogger.setLoggerLevel("WARNING");
        try{
            testLogger.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        testLogger.setLoggerLevel("WARN");
        testLogger.addAppenderName("appendJedna s");
        try{
            testLogger.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        } */
    }
}
