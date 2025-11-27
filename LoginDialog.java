package InfoPoint;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {

    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JButton btnEntrar, btnCancelar;
    private int intentosFallidos = 0;

    public LoginDialog() {

        setTitle("Acceso al Sistema");
        setModal(true);
        setSize(320, 180);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        txtUsuario = new JTextField();
        txtClave = new JPasswordField();
        btnEntrar = new JButton("Entrar");
        btnCancelar = new JButton("Cancelar");

        btnEntrar.setMnemonic('E');
        btnCancelar.setMnemonic('C');

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsuario);

        panel.add(new JLabel("Clave:"));
        panel.add(txtClave);

        panel.add(btnEntrar);
        panel.add(btnCancelar);

        add(panel);

        getRootPane().setDefaultButton(btnEntrar);

        btnEntrar.addActionListener(e -> validarLogin());
        btnCancelar.addActionListener(e -> System.exit(0));
    }

    private void validarLogin() {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword());

        boolean esAdmin = usuario.equals("admin") && clave.equals("1234");
        boolean esUsuario = usuario.equals("usuario") && clave.equals("1234");

        if (esAdmin || esUsuario) {

            String rol = esAdmin ? "admin" : "usuario";

            dispose();
            new MainWindow(rol).setVisible(true);

        } else {
            intentosFallidos++;
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");

            txtClave.setText("");
            txtUsuario.requestFocus();

            if (intentosFallidos >= 3) {
                JOptionPane.showMessageDialog(this, "Demasiados intentos fallidos.");
            }
        }
    }
}
