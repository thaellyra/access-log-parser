import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int filesCount = 0;

        while (true) {
            System.out.println("Введите путь к файлу и нажмите <Enter>:");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (isDirectory) {
                System.out.println("Это путь к директории, повторите попытку!");
                continue;
            }

            if (fileExists) {
                filesCount++;
                System.out.println("Путь указан верно" + "\nЭто файл номер " + filesCount + "\nПродолжим!");
            } else {
                System.out.println("Указан путь к несуществующему файлу, повторите попытку!");
            }
        }
    }
}
