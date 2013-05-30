/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j.model;

import cz.muni.pb138.log4j.AppUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;

/**
 *
 * @author xmarko2
 */
public class ThrowableRenderer {
    private String className;
    private Map<String, String> params = new HashMap<String, String>();
    
    public void addParam(String key, String value){
        if(params.get(key) == null){
            params.put(key, value);
        }else{
            AppUtils.crash("ThrowableRenderer: '" + className + "' with two same params: " + key);
        }
    }

    public void setClassName(String className) {
        this.className = className;
    }
    
    public void setUpFromElement(Element element){
        className = element.attributeValue("class");
        
        for(Element e : (List<Element>) element.elements("param")){
            addParam(e.attributeValue("name"), e.attributeValue("value"));
        }
    }
    
    public List<String> toProperty(List<String> prop, String prefix) {
        prefix = (prefix != "") ? prefix + "." : prefix;
        
        prop.add("");
        prop.add(AppUtils.prefix(prefix + "throwableRenderer = " + className));
        AppUtils.addParams(prop, prefix + "throwableRenderer", params);
        
        return prop;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + (this.className != null ? this.className.hashCode() : 0);
        hash = 11 * hash + (this.params != null ? this.params.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ThrowableRenderer other = (ThrowableRenderer) obj;
        if (this.params != other.params && (this.params == null || !this.params.equals(other.params))) {
            return false;
        }
        return true;
    }
    
}
