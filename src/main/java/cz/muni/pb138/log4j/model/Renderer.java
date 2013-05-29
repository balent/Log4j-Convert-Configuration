/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j.model;

import cz.muni.pb138.log4j.AppUtils;
import java.util.List;
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
    
    public List<String> toProperty(List<String> prop) {
        prop.add(AppUtils.prefix("renderer." + renderedClass + " = " + renderingClass));
        return prop;
    }
}
