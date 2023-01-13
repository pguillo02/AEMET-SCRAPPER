package es.ulpgc.aemet.scrapper;

public class Aemet {
    String descripcion;
    int estado;
    String datos;
    String metadatos;

    public Aemet(String descripcion, int estado, String datos, String metadatos){
        this.descripcion=descripcion;
        this.estado=estado;
        this.datos=datos;
        this.metadatos=metadatos;
    }

    public String getDatos() {
        return datos;
    }
}
