package InfoPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Formulario simple de alta de libros (Admin).
 * Validación: año debe ser numérico.
 */
public class FormLibro extends JDialog {

    private JTextField txtTitulo, txtAutor, txtAnio;

    public FormLibro(Frame padre) {
        super(padre, "Gestión de Libros", true);
        setSize(420, 260);
        setLocationRelativeTo(padre);
        setLayout(new BorderLayout(6,6));

        JPanel grid = new JPanel(new GridLayout(4,2,6,6));
        grid.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        grid.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        txtTitulo.setToolTipText("Título del libro");
        grid.add(txtTitulo);

        grid.add(new JLabel("Autor:"));
        txtAutor = new JTextField();
        txtAutor.setToolTipText("Autor del libro");
        grid.add(txtAutor);

        grid.add(new JLabel("Año:"));
        txtAnio = new JTextField();
        txtAnio.setToolTipText("Año de publicación (solo números)");
        grid.add(txtAnio);

        add(grid, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setMnemonic(KeyEvent.VK_A); // Alt+A
        btnAgregar.setToolTipText("Agregar libro nuevo (Alt+A)");
        btnAgregar.addActionListener(e -> agregarLibro());
        botones.add(btnAgregar);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        botones.add(btnCerrar);

        add(botones, BorderLayout.SOUTH);
    }

    private void agregarLibro() {
        String t = txtTitulo.getText().trim();
        String a = txtAutor.getText().trim();
        String y = txtAnio.getText().trim();
        if (t.isEmpty() || a.isEmpty() || y.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rellena todos los campos.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Validación numérica estricta
        try {
            int anio = Integer.parseInt(y);
            if (anio < 0 || anio > 3000) throw new NumberFormatException();
            Libro lib = new Libro(t, a, anio);
            DataStore.listaLibros.add(lib);
            DataStore.guardar();
            JOptionPane.showMessageDialog(this, "Libro añadido.");
            // limpiar campos y focus en título
            txtTitulo.setText(""); txtAutor.setText(""); txtAnio.setText("");
            txtTitulo.requestFocus();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Año inválido. Introduce solo números (ej: 2020).", "Validación", JOptionPane.ERROR_MESSAGE);
            txtAnio.requestFocus();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al añadir libro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
