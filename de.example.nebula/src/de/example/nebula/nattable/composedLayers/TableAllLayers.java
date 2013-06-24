package de.example.nebula.nattable.composedLayers;



import java.util.ArrayList;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.freeze.config.DefaultFreezeGridBindings;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DummyColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.layer.NatGridLayerPainter;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scrollable;

import de.example.nebula.nattable.externalScrollbars.ListenerCommand;
import de.example.nebula.nattable.externalScrollbars.ITableMainScrollable;
import de.example.nebula.nattable.externalScrollbars.ListenerRowPosition;
import de.example.nebula.nattable.externalScrollbars.custom.NattableViewportLayer;

/**
 * This fixture creates a simple, minimal 3x3 table with resizable columns whose
 * cells are of the form 'Row X, Col Y'.
 */
public class TableAllLayers extends AbstractNatExample implements ITableMainScrollable {
	
	protected static final String NO_SORT_LABEL = "noSortLabel";

	private NatTable natTable;
	private ILayer columnHeaderLayer;
	private Scrollable scrollable;
	private BodyStack bodyStack;
	
	private ArrayList<ListenerCommand> commandListeners = new ArrayList<>(); //Listeners will be triggered on NatTable Command execution
    // ACHTUNG:
    // Object below can only set once to the table. setting them twice - overrides old objects
    private ColumnOverrideLabelAccumulator cellLabelAccumulator; // can tag table cells
	
    
    
    
    public TableAllLayers(Composite parent, Scrollable scrollable) {
		this.scrollable = scrollable;
		createExampleControl(parent);
	}
    
