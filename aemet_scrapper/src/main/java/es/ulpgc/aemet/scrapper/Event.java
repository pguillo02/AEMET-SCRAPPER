package es.ulpgc.aemet.scrapper;

import java.time.LocalDateTime;

public class Event {
    private final String fint;
    private final String idema;
    private final String ubi;
    private final double ta;
    private final double lon;
    private final double lat;


    public Event(String fint, String idema, String ubi, double ta, double lon, double lat) {
        this.fint = fint;
        this.idema = idema;
        this.ubi = ubi;
        this.ta = ta;
        this.lat = lat;
        this.lon = lon;
    }

    public String getFint(){
        return this.fint;
    }

    public String getIdema(){
        return this.idema;
    }

    public String getUbi(){
        return this.ubi;
    }

    public double getTa(){
        return this.ta;
    }
    public double getLat(){
        return this.lat;
    }
    public  double getLon(){
        return this.lon;
    }
}
