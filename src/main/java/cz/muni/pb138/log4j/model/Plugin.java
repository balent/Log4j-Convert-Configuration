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
public class Plugin {
    private String name;
    private String className;
    private ConnectionSource connectionSource;

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

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public void setConnectionSource(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    
    private Map<String, String> params = new HashMap<String, String>();
    
    public void addParam(String key, String value){
        if(params.get(key) == null){
            params.put(key, value);
        }else{
            AppUtils.crash("Plugin: '" + name + "' with two same params: " + key);
        }
    }
    
    public void setUpFromElement(Element element){
        name = element.attributeValue("name");        
        className = element.attributeValue("class");
        
        for(Element e : (List<Element>) element.elements("param")){
            addParam(e.attributeValue("name"), e.attributeValue("value"));
        }
        
        if(element.element("connectionSource") != null){
            connectionSource = new ConnectionSource();
            connectionSource.setUpFromElement(element.element("connectionSource"));
        }
        
    }
    
}
