package cz.muni.pb138.log4j;

import java.util.List;
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
    
    public static String prefix(String str) {
        return "log4j." + str;        
    }
    
    public static String join(List<?> list, String delim) {
        int len = list.size();
        if (len == 0) {
                return "";
            }

        StringBuilder sb = new StringBuilder(list.get(0).toString());
        for (int i = 1; i < len; i++) {
            sb.append(delim);
            sb.append(list.get(i).toString());
        }

        return sb.toString();
    }
}
