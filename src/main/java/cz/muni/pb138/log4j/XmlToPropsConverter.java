package cz.muni.pb138.log4j;

import java.io.File;

import org.apache.log4j.Logger;

public class XmlToPropsConverter implements Converter {
    private static Logger log = Logger.getLogger(XmlToPropsConverter.class);
    
    public void convert(File sourceFile, File outputFile) throws Exception {
        DtdValidator dtdValidator = new DtdValidator(sourceFile);
        
        boolean valid = dtdValidator.validate();
        
        if (!valid) {
            log.error("Provided file is not valid log4j xml configuration.");
            System.exit(2);
        }
    }
}
