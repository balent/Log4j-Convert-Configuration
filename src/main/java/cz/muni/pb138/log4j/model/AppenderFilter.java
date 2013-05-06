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
public class AppenderFilter {
    private String className;
    private Map<String, String> params = new HashMap<String, String>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, String> getParams() {
        return params;
    }
    
    public void addFilterParam(String key, String value){
        if(params.get(key) == null){
             checkFilterParamSuported(key);
             params.put(key, value);
        }else{
            AppUtils.crash("Filter: '" + className + "' with two same params: " + key);
        }
    }
    
    public void setUpFromElement(Element element){
        className = element.attributeValue("class");
        for(Element e : (List<Element>) element.elements("param")){
            addFilterParam(e.attributeValue("name"), e.attributeValue("value"));
        }
    }
    
    private void checkFilterParamSuported(String param){
        // TO DO?
    }
}
