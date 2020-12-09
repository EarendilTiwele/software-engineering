/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guilayer;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Colored implemetation of JTable.
 *
 * @author Alfonso
 */
public class JColoredTable extends JTable {

    private ColorChanger colorChanger;

    /**
     * Common interface for color changer alghoritm.
     */
    public interface ColorChanger {

        /**
         * Returns the colored Component under the event location. The right
         * color is selected by querying the data model of the table for the
         * value at cell <code>row,col</code>.
         *
         * @param table the table
         * @param component the Component under the event location
         * @param row the row of the cell to paint, where 0 is the first row
         * @param col the column of the cell to paint, where 0 is the first
         * column
         * @return the colored Component under the event location
         */
        public Component changeColor(JTable table, Component component, int row, int col);
    }

    /**
     * Constructs a JColoredTable that is initialized with a default data model,
     * a default column model, and a default selection model and a
     * <code>ColorChanger</code> to paint the cells of the table.
     *
     * @param colorChanger the colorChanger
     */
    public JColoredTable(ColorChanger colorChanger) {
        this.colorChanger = colorChanger;
    }

    /**
     * Paints the cells using to the given <code>ColorChanger</code>.
     *
     * @param renderer the TableCellRenderer to prepare
     * @param row the row of the cell to render, where 0 is the first row
     * @param col the column of the cell to render, where 0 is the first
     * @return the colored Component under the event location
     */
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
        Component comp = super.prepareRenderer(renderer, row, col);
        return colorChanger.changeColor(this, comp, row, col);

    }

    /**
     * Sets the color changer for this table.
     *
     * @param colorChanger the color changer
     */
    public void setColorChanger(ColorChanger colorChanger) {
        this.colorChanger = colorChanger;
    }

}
