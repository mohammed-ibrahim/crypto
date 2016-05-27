package org.crypto.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import org.apache.commons.cli.GnuParser;;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.HelpFormatter;

class CloValidator {

    public static Logger log = LoggerFactory.getLogger(CloValidator.class);

    public static Config validate(String []args) {
        try {
            return validateClo(args);
        } catch (Exception ue) {
            log.info(ue.getMessage());
            return null;
        }
    }

    private static Config validateClo(String []args) throws Exception {

        Options options = new Options();

        options.addOption(new Option("i", "input-file", true, "Name of the input file."));
        options.addOption(new Option("o", "output-file", true, "Name of the output file."));
        options.addOption(new Option("e", "encrypt", false, "Perform encryption."));
        options.addOption(new Option("d", "decrypt", false, "Perform Decryption."));
        options.addOption(new Option("k", "key", true, "Secret Key."));
        options.addOption(new Option("h", "help", false, "Help page."));

        GnuParser parser = new GnuParser();

        CommandLine commandLine = parser.parse(options, args);
        Config config = new Config();

        if (commandLine.hasOption("help")) {
            displayHelp(options);
            return null;
        }

        if (!commandLine.hasOption("input-file")) {
            throw new Exception("Input file option is required");
        }
        config.withInputFileName(commandLine.getOptionValue("input-file"));

        if (!commandLine.hasOption("output-file")) {
            throw new Exception("Output file option is required");
        }
        config.withOutputFileName(commandLine.getOptionValue("output-file"));

        if (!commandLine.hasOption("key")) {
            throw new Exception("Secret key is required");
        }
        String key = commandLine.getOptionValue("key");

        if (key.length() > 16 || key.length() < 1) {
            throw new RuntimeException("Key length has to be between 1 and 16");
        }

        config.withSecretKey(key);

        if (commandLine.hasOption("encrypt") == false && commandLine.hasOption("decrypt") == false) {
            throw new RuntimeException("Either --encrypt or --decrypt is required");
        } else if (commandLine.hasOption("encrypt")) {
            config.withMode(Config.OperationMode.ENCRYPT);
        } else if (commandLine.hasOption("decrypt")) {
            config.withMode(Config.OperationMode.DECRYPT);
        }

        return config;
    }

    private static void displayHelp(Options options) {
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("java -jar crypto.jar --key <secret_key> --encrypt|--decrypt --input-file <input_file_name> --output-file <output_file_name>", options);
    }
}
