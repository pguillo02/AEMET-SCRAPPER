package es.ulpgc.aemet.scrapper;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.List;

public class Datalake implements FileManager{
    public Datalake(List<Event> events){
        File dir = new File("datalake");
        if (!dir.exists()) {
            dir.mkdir();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate currentDate = LocalDate.now();
        String fileName = currentDate.format(formatter) + ".events";

        File file = new File(dir,fileName);
        try {
            FileWriter fileWriter = new FileWriter(file);
            Gson gson = new Gson();
            for (int i = 0; i < events.size(); i++) {
                String a = gson.toJson(events.get(i));
                fileWriter.write(a);
                fileWriter.flush();
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
