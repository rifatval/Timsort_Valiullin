import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private final static String PATH = "src/main/java";
    private static final int COUNT_FILES = 80;

    public static void main(String[] args) throws FileNotFoundException {
        // Генерация данных
        GeneratingService dataGenerator = new GeneratingService(PATH, COUNT_FILES);
        try {
            dataGenerator.generateData();
        } catch(IOException e) {
            e.printStackTrace();
        }

        // Заголовок таблицы результатов
        System.out.println("Размер;Время(мс);Итерации;Время/итерация(мс)");

        // Тестирование на каждом файле
        for (int i = 0; i < COUNT_FILES; i++) {
            Scanner scanner = new Scanner(new File(PATH + "/data/file" + i + ".txt"));
            int[] currentArray = new int[scanner.nextInt()];
            TimSort.setIterationsCount(0);

            int j = 0;
            while (scanner.hasNext()) {
                currentArray[j++] = scanner.nextInt();
            }
            scanner.close();

            // Замер времени и итераций
            long startTime = System.nanoTime();
            TimSort.sort(currentArray);
            long endTime = System.nanoTime();

            double timeSort = (endTime - startTime) / 1_000_000.0;
            long countIteration = TimSort.getIterationsCount();

            // Вывод результатов в формате CSV
            System.out.println(currentArray.length + "; " +
                    String.format("%.3f", timeSort) + "; " +
                    countIteration + "; " +
                    String.format("%.6f", timeSort / countIteration));
        }
    }
}