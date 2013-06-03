/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j.model;

import cz.muni.pb138.log4j.AppUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

/**
 *
 * @author xmarko2
 */
public class LoggerFactory {
    private String className;
    private Map<String, String> params = new HashMap<String, String>();
    
    public void addParam(String key, String value){
        if(params.get(key) == null){
            params.put(key, value);
        }else{
            AppUtils.crash("LoggerFactory: '" + className + "' with two same params: " + key);
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
        prefix = (!"".equals(prefix)) ? prefix + "." : prefix;
        
        prop.add(AppUtils.prefix(prefix + "loggerFactory = " + className));
        AppUtils.addParams(prop, prefix + "loggerFactory", params);
        
        return prop;
    }
    
    public void verify() {
        if (className == null) {
            AppUtils.crash("Logger factory class name is empty.");
        }
        if(className != null && className.contains(" ")) {
            AppUtils.crash("Logger factory: " + className + " contains a space");
        }
        
        if(!AppUtils.testParams(params)) {
            AppUtils.crash("Param's name or value contains a space. Logger factory: " + className);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.className != null ? this.className.hashCode() : 0);
        hash = 73 * hash + (this.params != null ? this.params.hashCode() : 0);
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
        final LoggerFactory other = (LoggerFactory) obj;
        if ((this.className == null) ? (other.className != null) : !this.className.equals(other.className)) {
            return false;
        }
        if (this.params != other.params && (this.params == null || !this.params.equals(other.params))) {
            return false;
        }
        return true;
    }

    public Element toXmlElement() {
        Element appenderElement = DocumentFactory.getInstance().createElement("appender");
        appenderElement.addAttribute("class", className);
        for (String paramKey : params.keySet()) {
            Element paramElement = appenderElement.addElement("param");
            appenderElement.addAttribute("name", paramKey);
            appenderElement.addAttribute("value", params.get(paramKey));
        }
        return appenderElement;
    }
}
