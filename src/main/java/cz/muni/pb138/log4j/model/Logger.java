package cz.muni.pb138.log4j.model;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import cz.muni.pb138.log4j.AppUtils;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Logger {

    private String name;
    private List<String> appenderNames = new ArrayList<String>();
    private Map<String, String> params = new HashMap<String, String>();
    private String additivity;
    private String level;
    private LoggerLevel loggerLevel;
    private String customClass = "";
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

    public void addAppenderName(String appenderName){
        if(! appenderNames.contains(appenderName)){
            appenderNames.add(appenderName);
        }else{
            AppUtils.crash("Logger: '" + name + "' with two same appenders: " + appenderName);
        }
    }
    
    public String getAdditivity() {
        return additivity;
    }

    public void setAdditivity(String additivity) {
        this.additivity = additivity;
    }

    public String getLevel() {
        return level;
    }

    public boolean isRootLogger() {
        return isRootLogger;
    }

    public void setRootLogger(boolean isRootLogger) {
        this.isRootLogger = isRootLogger;
    }
    
    public void addParam(String key, String value){
        if(params.get(key) == null){
            checkLoggerParamSuported(key);
            params.put(key, value);
        }else{
            AppUtils.crash("Logger: '" + name + "' with two same params: " + key);
        }
    }

    public void addConfig(String value) {
        String[] values = value.replaceAll("\\s", "").split(",");

        try {
            level = Threshold.valueOf(values[0]).toString();
            // logger may even have no level assigned - however there is no way to recognize whether
            // first value is an appender name or a wrong level name = this app REQUIRES null/inherited
            // level for all loggers where implied inheritance is intended and debug level of root logger
            // if default level is intended
        } catch (Exception ex) {
            if (values[0].contains("#")) {    // custom level value
                level = values[0].substring(0, values[0].indexOf('#'));
                customClass = values[0].substring(values[0].indexOf('#') + 1);
            } else if (!isRootLogger && (values[0].equalsIgnoreCase("inherited") || 
                    values[0].equalsIgnoreCase("null"))) {      // rootlogger cannot be null/inherited
                level = values[0];
            } else {
                AppUtils.crash("You have specified wrong logger level", ex);
            }
        }
        // TODO
        for (int i = 1; i < values.length; i++) {
            appenderNames.add(values[i]);
        }
    }

    public Element addThisToElement(Element rootElement) {
        Element loggerElement = null;
        if (isRootLogger) {
            loggerElement = rootElement.addElement("root");
        } else {
            loggerElement = rootElement.addElement("logger");
        }
        if (name != null) { // root element doesn't have name
            loggerElement.addAttribute("name", name);
            if (additivity != null) {
                loggerElement.addAttribute("additivity", additivity);
            }
        }

        Element levelElement = loggerElement.addElement("level");
        levelElement.addAttribute("value", level);
        if (!customClass.equals("")) {
            levelElement.addAttribute("class", customClass);
        }

        for (String appenderName : appenderNames) {
            Element appenderElement = loggerElement.addElement("appender-ref");
            appenderElement.addAttribute("ref", appenderName);
        }
        return rootElement;
    }
    
    public void setUpFromElement(Element element){
        
        if(element.getQualifiedName().equalsIgnoreCase("root")){
            isRootLogger = true;
        }
        else
        {
            if(element.attributeValue("class") != null){
                customClass = element.attributeValue("class");
            }
            name = element.attributeValue("name");
            additivity =  element.attributeValue("additivity");
        }
        
        // TODO, zistit zoznam moznych parametrov pre logger
        for(Element e : (List<Element>) element.elements("param")){
            addParam(e.attributeValue("name"), e.attributeValue("value"));
        }
        
        if(element.element("level") != null){
            checkLevel(element.element("level").attributeValue("value"));
            loggerLevel = new LoggerLevel();
            loggerLevel.setUpFromElement(element.element("level"));
        }
        
        for(Element e : (List<Element>) element.elements("appender-ref")){
            addAppenderName(e.attributeValue("ref"));
        }
    }
    
    private void checkLevel(String level){
        try{
            LoggerLevelEnum.valueOf(level.toUpperCase(Locale.ENGLISH));
        }catch(Exception e)
        {
           AppUtils.crash(level + " isn't defined in LoggerLevelEnum", e);
        }
    }
    
    private void checkLoggerParamSuported(String param){
        // to do
    }
}