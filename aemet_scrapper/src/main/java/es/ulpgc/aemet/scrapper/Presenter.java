package es.ulpgc.aemet.scrapper;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.temporal.ChronoUnit;
public class Presenter {
    private final Connection connection;
    public Presenter(String dbPath) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        Statement statement = connection.createStatement();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduzca su petición: ");
        String request = scanner.next();
        List<String> dates = filterRequest(request);
        System.out.println(getRequest(statement, dates, request));

        statement.close();
        connection.close();
    }

    public static List<String> filterRequest(String request){
        String pattern = "\\d{4}-\\d{2}-\\d{2}";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(request);

        List<String> dates = new ArrayList<>();

        while(m.find()){
            dates.add(m.group());
        }
        return dates;
    }

    public static String getRequest(Statement statement, List<String> dates, String request) throws SQLException {
        LocalDate startDate = LocalDate.parse(dates.get(0));
        LocalDate endDate = LocalDate.parse(dates.get(1));

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        List<LocalDate> finalDates = new ArrayList<>();
        for (int i = 0; i <= daysBetween; i++) {
            LocalDate date = startDate.plusDays(i);
            finalDates.add(date);
        }

        List<Double> temperatures = new ArrayList<>();
        if(request.contains("max")){
            for (int i = 0; i < finalDates.size(); i++) {
                ResultSet rs = statement.executeQuery("SELECT temp FROM maxtemp WHERE date = '"+finalDates.get(i)+"';");
                rs.next();
                Double temp = rs.getDouble(1);
                temperatures.add(temp);
            }

            ResultSet rsi = statement.executeQuery("SELECT place FROM maxtemp WHERE temp="+ Collections.max(temperatures)+";");
            rsi.next();
            String ubi = rsi.getString(1);

            return ubi;
        }
        else{
            for (int i = 0; i < finalDates.size(); i++) {
                ResultSet rs = statement.executeQuery("SELECT temp FROM mintemp WHERE date = '"+finalDates.get(i)+"';");
                rs.next();
                Double temp = rs.getDouble(1);
                temperatures.add(temp);
            }

            ResultSet rsi = statement.executeQuery("SELECT place FROM mintemp WHERE temp="+ Collections.min(temperatures)+";");
            rsi.next();
            String ubi = rsi.getString(1);

            return ubi;
        }
    }
}
