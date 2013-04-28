package cz.muni.pb138.log4j;

import org.apache.log4j.Logger;

public class AppUtils {
    private static Logger log = Logger.getLogger(AppUtils.class);
    
    public static void crash(String errorMessage) {
        log.error(errorMessage);
        System.exit(1);
    }
    
    public static void crash(String errorMessage, Exception exception) {
        log.error(errorMessage);
        log.debug(errorMessage, exception);
        System.exit(1);
    }
}
