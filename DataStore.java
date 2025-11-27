package InfoPoint;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DataStore básico con persistencia a fichero (serialización).
 * Aviso y Libro implementan Serializable.
 */
public class DataStore {

    public static List<Libro> listaLibros = new ArrayList<>();
    public static List<Aviso> listaAvisos = new ArrayList<>();

    private static final String FICHERO = "datastore.bin";

    public static void guardar() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FICHERO))) {
            out.writeObject(new ArrayList<>(listaLibros));
            out.writeObject(new ArrayList<>(listaAvisos));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    public static void cargar() {
        File f = new File(FICHERO);
        if (!f.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            listaLibros = (List<Libro>) in.readObject();
            listaAvisos = (List<Aviso>) in.readObject();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
