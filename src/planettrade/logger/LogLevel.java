package planettrade.logger;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

public enum LogLevel {

    SILENT(-1) {
        @Override
        boolean isAcceptable(LogLevel logLevel) {
            return false;
        }
    },
    DEBUG(0) {
        @Override
        boolean isAcceptable(LogLevel logLevel) {
            return logLevel != SILENT;
        }
    },
    INFO(1),
    WARNING(2) ,
    ERROR(3),

    RELEASE(4) {
        @Override
        boolean isAcceptable(LogLevel logLevel) {
            return logLevel == RELEASE;
        }
    };

    private static Comparator<LogLevel> comparator = Comparator.comparingInt(logLevel -> logLevel.level);
    private final int level;

    LogLevel(int level) {
        this.level = level;
    }

    boolean isAcceptable(LogLevel logLevel) {
        return logLevel.isHigherThanOrEqualTo(this);
    }

    void ifAcceptable(LogLevel logLevel, Function<LogLevel, Void> function) {
        if (isAcceptable(logLevel)) {
            function.apply(logLevel);
        }
    }

    public static LogLevel lowest() {
        return Arrays.stream(values()).sorted(comparator).findFirst().orElseThrow();
    }

    public static LogLevel highest() {
        return Arrays.stream(values()).sorted(comparator.reversed()).findFirst().orElseThrow();
    }

    public boolean isHigherThan(LogLevel logLevel) {
        return comparator.compare(this, logLevel) > 0;
    }

    public boolean isLowerThan(LogLevel logLevel) {
        return comparator.compare(this, logLevel) < 0;
    }

    public boolean isHigherThanOrEqualTo(LogLevel logLevel) {
        return comparator.compare(this, logLevel) >= 0;
    }


    public boolean isLowerThanOrEqualTo(LogLevel logLevel) {
        return comparator.compare(this, logLevel) <= 0;
    }

    public boolean isEqualTo(LogLevel logLevel) {
        return comparator.compare(this, logLevel) == 0;
    }
}
