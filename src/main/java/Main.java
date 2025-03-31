import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class Main {
    public static void main(String[] args) throws IOException {
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

        List<String> lines = Files.readAllLines(Path.of("D:\\downloads\\access.log"));
        List<LogEntry> entries = new CopyOnWriteArrayList<>();
        if(lines.stream().anyMatch(a -> a.length() > 1024)) {
            System.out.println("офигенно здоровая строка");
            throw new RuntimeException();
        }
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(12);
        executor.setQueueCapacity(20);
        executor.setMaxPoolSize(Integer.MAX_VALUE);
        executor.initialize();
        lines.stream().map(line -> CompletableFuture.runAsync(() -> entries.add(new LogEntry(line)),executor)).forEach(a->a.join());
        System.out.printf("логов стало {%s}", entries.size());
    }
}
