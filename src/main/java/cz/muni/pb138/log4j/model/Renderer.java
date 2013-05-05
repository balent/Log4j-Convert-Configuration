/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j.model;

import org.dom4j.Element;

/**
 *
 * @author jozef
 */
public class Renderer {

    private String renderedClass;
    private String renderingClass;
    
    public String getRenderedClass() {
        return renderedClass;
    }

    public void setRenderedClass(String renderedClass) {
        this.renderedClass = renderedClass;
    }

    public String getRenderingClass() {
        return renderingClass;
    }

    public void setRenderingClass(String renderingClass) {
        this.renderingClass = renderingClass;
    }
    
    public void setUpFromElement(Element element){
        renderedClass = element.attributeValue("renderedClass");
        renderingClass = element.attributeValue("renderingClass");
    }
}
