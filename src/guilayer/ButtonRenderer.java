package guilayer;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * Button render to use as table cell renderer.
 * 
 * @author Alfonso and carbo
 */
class ButtonRenderer extends JButton implements TableCellRenderer {
    
    /**
     * Costructs the ButtonRender.
     */
    public ButtonRenderer() {
        setOpaque(true);
        
    }
    
    /**
     * Returns the component used for drawing the cell. This method is used to 
     * configure the renderer appropriately before drawing.
     * 
     * @param table         the JTable that is asking the render to draw; 
     *                      can be null
     * @param value         the value of the cell to be rendered
     * @param isSelected    true if the cell is to be rendered with the 
     *                      selection highlighted; otherwise false
     * @param hasFocus      if true, render cell appropriately
     * @param row           the row index of the cell being drawn.
     *                      When drawing the header, the value of row is -1     * 
     * @param column        the column index of the cell being drawn
     * @return              the component used for drawing the cell
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        setText((value == null) ? "" : value.toString());
        return this;
    }
}