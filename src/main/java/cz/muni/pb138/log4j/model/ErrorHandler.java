/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;

/**
 *
 * @author jozef
 */
public class ErrorHandler {
    private String className;
    private Map<String, String> params = new HashMap<String, String>();
    private List<String> loggers = new ArrayList<String>();
    private String root;
    private String appender;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void addParam(String key, String value) {
        
    }

    public List<String> getLoggers() {
        return loggers;
    }

    public void addLoggers(String logger) {
        
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getAppender() {
        return appender;
    }

    public void setAppender(String appender) {
        this.appender = appender;
    }
    
    public void setUpFromElement(Element element){
        
    }
}
