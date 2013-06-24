package de.example.nebula.nattable.externalScrollbars;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.command.ILayerCommand;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfiguration;
import org.eclipse.nebula.widgets.nattable.config.NullComparator;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.fixtures.Person;
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
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.event.RowUpdateEvent;
import org.eclipse.nebula.widgets.nattable.painter.layer.NatGridLayerPainter;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.command.MoveSelectionCommand;
import org.eclipse.nebula.widgets.nattable.selection.command.ScrollSelectionCommand;
import org.eclipse.nebula.widgets.nattable.sort.SortConfigAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.test.fixture.data.RowDataListFixture;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.nebula.widgets.nattable.viewport.command.RecalculateScrollBarsCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;

import de.example.nebula.nattable.externalScrollbars.custom.NattableViewportLayer;

/**
 * This fixture creates a simple, minimal 3x3 table with resizable columns whose
 * cells are of the form 'Row X, Col Y'.
 */
public class TableMain extends AbstractNatExample implements ITableMainScrollable {
	
	private static final String CUSTOM_COMPARATOR_LABEL = "customComparatorLabel";
	protected static final String NO_SORT_LABEL = "noSortLabel";

	private IDataProvider bodyDataProvider;
	private String[] propertyNames;
	private Map propertyToLabels;
	
	private NatTable natTable;
	private ILayer columnHeaderLayer;
	
	private ArrayList<ListenerCommand> commandListeners = new ArrayList<>(); //Listeners will be triggered on NatTable Command execution
	
	
	
	private Scrollable scrollable;
	
    // ACHTUNG:
    // Object below can only set once to the table. setting them twice - overrides old objects
    private ColumnOverrideLabelAccumulator cellLabelAccumulator; // can tag table cells
	
    private int tableHeight =0;
    private int tableWidth =0;
	private BodyStack bodyStack;
    
    
    public TableMain(Scrollable scrollable) {
		this.scrollable = scrollable;
	}
    
	public Control createExampleControl(Composite parent) {
		
		// Body
		//create a data provider, incl. the labels for the table-columns
		final IDataProvider bodyDataProviderPure = setupBodyDataProvider();
		final IDataProvider bodyDataProvider = new IDataProvider() {
			
			@Override
			public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
				bodyDataProviderPure.setDataValue(columnIndex, rowIndex, newValue);
			}
			
			@Override
			public int getRowCount() {
				return bodyDataProviderPure.getRowCount();
			}
			
			@Override
			public Object getDataValue(int columnIndex, int rowIndex) {
				if(columnIndex == 0){
					return "rows "+natTable.getRowCount(); //gives 39. Reads from viewPortLayer
				}
				if(columnIndex == 2){
					return tableWidth+" x "+tableHeight;
				}
				return bodyDataProviderPure.getDataValue(columnIndex, rowIndex);
			}
			
			@Override
			public int getColumnCount() {
				return bodyDataProviderPure.getColumnCount();
			}
		};
		
		
		//LAYERS
		// Layer contains existing domain objects
		this.bodyStack = new BodyStack(bodyDataProvider);
		
		
		
	    // Column header
		final IDataProvider columnHeaderDataProvider = new DummyColumnHeaderDataProvider(bodyDataProvider);
		columnHeaderLayer = new ColumnHeaderLayer(new DefaultColumnHeaderDataLayer(columnHeaderDataProvider), bodyStack.viewportLayer, bodyStack.selectionLayer);
		
