package InfoPoint;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "No se pudo aplicar el Look & Feel.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }

        SwingUtilities.invokeLater(() -> new SplashScreen().setVisible(true));
    }
}
