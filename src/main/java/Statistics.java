import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {
    private double totalTraffic;
    private ZonedDateTime minTime;
    private ZonedDateTime maxTime;
    private HashSet<String> existingPages = new HashSet<>();
    private HashMap<String, Integer> osFrequency = new HashMap<>();

    public Statistics() {
        minTime = ZonedDateTime.now();
        maxTime = ZonedDateTime.now();
    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getResponseSize();

        if (logEntry.getTime().compareTo(minTime) > 0) {
            minTime = logEntry.getTime();
        } else {
            maxTime = logEntry.getTime();
        }

        if (logEntry.getResponseCode() == 200) {
            existingPages.add(logEntry.getPath());
        }

        String os = logEntry.getAgent().getOsType();
        if (osFrequency.containsKey(os)) {
            osFrequency.replace(os, osFrequency.get(os) + 1);
        } else {
            osFrequency.put(os, 1);
        }
    }

    public double getTrafficRate() {
        return totalTraffic / Duration.between(maxTime, minTime).toHours();
    }

    public HashSet<String> getExistingPages() {
        return new HashSet<>(existingPages);
    }

    public HashMap<String, Double> getOsStatistics() {
        HashMap<String, Double> osStatistics = new HashMap<>();
        int totalOsCount = 0;

        for (Map.Entry<String, Integer> currOs : osFrequency.entrySet()) {
            totalOsCount += currOs.getValue();
        }

        for (Map.Entry<String, Integer> currOs : osFrequency.entrySet()) {
            osStatistics.put(currOs.getKey(), (double) currOs.getValue() / totalOsCount);
        }

        return osStatistics;
    }
}
