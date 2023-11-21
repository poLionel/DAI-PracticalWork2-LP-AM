package ch.heigvd.shared.logs;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class Logger {
    private static Logger instance;
    private Logger() { }
    public static synchronized Logger getInstance() {
        if(instance == null) instance = new Logger();
        return instance;
    }

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm ss.mmm");
    public void log(String message, Object sender) {
        log(message, sender, LogLevel.Information);
    }
    public void log(String message, Object sender, LogLevel logLevel) {
        String output = String.format("[%s@%s | %s | %s]\n",
                logLevel, simpleDateFormat.format(LocalDateTime.now()), sender.getClass(), message);

        System.out.println(output);
    }
}
