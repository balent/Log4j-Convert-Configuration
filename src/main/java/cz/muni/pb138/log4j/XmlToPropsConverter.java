package cz.muni.pb138.log4j;

import java.io.File;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;



public class XmlToPropsConverter implements Converter {
    private static Logger log = Logger.getLogger(XmlToPropsConverter.class);
    
    public void convert(File sourceFile, File outputFile) throws Exception {
        DtdValidator dtdValidator = new DtdValidator(sourceFile);
        
        Document document = dtdValidator.validate();
        
        if (document == null) {
            log.error("Provided file is not valid log4j xml configuration.");
            System.exit(2);
        }
        
        Element rootElement = document.getRootElement();
        
        // now you have root element and you can work with it.
    }
}
