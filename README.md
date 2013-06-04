Log4j-Convert-Configuration
===========================

Converts Log4j configuration from properties to xml and vice versa

If you want to use converter as an standalone application. Run it with command:

```
java -jar log4j-convert.jar
```

```
usage: log4j-convert
 -h          Display this help
 -i <file>   Input file to convert
 -o <file>   Output file
```

To use library in application use XmlToPropsConverter and PropsToXmlConverter class.

Usage:
```java
Converter converter = new XmlToPropsConverter();
converter.convert(inputStream, outputStream);
```
```java
Converter converter = new PropsToXmlConverter();
converter.convert(inputStream, outputStream);
```
