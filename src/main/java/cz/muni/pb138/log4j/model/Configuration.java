package cz.muni.pb138.log4j.model;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cz.muni.pb138.log4j.AppUtils;
import java.util.Iterator;

public class Configuration {

    private Map<String, Logger> loggers = new HashMap<String, Logger>();
    private Map<String, Appender> appenders = new HashMap<String, Appender>();
    private Logger rootLogger;
    private Threshold threshold;
    private Boolean debug;
    private Boolean reset;
    private String wideThreshold;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Configuration.class);

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
            String loggerName = key.substring(7);   // logger names often contain "."
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
        } else if (key.startsWith("additivity")) {
            String loggerName = key.substring(11);
            Logger logger = loggers.get(loggerName);
            if (logger == null) {
                logger = new Logger();             // subelements level and appender-ref are not compulsory
                logger.setName(loggerName);
                loggers.put(loggerName, logger);
            }
            if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                AppUtils.crash("Wrong value for additivity has been given.");
            } else {
                logger.setAdditivity(value);
            }
        } else if (key.equals("threshold")) {
            try {
                wideThreshold = Threshold.valueOf(value).toString();
            } catch (IllegalArgumentException ex) {
                AppUtils.crash("Wrong hierarchy-wide threshold has been given.", ex);
            }
        }

    }

    public void verify() {
        // verify loggers - TODO

        // verify appenders + layouts
        for (Appender appender : appenders.values()) {
            boolean loggedAlready = false;
            // verify layouts
            if (appender.getLayoutClassName() != null) {     // there is a layout present
                for (Layout layout : Layout.values()) {
                    if (appender.getLayoutClassName().equalsIgnoreCase(layout.toString())) {
                        for (String layoutParam : appender.getLayoutParams().keySet()) {
                            if (!layoutParam.equalsIgnoreCase(layout.getParam1())
                                    && !layoutParam.equalsIgnoreCase(layout.getParam2())) {
                                AppUtils.crash("You have entered wrong layout parameter.");
                            }
                        }
                    }
                }
            }
            Iterator<Map.Entry<String, String>> iter = appender.getParams().entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> paramCouple = iter.next();
                if (paramCouple.getKey().equals("threshold")) {
                    try {
                        Threshold.valueOf(paramCouple.getValue());
                    } catch (IllegalArgumentException ex) {
                        AppUtils.crash("You have entered wrong threshold for appender" + appender.getName());
                    }
                } else {
                    try {
                        boolean paramFound = false;
                        for (String param : AppenderParams.valueOf(appender.getName()).getParams()) {
                            if (param.equalsIgnoreCase(paramCouple.getKey())) {
                                paramFound = true;
                                break;
                            } 
                        }
                        if(!paramFound) {
                            AppUtils.crash("You have entered wrong parameter \""
                                    + paramCouple.getKey() + "\" for appender: " + appender.getName());
                        }
                    } catch (IllegalArgumentException ex) {
                        // custom defined appender: possible & it can have any parameter
                        if(!loggedAlready) {
                            log.info("Custom appender: " + appender.getName());
                        }
                        loggedAlready = true;
                    }
                }
            }
            // + verify correctness of some values for defined parameters
        }

        // verify other object structure - TODO
    }

    public Document toXML() {
        Document document = DocumentHelper.createDocument();
        document.addDocType("log4j:configuration", null, "log4j.dtd");
        Element rootElement = document.addElement("log4j:configuration");
        rootElement.addNamespace("log4j", "http://jakarta.apache.org/log4j/");
        if (wideThreshold != null) {
            rootElement.addAttribute("threshold", wideThreshold);
        }

        for (Appender appender : appenders.values()) {
            appender.addThisToElement(rootElement);
        }
        for (Logger logger : loggers.values()) {
            logger.addThisToElement(rootElement);
        }
        if (rootLogger != null) {
            rootLogger.addThisToElement(rootElement);
        }
        return document;
    }
}
