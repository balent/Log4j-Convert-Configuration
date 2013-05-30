/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j;

import cz.muni.pb138.log4j.model.Appender;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private Appender patternAppenderXML;
    private Appender testAppender;
    private List<String> patternAppenderProp;

    @Before
    public void setUp() {
        patternAppenderXML = new Appender();
        patternAppenderXML.setName("fileAPPENDER");
        List<String> logs = new ArrayList<String>();
        logs.add("odkazNaLoger");
        patternAppenderXML.setErrorHandler(patternAppenderXML.createErrorHandler("org.apache.BestHandler", null, logs, null, "FallbackServerAppender"));
        patternAppenderXML.setClassName("org.apache.log4j.FileAppender");
        patternAppenderXML.setLayoutClassName("org.apache.log4j.PatternLayout");
        patternAppenderXML.addParam("File", "/tmp/debug.log");
        patternAppenderXML.addParam("Append", "false");
        patternAppenderXML.addParam("Encoding", "UTF-8");
        patternAppenderXML.addParam("BufferSize", "1024");
        patternAppenderXML.addParam("Threshold", "WARN");
        patternAppenderXML.addLayoutParam("ConversionPattern", "%d{HH:mm:ss}");
        patternAppenderXML.addAppenderRef("unknownAppender");
        
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
        
        assertEquals(patternAppenderXML, readedAppender);
    }
    
    @Test
    public void toPropertyTest() {
        
        List<String> ourOutput = patternAppenderXML.toProperty(new ArrayList<String>());
        
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
    
    
}
