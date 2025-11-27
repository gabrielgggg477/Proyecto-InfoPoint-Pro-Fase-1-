package InfoPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Formulario para crear/editar avisos.
 * Si modoAdmin = true, se muestran validaciones y boton guardar.
 */
public class FormAviso extends JDialog {

    private UndoTextArea area;
    private JButton btnGuardar;
    private boolean adminMode;

    public FormAviso(Frame padre, boolean modoAdmin) {
        super(padre, "Crear Aviso", true);
        this.adminMode = modoAdmin;
        setSize(420, 320);
        setLocationRelativeTo(padre);
        setLayout(new BorderLayout(6,6));

        area = new UndoTextArea();
        area.setToolTipText("Escribe el mensaje del aviso. Soporta Undo/Redo (Ctrl+Z/Ctrl+Y).");

        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnGuardar.setMnemonic(KeyEvent.VK_G); // Alt+G
        btnGuardar.setToolTipText("Guardar aviso (Alt+G)");
        btnGuardar.addActionListener(e -> guardar());
        south.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setToolTipText("Cancelar sin guardar");
        btnCancelar.addActionListener(e -> dispose());
        south.add(btnCancelar);

        if (!modoAdmin) btnGuardar.setEnabled(false); // usuarios no guardan

        add(south, BorderLayout.SOUTH);
    }

    private void guardar() {
        String t = area.getText().trim();
        if (t.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El aviso no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            area.requestFocus();
            return;
        }
        // Guardar y cerrar
        DataStore.listaAvisos.add(new Aviso(t));
        DataStore.guardar(); // persistencia inmediata (opcional)
        dispose();
    }
}
