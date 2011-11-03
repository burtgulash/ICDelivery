import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

/**
 * Command line options parser
 *
 * 1) specify string, boolean or integer options
 * 2) parse String[] args
 * 3) retrieve options and left over arguments
 * 4) profit.
 *
 * Don't use -h or --help for String option.
 */
public class Parser {

    private Map<String, Object> values;
    private List<Option> options;

    private String chapeau;
    private String usage;



    /**
     * Constructs new Parser object.
     *
     * @param title to be printed in the beginning of help string
     * @param usage usage string
     */
    public Parser(String title, String usage) {
        values      = new HashMap<String, Object>();
        options     = new LinkedList<Option>();
        chapeau     = title;
        this.usage  = usage;

        options.add(new HelpOption());
    }

    /**
     * Constructs new Parser object.
     */
    public Parser() {
        this(null, "usage:");
    }


    /**
     * Prints help.
     */
    private void printHelp() {
        if (chapeau != null) {
            System.err.println(chapeau);
            System.err.println();
        }

        System.err.println(usage);
        for (Option option : options)
            System.err.println(String.format("    -%s | --%s   \t\t%s",
                                  option.abbr, option.name, option.help));
    }


    /**
     * Retrieve parsed value for given option.
     * Should be called after parsing.
     *
     * @param name name of an option
     * @return Parsed value or null if it wasn't specified 
     *         or name doesn't exist.
     */
    public Object getValue(String name) {
        return values.get(name);
    }


    /**
     * Parse previously specified options and returns non-option arguments.
     *
     * @param args Command line arguments
     * @return Non optional arguments.
     * @throws MissingValueException if there was no value to an option.
     * @throws UnknownArgumentException if an argument was an option but was
     *                                  not specified.
     * @throws NumberFormatException if an integer option could not be parsed.
     */
    public String[] parse(String[] args) 
          throws MissingValueException, UnknownArgumentException,
                 NumberFormatException 
    {
        List<String> leftOvers = new LinkedList<String>();

        outer:
        for (int i = 0; i < args.length; i++) {
            // if it is an option
            if (args[i].startsWith("-")) {
                for (Option option : options) {
                    if (option.matches(args[i])) {
                        i = option.addValue(args, i);
                        continue outer;
                    }
                }
                throw new UnknownArgumentException(args[i]);
            // else non-option
            } else {
                leftOvers.add(args[i]);
            }
        }

        return leftOvers.toArray(new String[leftOvers.size()]);
    }


    /**
     * Specifies integer option
     *
     * @param longName full name of the option
     * @param shortName should be one character identifier of the option
     * @param help help message
     */
    public void 
    addIntegerOption(String longName, String shortName, String help) {
        addIntegerOption(longName, shortName, help, 0);
    }

    /**
     * Specifies integer option
     *
     * @param longName full name of the option
     * @param shortName should be one character identifier of the option
     * @param help help message
     * @param defaultValue value if the option was not present
     */
    public void 
    addIntegerOption(String longName, String shortName,
                     String help, int defaultValue) {
        String regex =
           String.format("(?:-%s|--%s=)(\\d*)|--%s", 
                          shortName, longName, longName);

        options.add(new IntegerOption(longName, shortName, regex, help));
        values.put(longName, defaultValue);
    }



    /**
     * Specifies string option
     *
     * @param longName full name of the option
     * @param shortName should be one character identifier of the option
     * @param help help message
     */
    public void 
    addStringOption(String longName, String shortName, String help) {
        addStringOption(longName, shortName, help, null);
    }


    /**
     * Specifies string option
     *
     * @param longName full name of the option
     * @param shortName should be one character identifier of the option
     * @param help help message
     * @param defaultValue value if the option was not present
     */
    public void 
    addStringOption(String longName, String shortName, 
                    String help, String defaultValue)
    {
        String regex =
           String.format("--%s=(\\S+)|-%s|--%s", longName, shortName, longName);

        options.add(new StringOption(longName, shortName, regex, help));
        values.put(longName, defaultValue);
    }



    /**
     * Specifies flag
     *
     * @param longName full name of the option
     * @param shortName should be one character identifier of the option
     * @param help help message
     */
    public void 
    addBooleanOption(String longName, String shortName, String help) {
        addBooleanOption(longName, shortName, help, false);
    }


    /**
     * Specifies flag
     *
     * @param longName full name of the option
     * @param shortName should be one character identifier of the option
     * @param help help message
     * @param defaultValue value if the option was not present
     */
    public void 
    addBooleanOption(String longName, String shortName, 
                     String help, boolean defaultValue) 
    {
        String regex = String.format("-%s|--%s", shortName, longName);
        options.add(new BooleanOption(longName, shortName, regex, help));
        values.put(longName, defaultValue);
    }





    private abstract class Option {
        String name, abbr, regex, help;

        Option (String name, String abbr, String regex, String help) {
            this.name   = name;
            this.abbr   = abbr;
            this.regex  = regex;
            this.help   = help;
        }

        final boolean matches(String arg) {
            return Pattern.matches(regex, arg);
        }

        abstract int addValue(String[] args, int i) 
                     throws MissingValueException ;
    }


    private class StringOption extends Option {
        StringOption(String name, String abbr, String regex, String help) {
            super(name, abbr, regex, help);
        }

        @Override
        int addValue(String[] args, int i) throws MissingValueException {
            Matcher m = Pattern.compile(regex).matcher(args[i]);
            m.find();

            String tmp = m.group(1);
            if (tmp == null || tmp.equals("")) {
                if (args[i].endsWith("=") || i + 1 >= args.length)
                    throw new MissingValueException(args[i]);
                tmp = args[++i];
            }
            values.put(name, tmp);

            return i;
        }
    }


    private class IntegerOption extends Option {
        IntegerOption(String name, String abbr, String regex, String help) {
            super(name, abbr, regex, help);
        }

        @Override
        int addValue(String[] args, int i) 
                     throws MissingValueException, NumberFormatException 
        {
            Matcher m = Pattern.compile(regex).matcher(args[i]);
            m.find();

            String tmp = m.group(1);
            if (tmp == null || tmp.equals("")) {
                if (args[i].endsWith("=") || i + 1 >= args.length)
                    throw new MissingValueException(args[i]);
                tmp = args[++i];
            }

            Integer parsed = Integer.valueOf(tmp);
            values.put(name, parsed);

            return i;
        }
    }


    private class BooleanOption extends Option {
        BooleanOption(String name, String abbr, String regex, String help) {
            super(name, abbr, regex, help);
        }

        @Override
        int addValue(String[] args, int i) {
            values.put(name, true);
            return i;
        }
    }

    private class HelpOption extends Option {
        HelpOption() {
            super("help", "h", "-h|--help", "shows help");
        }

        @Override
         int addValue(String[] args, int i) {
            printHelp();
            System.exit(1);
            return i;
        }
    }




    /**
     * Thrown when there is no value to an option but should be present.
     */
    public static class MissingValueException extends Exception {
        final String arg;
        MissingValueException(String arg) {
            super();
            this.arg = arg;
        }
    }

    /**
     * Thrown if argument is an option but not recognized.
     */
    public static class UnknownArgumentException extends Exception {
        final String arg;
        UnknownArgumentException(String arg) {
            super();
            this.arg = arg;
        }
    }
}
