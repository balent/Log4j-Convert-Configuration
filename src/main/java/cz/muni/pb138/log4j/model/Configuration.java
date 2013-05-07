package cz.muni.pb138.log4j.model;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cz.muni.pb138.log4j.AppUtils;
import java.util.Iterator;
import java.util.Properties;

public class Configuration {

    private Map<String, Renderer> renderers = new HashMap<String, Renderer>();
    private Map<String, Logger> loggers = new HashMap<String, Logger>();
    private Map<String, Appender> appenders = new HashMap<String, Appender>();
    private Map<String, Plugin> plugins = new HashMap<String, Plugin>();
    private Logger rootLogger;
    private Threshold threshold;
    private Boolean debug;
    private Boolean reset;
    private String wideThreshold;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Configuration.class);
    private ThrowableRenderer throwableRenderer;
    private LoggerFactory loggerFactory;

    public LoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    public void setLoggerFactory(LoggerFactory loggerFactory) {
        this.loggerFactory = loggerFactory;
    }

    public ThrowableRenderer getThrowableRenderer() {
        return throwableRenderer;
    }

    public void setThrowableRenderer(ThrowableRenderer throwableRenderer) {
        this.throwableRenderer = throwableRenderer;
    }

    public Map<String, Renderer> getRenderers() {
        return renderers;
    }
    
    public void addRenderer(Renderer renderer){
        if(renderers.get(renderer.getRenderedClass()) == null){
            renderers.put(renderer.getRenderedClass(), renderer);
        }else{
            AppUtils.crash("Two renderers for the class: " + renderer.getRenderedClass());
        }
    }
    
    public void addPlugin(Plugin plugin){
        if(plugins.get(plugin.getName()) == null){
            plugins.put(plugin.getName(), plugin);
        }else{
            AppUtils.crash("Two plugins with the same name: " + plugin.getName());
        }
    }

    public Map<String, Appender> getAppenders() {
        return appenders;
    }
    
    public void addAppender(Appender appender){
        if(appenders.get(appender.getName()) == null){
            appenders.put(appender.getName(), appender);
        }else{
            AppUtils.crash("Two appenders with the same name: " + appender.getName());
        }
    }

    public Map<String, Logger> getLoggers() {
        return loggers;
    }
    
    public void addLogger(Logger logger){
        if(loggers.get(logger.getName()) == null){
            loggers.put(logger.getName(), logger);
        }else{
            AppUtils.crash("Two loggers with the same name: " + logger.getName());
        }
    }

    public Logger getRootLogger() {
        return rootLogger;
    }

    public Threshold getThreshold() {
        return threshold;
    }

    public Boolean isDebug() {
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

    public void setReset(String reset) {
         if ("true".equalsIgnoreCase(reset)) {
            this.reset = true;
        } else if ("false".equalsIgnoreCase(reset)) {
            this.reset = false;
        } else {
            AppUtils.crash("You have specified wrong configuration reset parameter");
        }
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
    
    public Properties toProperties(){        
        Properties prop = new Properties();
        
        for(cz.muni.pb138.log4j.model.Logger logger : loggers.values()) {
            logger.toProperty(prop); //todo
        }
        return prop;
    }
}
