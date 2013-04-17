package cz.muni.pb138.log4j;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropsToXmlConverter implements Converter {

    private static Logger log = Logger.getLogger(PropsToXmlConverter.class);
    
    public void convert(File sourceFile, File outputFile) throws Exception {
        FileInputStream fis = new FileInputStream(sourceFile);
        
        Properties properties = new Properties();

        // now you have properties object and you can work with it.
    }

}
