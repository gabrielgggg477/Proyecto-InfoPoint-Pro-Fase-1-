package InfoPoint;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        // Intentamos aplicar L&F del sistema por defecto (robusto)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se pudo aplicar Look & Feel del sistema.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }

        // Cargamos datos guardados (si hay)
        DataStore.cargar();

        SwingUtilities.invokeLater(() -> {
            new SplashScreen().setVisible(true);
        });
    }
}
