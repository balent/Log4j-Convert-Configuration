package cz.muni.pb138.log4j.model;

public enum Layout {
    SimpleLayout("org.apache.log4j.SimpleLayout", null, null),
    PatternLayout("org.apache.log4j.PatternLayout", "ConversionPattern", null),
    EnhancedPatternLayout("org.apache.log4j.EnhancedPatternLayout", "ConversionPattern", null),
    DateLayout("org.apache.log4j.DateLayout", "DateFormat", null),
    HTMLLayout("org.apache.log4j.HTMLLayout", "LocationInfo", "Title"),
    XMLLayout("org.apache.log4j.XMLLayout", "LocationInfo", null);
    
    private String fullName;
    private String param1;
    private String param2;
    
    Layout(String fullName, String param1, String param2) {
        this.fullName = fullName;
        this.param1 = param1;
        this.param2 = param2;
    }

    public String getParam1() {
        return param1;
    }

    public String getParam2() {
        return param2;
    }
    
    @Override
    public String toString() {
        return fullName;
    }
}