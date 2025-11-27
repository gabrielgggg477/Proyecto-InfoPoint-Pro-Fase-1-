package InfoPoint;

import java.io.Serializable;

public class Aviso implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mensaje;

    public Aviso(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String m) { this.mensaje = m; }

    @Override
    public String toString() {
        return mensaje;
    }
}
