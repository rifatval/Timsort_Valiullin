import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class GeneratingService {
    private String path;
    private int countFiles;

    public GeneratingService(String path, int countFiles) {
        this.path = path;
        this.countFiles = countFiles;
        try {
            Files.createDirectories(Paths.get(path + "/data"));
            this.path = path + "/data";

            for (int i = 0; i < countFiles; i++) {
                File file = new File(this.path + "/file" + i + ".txt");
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateData() throws FileNotFoundException {
        Random random = new Random();
        for (int i = 0; i < countFiles; i++) {
            int arraySize = random.nextInt(100, 10_000);
            PrintWriter out = new PrintWriter(path + "/file" + i + ".txt");
            out.println(arraySize);
            for (int j = 0; j < arraySize; j++) {
                out.println(random.nextInt(-1000, 1000));
            }
            out.close();
        }
    }
}