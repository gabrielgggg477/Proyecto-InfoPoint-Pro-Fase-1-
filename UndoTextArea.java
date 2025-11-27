package InfoPoint;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

/**
 * JTextArea con UndoManager integrado.
 */
public class UndoTextArea extends JTextArea {
    private UndoManager undoManager = new UndoManager();

    public UndoTextArea() {
        super();
        getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });

        // Mapear Ctrl+Z / Ctrl+Y
        getInputMap().put(KeyStroke.getKeyStroke("control Z"), "undo");
        getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                undo();
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke("control Y"), "redo");
        getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                redo();
            }
        });
    }

    public void undo() {
        if (undoManager.canUndo()) undoManager.undo();
    }

    public void redo() {
        if (undoManager.canRedo()) undoManager.redo();
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }
}
