/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j;

import cz.muni.pb138.log4j.model.LoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private LoggerFactory patternLoggerFactoryXML;
    private List<String> patternLoggerFactoryProp;
    
    @Before
    public void setUp() {
        patternLoggerFactoryXML = new LoggerFactory();
        patternLoggerFactoryXML.setClassName("com.my.log.MyLoggerFactory");
        patternLoggerFactoryXML.addParam("messageBundle", "MyLoggerBundle");
        patternLoggerFactoryXML.addParam("xyz", "XYZ");        
        
        patternLoggerFactoryProp = new ArrayList<String>();
        patternLoggerFactoryProp.add("");
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
        
        assertEquals(patternLoggerFactoryXML, readedLoggerFactory);
    }
    
    @Test
    public void toPropertyTest() {
        
        List<String> ourOutput = patternLoggerFactoryXML.toProperty(new ArrayList<String>(), "");
        
        Collections.sort(ourOutput);
        Collections.sort(patternLoggerFactoryProp);
        assertEquals(ourOutput.size(), patternLoggerFactoryProp.size());
        assertEquals(ourOutput, patternLoggerFactoryProp);
    }
}
