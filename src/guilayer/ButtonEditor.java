package guilayer;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.table.TableCellEditor;
import javax.swing.JTable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Button editor to use as table cell editor.
 *
 * @author Alfonso and carbo
 */
public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

    private Object editorValue;
    private final JButton editButton = new JButton();

    /**
     * Constructs the button editor specifying the actionListener to be invoked
     * when the button is pressed.
     *
     * @param actionListener the actionListener to be invocked
     */
    public ButtonEditor(ActionListener actionListener) {
        editButton.addActionListener(actionListener);
    }

    /**
     * Sets an initial value for the editor. This will cause the editor to 
     * stopEditing and lose any partially edited value if the editor is editing 
     * when this method is called.
     * Returns the component that should be added to the client's Component
     * hierarchy. Once installed in the client's hierarchy this component will
     * then be able to draw and receive user input
     * 
     * @param table         the JTable that is asking the editor to edit; 
     *                      can be null 
     * @param value         the value of the cell to be edited
     * @param isSelected    true if the cell is to be rendered with highlighting
     * @param row           the row of the cell being edited
     * @param column        the column of the cell being edited
     * @return              the component for editing
     */
    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {

        if (value == null) {
            editButton.setText("");
            editButton.setIcon(null);
        } else if (value instanceof Icon) {
            editButton.setText("");
            editButton.setIcon((Icon) value);
        } else {
            editButton.setText(value.toString());
            editButton.setIcon(null);
        }

        this.editorValue = value;
        return editButton;
    }

    /**
     * Returns the value contained in the editor.
     * 
     * @return the value contained in the editor
     */
    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }
}
