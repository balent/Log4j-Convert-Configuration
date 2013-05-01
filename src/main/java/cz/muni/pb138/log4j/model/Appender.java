package cz.muni.pb138.log4j.model;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import cz.muni.pb138.log4j.AppUtils;

public class Appender {
    private String name;
    private String className;
    private String layoutClassName = null;
    private boolean hasLayoutAlready = false;
    private String threshold;
    private Map<String, String> params = new HashMap<String, String>();
    private Map<String, String> layoutParams = new HashMap<String, String>();

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

    public Map<String, String> getParams() {
        return params;
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
}
