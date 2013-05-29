/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j;

import static org.junit.Assert.*;

import cz.muni.pb138.log4j.model.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private List<String> patternLoggerProp;
    
    @Before
    public void setUp() {
        patternLoggerXML = new Logger();
        patternLoggerXML.setName("com.google.name");
        patternLoggerXML.addAppenderName("appendJedna");
        patternLoggerXML.addAppenderName("appendDva");        
        patternLoggerXML.setAdditivity("false");
        patternLoggerXML.setLoggerLevel("WARN");
        patternLoggerXML.addParam("DebugEnabled", "false");
        
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
}
