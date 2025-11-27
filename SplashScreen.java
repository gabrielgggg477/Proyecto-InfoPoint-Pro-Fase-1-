package InfoPoint;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JDialog {

    private JProgressBar barra;
    private JLabel mensaje;

    public SplashScreen() {
        setUndecorated(true);
        setSize(420, 180);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        mensaje = new JLabel("Iniciando...", SwingConstants.CENTER);
        mensaje.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        barra = new JProgressBar(0, 100);
        barra.setStringPainted(true);

        add(mensaje, BorderLayout.CENTER);
        add(barra, BorderLayout.SOUTH);

        // Hilo que simula estados y carga
        new Thread(() -> {
            try {
                String[] pasos = {"Conectando...", "Cargando estilos...", "Inicializando componentes...", "Cargando datos..."};
                for (int i = 0; i <= 100; i++) {
                    barra.setValue(i);
                    // Cambiar mensaje cada 25%
                    if (i % 25 == 0 && i/25 < pasos.length) mensaje.setText(pasos[i/25]);
                    Thread.sleep(25);
                }
            } catch (InterruptedException ignored) {}
            // tras la "carga"
            dispose();
            // Abrir Login
            LoginDialog login = new LoginDialog(null);
            login.setVisible(true);
        }).start();
    }
}
