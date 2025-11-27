package InfoPoint;

import javax.swing.*;
import java.awt.*;

/**
 * Gestión robusta de Look & Feel y modo alto contraste simulado.
 */
public class LookAndFeelManager {

    private static boolean highContrast = false;

    public static void aplicarLookAndFeel(Window ventana, String laf) {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(ventana);
            // reajustar tamaño para mantener layout
            ventana.pack();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ventana, "No se pudo aplicar el Look & Feel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void toggleHighContrast(Window ventana) {
        highContrast = !highContrast;
        if (highContrast) {
            // Simulamos alto contraste con colores UIManager
            UIManager.put("Panel.background", Color.BLACK);
            UIManager.put("Label.foreground", Color.WHITE);
            UIManager.put("Button.background", Color.DARK_GRAY);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("TextField.background", Color.BLACK);
            UIManager.put("TextField.foreground", Color.WHITE);
            UIManager.put("TextArea.background", Color.BLACK);
            UIManager.put("TextArea.foreground", Color.WHITE);
        } else {
            // Restauramos valores por defecto pidiendo el L&F del sistema
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }
        // Actualizar todos los frames existentes
        for (Frame f : Frame.getFrames()) {
            SwingUtilities.updateComponentTreeUI(f);
        }
    }
}
