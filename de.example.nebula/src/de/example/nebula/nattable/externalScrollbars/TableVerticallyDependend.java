package de.example.nebula.nattable.externalScrollbars;


import java.util.ArrayList;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.command.ILayerCommand;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.command.EditCellCommand;
import org.eclipse.nebula.widgets.nattable.grid.layer.DimensionallyDependentLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayerListener;
import org.eclipse.nebula.widgets.nattable.layer.IUniqueIndexLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.event.ILayerEvent;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.command.SelectCellCommand;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

import de.example.nebula.nattable.externalScrollbars.custom.NattableViewportLayer;

/**
 * This fixture creates a simple, minimal 3x3 table with resizable columns whose
 * cells are of the form 'Row X, Col Y'.
 */
public abstract class TableVerticallyDependend {
	
	private NatTable natTable;
    private IConfigRegistry configRegistry = new ConfigRegistry();
    private ColumnOverrideLabelAccumulator columnOverrideLabelAccumulator;
	private UiBindingRegistry uiBindingRegistry;
	
	private Scrollable externalScrollable;
	private VerticallyDependentDataLayer verticallyDependedLayer;
	
	private ViewportLayer viewportLayer;
    
    public static final String CUSTOM_CELL_LABEL = "Cell_LABEL";
    
    public static final String CHECK_BOX_CONFIG_LABEL = "checkBox";
	public static final String CHECK_BOX_EDITOR_CONFIG_LABEL = "checkBoxEditor";
	
	//PUBLIC
    
    public TableVerticallyDependend( Scrollable externalScrollable, ITableMainScrollable mainTable) {
    	this.externalScrollable = externalScrollable;
    	this.verticallyDependedLayer = new VerticallyDependentDataLayer(mainTable);
	}
    
    /**
	 * Use this method to create the table.
	 * @param parent - parent composite. Will be layed out with a {@link FillLayout}
	 * @return - the Table as composite
	 */
	public Composite createTableComposite(Composite parent) {
		
		//stack with layers
		AbstractLayerTransform layerStack = getLayerStack();
		
	    natTable = new NatTable(parent, layerStack, false);
	    
	    //set config variables
	    this.configRegistry = natTable.getConfigRegistry();
	    this.uiBindingRegistry = natTable.getUiBindingRegistry();
	    this.columnOverrideLabelAccumulator = new ColumnOverrideLabelAccumulator(verticallyDependedLayer);
	    this.verticallyDependedLayer.setConfigLabelAccumulator(this.columnOverrideLabelAccumulator);
	    
	    //use config variables
	    customizeTable(natTable);
	    
	    //allow the table modification for subclasses
	    postTableConstruct();
	    
		return natTable;
	}
    
    
	/**
	 * Sets the width of column.
	 * @param columnPosition - the position of column as used in NatTable framework
	 * @param width - the width in px
	 */
	public void setColumnWidth(int columnPosition, int width){
		this.getDataLayer().setColumnWidthByPosition(columnPosition, width);
	}
    
	/**
	 * Retrieves the preferred table width, which is the sum of all column widths.
	 */
	public int getWidth(){
		int result = 0;
		for( int i=0; i< getDataProvider().getColumnCount(); i++){
			result += getDataLayer().getColumnWidthByPosition(i);
		}
		return result;
	}
	
	public DataLayer getDataLayer() {
		return verticallyDependedLayer.dataLayer;
	}
	
	
	//PROTECTED
	
	/**
	 * Used to synchronize the Table with the main table. 
	 */
	NatTable getNatTable(){
		return natTable;
	}
	
	
	
	
	//PRIVATE
	
	private AbstractLayerTransform getLayerStack(){
		return new AbstractLayerTransform() {
			{
				//SelectionLayer is not needed? Only if we want to reallyselect / mark rows.
//				SelectionLayer selectionLayer = new SelectionLayer(verticallyDependedLayer){
//			    	@Override
//			    	public boolean doCommand(ILayerCommand command) {
//			    		if(command instanceof EditCellCommand ){
//			    			//map the SelectCellCommand to the first column, same row
//			    			EditCellCommand editCellCommand  = (EditCellCommand) command;
//			    			int rowPos = editCellCommand.getCell().getRowPosition();
//			    			
//			    			SelectCellCommand selectCellCommand = new SelectCellCommand(natTable, 0, rowPos, false, false);
//			    			return super.doCommand(selectCellCommand );
//			    		}
//			    		return super.doCommand(command);
//			    	}
//			    };
			    viewportLayer = new NattableViewportLayer(verticallyDependedLayer, null, false, externalScrollable.getVerticalBar(), false);
			    
			    
			    ILayer underlyingLayer = viewportLayer;
			    
			    //give the children a chance to add own layers
			    underlyingLayer = modifyLayerStack(underlyingLayer);
			    
			    //choose the top most layer, which should be used by the natTable
			    setUnderlyingLayer(underlyingLayer);
			}
		};
	}
	
	/**
	 * Register everything, what makes the table looking customized
	 * @param natTable
	 */
	private void customizeTable(NatTable natTable){
		//register the 
	    natTable.setConfigRegistry(configRegistry);
		
		// DefaultNatTableStyleConfiguration configuration
		IConfiguration configuration = new DefaultNatTableStyleConfiguration();
	    natTable.addConfiguration(configuration);
	    
	    //add custom Labels, so that I can talk to layers using getColumnLabelByPosition()
	    registerLabelsToColumns(getDataProvider());

	    
	    //allow to register new labels to the table
	    registerDomainSpecificLabels(columnOverrideLabelAccumulator);
	    
	    //allow doing content specific things to the IconfigRegistry
	    modifyConfigRegistry(configRegistry);
	    
	    //allow doing content specific things to the UIBindings
	    modifyUiBindingRegistry(uiBindingRegistry);
	    
	    //scrollbars
	    natTable.getVerticalBar().setEnabled(true);
	    natTable.getHorizontalBar().setEnabled(true);
	    
	    natTable.getVerticalBar().setVisible(false);
	    natTable.getHorizontalBar().setVisible(false);
	    
	    natTable.configure();
	}
	
