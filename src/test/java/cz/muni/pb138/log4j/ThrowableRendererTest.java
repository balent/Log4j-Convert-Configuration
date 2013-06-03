/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j;

import cz.muni.pb138.log4j.model.Configuration;
import cz.muni.pb138.log4j.model.ThrowableRenderer;
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
public class ThrowableRendererTest {
    private ThrowableRenderer patternThrowableRenderer;
    private ThrowableRenderer testThrowableRenderer;
    private List<String> patternThrowableRendererProp;
    
    @Before
    public void setUp() {
        patternThrowableRenderer = new ThrowableRenderer();
        patternThrowableRenderer.setClassName("cz.muni.fi.ThrowableRendererImpl");
        patternThrowableRenderer.addParam("parameter1","value1");
        patternThrowableRenderer.addParam("parameter2","value2");        
        
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
        
        assertEquals(patternThrowableRenderer, readedThrowableRenderer);
    }
    
    @Test
    public void toPropertyTest() {
        
        List<String> ourOutput = patternThrowableRenderer.toProperty(new ArrayList<String>(), "");
        
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
    
    @Test
    public void fromPropertiesToModelTest() throws FileNotFoundException, IOException{
        Properties properties = new Properties();
        properties.load(new FileInputStream("unitTestFiles/patternThrowableRenderer.properties"));

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
        
        assertEquals(patternThrowableRenderer, configuration.getThrowableRenderer());
    }
}
