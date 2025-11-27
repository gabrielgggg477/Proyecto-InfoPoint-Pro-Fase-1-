package InfoPoint.modelos;

public class Aviso {

    private String mensaje;

    public Aviso(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return mensaje;
    }
}

