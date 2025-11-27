package InfoPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainWindow extends JFrame {

    private String rol;
    private JList<Aviso> listaAvisos;
    private JList<Libro> listaLibros;
    private DefaultListModel<Aviso> modeloAvisos;
    private DefaultListModel<Libro> modeloLibros;

    // Componentes para terminal usuario
    private JTextField txtBusqueda;
    private UndoTextArea areaComentarios;

    public MainWindow(String rol) {
        super("InfoPoint - Rol: " + rol);
        this.rol = rol;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8,8));

        crearMenuYToolbar();
        crearPanelCentral();
        actualizarListasDesdeDataStore();

        // Accesibilidad: que todo sea navegable sin ratón
        setFocusable(true);
    }

    private void crearMenuYToolbar() {
        JMenuBar menuBar = new JMenuBar();

        // --- Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setMnemonic(KeyEvent.VK_A); // Alt+A

        JMenuItem miGuardar = new JMenuItem("Guardar datos");
        miGuardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)); // Ctrl+S
        miGuardar.setToolTipText("Guardar los datos actuales en disco (Ctrl+S)");
        miGuardar.addActionListener(e -> {
            DataStore.guardar();
            JOptionPane.showMessageDialog(this, "Datos guardados.");
        });

        JMenuItem miCargar = new JMenuItem("Cargar datos");
        miCargar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)); // Ctrl+O
        miCargar.setToolTipText("Cargar datos desde disco (Ctrl+O)");
        miCargar.addActionListener(e -> {
            DataStore.cargar();
            actualizarListasDesdeDataStore();
            JOptionPane.showMessageDialog(this, "Datos cargados.");
        });

        JMenuItem miCerrarSesion = new JMenuItem("Cerrar sesión");
        miCerrarSesion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK)); // Ctrl+Q
        miCerrarSesion.setToolTipText("Cerrar sesión actual (Ctrl+Q)");
        miCerrarSesion.addActionListener(e -> {
            dispose();
            new LoginDialog(null).setVisible(true);
        });

        menuArchivo.add(miGuardar);
        menuArchivo.add(miCargar);
        menuArchivo.addSeparator();
        menuArchivo.add(miCerrarSesion);

        // --- Administracion (solo visible para admin)
        JMenu menuAdmin = new JMenu("Administración");
        menuAdmin.setMnemonic(KeyEvent.VK_D); // Alt+D

        JMenuItem miNuevoAviso = new JMenuItem("Nuevo Aviso");
        miNuevoAviso.setMnemonic(KeyEvent.VK_N); // Alt+N
        miNuevoAviso.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)); // Ctrl+N
        miNuevoAviso.setToolTipText("Crear un nuevo aviso (Admin)");
        miNuevoAviso.addActionListener(e -> {
            new FormAviso(this, true).setVisible(true);
            actualizarListasDesdeDataStore();
        });

        JMenuItem miGestionLibros = new JMenuItem("Gestionar Libros");
        miGestionLibros.setMnemonic(KeyEvent.VK_L); // Alt+L
        miGestionLibros.setToolTipText("Abrir formulario de gestión de libros (Admin)");
        miGestionLibros.addActionListener(e -> {
            new FormLibro(this).setVisible(true);
            actualizarListasDesdeDataStore();
        });

        menuAdmin.add(miNuevoAviso);
        menuAdmin.add(miGestionLibros);

        // --- Ver / Tema
        JMenu menuVer = new JMenu("Ver");
        menuVer.setMnemonic(KeyEvent.VK_V); // Alt+V

        JMenuItem miAltoContraste = new JMenuItem("Alternar Alto Contraste");
        miAltoContraste.setToolTipText("Alternar modo de alto contraste (simulado)");
        miAltoContraste.addActionListener(e -> LookAndFeelManager.toggleHighContrast(this));

        JMenu menuTemas = new JMenu("Temas");
        JMenuItem tNimbus = new JMenuItem("Nimbus");
        tNimbus.addActionListener(e -> LookAndFeelManager.aplicarLookAndFeel(this, "javax.swing.plaf.nimbus.NimbusLookAndFeel"));
        JMenuItem tMetal = new JMenuItem("Metal");
        tMetal.addActionListener(e -> LookAndFeelManager.aplicarLookAndFeel(this, "javax.swing.plaf.metal.MetalLookAndFeel"));
        JMenuItem tSystem = new JMenuItem("Sistema");
        tSystem.addActionListener(e -> {
            try {
                LookAndFeelManager.aplicarLookAndFeel(this, UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                LookAndFeelManager.aplicarLookAndFeel(this, "javax.swing.plaf.metal.MetalLookAndFeel");
            }
        });

        menuTemas.add(tNimbus);
        menuTemas.add(tMetal);
        menuTemas.add(tSystem);

        menuVer.add(menuTemas);
        menuVer.add(miAltoContraste);

        // Añadir menús al menuBar
        menuBar.add(menuArchivo);
        if ("admin".equalsIgnoreCase(rol)) menuBar.add(menuAdmin);
        menuBar.add(menuVer);

        setJMenuBar(menuBar);
    }

    private void crearPanelCentral() {
        JPanel centro = new JPanel(new BorderLayout(8,8));
        centro.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        // Panel superior con búsqueda y controles accesibles
        JPanel top = new JPanel(new BorderLayout(6,6));
        JPanel busquedaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        busquedaPanel.add(new JLabel("Buscar:"));
        txtBusqueda = new JTextField(30);
        txtBusqueda.setToolTipText("Buscar entre avisos o libros. Usa TAB para acceder. (Ctrl+Z/ Ctrl+Y en comentarios)");
        busquedaPanel.add(txtBusqueda);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setMnemonic(KeyEvent.VK_B); // Alt+B
        btnBuscar.setToolTipText("Iniciar búsqueda (Alt+B)");
        btnBuscar.addActionListener(e -> realizarBusqueda());
        busquedaPanel.add(btnBuscar);

        top.add(busquedaPanel, BorderLayout.CENTER);

        centro.add(top, BorderLayout.NORTH);

        // Panel principal dividido: avisos | libros | terminal usuario (según rol)
        JPanel contenidos = new JPanel(new GridLayout(1,3,10,10));

        // Avisos
        modeloAvisos = new DefaultListModel<>();
        listaAvisos = new JList<>(modeloAvisos);
        listaAvisos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane spAvisos = new JScrollPane(listaAvisos);
        JPanel pAvisos = new JPanel(new BorderLayout());
        pAvisos.setBorder(BorderFactory.createTitledBorder("Avisos"));
        pAvisos.add(spAvisos, BorderLayout.CENTER);

        // Botones admin para avisos (eliminar/editar)
        if ("admin".equalsIgnoreCase(rol)) {
            JPanel paButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton btnEliminarAviso = new JButton("Eliminar");
            btnEliminarAviso.setMnemonic(KeyEvent.VK_E); // Alt+E
            btnEliminarAviso.setToolTipText("Eliminar el aviso seleccionado");
            btnEliminarAviso.addActionListener(e -> {
                Aviso sel = listaAvisos.getSelectedValue();
                if (sel == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona un aviso primero.");
                    return;
                }
                DataStore.listaAvisos.remove(sel);
                actualizarListasDesdeDataStore();
            });
            paButtons.add(btnEliminarAviso);
            pAvisos.add(paButtons, BorderLayout.SOUTH);
        }

        // Libros
        modeloLibros = new DefaultListModel<>();
        listaLibros = new JList<>(modeloLibros);
        JScrollPane spLibros = new JScrollPane(listaLibros);
        JPanel pLibros = new JPanel(new BorderLayout());
        pLibros.setBorder(BorderFactory.createTitledBorder("Libros"));
        pLibros.add(spLibros, BorderLayout.CENTER);

        if ("admin".equalsIgnoreCase(rol)) {
            JPanel plButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton btnEliminarLibro = new JButton("Eliminar libro");
            btnEliminarLibro.setToolTipText("Eliminar libro seleccionado (Admin)");
            btnEliminarLibro.addActionListener(e -> {
                Libro sel = listaLibros.getSelectedValue();
                if (sel == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona un libro primero.");
                    return;
                }
                DataStore.listaLibros.remove(sel);
                actualizarListasDesdeDataStore();
            });
            plButtons.add(btnEliminarLibro);
            pLibros.add(plButtons, BorderLayout.SOUTH);
        }

        // Terminal público / panel comentarios con Undo
        JPanel pTerminal = new JPanel(new BorderLayout());
        pTerminal.setBorder(BorderFactory.createTitledBorder("Terminal Público"));
        areaComentarios = new UndoTextArea();
        areaComentarios.setToolTipText("Área de comentarios del usuario. Soporta Undo/Redo (Ctrl+Z/Ctrl+Y).");
        pTerminal.add(new JScrollPane(areaComentarios), BorderLayout.CENTER);

        // Añadir menús rápidos del terminal (undo/redo)
        JPanel pTermBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnUndo = new JButton("Undo");
        btnUndo.setToolTipText("Deshacer (llama a Ctrl+Z)");
        btnUndo.addActionListener(e -> areaComentarios.undo());
        JButton btnRedo = new JButton("Redo");
        btnRedo.setToolTipText("Rehacer (llama a Ctrl+Y)");
        btnRedo.addActionListener(e -> areaComentarios.redo());
        pTermBtns.add(btnUndo);
        pTermBtns.add(btnRedo);
        pTerminal.add(pTermBtns, BorderLayout.SOUTH);

        contenidos.add(pAvisos);
        contenidos.add(pLibros);
        contenidos.add(pTerminal);

        centro.add(contenidos, BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);
    }

    private void realizarBusqueda() {
        String q = txtBusqueda.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            actualizarListasDesdeDataStore();
            return;
        }
        // Filtrado simple
        DefaultListModel<Aviso> ma = new DefaultListModel<>();
        for (Aviso a : DataStore.listaAvisos) {
            if (a.toString().toLowerCase().contains(q)) ma.addElement(a);
        }
        listaAvisos.setModel(ma);

        DefaultListModel<Libro> ml = new DefaultListModel<>();
        for (Libro l : DataStore.listaLibros) {
            if (l.toString().toLowerCase().contains(q)) ml.addElement(l);
        }
        listaLibros.setModel(ml);
    }

    private void actualizarListasDesdeDataStore() {
        // Avisos
        modeloAvisos.clear();
        for (Aviso a : DataStore.listaAvisos) modeloAvisos.addElement(a);

        // Libros
        modeloLibros.clear();
        for (Libro l : DataStore.listaLibros) modeloLibros.addElement(l);
    }
}
