package cz.muni.pb138.log4j.model;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import cz.muni.pb138.log4j.AppUtils;

public class Logger {

    private String name;
    private List<String> appenderNames = new ArrayList<String>();
    private Boolean additivity;
    private Threshold level;
    private boolean isRootLogger = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAppenderNames() {
        return appenderNames;
    }

    public Boolean getAdditivity() {
        return additivity;
    }

    public Threshold getLevel() {
        return level;
    }
    
    public boolean isRootLogger() {
        return isRootLogger;
    }
    
    public void setRootLogger(boolean isRootLogger) {
        this.isRootLogger = isRootLogger;
    }

    public void addConfig(String value) {
        String[] values = value.replaceAll("\\s", "").split(",");

        try {
            level = Threshold.valueOf(values[0]);
        } catch (Exception ex) {
            AppUtils.crash("You have specified wrong logger level", ex);
        }
        
        for (int i = 1; i < values.length; i++) {
            appenderNames.add(values[i]);
        }
    }
    
    public Element addElement(Element rootElement) {
        Element loggerElement = null;
        if (isRootLogger) {
            loggerElement = rootElement.addElement("root");
        } else {
            loggerElement = rootElement.addElement("logger");
        }
        if (name != null) { // root element doesn't have name
            loggerElement.addAttribute("name", name);
        }
        
        loggerElement.addElement("level").addAttribute("value", level.name());
        
        for (String appenderName: appenderNames) {
            Element appenderElement = loggerElement.addElement("appender-ref");
            appenderElement.addAttribute("ref", appenderName);
        }
        return rootElement;
    }
}