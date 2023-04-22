package planettrade.logger;

public final class Logger {

    private static Logger instance;
    private LogLevel logLevel = LogLevel.lowest();

    private Logger() {
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public static void seed(LogLevel logLevel) {
        getInstance().logLevel = logLevel;
    }

    public static void error(String message) {
        LogLevel logLevel = getInstance().logLevel;
        if (logLevel.isAcceptable(LogLevel.ERROR)) {
            printWithColor("ERROR: " + message, ConsoleColor.RED);
        }
    }

    private static void printWithColor(String message, ConsoleColor...color) {
        for (ConsoleColor consoleColor : color) {
            System.out.print(consoleColor);
        }
        System.out.println(message + ConsoleColor.RESET);
    }

    public static void warning(String message) {
        LogLevel logLevel = getInstance().logLevel;
        if (logLevel.isAcceptable(LogLevel.WARNING)) {
            printWithColor("WARNING: " + message, ConsoleColor.YELLOW);
        }
    }

    public static void info(String message) {
        LogLevel logLevel = getInstance().logLevel;
        if (logLevel.isAcceptable(LogLevel.INFO)) {
            printWithColor("INFO: " + message, ConsoleColor.WHITE_UNDERLINED);
        }
    }

    public static void debug(String message) {
        LogLevel logLevel = getInstance().logLevel;
        if (logLevel.isAcceptable(LogLevel.DEBUG)) {
            printWithColor("DEBUG: " + message,ConsoleColor.BLACK_BOLD ,ConsoleColor.WHITE_BACKGROUND);
        }
    }

    public static void release(String message) {
        LogLevel logLevel = getInstance().logLevel;
        if (logLevel.isAcceptable(LogLevel.RELEASE)) {
            printWithColor("RELEASE: " + message, (ConsoleColor.BLUE_BRIGHT));
        }
    }
}
