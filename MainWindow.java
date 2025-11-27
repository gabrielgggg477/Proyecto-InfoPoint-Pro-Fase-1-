package InfoPoint;

import InfoPoint.Formularios.FormAviso;

import javax.swing.*;
import java.awt.event.*;

public class MainWindow extends JFrame {

    public MainWindow(String rol) {

        setTitle("Biblioteca Municipal - Rol: " + rol);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar barra = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setMnemonic('A');

        JMenuItem itemNuevoAviso = new JMenuItem("Nuevo Aviso");
        itemNuevoAviso.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        itemNuevoAviso.setToolTipText("Crear un aviso nuevo");
        itemNuevoAviso.addActionListener(e -> new FormAviso(this).setVisible(true));

        JMenuItem itemGuardar = new JMenuItem("Guardar");
        itemGuardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));

        JMenuItem itemAbrir = new JMenuItem("Abrir");
        itemAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));

        JMenuItem itemCambiarLF = new JMenuItem("Cambiar Tema");
        itemCambiarLF.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cambiar el tema");
            }
        });

        menuArchivo.add(itemNuevoAviso);
        menuArchivo.add(itemGuardar);
        menuArchivo.add(itemAbrir);
        menuArchivo.add(itemCambiarLF);

        barra.add(menuArchivo);
        setJMenuBar(barra);

        add(new JLabel("Bienvenido, rol: " + rol, SwingConstants.CENTER));
    }
}
