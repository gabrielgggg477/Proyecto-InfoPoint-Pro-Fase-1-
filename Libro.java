package InfoPoint;

import java.io.Serializable;

public class Libro implements Serializable {
    private static final long serialVersionUID = 1L;

    private String titulo;
    private String autor;
    private int anio;

    public Libro(String titulo, String autor, int anio) {
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnio() { return anio; }

    public void setTitulo(String t) { this.titulo = t; }
    public void setAutor(String a) { this.autor = a; }
    public void setAnio(int y) { this.anio = y; }

    @Override
    public String toString() {
        return titulo + " - " + autor + " (" + anio + ")";
    }
}
