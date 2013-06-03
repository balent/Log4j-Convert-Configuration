package cz.muni.pb138.log4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public interface Converter {
    public void convert(File sourceFile, File outputFile) throws Exception;
    
    public void convert(InputStream inputStream, OutputStream outputStream) throws Exception;
}