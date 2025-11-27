package InfoPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {

    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JButton btnEntrar, btnCancelar;
    private int intentosFallidos = 0;
    private final int MAX_INTENTOS = 5;

    /**
     * Constructor: padre puede ser null
     */
    public LoginDialog(Frame padre) {
        super(padre, "Acceso Seguro - InfoPoint", true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(360, 200);
        setLocationRelativeTo(padre);
        setLayout(new BorderLayout());

        // PANEL CENTRAL
        JPanel panel = new JPanel(new GridLayout(3,2,6,6));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panel.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        panel.add(txtUsuario);

        panel.add(new JLabel("Clave:"));
        txtClave = new JPasswordField();
        panel.add(txtClave);

        // Botones
        btnEntrar = new JButton("Entrar");
        btnEntrar.setMnemonic(KeyEvent.VK_E); // Alt+E
        btnEntrar.setToolTipText("Entrar con las credenciales proporcionadas (Alt+E)");
        panel.add(btnEntrar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setMnemonic(KeyEvent.VK_C); // Alt+C
        btnCancelar.setToolTipText("Salir de la aplicación (Alt+C)");
        panel.add(btnCancelar);

        add(panel, BorderLayout.CENTER);

        // Enter activa Entrar
        getRootPane().setDefaultButton(btnEntrar);

        // Listeners
        btnEntrar.addActionListener(e -> intentarValidar());
        btnCancelar.addActionListener(e -> {
            System.exit(0); // especificación: si cancelas -> salir
        });

        // Cerrar ventana con X = preguntar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // prevenir cierre accidental: preguntar
                int r = JOptionPane.showConfirmDialog(LoginDialog.this,
                        "¿Deseas salir de la aplicación?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) System.exit(0);
            }
        });

        // Accesibilidad: TAB order ya natural, request focus en usuario
        txtUsuario.requestFocusInWindow();
    }

    private void intentarValidar() {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword());

        // VALIDACIÓN: no vacíos
        if (usuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y clave obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            if (usuario.isEmpty()) txtUsuario.requestFocus();
            else txtClave.requestFocus();
            return;
        }

        // Aquí validamos roles (simulado): admin/admin123 -> admin ; user/user123 -> usuario
        boolean esAdmin = usuario.equals("admin") && clave.equals("admin123");
        boolean esUsuario = usuario.equals("usuario") && clave.equals("user123");

        if (esAdmin || esUsuario) {
            // Reset intentos y abrir MainWindow según rol
            intentosFallidos = 0;
            String rol = esAdmin ? "admin" : "usuario";
            dispose();
            SwingUtilities.invokeLater(() -> {
                MainWindow mw = new MainWindow(rol);
                mw.setVisible(true);
            });
        } else {
            intentosFallidos++;
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas. Intento " + intentosFallidos + " de " + MAX_INTENTOS, "Error", JOptionPane.ERROR_MESSAGE);

            // limpiar clave, mantener foco en usuario por especificación anterior
            txtClave.setText("");
            txtUsuario.requestFocus();

            if (intentosFallidos >= MAX_INTENTOS) {
                // Bloqueo temporal simple: deshabilitar boton Entrar
                btnEntrar.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Demasiados intentos. Contacte con el administrador.", "Bloqueado", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