	public Control createExampleControl(Composite parent) {
		
		//LAYERS
		
		// Layer contains existing domain objects
		this.bodyStack = new BodyStack();
		
		
	    // Column header
		final IDataProvider columnHeaderDataProvider = new DummyColumnHeaderDataProvider(this.bodyStack.dataProvider);
		columnHeaderLayer = new ColumnHeaderLayer(new DefaultColumnHeaderDataLayer(columnHeaderDataProvider), bodyStack.viewportLayer, bodyStack.selectionLayer);
		
		
		// Row header
//		final IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(this.bodyStack.dataProvider);
		final IDataProvider rowHeaderDataProvider = new IDataProvider() {
			
			@Override
			public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
			}
			
			@Override
			public int getRowCount() {
				return 202;
			}
			
			@Override
			public Object getDataValue(int columnIndex, int rowIndex) {
				if(rowIndex==200){
					System.out.println("row 200");
				}
				return rowIndex;
			}
			
			@Override
			public int getColumnCount() {
				return 1;
			}
		};
		final AbstractLayer rowHeaderLayer = new RowHeaderLayer(new DefaultRowHeaderDataLayer(rowHeaderDataProvider), bodyStack.underlyingLayer, bodyStack.selectionLayer);
	    
		
		// Corner
		final DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider);
		final CornerLayer cornerLayer = new CornerLayer(new DataLayer(cornerDataProvider), rowHeaderLayer, columnHeaderLayer);
		
		
	    // Grid
	    final GridLayer gridLayer = new GridLayer(this.bodyStack.viewportLayer, columnHeaderLayer, rowHeaderLayer, cornerLayer);
	    
	    final int DEFAULT_STYLE_OPTIONS = SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED  | SWT.V_SCROLL | SWT.H_SCROLL;
	    natTable = new NatTable(parent,DEFAULT_STYLE_OPTIONS, gridLayer, false);

	    
		// Configuration
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		natTable.addConfiguration(new HeaderMenuConfiguration(natTable));
		natTable.addConfiguration(new DefaultFreezeGridBindings());
		natTable.configure();

		
		//styling
		
	        // get configuration objectsF
	        cellLabelAccumulator = new ColumnOverrideLabelAccumulator(gridLayer);
	        gridLayer.setConfigLabelAccumulator(cellLabelAccumulator);

	        //labels are after accumulator
	        registerLabelsToColumns(this.bodyStack.dataProvider);	        
		
        // stretch the table to fill the empty space
        NatGridLayerPainter layerPainter = new NatGridLayerPainter(natTable);
        natTable.setLayerPainter(layerPainter);
        
		return natTable;
	}

	
	
	
	
	
	
	public void addCommandListener(ListenerCommand commandListener){
		commandListeners.add(commandListener);
	}
	public void removeCommandListener(ListenerCommand commandListener){
		commandListeners.remove(commandListener);
	}

	
    /**
     * Used to listen for events and update dependent table 
     * @return
     */
    public NatTable getNatTable(){
    	return natTable;
    }
    
    /**
     * Used to register vertically dependent layers
     * @return
     */
    public ILayer getBodyLayer(){
    	return this.bodyStack.layerCompositeAll;
    }  
    
    /**
     * Used to register vertically dependent headers
     * @return
     */
    public ILayer getHeaderLayer(){
    	return columnHeaderLayer;
    }
	
	
    
    
    // CLASSES
    class BodyStack extends AbstractLayerTransform{
    	
    	private LayerCompositeAll layerCompositeAll;
    	private SelectionLayer selectionLayer;
    	private NattableViewportLayer viewportLayer;
    	private IDataProvider dataProvider;
    	
    	private ILayer underlyingLayer; 
    	
    	
    	public BodyStack() {
    		//stack
    		layerCompositeAll = new LayerCompositeAll();
    		dataProvider = layerCompositeAll.getIDataProvider();
    		selectionLayer = new SelectionLayer(layerCompositeAll);
    		viewportLayer = new NattableViewportLayer(selectionLayer, scrollable.getHorizontalBar(), true, scrollable.getVerticalBar(), true);
    		
    		underlyingLayer = viewportLayer;
    		setUnderlyingLayer(underlyingLayer);
		}
    	
    }
    
    
	
	//HELPER MUELLS

    /**
     * Add Table Features by position.
     * Register the the Labels of columns by positions, not the indexes.
     * Positions are stable - indexes may be changes by reordering layer.
     * 
     * The labels can be requested by position number using {@link #getColumnLabelByPosition(int)}
     * 
     * Example:
     * 
     * Reorder Layer B
     * 0 1 2 3 4 5 <- column positions
     * 2 1 0 3 4 5 <- column indexes
     * 
     * @param columnDataProvider
     * @see ILayer
     */
    private void registerLabelsToColumns(IDataProvider columnDataProvider) {

        for (int columnPosition = 0; columnPosition < columnDataProvider.getColumnCount(); columnPosition++) {

            String customColumnLabel = getColumnLabelByPosition(columnPosition);
            int columnIndex = natTable.getColumnIndexByPosition(columnPosition);

            // accumulator will add custom label to column. Labels are used as identifiers inside
            // the table
            cellLabelAccumulator.registerColumnOverrides(columnIndex, customColumnLabel);
            
            System.out.println("Column Index by position : "+columnIndex);
        }
    }

    /**
     * Gets the label by position, which stays the same after reordering columns.
     * 
     * Reorder Layer B
     * 0 1 2 3 4 5 <- column positions
     * 2 1 0 3 4 5 <- column indexes
     * 
     * @param columnPosition
     * @return
     */
    private String getColumnLabelByPosition(int columnPosition) {
        // build a table wide unique label for a column
        return "_COLUMN_" + columnPosition;
    }

	@Override
	public void addScrollableRowPositionListener(
			ListenerRowPosition rowPositionListener) {
		this.bodyStack.viewportLayer.addScrollableRowPositionListener(rowPositionListener);
		
	}

	@Override
	public void removeScrollableRowPositionListener(
			ListenerRowPosition rowPositionListener) {
		this.bodyStack.viewportLayer.removeScrollableRowPositionListener(rowPositionListener);
	}

}

