/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j;

import cz.muni.pb138.log4j.model.Appender;
import cz.muni.pb138.log4j.model.Configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.dom4j.io.SAXReader;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jozef
 */
public class AppenderTest {
    private Appender patternAppender;
    private Appender testAppender;
    private List<String> patternAppenderProp;

    @Before
    public void setUp() {
        patternAppender = new Appender();
        patternAppender.setName("fileAPPENDER");
        List<String> logs = new ArrayList<String>();
        logs.add("odkazNaLoger");
        patternAppender.setErrorHandler(patternAppender.createErrorHandler("org.apache.BestHandler", null, logs, "FallbackServerAppender"));
        patternAppender.setClassName("org.apache.log4j.FileAppender");
        patternAppender.setLayoutClassName("org.apache.log4j.PatternLayout");
        patternAppender.addParam("File", "/tmp/debug.log");
        patternAppender.addParam("Append", "false");
        patternAppender.addParam("Encoding", "UTF-8");
        patternAppender.addParam("BufferSize", "1024");
        patternAppender.addParam("Threshold", "WARN");
        patternAppender.addLayoutParam("ConversionPattern", "%d{HH:mm:ss}");
        patternAppender.addAppenderRef("unknownAppender");
        
        patternAppenderProp = new ArrayList<String>();
        patternAppenderProp.add("log4j.appender.fileAPPENDER = org.apache.log4j.FileAppender");
        patternAppenderProp.add("log4j.appender.fileAPPENDER.errorHandler = org.apache.BestHandler");
        patternAppenderProp.add("log4j.appender.fileAPPENDER.errorHandler.logger-ref = odkazNaLoger");
        patternAppenderProp.add("log4j.appender.fileAPPENDER.errorHandler.appender-ref = FallbackServerAppender");
        patternAppenderProp.add("log4j.appender.fileAPPENDER.layout = org.apache.log4j.PatternLayout");
        patternAppenderProp.add("log4j.appender.fileAPPENDER.layout.ConversionPattern = %d{HH:mm:ss}");
        patternAppenderProp.add("log4j.appender.fileAPPENDER.File = /tmp/debug.log");
        patternAppenderProp.add("log4j.appender.fileAPPENDER.Append = false");
        patternAppenderProp.add("log4j.appender.fileAPPENDER.Encoding = UTF-8");
        patternAppenderProp.add("log4j.appender.fileAPPENDER.BufferSize = 1024");
        patternAppenderProp.add("log4j.appender.fileAPPENDER.Threshold = WARN");                
        patternAppenderProp.add("log4j.appender.fileAPPENDER.appender-ref = unknownAppender");

    }

    @Test
    public void setUpFromElementTest() {
        
        Appender readedAppender = new Appender();
                
        Document document = null;
        try {
            document = (new SAXReader()).read("unitTestFiles/patternAppender.xml");
        } catch (DocumentException ex) {
            fail();
        }
        
        Element rootElement = document.getRootElement();
        
        readedAppender.setUpFromElement(rootElement);
        
        assertEquals(patternAppender, readedAppender);
    }
    
    @Test
    public void toPropertyTest() {
        
        List<String> ourOutput = patternAppender.toProperty(new ArrayList<String>());
        
        Collections.sort(ourOutput);
        Collections.sort(patternAppenderProp);
        assertEquals(ourOutput.size(), patternAppenderProp.size());
        assertEquals(ourOutput, patternAppenderProp);
    }
    
    public void verifyTest() {
        testAppender = new Appender();
        
        testAppender.verify();
        
        testAppender.setClassName("org.apache.log4j. FileAppender");
        try{
            testAppender.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        
        testAppender.setClassName("org.apache.log4j.FileAAppender");
        try{
            testAppender.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        
        testAppender.setClassName("org.apache.log4j.FileAppender");
        testAppender.setName("fileAPPENDER ");
        try{
            testAppender.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        

        testAppender.setName("fileAPPENDER");
        testAppender.addParam("File", " /tmp/debug.log ");
        try{
            testAppender.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        
        testAppender.addParam(" File", "/tmp/debug.log");
        
        try{
            testAppender.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        
        testAppender.addParam("File", "/tmp/debug.log");
        testAppender.addParam("Threshold", "WARNING");
        try{
            testAppender.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        
        testAppender.addParam("Threshold", "WARN");
        try{
            testAppender.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
    
    }
    
    @Test
    public void fromPropertiesToModelTest() throws FileNotFoundException, IOException{
        Properties properties = new Properties();
        properties.load(new FileInputStream("unitTestFiles/patternAppender.properties"));

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
        
        assertEquals(1, configuration.getAppenders().size());

        Appender outAppender = configuration.getAppenders().get("fileAPPENDER");
        assertEquals(patternAppender.getParams(), outAppender.getParams());
        assertEquals(patternAppender, outAppender);
    }
}
