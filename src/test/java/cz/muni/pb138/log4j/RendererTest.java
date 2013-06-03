/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j;

import cz.muni.pb138.log4j.model.Configuration;
import cz.muni.pb138.log4j.model.Renderer;
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
public class RendererTest {
    private Renderer patternRenderer;
    private Renderer testRenderer;
    private List<String> patternRendererProp;
    
    @Before
    public void setUp() {
        patternRenderer = new Renderer();
        patternRenderer.setRenderedClass("cz.muni.fi.RenderedClass");
        patternRenderer.setRenderingClass("cz.muni.fi.RenderingClass");
        
        patternRendererProp = new ArrayList<String>();
        patternRendererProp.add("log4j.renderer.cz.muni.fi.RenderedClass = cz.muni.fi.RenderingClass");
    }

    @Test
    public void setUpFromElementTest() {
        
        Renderer readedRenderer = new Renderer();
                
        Document document = null;
        try {
            document = (new SAXReader()).read("unitTestFiles/patternRenderer.xml");
        } catch (DocumentException ex) {
            fail();
        }
        
        Element rootElement = document.getRootElement();
        
        readedRenderer.setUpFromElement(rootElement);
        
        assertEquals(patternRenderer, readedRenderer);
    }
    
    @Test
    public void toPropertyTest() {
        
        List<String> ourOutput = patternRenderer.toProperty(new ArrayList<String>());
        
        Collections.sort(ourOutput);
        Collections.sort(patternRendererProp);
        assertEquals(ourOutput.size(), patternRendererProp.size());
        assertEquals(ourOutput, patternRendererProp);
    }
    
    @Test
    public void verifyTest() {
        testRenderer = new Renderer();
        testRenderer.verify();
        
        testRenderer.setRenderedClass("cz.muni.fi.ThrowableRendererImpl ");
        try {
            testRenderer.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        
        testRenderer.setRenderedClass("cz.muni.fi.ThrowableRendererImpl");
        testRenderer.setRenderingClass("cz.muni.fi.ThrowableRendererImpl ");
        try {
            testRenderer.verify();
            fail();
        }catch(RuntimeException ex) {
            //good
        }
        
        testRenderer.setRenderingClass("cz.muni.fi.ThrowableRendererImpl");
        testRenderer.verify();
    }
    
    @Test
    public void fromPropertiesToModelTest() throws FileNotFoundException, IOException{
        Properties properties = new Properties();
        properties.load(new FileInputStream("unitTestFiles/patternRenderer.properties"));

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
        
        assertEquals(1, configuration.getRenderers().size());
        assertEquals(patternRenderer, configuration.getRenderers().get("cz.muni.fi.renderedclass"));
    }
}
