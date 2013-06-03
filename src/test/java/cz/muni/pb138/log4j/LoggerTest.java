/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j;

import cz.muni.pb138.log4j.model.Configuration;
import static org.junit.Assert.*;

import cz.muni.pb138.log4j.model.Logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
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
    private Logger patternLogger;    
    private Logger patternLoggerNoParam;
    private Logger testLogger;
    private List<String> patternLoggerProp;

    public static Logger CreateLogger(
            String name, 
            Map<String, String> params, 
            List<String> apps, 
            String add, 
            String loggerClass, 
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
        
        l.setLoggerClass(loggerClass);
        
        return l;
    }
    
    @Before
    public void setUp() {
        
        Map<String, String> params = new HashMap();
        params.put("DebugEnabled", "false");
        List<String> apps = new ArrayList<String>();
        apps.add("appendJedna");
        apps.add("appendDva");
        patternLogger = CreateLogger("com.google.name",params,apps,"false","",false);
        patternLogger.setLoggerLevel(patternLogger.CreateLoggerLevel("WARN", null, ""));
        
        patternLoggerNoParam = CreateLogger("fi.muni.cz.Logger", null, apps, "true", "", false);
        patternLoggerNoParam.setLoggerLevel(patternLoggerNoParam.CreateLoggerLevel("ERROR", null, ""));
        
        patternLoggerProp = new ArrayList<String>();
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
        
        assertEquals(patternLogger, readedLogger);
    }
    
    @Test
    public void toPropertyTest() {
        
        List<String> ourOutput = patternLogger.toProperty(new ArrayList<String>());
        
        Collections.sort(ourOutput);
        Collections.sort(patternLoggerProp);
        assertEquals(ourOutput.size(), patternLoggerProp.size());
        assertEquals(ourOutput, patternLoggerProp);
    }
    
    @Test
    public void verifyTest() {
        testLogger = new Logger();
        
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
        testLogger.setLoggerClass("com. google. name");
        try{
            testLogger.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        } 
        testLogger.setLoggerClass("com.google.name");
        
        try{
            testLogger.setLoggerLevel(testLogger.CreateLoggerLevel("WARN-ING", null, null));
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        
        testLogger.setLoggerLevel(testLogger.CreateLoggerLevel("WARN", null, null));
        testLogger.addAppenderName("appendJedna s");
        try{
            testLogger.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
    }
    
    @Test
    public void fromPropertiesToModelTest() throws FileNotFoundException, IOException{
        Properties properties = new Properties();
        properties.load(new FileInputStream("unitTestFiles/patternLogger.properties"));

        // now you have properties object and you can work with it.
        Configuration configuration = new Configuration();

        for (String propertyKey : properties.stringPropertyNames()) {
            String key = propertyKey;                     
            String value = properties.getProperty(propertyKey);   
            if (key.toLowerCase(Locale.ENGLISH).startsWith("log4j")) {
                String newKey = key.substring(6); // remove initial "log4j."
                configuration.addConfig(newKey, value);
            } else {
                AppUtils.crash("every property name must start with 'log4j': " + key);
            }
        }
        
        assertEquals(1, configuration.getLoggers().size());
        Logger outLogger = configuration.getLoggers().get("fi.muni.cz.Logger");
        assertEquals(patternLoggerNoParam, outLogger);
    }
}
