package cz.muni.pb138.log4j;

import cz.muni.pb138.log4j.model.Appender;
import cz.muni.pb138.log4j.model.Configuration;
import cz.muni.pb138.log4j.model.LoggerFactory;
import cz.muni.pb138.log4j.model.Plugin;
import cz.muni.pb138.log4j.model.Renderer;
import cz.muni.pb138.log4j.model.Threshold;
import cz.muni.pb138.log4j.model.ThrowableRenderer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;



public class XmlToPropsConverter implements Converter {
    private static Logger log = Logger.getLogger(XmlToPropsConverter.class);
    private Configuration configuration;
    
    public void convert(File sourceFile, File outputFile) throws Exception {
        DtdValidator dtdValidator = new DtdValidator(sourceFile);
        
        Document document = dtdValidator.validate();
        
        if (document == null) {
            AppUtils.crash("Provided file is not valid log4j xml configuration.");
        }
        
        Element rootElement = document.getRootElement();
        
        configuration = new Configuration();
        
        configuration.setUpFromElement(rootElement);
                
        //writing out
        
        List<String> prop = configuration.toProperties();
        AppUtils.store(prop, outputFile);
    }
}
