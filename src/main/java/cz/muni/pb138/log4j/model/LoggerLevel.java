/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j.model;

import com.sun.org.apache.xpath.internal.axes.LocPathIterator;
import cz.muni.pb138.log4j.AppUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.dom4j.Element;

/**
 *
 * @author jozef
 */
public class LoggerLevel {
    
    private LoggerLevelEnum level;
    private Map<String, String> params = new HashMap<String, String>();
    private String levelClass = "";

    public LoggerLevelEnum getLevel() {
        return level;
    }

    public void setLevel(LoggerLevelEnum level) {
        this.level = level;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void addParam(String name, String value) {
        if(!params.containsKey(name)){
            this.params.put(name, value);
        }else{
            AppUtils.crash("occurence of two same params for the same logger level");
        }
    }

    public String getLevelClass() {
        return levelClass;
    }

    public void setLevelClass(String levelClass) {
        this.levelClass = levelClass;
    }
    
    public void setUpFromElement(Element element){
        level = LoggerLevelEnum.valueOf(element.attributeValue("value").toUpperCase(Locale.ENGLISH));
        
        if(element.attributeValue("class") != null){
            levelClass = element.attributeValue("class");
        }
        
        for(Element e : (List<Element>) element.elements("param")){
            params.put(e.attributeValue("name"), e.attributeValue("value"));
        }
    }
    
}
