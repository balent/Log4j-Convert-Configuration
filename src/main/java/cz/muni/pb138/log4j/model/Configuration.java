package cz.muni.pb138.log4j.model;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cz.muni.pb138.log4j.AppUtils;

public class Configuration {
    private Map<String, Logger> loggers = new HashMap<String, Logger>();
    private Map<String, Appender> appenders = new HashMap<String, Appender>();
    private Logger rootLogger;

    private Threshold threshold;
    private Boolean debug;
    private Boolean reset;

    public Map<String, Appender> getAppenders() {
        return appenders;
    }

    public Map<String, Logger> getLoggers() {
        return loggers;
    }

    public Logger getRootLogger() {
        return rootLogger;
    }

    public Threshold getThreshold() {
        return threshold;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        if ("true".equalsIgnoreCase(debug)) {
            this.debug = true;
        } else if ("false".equalsIgnoreCase(debug)) {
            this.debug = false;
        } else {
            AppUtils.crash("You have specified wrong configuration debug parameter");
        }
    }

    public boolean isReset() {
        return reset;
    }

    public void setThreshold(String threshold) {
        try {
            this.threshold = Threshold.valueOf(threshold);
        } catch (Exception ex) {
            AppUtils.crash("You have specified wrong configuration threshold", ex);
        }
    }

    public void addConfig(String key, String value) {
        if (key.startsWith("logger")) {
            String newKey = key.substring(7);
            String loggerName = newKey.split("\\.")[0];
            Logger logger = loggers.get(loggerName);
            if (logger == null) {
                logger = new Logger();
                logger.setName(loggerName);
                loggers.put(loggerName, logger);
            }
            logger.addConfig(value);
        } else if (key.startsWith("rootlogger")) {
            if (rootLogger == null) {
                rootLogger = new Logger();
                rootLogger.setRootLogger(true);
            }
            rootLogger.addConfig(value);
        } else if (key.startsWith("appender")) {
            String newKey = key.substring(9);
            String appenderName = newKey.split("\\.")[0];
            Appender appender = appenders.get(appenderName);
            if (appender == null) {
                appender = new Appender();
                appender.setName(appenderName);
                appenders.put(appenderName, appender);
            }
            appender.addConfig(newKey, value);
        }
    }
    
    public void verify() {
        // verify object structure
    }
    
    public Document toXML() {
        Document document = DocumentHelper.createDocument();
        document.addDocType("log4j:configuration", null, "log4j.dtd");
        Element rootElement = document.addElement("log4j:configuration");
        rootElement.addNamespace("log4j", "http://jakarta.apache.org/log4j/");
        
        for (Appender appender: appenders.values()) {
            appender.addElement(rootElement);
        }
        for (Logger logger: loggers.values()) {
            logger.addElement(rootElement);
        }
        if (rootLogger != null) {
            rootLogger.addElement(rootElement);
        }
        return document;
    }
}
