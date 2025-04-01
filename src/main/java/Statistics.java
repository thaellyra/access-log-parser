import java.time.Duration;
import java.time.ZonedDateTime;

public class Statistics {
    private double totalTraffic;
    private ZonedDateTime minTime;
    private ZonedDateTime maxTime;

    public Statistics() {
        minTime = ZonedDateTime.now();
        maxTime = ZonedDateTime.now();
    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getResponseSize();
        if(logEntry.getTime().compareTo(minTime) > 0) {
            minTime = logEntry.getTime();
        } else {
            maxTime = logEntry.getTime();
        }
    }

    public double getTrafficRate() {
        return totalTraffic / Duration.between(minTime, maxTime).toHours();
    }
}
