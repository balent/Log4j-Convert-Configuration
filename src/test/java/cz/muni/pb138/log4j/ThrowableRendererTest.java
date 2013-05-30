/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j;

import cz.muni.pb138.log4j.model.ThrowableRenderer;
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
public class ThrowableRendererTest {
    private ThrowableRenderer patternThrowableRendererXML;
    private ThrowableRenderer testThrowableRenderer;
    private List<String> patternThrowableRendererProp;
    
    @Before
    public void setUp() {
        patternThrowableRendererXML = new ThrowableRenderer();
        patternThrowableRendererXML.setClassName("cz.muni.fi.ThrowableRendererImpl");
        patternThrowableRendererXML.addParam("parameter1","value1");
        patternThrowableRendererXML.addParam("parameter2","value2");        
        
        patternThrowableRendererProp = new ArrayList<String>();
        patternThrowableRendererProp.add("log4j.throwableRenderer = cz.muni.fi.ThrowableRendererImpl");
        patternThrowableRendererProp.add("log4j.throwableRenderer.parameter1 = value1");
        patternThrowableRendererProp.add("log4j.throwableRenderer.parameter2 = value2");        
    }

    @Test
    public void setUpFromElementTest() {
        
        ThrowableRenderer readedThrowableRenderer = new ThrowableRenderer();
                
        Document document = null;
        try {
            document = (new SAXReader()).read("unitTestFiles/patternThrowableRenderer.xml");
        } catch (DocumentException ex) {
            fail();
        }
        
        Element rootElement = document.getRootElement();
        
        readedThrowableRenderer.setUpFromElement(rootElement);
        
        assertEquals(patternThrowableRendererXML, readedThrowableRenderer);
    }
    
    @Test
    public void toPropertyTest() {
        
        List<String> ourOutput = patternThrowableRendererXML.toProperty(new ArrayList<String>(), "");
        
        Collections.sort(ourOutput);
        Collections.sort(patternThrowableRendererProp);
        assertEquals(ourOutput.size(), patternThrowableRendererProp.size());
        assertEquals(ourOutput, patternThrowableRendererProp);
    }
    
    @Test
    public void verifyTest() {
        
        testThrowableRenderer = new ThrowableRenderer();
        testThrowableRenderer.verify();
       
        testThrowableRenderer.setClassName("cz.muni.fi.ThrowableRendererImpl ");
        try {
            testThrowableRenderer.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        
        testThrowableRenderer.setClassName("cz.muni.fi.ThrowableRendererImpl");
        testThrowableRenderer.addParam("parameter1 -","value1");
        try {
            testThrowableRenderer.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
         
        testThrowableRenderer.addParam("parameter1","value1 -");
        try {
            testThrowableRenderer.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        
        
    }
    

}
