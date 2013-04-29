package cz.muni.pb138.log4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import cz.muni.pb138.log4j.model.Configuration;

public class PropsToXmlConverter implements Converter {

    private static Logger log = Logger.getLogger(PropsToXmlConverter.class);

    public void convert(File sourceFile, File outputFile) throws Exception {
        FileInputStream fis = new FileInputStream(sourceFile);

        Properties properties = new Properties();
        try {
            properties.load(fis);
        } catch (Exception ex) {
            log.error("Provided file is not in valid property format.");
            System.exit(2);
        }

        // now you have properties object and you can work with it.
        Configuration configuration = new Configuration();

        for (String propertyKey : properties.stringPropertyNames()) {
            String key = propertyKey.toLowerCase();
            String value = properties.getProperty(propertyKey).toLowerCase();
            // TODO: value moze obsahovat aj premennu vo ${} vyraze, tato premenna je definovana sposobom 
            // nazovPremennej = hodnota.
            if (key.startsWith("log4j")) {
                String newKey = key.substring(6); // remove initial "log4j."
                configuration.addConfig(newKey, value);
            } else {
                AppUtils.crash("every property name must start with 'log4j': " + key);
            }
        }

        configuration.verify();

        Document document = configuration.toXML();

        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileWriter(outputFile), format);
        writer.write(document);
        writer.close();
    }
}
