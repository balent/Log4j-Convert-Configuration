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
 * @author jozef
 */
public class ConnectionSource {
    private String className;
    private Map<String, String> params = new HashMap<String, String>();
    private DataSource dataSource;
    
    
    public void addParam(String key, String value){
        if(params.get(key) == null){
            params.put(key, value);
        }else{
            AppUtils.crash("ConnectionSource: '" + className + "' with two same params: " + key);
        }
    }
    
    public void setUpFromElement(Element element){
        className = element.attributeValue("class");
        
        if(element.element("dataSource") != null){
            dataSource = new DataSource();
            dataSource.setUpFromElement(element.element("dataSource"));
        }
        
        for(Element e : (List<Element>) element.elements("param")){
            addParam(e.attributeValue("name"), e.attributeValue("value"));
        }
        
    }
}
