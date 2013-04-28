package cz.muni.pb138.log4j.model;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import cz.muni.pb138.log4j.AppUtils;
public class Appender {
    private String name;
    private String className;
    private Map<String, String> params = new HashMap<String, String>();
   
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public void addConfig(String key, String value) {
        if (key.equals(name)) {
            className = value;
        } else {
            String newKey = key.substring(name.length() + 1); // removing appenderName from key
            if (newKey.startsWith("option")) {              // those values are Bean-type based, they are  
                AppUtils.crash("TO DO (See commentary)");   // NOT named option1, option2 etc., see Log4j the CM
                params.put(newKey, value);                  // detto nezabudnut ignorecase
                // params: layout, file, append, buffersize, target... it also depends on appender name :((
            } else {
                AppUtils.crash("Not supported yet: log4j." + key + "=" + value);
            }
        }
    }
    
    public Element addElement(Element rootElement) {
        Element appenderElement = rootElement.addElement("appender");
        appenderElement.addAttribute("name", name);
        appenderElement.addAttribute("class", className);
        for (String paramKey: params.keySet()) {
            Element paramElement = appenderElement.addElement("param");
            paramElement.addAttribute("name", paramKey);
            paramElement.addAttribute("value", params.get(paramKey));
        }
        return rootElement;
    }
}
