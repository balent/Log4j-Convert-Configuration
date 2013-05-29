/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j;

import cz.muni.pb138.log4j.model.Appender;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.dom4j.io.SAXReader;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jozef
 */
public class AppenderTest {
    private Appender patternAppender;

    @Before
    public void setUp() {
        patternAppender = new Appender();
        patternAppender.setName("fileAPPENDER");
        patternAppender.setClassName("org.apache.log4j.FileAppender");
        patternAppender.setLayoutClassName("org.apache.log4j.PatternLayout");
        patternAppender.addParam("File", "/tmp/debug.log");
        patternAppender.addParam("Append", "false");
        patternAppender.addParam("Encoding", "UTF-8");
        patternAppender.addParam("BufferSize", "1024");
        patternAppender.addParam("Threshold", "WARN");
        patternAppender.addLayoutParam("ConversionPattern", "%d{HH:mm:ss}");
        patternAppender.addAppenderRef("unknownAppender");
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
}
