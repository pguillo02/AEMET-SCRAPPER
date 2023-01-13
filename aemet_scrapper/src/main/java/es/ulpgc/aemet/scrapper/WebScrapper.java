package es.ulpgc.aemet.scrapper;

import com.google.gson.Gson;
import org.jsoup.Connection;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.jsoup.Jsoup.connect;

public class WebScrapper implements  Scrapper{
    private final Gson gson;
    private final String url;
    private final String apiKey;

    public WebScrapper(Gson gson, String url, String apiKey){
        this.gson = gson;
        this.url = url;
        this.apiKey = apiKey;
    }

    public List<Event> getResponse(){
        try {
            String response = connect(url)
                    .validateTLSCertificates(false)
                    .timeout(10000)
                    .ignoreContentType(true)
                    .header("accept", "application/json")
                    .header("api_key", apiKey)
                    .method(Connection.Method.GET)
                    .maxBodySize(0).execute().body();

            Aemet aemet = gson.fromJson(response, Aemet.class);

            String url_aemet = aemet.getDatos();

            String response2 = connect(url_aemet)
                    .validateTLSCertificates(false)
                    .timeout(20000)
                    .ignoreContentType(true)
                    .header("accept", "application/json")
                    .header("api_key", apiKey)
                    .method(Connection.Method.GET)
                    .maxBodySize(0).execute().body();

            List<Event> events = eventsStripper(response2);
            return events;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Event> eventsStripper(String response2) {
        Gson gson = new Gson();

        String[] events = response2.split(", ");
        List<Event> localEvents = new ArrayList<>();

        for (int i = 0; i < events.length; i++) {
            String a = events[i].replace("[","");
            String b = a.replace("]","");
            Event event = gson.fromJson(b, Event.class);

            String dateHour = event.getFint();
            String[] separation = dateHour.split("T");
            String date = separation[0];

            if(event.getLat()<28.4 && event.getLat()>27.5 && event.getLon()<-15 && event.getLon()>-16 &&
                    LocalDate.parse(date).equals(LocalDate.now())){
                localEvents.add(event);
            }
        }
        return localEvents;
    }
}
