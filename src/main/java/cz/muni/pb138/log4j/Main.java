package cz.muni.pb138.log4j;

import java.io.Console;
import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;


/**
 * Main deals with command line arguments
 * 
 */
public class Main {

    private static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        // dealing with command line arguments
        Option inputFileOption = OptionBuilder.withArgName("file").hasArg()
                .withDescription("Input file to convert")
                .create("i");
        Option outputFileOption = OptionBuilder.withArgName("file").hasArg()
                .withDescription("Output file")
                .create("o");
        Option helpOption = new Option("h", "Display this help");

        Options options = new Options();

        options.addOption(inputFileOption);
        options.addOption(outputFileOption);
        options.addOption(helpOption);

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException exp) {
            log.error(exp.getMessage());
            displayHelpAndExit(options, 1);
        }

        if (cmd.hasOption("h")) {
            displayHelpAndExit(options, 0);
        }

        if (!cmd.hasOption("i")) {
            log.error("Please select input file");
            displayHelpAndExit(options, 1);
        }
        
        String inputFileName = cmd.getOptionValue("i");
        String outputFileName = null;

        Converter converter;

        if (cmd.hasOption("o")) {
            outputFileName = cmd.getOptionValue("o");
        }

        if (inputFileName.contains(".xml")) {
            if (outputFileName == null) {
                outputFileName = inputFileName.replace(".xml", ".properties");
            }
            
            converter = new XmlToPropsConverter();
        } else {
            if (outputFileName == null) {
                outputFileName = inputFileName.replace(".properties", ".xml");
            }
            
            converter = new PropsToXmlConverter();
        }
        
        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);
        
        if (!inputFile.exists()) {
            System.err.println("Input file doesn't exist");
            System.exit(1);
        }

        try {
            converter.convert(inputFile, outputFile);
        } catch (Exception ex) {
            System.out.println("Conversion failed.");
            System.exit(1);
        }
        
        System.out.println("File successfully converted");
    }

    private static void displayHelpAndExit(Options options, int exitValue) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("log4j-convert", options);
        System.exit(1);
    }
}