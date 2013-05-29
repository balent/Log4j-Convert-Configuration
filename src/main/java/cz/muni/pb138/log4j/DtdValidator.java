package cz.muni.pb138.log4j;

import java.io.File;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class DtdValidator {
    private static Logger log = Logger.getLogger(DtdValidator.class);
    
    InputStream xmlFile;
    
    public DtdValidator(InputStream xmlFile) {
        this.xmlFile = xmlFile;
    }
    
    public Document validate() {
        // resolver to load dtd from log4j resources
        EntityResolver resolver = new EntityResolver() {
            public InputSource resolveEntity(String publicId, String systemId) {
                if (systemId.contains("log4j.dtd")) {
                    InputStream in = getClass().getResourceAsStream(
                        "/org/apache/log4j/xml/log4j.dtd"
                    );
                    return new InputSource(in);
                }
                return null;
            }
        };

        // Reading document from input stream
        SAXReader saxReader = new SAXReader();
        saxReader.setEntityResolver(resolver);
        saxReader.setValidation(true);
        
        try {
            Document doc = saxReader.read(xmlFile);
            log.debug("Succesfully validated against DTD");
            return doc;
        } catch (DocumentException dex) {
            log.error(dex.getMessage());
            log.debug(dex.getMessage(), dex);
        }
        return null;
    }
}
