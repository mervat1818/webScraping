package utils;

import org.json.JSONArray;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

    public static void saveAsJson(JSONArray data, String baseFilename) {
        try {
            Files.createDirectories(Paths.get("test-output"));

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "test-output/" + baseFilename.replace(".json", "") + "_" + timestamp + ".json";

            try (FileWriter file = new FileWriter(filename)) {
                file.write(data.toString(2));
                file.flush();
                System.out.println("Results saved to: " + filename);
            }
        } catch (IOException e) {
            System.err.println("Error saving JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }


}