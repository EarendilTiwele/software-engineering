package guilayer;

import javax.swing.table.AbstractTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Custom implementation of a AbstractTableModel.
 *
 * @author Alfonso and carbo
 */
public class CustomTableModel extends AbstractTableModel {

    private final String[] columnNames;
    private final Object[][] data;

    /**
     * Costructs a table model specifying the names of the columns and data.
     * Names are specified as an array of <code>String</code> and data as a
     * matrix of <code>Object</code>.
     * 
     * @param columnNames the names of columns
     * @param data        the data of the model
     */
    public CustomTableModel(String[] columnNames, Object[][] data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    /**
     * Returns the number of columns in the model.
     * 
     * @return the number of columns in the model
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Returns the number of rows in the model.
     * 
     * @return the number of rows in the model
     */
    @Override
    public int getRowCount() {
        return data.length;
    }

    /**
     * Returns the name of the column at <code>columnIndex</code>. 
     * This is used to initialize the table's column header name. 
     * Note: this name does not need to be unique; two columns in a 
     * table can have the same name.
     * 
     * 
     * @param columnIndex   the index of the column
     * @return              the name of the column
     */
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     * 
     * @param rowIndex      the row whose value is to be queried
     * @param columnIndex   the column whose value is to be queried
     * @return              the value Object at specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    /**
     * Returns the <code>Object.Class</code> regardless of the <code>columnIndex</code>.
     * @param columnIndex   the column being queried
     * @return              the Object.Class
     */
    @Override
    public Class getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    /**
     * Returns <code>true</code> if the cell at <code>rowIndex</code> and
     * <code>columnIndex</code> is editable. Otherwise, <code>setValueAt</code>
     * on the cell will not change the value of that cell.
     * 
     * @param rowIndex   the row whose value to be queried
     * @param columnIndex   the column whose value to be queried
     * @return      true if the cell is editable
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        return false;
    }

    /**
     * Sets the value in the cell at <code>rowIndex</code> and
     * <code>columnIndex</code> to value.
     * 
     * @param value         the new value
     * @param rowIndex      the row whose value is to be changed
     * @param columnIndex   the column whose value is to be changed
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = value;
        fireTableCellUpdated(rowIndex, columnIndex);

    }

}
