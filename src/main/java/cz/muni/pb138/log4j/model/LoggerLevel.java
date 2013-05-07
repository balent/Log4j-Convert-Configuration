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
    
    private String level;
    private Map<String, String> params = new HashMap<String, String>();
    private String levelClass = "";

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
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
        String value = element.attributeValue("value");
        String classAtt = element.attributeValue("class");
        
        if(checkStandardLevel(value))
        {
            level = value;
            if(classAtt != null){
                levelClass = classAtt;
            }
        }
        else{
            String[] ownLevelarr = value.split("#");
            if(ownLevelarr.length == 2 && classAtt == null){
                level = ownLevelarr[0];
                levelClass = ownLevelarr[1];                
            }else if(ownLevelarr.length == 1){
                level = ownLevelarr[0];
                if(classAtt != null){
                    levelClass = classAtt;
                }
            }else{
                AppUtils.crash("Invalid setting for level attributes value: " + value + " class: " + classAtt);
            }
        }
        
        
        for(Element e : (List<Element>) element.elements("param")){
            addParam(e.attributeValue("name"), e.attributeValue("value"));
        }
    }
    
    private boolean checkStandardLevel(String level){
        try{
            LoggerLevelEnum.valueOf(level.toUpperCase(Locale.ENGLISH));
            return true;
        }catch(Exception e)
        {
           return false;
        }
    }
    
}