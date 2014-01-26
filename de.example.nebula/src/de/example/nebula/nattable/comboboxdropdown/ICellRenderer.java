package de.example.nebula.nattable.comboboxdropdown;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;

/**
 * Used for the extension point de.ivu.fare.rcp.cellrendererProvider.
 * Renderer which are introduced as a Renderer with a particular id to the system using the above
 * extension point - should implement this interface.
 * 
 * @author alf
 * 
 */
public interface ICellRenderer extends ICellPainter {

    /**
     * Returns the id. @see #setId(String)
     * 
     * @return the id
     */
    String getId();

    /**
     * This is is set defined inside of the extension point.
     * From there it is put into the registered {@link IValidatedCellEditor} instance.
     * 
     * @param id
     *            - the id which was passed through the extension point
     */
    void setId(String id);

    /**
     * Registers the renderer to the cell.
     * 
     * @param configRegistry
     *            - the table registry
     * @param configLabel
     *            - the cell / column label
     */
    void register(IConfigRegistry configRegistry, String configLabel);

    /**
     * Unregister what was registered in {@link #register(IConfigRegistry, String)}
     * 
     * @param configRegistry
     *            - the table registry
     * @param configLabel
     *            - the cell / column label
     */
    void unregister(IConfigRegistry configRegistry, String configLabel);

}
