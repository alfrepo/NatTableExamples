package de.example.nebula.nattable.externalScrollbars;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;

public interface ITableMainScrollable {
	
	/**
	 * Main Table to which controls the scroll bars
	 * @return
	 */
	NatTable getNatTable();
    
    /**
     * Used to register vertically dependent layers
     * @return
     */
    ILayer getBodyLayer();
    
    /**
     * Used to register vertically dependent headers
     * @return
     */
    ILayer getHeaderLayer();
    
    /**
     * Listener is triggered, when a command on the main table is executed
     * @param commandListener
     */
    void addCommandListener(ListenerCommand commandListener);
    
    /**
     * Listener is triggered, when a command on the main table is executed
     * @param commandListener
     */
    void removeCommandListener(ListenerCommand commandListener);
    
    /**
     * Listener is notified, when the scrolls to a new row
     * @param rowPositionListener
     */
	public void addScrollableRowPositionListener(ListenerRowPosition rowPositionListener);
	
	 /**
     * Listener is notified, when the scrolls to a new row
     * @param rowPositionListener
     */
	public void removeScrollableRowPositionListener(ListenerRowPosition rowPositionListener);
}
