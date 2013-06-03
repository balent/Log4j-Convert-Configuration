/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pb138.log4j;

import cz.muni.pb138.log4j.model.Appender;
import static org.junit.Assert.*;

import cz.muni.pb138.log4j.model.Configuration;
import cz.muni.pb138.log4j.model.Logger;
import cz.muni.pb138.log4j.model.LoggerFactory;
import cz.muni.pb138.log4j.model.Renderer;
import cz.muni.pb138.log4j.model.ThrowableRenderer;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jozef
 */
public class ConfigurationTest {
    private Configuration patternConfiguration;
    private Configuration patternConfiguration1;
    private List<String> patternConfigurationProp;
    
    @Before
    public void setUp() {
        patternConfiguration = new Configuration();
        
        patternConfiguration.setReset("false");
        patternConfiguration.setThreshold("fatal");
        patternConfiguration.setDebug("true");
        
        Renderer renderer = new Renderer();
        renderer.setRenderedClass("classToBeRendered");
        renderer.setRenderingClass("renderingClass");
        patternConfiguration.addRenderer(renderer);
        
        ThrowableRenderer throwRend = new ThrowableRenderer();
        throwRend.setClassName("THROW");
        throwRend.addParam("CATCH","false");
        patternConfiguration.setThrowableRenderer(throwRend);
        
        Logger root;
        Map<String, String> params = new HashMap();
        params.put("rootparam", "rootvalue");
        List<String> apps = new ArrayList<String>();
        apps.add("rootapp-link");
        root = LoggerTest.CreateLogger(null,params,apps,null,"",true);
        Map<String, String> rootparams = new HashMap();
        rootparams.put("rootlevparam", "rootlevvalue");
        root.setLoggerLevel(root.CreateLoggerLevel("WARN", rootparams, "RootLevelClass"));
        patternConfiguration.setRootLogger(root);
        
        Logger logger;
        params.clear();
        params.put("logparam", "logvalue");
        apps.clear();
        apps.add("logapp-link");
        logger = LoggerTest.CreateLogger("LOGGER",params,apps,"true","StandardLogger",false);
        Map<String, String> logparams = new HashMap();
        logparams.put("loglevparam", "loglevvalue");
        logger.setLoggerLevel(logger.CreateLoggerLevel("INFO", logparams, "LogLevelClass"));
        patternConfiguration.addLogger(logger);
        
        LoggerFactory logfac = new LoggerFactory();
        logfac.setClassName("SimpleFactoryClass");
        logfac.addParam("factparam", "factvalue");
        patternConfiguration.setLoggerFactory(logfac);
        
        Appender appender = new Appender();
        appender.setName("FILEAPPENDER");
        appender.setClassName("FileAppender");
        appender.setLayoutClassName("AppenderLayout");
        appender.addParam("appparam", "appvalue");
        appender.addLayoutParam("layparam", "layvalue");
        appender.addAppenderRef("app-link");
        List<String> logs = new ArrayList<String>();
        logs.add("ehlog-link");
        Map<String, String> pars = new HashMap<String, String>();
        pars.put("ehparam","ehvalue");
        appender.setErrorHandler(appender.createErrorHandler("ErrorHandler", pars, logs, "ehapp-link"));
        patternConfiguration.addAppender(appender);       
        
        
        patternConfiguration1 = new Configuration();
        
        patternConfiguration1.setReset("false");
        patternConfiguration1.setThreshold("fatal");
        patternConfiguration1.setDebug("true");
        
        patternConfiguration1.addRenderer(renderer);

        patternConfiguration1.setThrowableRenderer(throwRend);
        
        apps.clear();
        apps.add("rootapp-link");
        root = LoggerTest.CreateLogger(null,null,apps,null,"",true);
        root.setLoggerLevel(root.CreateLoggerLevel("WARN", null, ""));
        patternConfiguration1.setRootLogger(root);
        
        apps.clear();
        apps.add("logapp-link");
        logger = LoggerTest.CreateLogger("LOGGER",null,apps,"true","",false);
        logger.setLoggerLevel(logger.CreateLoggerLevel("INFO", null, "LogLevelClass"));
        patternConfiguration1.addLogger(logger);
        
        patternConfiguration1.setLoggerFactory(logfac);
        
        patternConfiguration1.addAppender(appender);
        
        patternConfigurationProp = new ArrayList<String>();
        patternConfigurationProp.add("log4j.threshold = fatal");
        patternConfigurationProp.add("log4j.reset = false");
        patternConfigurationProp.add("log4j.debug = true");
        
        patternConfigurationProp.add("log4j.renderer.classToBeRendered = renderingClass");
        
        patternConfigurationProp.add("log4j.throwableRenderer = THROW");
        patternConfigurationProp.add("log4j.throwableRenderer.CATCH = false");
        
        patternConfigurationProp.add("log4j.loggerFactory = SimpleFactoryClass");
        patternConfigurationProp.add("log4j.loggerFactory.factparam = factvalue");
        
        patternConfigurationProp.add("log4j.appender.FILEAPPENDER = FileAppender");
        patternConfigurationProp.add("log4j.appender.FILEAPPENDER.errorHandler = ErrorHandler");
        patternConfigurationProp.add("log4j.appender.FILEAPPENDER.errorHandler.ehparam = ehvalue");        
        patternConfigurationProp.add("log4j.appender.FILEAPPENDER.errorHandler.logger-ref = ehlog-link");
        patternConfigurationProp.add("log4j.appender.FILEAPPENDER.errorHandler.appender-ref = ehapp-link");
        patternConfigurationProp.add("log4j.appender.FILEAPPENDER.layout = AppenderLayout");
        patternConfigurationProp.add("log4j.appender.FILEAPPENDER.layout.layparam = layvalue");
        patternConfigurationProp.add("log4j.appender.FILEAPPENDER.appparam = appvalue");         
        patternConfigurationProp.add("log4j.appender.FILEAPPENDER.appender-ref = app-link");
        
        patternConfigurationProp.add("log4j.logger.LOGGER = INFO#LogLevelClass, logapp-link");
        patternConfigurationProp.add("log4j.logger.LOGGER.level.loglevparam = loglevvalue");        
        patternConfigurationProp.add("log4j.additivity.LOGGER = true");
        patternConfigurationProp.add("log4j.logger.LOGGER.logparam = logvalue");
        patternConfigurationProp.add("log4j.logger.LOGGER.class = StandardLogger");

        
        patternConfigurationProp.add("log4j.rootLogger = WARN#RootLevelClass, rootapp-link");
        patternConfigurationProp.add("log4j.rootLogger.level.rootlevparam = rootlevvalue");        
        patternConfigurationProp.add("log4j.rootLogger.rootparam = rootvalue");
    }
    
