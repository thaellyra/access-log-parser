import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String path;
        while (true) {
            System.out.println("Введите путь к файлу и нажмите <Enter>:");
            path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (isDirectory) {
                System.out.println("Это путь к директории, повторите попытку!");
                continue;
            }

            if (fileExists) {
                System.out.println("Путь указан верно");
                break;
            } else {
                System.out.println("Указан путь к несуществующему файлу, повторите попытку!");
            }
        }

        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            List<LogEntry> logEntries = new ArrayList<>();
            Statistics statistics = new Statistics();

            while ((line = reader.readLine()) != null) {
                int length = line.length();
                if (length > 1024) {
                    throw new RuntimeException("В файле найдена строка длиннее 1024 символов");
                }
                logEntries.add(new LogEntry(line));
                statistics.addEntry(logEntries.getLast());

            }

            for (LogEntry l : logEntries) {
                System.out.println("ipAddr: " + l.getIpAddr());
                System.out.println("time: " + l.getTime());
                System.out.println("method: " + l.getMethod());
                System.out.println("path: " + l.getPath());
                System.out.println("responseCode: " + l.getResponseCode());
                System.out.println("responseSize: " + l.getResponseSize());
                System.out.println("referer: " + l.getReferer());
                System.out.println("browser: " + l.getAgent().getBrowser());
                System.out.println("os: " + l.getAgent().getOsType());
                System.out.println("===========================");
            }
            System.out.println("Всего получено строк: " + logEntries.size());
            System.out.println("Список существующих страниц: " + statistics.getExistingPages());
            System.out.println("Средний объем трафика за час: " + statistics.getTrafficRate());
            System.out.println("Статистика используемых ОС: " + statistics.getOsStatistics());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
