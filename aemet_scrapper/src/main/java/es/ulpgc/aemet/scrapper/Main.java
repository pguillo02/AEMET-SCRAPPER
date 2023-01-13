package es.ulpgc.aemet.scrapper;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        Gson gson = new Gson();
        String url = "https://opendata.aemet.es/opendata/api/observacion/convencional/todas";
        String apiKey = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYWJsb2d1aWxsb2pAZ21haWwuY29tIiwianRpIjoiODcwNmNjZDAtYTBhYi00MTVmLWI5ZWUtOGFkMmZhMWQxYzk5IiwiaXNzIjoiQUVNRVQiLCJpYXQiOjE2NzI2NjI5NzYsInVzZXJJZCI6Ijg3MDZjY2QwLWEwYWItNDE1Zi1iOWVlLThhZDJmYTFkMWM5OSIsInJvbGUiOiIifQ.w7tnhNGd0oxSSgaZk5_34xl1rYVZhiNhh7F4rlfi2RI";
        String dbPath ="C:\\Users\\pablo\\Desktop\\UNIVERSIDAD\\SEGUNDO\\DACD\\aemet_scrapper\\src\\main\\java\\es\\ulpgc\\aemet\\scrapper\\datamart.db";

        WebScrapper scrapper = new WebScrapper(gson, url, apiKey);
        List<Event> events = scrapper.getResponse();
        Datalake datalake = new Datalake(events);
        SQLiteDatabase db = new SQLiteDatabase(dbPath, events);
        Presenter presenter = new Presenter(dbPath);
    }
}