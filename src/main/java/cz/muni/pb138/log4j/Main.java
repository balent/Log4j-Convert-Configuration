package cz.muni.pb138.log4j;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Main deals with command line arguments
 * 
 */
public class Main {
    public static void main(String[] args) {

        // dealing with command line arguments
        Option xmlFileOption = OptionBuilder.withArgName("file").hasArg()
                .withDescription("XML File to convert to properties")
                .create("j");
        Option propertyFileOption = OptionBuilder.withArgName("file").hasArg()
                .withDescription("Property File to convert to XML")
                .create("l");        
        Option outputFileOption = OptionBuilder.withArgName("file").hasArg()
                .withDescription("Output file")
                .create("o");
        Option helpOption = new Option("h", "Display this help");
        
        Options options = new Options();
        
        options.addOption(xmlFileOption);
        options.addOption(propertyFileOption);
        options.addOption(outputFileOption);
        options.addOption(helpOption);
        

        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            
            if (cmd.hasOption("h")) {
                displayHelpAndExit(options, 0);
            }
            
            if (cmd.hasOption("j") && cmd.hasOption("l")) {
                System.err.println("Both arguments are specified, select XML or Property file");
                System.err.println();
                displayHelpAndExit(options, 1);
            }
            
            if (!(cmd.hasOption("j") || cmd.hasOption("l"))) {
                System.err.println("Please select XML or Property file");
                System.err.println();
                displayHelpAndExit(options, 1);
            }

        } catch (ParseException exp) {
            System.err.println(exp.getMessage());
            System.err.println();
            displayHelpAndExit(options, 1);
        }

        System.out.println("Hello World!");
        
        /* here will be something like
         * 
         * String outputFile = ...
         * Log4jConverter log4jConverter = new Log4jConverter(inputFile, outputFile);
         * log4jConverter.convert();
         * 
         */
    }
    
    private static void displayHelpAndExit(Options options, int exitValue) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("log4j-convert", options);
        System.exit(exitValue);
    }
}