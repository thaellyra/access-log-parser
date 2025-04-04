import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    private String ipAddr;
    private ZonedDateTime time;
    private HttpMethod method;
    private String path;
    private String responseCode;
    private int responseSize;
    private String referer;
    private UserAgent agent;

    public LogEntry(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

        String regexp = "^([0-9.]+)[^\\[]+\\[([^]]+)][^\"]+\"([a-z]+)[^\\S]+([\\S]+)[^\"]+\"[^\\S]+([0-9]+)[^\\S]+([0-9]+)[^\\S]+\"([^\"]+)\"[^\\S]+\"([^\"]+)\"$";
        Pattern logPattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = logPattern.matcher(str);
        if (matcher.find()) {
            ipAddr = matcher.group(1);
            time = ZonedDateTime.parse(matcher.group(2), formatter);
            method = HttpMethod.valueOf(matcher.group(3));
            path = matcher.group(4);
            responseCode = matcher.group(5);
            responseSize = Integer.parseInt(matcher.group(6));
            referer = matcher.group(7);
            agent = new UserAgent(matcher.group(8));
        } else {
            System.out.println("Ошибка парсинга строки");
        }
    }


public String getIpAddr() {
    return ipAddr;
}

public ZonedDateTime getTime() {
    return time;
}

public HttpMethod getMethod() {
    return method;
}

public String getPath() {
    return path;
}

public String getResponseCode() {
    return responseCode;
}

public int getResponseSize() {
    return responseSize;
}

public String getReferer() {
    return referer;
}

public UserAgent getAgent() {
    return agent;
}
}
