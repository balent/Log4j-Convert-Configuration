package cz.muni.pb138.log4j;

import java.io.File;

public interface Converter {
    public void convert(File sourceFile, File outputFile) throws Exception;
}