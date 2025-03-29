import java.io.*;
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
            int lineCount = 0;
            int lineMin = Integer.MAX_VALUE;
            int lineMax = -1;
            while ((line = reader.readLine()) != null) {
                int length = line.length();
                lineCount++;
                if (length > lineMax) lineMax = length;
                if (length < lineMin) lineMin = length;
                if (length > 1024) throw new RuntimeException("В файле найдена строка длиннее 1024 символов");
            }
            System.out.println("Количество строк в файле: " + lineCount);
            System.out.println("Длина самой короткой строки в файле: " + lineMin + " символов");
            System.out.println("Длина самой длинной строки в файле: " + lineMax + " символов");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
