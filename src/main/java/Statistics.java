import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Statistics {
    private double totalTraffic;
    private ZonedDateTime minTime;
    private ZonedDateTime maxTime;
    private HashSet<String> existingPages = new HashSet<>();
    private HashSet<String> notExistingPages = new HashSet<>();
    private HashMap<String, Integer> osFrequency = new HashMap<>();
    private HashMap<String, Integer> browserFrequency = new HashMap<>();
    private boolean firstEntry = true;
    private int numberOfUsers;
    private int numberOfErrorRequests;
    private HashSet<String> uniqueIpAddr = new HashSet<>();
    private HashMap<ZonedDateTime, Integer> attendanceAtTime = new HashMap<>();
    private HashSet<String> referers = new HashSet<>();
    private HashMap<String, Integer> attendancePerUser = new HashMap<>();

    public Statistics() {
    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getResponseSize();

        if (firstEntry) {
            minTime = logEntry.getTime();
            maxTime = logEntry.getTime();
            firstEntry = false;
        }
        if (logEntry.getTime().compareTo(minTime) < 0) {
            minTime = logEntry.getTime();
        } else {
            maxTime = logEntry.getTime();
        }

        if (logEntry.getResponseCode().equals("200")) {
            existingPages.add(logEntry.getPath());
        } else if (logEntry.getResponseCode().equals("404")) {
            notExistingPages.add(logEntry.getPath());
        }

        String os = logEntry.getAgent().getOsType();
        if (osFrequency.containsKey(os)) {
            osFrequency.replace(os, osFrequency.get(os) + 1);
        } else {
            osFrequency.put(os, 1);
        }

        String browser = logEntry.getAgent().getBrowser();
        if (browserFrequency.containsKey(browser)) {
            browserFrequency.replace(browser, browserFrequency.get(browser) + 1);
        } else {
            browserFrequency.put(browser, 1);
        }

        if (!logEntry.getAgent().isBot()) {
            numberOfUsers++;

            uniqueIpAddr.add(logEntry.getIpAddr());

            ZonedDateTime currTime = logEntry.getTime();
            if (attendanceAtTime.containsKey(currTime)) {
                attendanceAtTime.replace(currTime, attendanceAtTime.get(currTime) + 1);
            } else {
                attendanceAtTime.put(currTime, 1);
            }

            String currIpAddr = logEntry.getIpAddr();
            if (attendancePerUser.containsKey(currIpAddr)) {
                attendancePerUser.replace(currIpAddr, attendancePerUser.get(currIpAddr) + 1);
            } else {
                attendancePerUser.put(currIpAddr, 1);
            }
        }

        String regexp = "^(4\\d\\d|5\\d\\d)$";
        Pattern logPattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = logPattern.matcher(logEntry.getResponseCode());
        if (matcher.find()) {
            numberOfErrorRequests++;
        }

        regexp = "^(?:https?:\\/\\/)?([a-z0-9-]+(?:\\.[a-z0-9-]+)*\\.[a-z]{2,})(?:\\/|$)";
        logPattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        matcher = logPattern.matcher(logEntry.getReferer());
        if (matcher.find()) {
            referers.add(matcher.group(1));
        }
    }

    public double getTrafficRate() {
        return totalTraffic / Duration.between(minTime, maxTime).toHours();
    }

    public HashSet<String> getExistingPages() {
        return new HashSet<>(existingPages);
    }

    public HashSet<String> getNotExistingPages() {
        return new HashSet<>(notExistingPages);
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

    public HashMap<String, Double> getBrowserStatistics() {
        HashMap<String, Double> browserStatistics = new HashMap<>();
        int totalBrowserCount = 0;

        for (Map.Entry<String, Integer> currBrowser : browserFrequency.entrySet()) {
            totalBrowserCount += currBrowser.getValue();
        }

        for (Map.Entry<String, Integer> currBrowser : browserFrequency.entrySet()) {
            browserStatistics.put(currBrowser.getKey(), (double) currBrowser.getValue() / totalBrowserCount);
        }

        return browserStatistics;
    }

    public double getSiteVisitsRate() {
        return (double) numberOfUsers / Duration.between(minTime, maxTime).toHours();
    }

    public double getErrorRequestsRate() {
        return (double) numberOfErrorRequests / Duration.between(minTime, maxTime).toHours();
    }

    public double getPerUserAttendanceRate() {
        return (double) numberOfUsers / uniqueIpAddr.size();
    }

    public HashMap<ZonedDateTime, Integer> getMaxAttendanceAtTime() {
        /*ZonedDateTime maxAttendanceTime = null;
        Integer maxAttendance = 0;
        for (Map.Entry<ZonedDateTime, Integer> tmp : attendanceAtTime.entrySet()) {
            if (tmp.getValue() > maxAttendance) {
                maxAttendance = tmp.getValue();
                maxAttendanceTime = tmp.getKey();
            }
        }

        HashMap<ZonedDateTime, Integer> maxAttendanceAtTime = new HashMap<>();
        maxAttendanceAtTime.put(maxAttendanceTime, maxAttendance);

        return maxAttendanceAtTime;*/
        return attendanceAtTime.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> {
                    HashMap<ZonedDateTime, Integer> map = new HashMap<>();
                    map.put(entry.getKey(), entry.getValue());
                    return map;
                })
                .orElseGet(HashMap::new);
    }

    public HashSet<String> getReferers() {
        return new HashSet<>(referers);
    }

    public int getMaxAttendancePerUser() {
        Optional<Integer> maxAttendancePerUser = attendancePerUser.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getValue());

        return maxAttendancePerUser.get();
    }
}
