package com.consol.citrus.db.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Christoph Deppisch
 */
public class JdbcServerOptions {

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(JdbcServerOptions.class);

    protected final List<CliOption> options = new ArrayList<>();

    public JdbcServerOptions() {
        options.add(new CliOption("h", "help", "Displays cli option usage") {
            @Override
            protected void doProcess(JdbcServerConfiguration configuration, String arg, String value, LinkedList<String> remainingArgs) {
                StringBuilder builder = new StringBuilder();
                builder.append(System.lineSeparator()).append("Jdbc server option usage:").append(System.lineSeparator());
                for (CliOption option : options) {
                    builder.append(option.getInformation()).append(System.lineSeparator());
                }

                log.info(builder.toString());
                configuration.setTimeToLive(1000);
            }
        });

        options.add(new CliOption("t", "time", "Maximum time in milliseconds the server should be up and running - server will terminate automatically when time exceeds") {
            @Override
            protected void doProcess(JdbcServerConfiguration configuration, String arg, String value, LinkedList<String> remainingArgs){
                if (value != null && value.length() > 0) {
                    configuration.setTimeToLive(Long.valueOf(value));
                } else {
                    throw new JdbcServerException("Missing parameter value for -t/-time option");
                }
            }
        });

        options.add(new CliOption("d", "deamon", "Flag to indicate that this server should be started as deamon thread") {
            @Override
            protected void doProcess(JdbcServerConfiguration configuration, String arg, String value, LinkedList<String> remainingArgs){
                if (value != null && value.length() > 0) {
                    configuration.setDeamon(Boolean.valueOf(value));
                } else {
                    throw new JdbcServerException("Missing parameter value for -d/-deamon option");
                }
            }
        });

        options.add(new CliOption("p", "port", "Jdbc server port") {
            @Override
            protected void doProcess(JdbcServerConfiguration configuration, String arg, String value, LinkedList<String> remainingArgs){
                if (value != null && value.length() > 0) {
                    configuration.setPort(Integer.valueOf(value));
                } else {
                    throw new JdbcServerException("Missing parameter value for -p/-port option");
                }
            }
        });
    }

    /**
     * Apply options based on given argument line.
     * @param configuration
     * @param arguments
     * @throws JdbcServerException
     */
    public void apply(JdbcServerConfiguration configuration, String[] arguments){
        LinkedList<String> args = new LinkedList<String>(Arrays.asList(arguments));

        while (!args.isEmpty()) {
            String arg = args.removeFirst();

            for (CliOption option : options) {
                if (option.processOption(configuration, arg, args)) {
                    break;
                }
            }
        }
    }

    /**
     * Command line option represented with either short of full name.
     */
    public abstract class CliOption {
        private String shortName;
        private String fullName;
        private String description;

        protected CliOption(String shortName, String fullName, String description) {
            this.shortName = "-" + shortName;
            this.fullName = "-" + fullName;
            this.description = description;
        }

        public boolean processOption(JdbcServerConfiguration configuration, String arg, LinkedList<String> remainingArgs){
            if (arg.equalsIgnoreCase(shortName) || fullName.startsWith(arg)) {
                if (remainingArgs.isEmpty()) {
                    doProcess(configuration, arg, null, remainingArgs);
                } else {
                    doProcess(configuration, arg, remainingArgs.removeFirst(), remainingArgs);
                }
                return true;
            }
            return false;
        }

        public String getShortName() {
            return shortName;
        }

        public String getDescription() {
            return description;
        }

        public String getFullName() {
            return fullName;
        }

        public String getInformation() {
            return "  " + getShortName() + " or " + getFullName() + " = " + getDescription();
        }

        protected abstract void doProcess(JdbcServerConfiguration configuration, String arg, String value, LinkedList<String> remainingArgs) throws JdbcServerException;
    }
}
