package cz.muni.pb138.log4j.model;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import cz.muni.pb138.log4j.AppUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Level;
import org.apache.log4j.lf5.LogLevel;

public class Logger {

    private String name;
    private List<String> appenderNames = new ArrayList<String>();
    private Map<String, String> params = new HashMap<String, String>();
    private String additivity; // TODO:  it should by only boolean 
    private String level; //deprecated
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

    public LoggerLevel getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(String level) {
        if(loggerLevel == null) loggerLevel = new LoggerLevel();
        loggerLevel.setLevel(level);   
    }
    
    public void setLoggerLevelClass(String levelClass) {
        if(loggerLevel == null) loggerLevel = new LoggerLevel();
        loggerLevel.setLevelClass(levelClass);   
    }
    
    public void addLoggerLevelParam(String key, String val) {
        if(loggerLevel == null) loggerLevel = new LoggerLevel();
        loggerLevel.addParam(key, val); 
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
            level = Level.toLevel(values[0]).toString(); // FOR MARTIN: Nahrada za level = Threshold.valueOf(values[0]).toString()
            
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
        //TODO zistit co to ma robit ked nie je uvedeny level
        if(element.element("level") != null){
            
            loggerLevel = new LoggerLevel();
            loggerLevel.setUpFromElement(element.element("level"));
        }
        
        
        for(Element e : (List<Element>) element.elements("appender-ref")){
            addAppenderName(e.attributeValue("ref"));
        }
    }
    
    private void checkLoggerParamSuported(String param){
        // to do
    }
    
    
    public List<String> toProperty(List<String> prop) {
        
        String levelToProp = (loggerLevel != null) ? loggerLevel.getLevel() : "";
        String apenders = AppUtils.join(appenderNames, ", ");
        String value;
        
        
        prop.add("");
        
        if(loggerLevel != null && !loggerLevel.getLevelClass().isEmpty()) {
            
            StringBuilder levelToPropBld = new StringBuilder(levelToProp);
            levelToPropBld.append("#");
            levelToPropBld.append(loggerLevel.getLevelClass());
            
            levelToProp = levelToPropBld.toString();
        }
        
        if(!apenders.isEmpty()) {
            value = levelToProp + ", " + apenders;
        }
        else {
            value = levelToProp;
        }
        
        //root
        if(isRootLogger()) {            
            prop.add(AppUtils.prefix("rootLogger") + " = " + value);
        }
        //anoteher logger
        else {
            prop.add(AppUtils.prefix("logger." + name) + " = " + value);
            if(additivity!= null && !additivity.isEmpty()){
                prop.add(AppUtils.prefix("additivity." + name) + " = " + additivity);
            }
            
        }
        //params
        AppUtils.addParams(prop, "logger." + name, params);
        
        //custom class
        if(customClass !=null && !customClass.isEmpty()) {
            prop.add(AppUtils.prefix("logger." + name + ".class = "+customClass));
        }
        
        //level
        if(loggerLevel != null) {
            for (Map.Entry<String, String> entry : loggerLevel.getParams().entrySet()) {
                prop.add(AppUtils.prefix("logger.level." + entry.getKey() + " = "+entry.getValue()));
            }
        }
        
        
        return prop;
    }
    
    private class LoggerLevel {
    
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
                LogLevel.valueOf(level.toUpperCase(Locale.ENGLISH));
                return true;
            }catch(Exception e)
            {
               return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + (this.level != null ? this.level.hashCode() : 0);
            hash = 97 * hash + (this.params != null ? this.params.hashCode() : 0);
            hash = 97 * hash + (this.levelClass != null ? this.levelClass.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LoggerLevel other = (LoggerLevel) obj;
            if ((this.level == null) ? (other.level != null) : !this.level.equals(other.level)) {
                return false;
            }
            if (this.params != other.params && (this.params == null || !this.params.equals(other.params))) {
                return false;
            }
            if ((this.levelClass == null) ? (other.levelClass != null) : !this.levelClass.equals(other.levelClass)) {
                return false;
            }
            return true;
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 79 * hash + (this.appenderNames != null ? this.appenderNames.hashCode() : 0);
        hash = 79 * hash + (this.params != null ? this.params.hashCode() : 0);
        hash = 79 * hash + (this.additivity != null ? this.additivity.hashCode() : 0);
        hash = 79 * hash + (this.loggerLevel != null ? this.loggerLevel.hashCode() : 0);
        hash = 79 * hash + (this.isRootLogger ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Logger other = (Logger) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.appenderNames != other.appenderNames && (this.appenderNames == null || !this.appenderNames.equals(other.appenderNames))) {
            return false;
        }
        if (this.params != other.params && (this.params == null || !this.params.equals(other.params))) {
            return false;
        }
        if ((this.additivity == null) ? (other.additivity != null) : !this.additivity.equals(other.additivity)) {
            return false;
        }
        if (this.loggerLevel != other.loggerLevel && (this.loggerLevel == null || !this.loggerLevel.equals(other.loggerLevel))) {
            return false;
        }
        if (this.isRootLogger != other.isRootLogger) {
            return false;
        }
        return true;
    }
}