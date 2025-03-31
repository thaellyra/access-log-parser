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
            int count = 0;


            while ((line = reader.readLine()) != null) {
                int length = line.length();
                if (length > 1024) {
                    throw new RuntimeException("В файле найдена строка длиннее 1024 символов");
                }
                logEntries.add(new LogEntry(line));
                count++;
                System.out.println(count);
            }

            System.out.println(logEntries.get(234).getAgent().getBrowser());
            System.out.println(logEntries.get(234).getAgent().getOsType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
