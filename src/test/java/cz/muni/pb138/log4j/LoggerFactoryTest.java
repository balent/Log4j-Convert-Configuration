/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j;

import cz.muni.pb138.log4j.model.Configuration;
import cz.muni.pb138.log4j.model.LoggerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jozef
 */
public class LoggerFactoryTest {
    private LoggerFactory patternLoggerFactory;    
    private LoggerFactory testLoggerFactory;
    private List<String> patternLoggerFactoryProp;
    
    @Before
    public void setUp() {
        patternLoggerFactory = new LoggerFactory();
        patternLoggerFactory.setClassName("com.my.log.MyLoggerFactory");
        patternLoggerFactory.addParam("messageBundle", "MyLoggerBundle");
        patternLoggerFactory.addParam("xyz", "XYZ");        
        
        patternLoggerFactoryProp = new ArrayList<String>();
        patternLoggerFactoryProp.add("log4j.loggerFactory = com.my.log.MyLoggerFactory");
        patternLoggerFactoryProp.add("log4j.loggerFactory.messageBundle = MyLoggerBundle");
        patternLoggerFactoryProp.add("log4j.loggerFactory.xyz = XYZ");        
    }

    @Test
    public void setUpFromElementTest() {
        
        LoggerFactory readedLoggerFactory = new LoggerFactory();
                
        Document document = null;
        try {
            document = (new SAXReader()).read("unitTestFiles/patternLoggerFactory.xml");
        } catch (DocumentException ex) {
            fail();
        }
        
        Element rootElement = document.getRootElement();
        
        readedLoggerFactory.setUpFromElement(rootElement);
        
        assertEquals(patternLoggerFactory, readedLoggerFactory);
    }
    
    @Test
    public void toPropertyTest() {
        
        List<String> ourOutput = patternLoggerFactory.toProperty(new ArrayList<String>(), "");
        
        Collections.sort(ourOutput);
        Collections.sort(patternLoggerFactoryProp);
        assertEquals(ourOutput.size(), patternLoggerFactoryProp.size());
        assertEquals(ourOutput, patternLoggerFactoryProp);
    }
    
    @Test
    public void verifyTest() {
        testLoggerFactory = new LoggerFactory();
        testLoggerFactory.verify();
        
        testLoggerFactory.setClassName("com.my.log.MyLoggerFactory ");
        try {
            testLoggerFactory.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        testLoggerFactory.setClassName("com.my.log.MyLoggerFactory");
        testLoggerFactory.addParam("messageBundle ", "MyLoggerBundle");
        try {
            testLoggerFactory.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
    }
    
    @Test
    public void fromPropertiesToModelTest() throws FileNotFoundException, IOException{
        Properties properties = new Properties();
        properties.load(new FileInputStream("unitTestFiles/patternLoggerFactory.properties"));

        // now you have properties object and you can work with it.
        Configuration configuration = new Configuration();

        for (String propertyKey : properties.stringPropertyNames()) {
            String key = propertyKey.toLowerCase(Locale.ENGLISH);                     
            String value = properties.getProperty(propertyKey).toLowerCase(Locale.ENGLISH);   
            if (key.toLowerCase(Locale.ENGLISH).startsWith("log4j")) {
                String newKey = key.substring(6); // remove initial "log4j."
                configuration.addConfig(newKey, value);
            } else {
                AppUtils.crash("every property name must start with 'log4j': " + key);
            }
        }
        
        assertEquals(patternLoggerFactory, configuration.getLoggerFactory());
    }
}