    private void registerLabelsToColumns(IDataProvider tableDataProvider) {
        for (int columnPosition = 0; columnPosition < tableDataProvider.getColumnCount(); columnPosition++) {

            String customColumnLabel = getColumnLabelByPosition(columnPosition);
            int columnIndex = natTable.getColumnIndexByPosition(columnPosition);

            // accumulator will add custom label to column. Labels are used as identifiers inside the table
            columnOverrideLabelAccumulator.registerColumnOverrides(columnIndex, customColumnLabel);
        }
    }
    
    protected String getColumnLabelByPosition(int columnNumber) {
        // build a table wide unique label for a column
        return CUSTOM_CELL_LABEL + "_COLUMN_" + columnNumber;
    }
	
    
    
    
    //CLASSES
    
    /**
     * Class represents the LAYER, which vertically depends from another layer. It has as many rows, as the layer from which it depends. It resizes the rows together with the table, from which it depends.
     * @author alf
     *
     */
    public class VerticallyDependentDataLayer extends AbstractLayerTransform implements IUniqueIndexLayer{
    	DataLayer dataLayer;
    	IDataProvider dataProvider;
    	
    	public VerticallyDependentDataLayer(ITableMainScrollable verticallyDependFromTable){
    		dataLayer = getDataLayer();
    		DimensionallyDependentLayer verticallyDependentLayer = new DimensionallyDependentLayer(dataLayer, dataLayer, verticallyDependFromTable.getBodyLayer());
        	setUnderlyingLayer( verticallyDependentLayer);
        	
        	// SYNC Tables
        	
        	// the events are passed Bottom Up through the table - listen on the lowest layer 
        	verticallyDependFromTable.getBodyLayer().addLayerListener(new ILayerListener() {
				@Override
				public void handleLayerEvent(ILayerEvent event) {
					natTable.handleLayerEvent(event);
				}
			});
        	
        	//commands are passed top-down
        	verticallyDependFromTable.addCommandListener(new ListenerCommand() {
				@Override
				public void doCommand(ILayerCommand command) {
					natTable.doCommand(command);
				}
			});
        	
        	//listen for row changes
        	verticallyDependFromTable.addScrollableRowPositionListener(new ListenerRowPosition() {
				@Override
				public void handleSetRowPosition(int scrollableRowPosition) {
					viewportLayer.setOriginRowPosition(scrollableRowPosition);
				}
			});

        	//redraw is done using SWT API
        	verticallyDependFromTable.getNatTable().addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					natTable.redraw();
				}
			});
    	}
    	
        /**
         * Layer is used to generate the data of the vertically dependent table
         * @return
         */
        private DataLayer getDataLayer(){
        	dataProvider = getDataProvider();
    		DataLayer dataLayer = new DataLayer(dataProvider); 
    		return dataLayer;
        }
        
		@Override
		public int getColumnPositionByIndex(int columnIndex) {
			return dataLayer.getColumnPositionByIndex(columnIndex);
		}

		@Override
		public int getRowPositionByIndex(int rowIndex) {
			return dataLayer.getRowPositionByIndex(rowIndex);
		}
    }
    
    
    public abstract class VerticallyDependentDataProvider implements IDataProvider{

		/**
		 * 	Is never used, because this layer is create as a vertically depended layer.
		 * 	The rowCount is set by the layer, from which the current layer vertically depends.
		 */
		public int getRowCount() {
			return 1; // random value of 1!
		}
    }
    
    
    //ABSTRACT
    
    /**
     * Use the accumulator to register new labels
     * @param columnOverrideLabelAccumulator - the object, that may be used to register labels
     */
    protected abstract void registerDomainSpecificLabels(ColumnOverrideLabelAccumulator columnOverrideLabelAccumulator );
    
    /**
     * Do some stuff with the registry. Add Editors, Converters, Painters, Listeners.... Use Labels you registered in {@link #registerContentSpecificColumnLabels()}
     * @param configRegistry
     * @param checkBoxCellPainter
     * @param checkBoxCellEditor
     */
    protected abstract void modifyConfigRegistry(IConfigRegistry configRegistry);
    
    /**
     * Do some stuff with the uiBindingRegistry. Add CellClick listeners, Event matchers  ... use Labels you registered in {@link #registerContentSpecificColumnLabels()}
     * @param uiBindingRegistry
     */
    protected abstract void modifyUiBindingRegistry(UiBindingRegistry uiBindingRegistry);

    /**
     * Get the data provider
     * @return
     */
    public abstract VerticallyDependentDataProvider getDataProvider();
    
    /**
     * Can add some additional layers to the layer stack here
     * @param oldTopLayer - the existing layer stack.
     * 
     * ACHTUNG: adopt the {@link #getDataLayer()} method, if a new data layer is introduced 
     * 
     * @return the new top layer, or the old top layer if no changes should be done to the stack.
     */
    public abstract ILayer modifyLayerStack(ILayer oldTopLayer);
    
    /**
     * Is triggered, after the table Composite was created. Modify the things like table column width etc. here.
     * @return
     */
    public abstract void postTableConstruct();
}

