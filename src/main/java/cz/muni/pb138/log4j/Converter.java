package cz.muni.pb138.log4j;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public interface Converter {
    public void convert(File sourceFile, File outputFile) throws Exception;
    
    public void convert(InputStream inputStream, OutputStream outputStream) throws Exception;
}