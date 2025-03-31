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
            int yandexBotCount = 0;
            int googleBotCount = 0;

            while ((line = reader.readLine()) != null) {
                int length = line.length();
                if (length > 1024) {
                    throw new RuntimeException("В файле найдена строка длиннее 1024 символов");
                }

                String[] partsOne = logLineToComponents(line)[7].split(";");
                if (partsOne.length >= 2) {
                    String fragmentPartOne = partsOne[1].replace(" ", "");

                    String[] partsTwo = fragmentPartOne.split("/");
                    String fragmentPartTwo = partsTwo[0];

                    if (fragmentPartTwo.equalsIgnoreCase("YANDEXBOT")) {
                        yandexBotCount++;
                    }
                    else if (fragmentPartTwo.equalsIgnoreCase("GOOGLEBOT")) {
                        googleBotCount++;
                    }
                }
            }
            System.out.println("Количество запросов от YandexBot: " + yandexBotCount);
            System.out.println("Количество запросов от GoogleBot: " + googleBotCount);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] logLineToComponents(String s) {
        String[] components = new String[8];
        char[] line = s.toCharArray();
        int length = line.length;

        //Заполнение components[0] - IP-адрес клиента, который сделал запрос к серверу (в примере выше — 37.231.123.209)
        int currentPosition = 0;
        components[0] = "";
        for (int i = 0; i < length; i++) {
            if (line[i] == ' ') {
                currentPosition = i;
                break;
            }
            components[0] += line[i];
        }

        //Заполнение components[1] - Два пропущенных свойства, на месте которых обычно стоят дефисы, но могут встречаться также и пустые строки ("")
        components[1] = "";
        for (int i = currentPosition; i < length; i++) {
            if (line[i] == '[') {
                currentPosition = i;
                break;
            }
            components[1] += line[i];
        }

        //Заполнение components[2] - Дата и время запроса в квадратных скобках
        components[2] = "";
        for (int i = currentPosition; i < length; i++) {
            if (line[i] == '"') {
                currentPosition = i;
                break;
            }
            components[2] += line[i];
        }

        //Заполнение components[3] - Метод запроса (в примере выше — GET) и путь, по которому сделан запрос
        components[3] = "\"";
        for (int i = currentPosition + 1; i < length; i++) {
            if (line[i] == '"') {
                currentPosition = i;
                components[3] += "\"";
                break;
            }
            components[3] += line[i];
        }

        //Заполнение components[4] - Код HTTP-ответа (в примере выше — 200)
        components[4] = "";
        for (int i = currentPosition + 2; i < length; i++) {
            if (line[i] == ' ') {
                currentPosition = i;
                break;
            }
            components[4] += line[i];
        }

        //Заполнение components[5] - Размер отданных данных в байтах (в примере выше — 61096)
        components[5] = "";
        for (int i = currentPosition + 1; i < length; i++) {
            if (line[i] == ' ') {
                currentPosition = i;
                break;
            }
            components[5] += line[i];
        }

        //Заполнение components[6] - Путь к странице, с которой перешли на текущую страницу, — referer (в примере выше — “https://nova-news.ru/search/?rss=1&lg=1”)
        components[6] = "\"";
        for (int i = currentPosition + 2; i < length; i++) {
            if (line[i] == '\"') {
                currentPosition = i;
                components[6] += "\"";
                break;
            }
            components[6] += line[i];
        }

        //Заполнение components[7] - User-Agent — информация о браузере или другом клиенте, который выполнил запрос
        components[7] = s.substring(currentPosition + 2);

        return components;
    }
}
