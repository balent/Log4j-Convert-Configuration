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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.renderedClass != null ? this.renderedClass.hashCode() : 0);
        hash = 23 * hash + (this.renderingClass != null ? this.renderingClass.hashCode() : 0);
        return hash;
    }
    
    public void verify() {
        if(renderedClass != null && renderedClass.contains(" ")) {
            AppUtils.crash("Rendered class: " + renderedClass + " contains a space");
        }
        
        if(renderingClass != null && renderingClass.contains(" ")) {
            AppUtils.crash("Rendering class: " + renderingClass + " contains a space");
        }
        
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Renderer other = (Renderer) obj;
        if ((this.renderedClass == null) ? (other.renderedClass != null) : !this.renderedClass.equals(other.renderedClass)) {
            return false;
        }
        if ((this.renderingClass == null) ? (other.renderingClass != null) : !this.renderingClass.equals(other.renderingClass)) {
            return false;
        }
        return true;
    }
    
}
