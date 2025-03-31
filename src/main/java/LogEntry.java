import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class LogEntry {
    private static AtomicInteger counter = new AtomicInteger(0);;
    private final String ipAddr;
    private final ZonedDateTime time;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final int responseSize;
    private final String referer;
    private final UserAgent agent;

    public LogEntry(String str) {
        System.out.println(counter.incrementAndGet());
        char[] line = str.toCharArray();
        int length = line.length;

        //Заполнение ipAddr - IP-адрес клиента, который сделал запрос к серверу
        int currentPosition = 0;
        for (int i = 0; i < length; i++) {
            if (line[i] == ' ') {
                currentPosition = i;
                break;
            }
        }
        ipAddr = str.substring(0, currentPosition - 1);

        //Заполнение time - Дата и время запроса
        int tmpPosition = 0;
        for (int i = currentPosition; i < length; i++) {
            if (line[i] == '[') {
                tmpPosition = i;
            }
            if (line[i] == ']') {
                currentPosition = i;
                break;
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        time = ZonedDateTime.parse(str.substring(tmpPosition + 1, currentPosition), formatter);

        //Заполнение method - Метод запроса
        for (int i = currentPosition + 2; i < length; i++) {
            if (line[i] == '"') {
                tmpPosition = i;
            }
            if (line[i] == ' ') {
                currentPosition = i;
                break;
            }
        }
        method = HttpMethod.valueOf(str.substring(tmpPosition + 1, currentPosition));

        //Заполнение path - Путь, по которому сделан запрос
        tmpPosition = currentPosition;
        for (int i = currentPosition + 1; i < length; i++) {
            if (line[i] == '"') {
                currentPosition = i;
                break;
            }
        }
        path = str.substring(tmpPosition + 1, currentPosition - 1);

        //Заполнение responseCode - Код HTTP-ответа
        currentPosition += 2;
        tmpPosition = currentPosition;
        for (int i = currentPosition; i < length; i++) {
            if (line[i] == ' ') {
                currentPosition = i;
                break;
            }
        }
        responseCode = Integer.parseInt(str.substring(tmpPosition, currentPosition - 1));

        //Заполнение responseSize - Размер отданных данных в байтах
        currentPosition += 1;
        tmpPosition = currentPosition;
        for (int i = currentPosition + 1; i < length; i++) {
            if (line[i] == ' ') {
                currentPosition = i;
                break;
            }
        }
        responseSize = Integer.parseInt(str.substring(tmpPosition, currentPosition - 1));

        //Заполнение referer - Путь к странице, с которой перешли на текущую страницу
        currentPosition += 2;
        tmpPosition = currentPosition;
        for (int i = currentPosition; i < length; i++) {
            if (line[i] == '\"') {
                currentPosition = i;
                break;
            }
        }
        referer = str.substring(tmpPosition, currentPosition - 1);

        //Заполнение agent - User-Agent — информация о браузере или другом клиенте, который выполнил запрос
        agent = new UserAgent(str.substring(currentPosition + 3, length - 1));
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

public int getResponseCode() {
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
