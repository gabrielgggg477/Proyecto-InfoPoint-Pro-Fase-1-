package InfoPoint;

import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow(String rol) {
        setTitle("Biblioteca Municipal - Rol: " + rol);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lbl = new JLabel("Bienvenido, rol: " + rol, SwingConstants.CENTER);
        add(lbl);
    }
}
