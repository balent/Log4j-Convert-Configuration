package cz.muni.pb138.log4j.model;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import cz.muni.pb138.log4j.AppUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Appender {
    private String name;
    private String className;
    private String layoutClassName = null;
    private boolean hasLayoutAlready = false;
    private String threshold;
    private Map<String, String> params = new HashMap<String, String>();
    private Map<String, String> layoutParams = new HashMap<String, String>();
    private ErrorHandler errorHandler;
    private Map<String, AppenderFilter> filters  = new HashMap<String, AppenderFilter>();
    private List<String> appenderRefs = new ArrayList<String>();

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
    
    public String getLayoutClassName() {
        return layoutClassName;
    }

    public void setLayoutClassName(String layoutClassName) {
        this.layoutClassName = layoutClassName;
    }

    public String getThreshold() {
        return threshold;
    }

    public Map<String, String> getLayoutParams() {
        return layoutParams;
    }

    public void addLayoutParam(String value, String key){
        if(params.get(key) == null){
             checkLayoutParamSuported(key);
             params.put(key, value);
        }else{
            AppUtils.crash("Appender: '" + name + "' with two same layout params: " + key);
        }
    }
    
    public Map<String, String> getParams() {
        return params;
    }
    
    public void addParam(String key, String value){
        if(params.get(key) == null){
           //checkAppenderParamSuported(key);
           params.put(key, value);
        }else{
            AppUtils.crash("Appender: '" + name + "' with two same params: " + key);
        }
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public Map<String, AppenderFilter> getFilters() {
        return filters;
    }

    public void addFilter(AppenderFilter filter){
        if(filters.get(filter.getClassName()) == null){
            filters.put(filter.getClassName(), filter);
        }else{
            AppUtils.crash("Two filters of the same class: '" +filter.getClassName() + "' in appender: " + name);
        }
    }
    
    public List<String> getAppenderRefs() {
        return appenderRefs;
    }
    
    public void addAppenderRef(String ref){
        if(!appenderRefs.contains(ref)){
            appenderRefs.add(ref);
        }else{
            AppUtils.crash("Appender: '" + name + "' with two same appender-ref: " + ref);
        }
    }

    public void addConfig(String key, String value) {
        if (key.equals(name)) {
            className = value;
        } else {
            String newKey = key.substring(name.length() + 1); // removing appenderName. from key
            if(newKey.startsWith("layout")) {               
                // each appender has max. one "private" layout
                String layout = newKey.substring(6);      // removing "layout" part without "."
                if(layout.contains(".")) {
                    layoutParams.put(layout.substring(1), value);
                } else {
                    if(hasLayoutAlready) {
                        AppUtils.crash("This appender already has a layout defined.");
                    }
                    layoutClassName = value;
                    hasLayoutAlready = true;
                }              
            } else {
                params.put(newKey, value);
                // those values are Bean-type based, they are
                // params: file, append, buffersize, target... it also
                // depends on appender class :((
            }
        }
    }

    public Element addThisToElement(Element rootElement) {
        Element appenderElement = rootElement.addElement("appender");
        appenderElement.addAttribute("name", name);
        appenderElement.addAttribute("class", className);
        for (String paramKey : params.keySet()) {
            Element paramElement = appenderElement.addElement("param");
            paramElement.addAttribute("name", paramKey);
            paramElement.addAttribute("value", params.get(paramKey));
        }
        
        if(layoutClassName != null) {
            Element layoutElement = appenderElement.addElement("layout");
            layoutElement.addAttribute("class", layoutClassName);
            for (String layoutParamKey : layoutParams.keySet()) {
                Element layoutParamElement = layoutElement.addElement("param");
                layoutParamElement.addAttribute("name", layoutParamKey);
                layoutParamElement.addAttribute("value", layoutParams.get(layoutParamKey));
            }
        }
        return rootElement;
    }
    
    public void setUpFromElement(Element element){
        name = element.attributeValue("name");
        className = element.attributeValue("class");
        //className = checkClassSuported(element.attributeValue("class"));
        
        if(element.element("errorHandler") != null){
            errorHandler = new ErrorHandler();
            errorHandler.setUpFromElement(element.element("errorHandler"));
        }
        
        for(Element e : (List<Element>) element.elements("param")){
            addParam(e.attributeValue("name"), e.attributeValue("value"));
        }
        
        if(element.element("layout") != null) {
            layoutClassName = element.element("layout").attributeValue("class");
            for(Element e : (List<Element>) element.elements("param")){
                addLayoutParam(e.attributeValue("name"), e.attributeValue("value"));
            }
        }
        
        for(Element e : (List<Element>) element.elements("filter")){
            
                AppenderFilter filter = new AppenderFilter();
                filter.setUpFromElement(e);
                addFilter(filter);
        }
        
        for(Element e : (List<Element>) element.elements("appender-ref")){
            addAppenderRef(e.attributeValue("ref"));
        }
    }
    
    private String checkClassSuported(String className){
        if(!className.startsWith("org.apache.log4j.")){
            AppUtils.crash("Unsuported appender class package for appender: '" + name + "'");
        }
        String[] classNameArr = className.split("\\.");
        
        if(classNameArr.length != 4){
            AppUtils.crash("Unsuported appender class for appender: '" + name + "'");
        }
        
        try{
            AppenderParams params = AppenderParams.valueOf(classNameArr[3].toLowerCase(Locale.ENGLISH));
        }catch(Exception e){
            AppUtils.crash("Unsuported appender class: '" + classNameArr[3] + "' for appender: " + name, e);
        }
        
        return classNameArr[3];
    }
    
    private void checkAppenderParamSuported(String param){
        AppenderParams appender = AppenderParams.valueOf(className.toLowerCase(Locale.FRENCH));
        
        for(String par : appender.getParams()){
            if(param.equalsIgnoreCase(par)) return;
        }
        
        AppUtils.crash("Unsuported appender param: '" + param + "' for appender class: " + className);
    }
    
    private void checkLayoutParamSuported(String param){
        // TO DO?
    }
}