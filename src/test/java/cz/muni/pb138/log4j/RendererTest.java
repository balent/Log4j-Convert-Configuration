/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j;

import cz.muni.pb138.log4j.model.Renderer;
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
public class RendererTest {
    private Renderer patternRendererXML;
    private List<String> patternRendererProp;
    
    @Before
    public void setUp() {
        patternRendererXML = new Renderer();
        patternRendererXML.setRenderedClass("cz.muni.fi.RenderedClass");
        patternRendererXML.setRenderingClass("cz.muni.fi.RenderingClass");
        
        patternRendererProp = new ArrayList<String>();
        patternRendererProp.add("");
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
        
        assertEquals(patternRendererXML, readedRenderer);
    }
    
    @Test
    public void toPropertyTest() {
        
        List<String> ourOutput = patternRendererXML.toProperty(new ArrayList<String>());
        
        Collections.sort(ourOutput);
        Collections.sort(patternRendererProp);
        assertEquals(ourOutput.size(), patternRendererProp.size());
        assertEquals(ourOutput, patternRendererProp);
    }
}
