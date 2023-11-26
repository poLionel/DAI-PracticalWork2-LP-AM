package ch.heigvd.shared.logs;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static boolean enabled;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm ss.SSS");
    public void log(String message, Object sender) {
        log(message, sender, LogLevel.Information);
    }

    public static void log(String message, LogLevel logLevel) {
        if(!enabled) return;
        String output = String.format("[%s @ %s | %s]",
                logLevel, dateFormatter.format(LocalDateTime.now()),  message);
        System.out.println(output);
    }

    public static void log(String message, Object sender, LogLevel logLevel) {
        if(!enabled) return;
        String output = String.format("[%s @ %s | %s | %s]",
                logLevel, dateFormatter.format(LocalDateTime.now()), sender.getClass().getSimpleName(), message);
        System.out.println(output);
    }

    public static void setEnabled() {
        enabled = true;
    }

    public static void setDisabled() {
        enabled = false;
    }
}
