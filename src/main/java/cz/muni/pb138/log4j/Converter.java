package cz.muni.pb138.log4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public interface Converter {
    
    /**
     * Method for converting files from source to output file
     * 
     * @param sourceFile - path to source file
     * @param outputFile - path to destination file
     * @throws Exception  - throws if application fails
     */
    public void convert(File sourceFile, File outputFile) throws Exception;
    
    /**
     * Method for converting files from source to output file
     * 
     * @param inputStream - inputStream of source file
     * @param outputStream - outputStream to destination file
     * @throws Exception - throws if application fails
     */
    public void convert(InputStream inputStream, OutputStream outputStream) throws Exception;
}