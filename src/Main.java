import java.io.*;
import java.util.Random;

public class Main {

    private static final String PATH = "output.txt";

    public static void main(String[] args) throws InterruptedException {
        // Создаем поток, который будет записывать четные числа
        Thread evenThread = new Thread(new EvenWriter());
        // Создаем поток, который будет записывать нечетные числа
        Thread oddThread = new Thread(new OddWriter());
        // Создаем поток, который будет читать файл
        Thread readerThread = new Thread(new FileReaderTask());

        // Запуск всех потоков
        evenThread.start();
        oddThread.start();
        readerThread.start();

        // Ожидаем завершения всех потоков
        evenThread.join();
        oddThread.join();
        readerThread.join();
    }

    // Поток для записи четных чисел
    static class EvenWriter implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                int evenNumber = random.nextInt(50) * 2; // Генерация случайного четного числа
                writeToFile("Even: " + evenNumber);
                try {
                    Thread.sleep(500); // Задержка для имитации работы
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // Поток для записи нечетных чисел
    static class OddWriter implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                int oddNumber = random.nextInt(50) * 2 + 1; // Генерация случайного нечетного числа
                writeToFile("Odd: " + oddNumber);
                try {
                    Thread.sleep(500); // Задержка для имитации работы
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // Поток для чтения файла и вывода последних цифр
    static class FileReaderTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                readFile();
                try {
                    Thread.sleep(1000); // Задержка для чтения файла
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private void readFile() {
            synchronized (Main.class) {
                try (BufferedReader reader = new BufferedReader(new FileReader(PATH))) {
                    String lastLine = null;
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lastLine = line;
                    }
                    if (lastLine != null) {
                        System.out.println("Последняя запись в файле: " + lastLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Метод для записи данных в файл с синхронизацией
    private static synchronized void writeToFile(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH, true))) {
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}