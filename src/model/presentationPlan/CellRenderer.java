
package model.presentationPlan;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Douglas
 */
public class CellRenderer extends DefaultTableCellRenderer{
    private static final long   serialVersionUID    = 1L;  
  
    public CellRenderer()   
    {  
        super();  
    }  
  
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,  
            boolean isSelected, boolean hasFocus, int row, int column) {  
        this.setHorizontalAlignment(CENTER);  
  
        return super.getTableCellRendererComponent(table, value, isSelected,  
                hasFocus, row, column);  
    }  
}