    @Test
    public void setUpFromElementTest() throws FileNotFoundException{
        
        DtdValidator dtdValidator = new DtdValidator(new FileInputStream("unitTestFiles/patternConfiguration.xml"));
        
        Document document = dtdValidator.validate();
        
        if (document == null) {
            AppUtils.crash("Provided file is not valid log4j xml configuration.");
        }
        
        Element rootElement = document.getRootElement();
        
        Configuration readedConfiguration = new Configuration();
        
        readedConfiguration.setUpFromElement(rootElement);
                
        assertEquals(patternConfiguration, readedConfiguration);
    }
    
    @Test
    public void toPropertyTest() {
        
        List<String> ourOutput = patternConfiguration.toProperty();
               
        Collections.sort(ourOutput);
        Collections.sort(patternConfigurationProp);
        
        assertEquals(ourOutput.size(), patternConfigurationProp.size());
        assertEquals(ourOutput, patternConfigurationProp);
    }
    
    @Test
    public void fromPropertiesToModelTest() throws FileNotFoundException, IOException{
        Properties properties = new Properties();
        properties.load(new FileInputStream("unitTestFiles/patternConfiguration.properties"));

        // now you have properties object and you can work with it.
        Configuration configuration = new Configuration();

        for (String propertyKey : properties.stringPropertyNames()) {
            String key = propertyKey;         
            String value = properties.getProperty(propertyKey);   
            if (key.toLowerCase(Locale.ENGLISH).startsWith("log4j")) {
                String newKey = key.substring(6); // remove initial "log4j."
                configuration.addConfig(newKey, value);
            } else {
                AppUtils.crash("every property name must start with 'log4j': " + key);
            }
        }
        
        assertEquals(patternConfiguration1, configuration);
    }
}