		// Row header
		final IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider);
		final AbstractLayer rowHeaderLayer = new RowHeaderLayer(new DefaultRowHeaderDataLayer(rowHeaderDataProvider), bodyStack.viewportLayer, bodyStack.selectionLayer);
	    
		
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


		
		//	visualize table size
		natTable.addListener(SWT.Resize, new Listener(){

			@Override
			public void handleEvent(Event event) {
				tableHeight = natTable.getHeight();
				tableWidth = natTable.getWidth();
			}
		});
		
		
		
		//styling
		
	        // get configuration objectsF
	        cellLabelAccumulator = new ColumnOverrideLabelAccumulator(gridLayer);
	        gridLayer.setConfigLabelAccumulator(cellLabelAccumulator);

	        //labels are after accumulator
	        registerLabelsToColumns(bodyDataProvider);	        
		
        // stretch the table to fill the empty space
        NatGridLayerPainter layerPainter = new NatGridLayerPainter(natTable);
        natTable.setLayerPainter(layerPainter);
        
		return natTable;
	}

	
	

	/**
	 * creates the content
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private IDataProvider setupBodyDataProvider() {
//		List people = Arrays.asList(
//				new Person(100, "Mickey Mouse", new Date(1000000)), 
//				new Person(110, "Batman", new Date(2000000)), 
//				new Person(120, "Bender", new Date(3000000)), 
//				new Person(130, "Cartman", new Date(4000000)), 
//				new Person(140, "Dogbert", new Date(5000000)));
		
		//TODO create many entries
		final List<Person> people = new ArrayList<Person>();
		Person p;
		for(int i=0; i<=3000; i++){
			p = new Person(150, "Hulk"+i, new Date(6000000));
			people.add(p);
		}

		//separate teh id from the label
		propertyToLabels = new HashMap();
		propertyToLabels.put("id", "ID");
		propertyToLabels.put("name", "First Name");
		propertyToLabels.put("birthDate", "DOB");

		propertyNames = new String[] { "id", "name", "birthDate", "adress", "color" ,"city", "country" };
		
		
		return new IDataProvider() {
			
			@Override
			public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
				
			}
			
			@Override
			public int getRowCount() {
				return people.size();
			}
			
			@Override
			public Object getDataValue(int columnIndex, int rowIndex) {
				Person p = people.get(rowIndex);
				if(columnIndex == 0){
					return p.getId();
				}else if(columnIndex == 1){
					return p.getName();
				}else{
					return p.getBirthDate();
				}
			}
			
			@Override
			public int getColumnCount() {
				return 14;
			}
		};
	}
	
	
	private class DynamicColumnsIDataProviderAdapter implements IDataProvider{
		
		IDataProvider dataProvider;
		int fillColumnIndex = -1;
		int columnCount = -1;
		
		public DynamicColumnsIDataProviderAdapter(IDataProvider dataProvider){
			this.dataProvider = dataProvider;
		}
		
		private void appendFillColumn(){
			
		}
		
		private void setFillColumn(int columnIndex){
			
		}
		
		@Override
		public Object getDataValue(int columnIndex, int rowIndex) {
			return dataProvider.getDataValue(columnIndex, rowIndex);
		}

		@Override
		public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
			dataProvider.setDataValue(columnIndex, rowIndex, newValue);
		}

		@Override
		public int getColumnCount() {
			return dataProvider.getColumnCount();
		}

		@Override
		public int getRowCount() {
			return dataProvider.getColumnCount();
		}
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
    	return bodyStack.bodyDataLayer;
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
    	
    	private SelectionLayer selectionLayer;
    	private NattableViewportLayer viewportLayer;
    	private DataLayer bodyDataLayer;
    	
    	public BodyStack(final IDataProvider bodyDataProvider) {
    		//stack
    		bodyDataLayer = new DataLayer(bodyDataProvider);
    		ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(bodyDataLayer);
    		ColumnHideShowLayer columnHideShowLayer = new ColumnHideShowLayer(columnReorderLayer);
    		selectionLayer = new SelectionLayer(columnHideShowLayer);
    		viewportLayer = new NattableViewportLayer(selectionLayer, scrollable.getHorizontalBar(), true, scrollable.getVerticalBar(), true);
    		setUnderlyingLayer(viewportLayer);
		}
    	
    	public SelectionLayer getSelectionLayer() {
			return selectionLayer;
		}
    	public NattableViewportLayer getViewportLayer() {
			return viewportLayer;
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
