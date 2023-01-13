package es.ulpgc.aemet.scrapper;

import java.sql.*;
import java.util.*;

public class SQLiteDatabase implements DataBase {
        private final Connection connection;

        public SQLiteDatabase(String dbPath, List<Event> events) throws SQLException {
                connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
                Statement statement = connection.createStatement();
                createTable(statement, "maxtemp");
                createTable(statement, "mintemp");
                insertMax(statement, events);
                insertMin(statement, events);
                statement.close();
                connection.close();
        }

        public static void createTable(Statement statement, String tableName) throws SQLException {
                statement.execute("CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                        "date TEXT NOT NULL,\n" +
                        "time TEXT NOT NULL,\n" +
                        "place TEXT NOT NULL,\n" +
                        "station TEXT NOT NULL,\n" +
                        "temp DOUBLE" +
                        ");");
        }

        public static void insertMax(Statement statement, List<Event> events) throws SQLException {
                List<Double> temp = new ArrayList<>();
                for (int i = 0; i < events.size(); i++) {
                        temp.add(events.get(i).getTa());
                }

                Double max = Collections.max(temp);
                int i = temp.indexOf(max);

                String dateHour = events.get(i).getFint();
                String[] a = dateHour.split("T");
                String date = a[0];
                String hour = a[1];

                ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM maxtemp WHERE date = '" + date + "';");
                rs.next();
                int count = rs.getInt(1);

                if (count == 0) {
                        statement.execute("INSERT INTO maxtemp (date, time, place, station, temp)\n" +
                                "VALUES(" + "'" + date + "'" + ", " + "'" + hour + "'" + ", " + "'" +
                                events.get(i).getUbi() + "'" + ", " + "'" + events.get(i).getIdema() + "'" + ", " + max + ");");
                }
                else {
                        ResultSet resultSet =statement.executeQuery("SELECT temp FROM maxtemp WHERE date = "+"'"+ date+"';");
                        resultSet.next();
                        Double OldT = resultSet.getDouble("temp");

                        if(events.get(i).getTa()>OldT){
                                statement.execute("DELETE FROM maxtemp WHERE date = "+"'"+date+"';");
                                statement.execute("INSERT INTO maxtemp (date, time, place, station, temp)\n" +
                                        "VALUES(" + "'" + date + "'" + ", " + "'" + hour + "'" + ", " + "'" +
                                        events.get(i).getUbi() + "'" + ", " + "'" + events.get(i).getIdema() + "'" + ", " + max + ");");
                        }

                }
        }

        public static void insertMin(Statement statement, List<Event> events) throws SQLException {
                List<Double> temp = new ArrayList<>();
                for (int i = 0; i < events.size(); i++) {
                        temp.add(events.get(i).getTa());
                }

                Double min = Collections.min(temp);
                int i = temp.indexOf(min);

                String dateHour = events.get(i).getFint();
                String[] a = dateHour.split("T");
                String date = a[0];
                String hour = a[1];

                ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM mintemp WHERE date = '" + date + "';");
                rs.next();
                int count = rs.getInt(1);

                if (count == 0) {
                        statement.execute("INSERT INTO mintemp (date, time, place, station, temp)\n" +
                                "VALUES(" + "'" + date + "'" + ", " + "'" + hour + "'" + ", " + "'" + events.get(i).getUbi()
                                + "'" + ", " + "'" + events.get(i).getIdema() + "'" + ", " + min + ");");
                }
                else {
                        ResultSet resultSet =statement.executeQuery("SELECT temp FROM mintemp WHERE date = "+"'"+ date+"';");
                        resultSet.next();
                        Double OldT = resultSet.getDouble("temp");

                        if(events.get(i).getTa()<OldT){
                                statement.execute("DELETE FROM mintemp WHERE date = "+"'"+date+"';");
                                statement.execute("INSERT INTO mintemp (date, time, place, station, temp)\n" +
                                        "VALUES(" + "'" + date + "'" + ", " + "'" + hour + "'" + ", " + "'" +
                                        events.get(i).getUbi() + "'" + ", " + "'" + events.get(i).getIdema() + "'" + ", " + min + ");");
                        }
                }
        }
}
