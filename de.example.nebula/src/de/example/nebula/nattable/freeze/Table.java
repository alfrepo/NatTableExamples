package de.example.nebula.nattable.freeze;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfiguration;
import org.eclipse.nebula.widgets.nattable.config.NullComparator;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.fixtures.Person;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowDataLayer;
import org.eclipse.nebula.widgets.nattable.freeze.CompositeFreezeLayer;
import org.eclipse.nebula.widgets.nattable.freeze.FreezeLayer;
import org.eclipse.nebula.widgets.nattable.freeze.command.FreezeSelectionCommand;
import org.eclipse.nebula.widgets.nattable.freeze.config.DefaultFreezeGridBindings;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DummyColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DimensionallyDependentLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayer;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.IUniqueIndexLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.layer.NatGridLayerPainter;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.sort.SortConfigAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.test.fixture.data.RowDataListFixture;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

/**
 * This fixture creates a simple, minimal 3x3 table with resizable columns whose
 * cells are of the form 'Row X, Col Y'.
 */
public class Table extends AbstractNatExample {
	
	private static final String CUSTOM_COMPARATOR_LABEL = "customComparatorLabel";
	protected static final String NO_SORT_LABEL = "noSortLabel";

	private IDataProvider bodyDataProvider;
	private String[] propertyNames;
	CompositeFreezeLayer compositeFreezeLayer;
	private Map propertyToLabels;
	
	private NatTable natTable;
	
    // ACHTUNG:
    // Object below can only set once to the table. setting them twice - overrides old objects
    private ColumnOverrideLabelAccumulator cellLabelAccumulator; // can tag table cells
	
    private int tableHeight =0;
    private int tableWidth =0;
    
    
    public Table() {
	}
    
	@Override
	public String getDescription() {
		return
				"This example demonstrates the column and row freezing functionality of NatTable.\n" +
				"\n" +
				"* FREEZE COLUMNS AND ROWS by selecting a cell and using ctrl-shift-f. The columns to the left and the rows to the right " +
				"of the selected cell will be frozen such that they will always remain on screen even when the viewport is scrolled.\n" +
				"* UNFREEZE COLUMNS AND ROWS with ctrl-shift-u.";
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
		
		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);

		//stack
		ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(bodyDataLayer);
		
		ColumnHideShowLayer columnHideShowLayer = new ColumnHideShowLayer(columnReorderLayer);
		final SelectionLayer selectionLayer = new SelectionLayer(columnHideShowLayer);
		final ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
		
		
		FreezeLayer freezeLayer = new FreezeLayer(selectionLayer);
		
		
	    final CompositeFreezeLayer compositeFreezeLayer = new CompositeFreezeLayer(freezeLayer, viewportLayer, selectionLayer);
	    
	    // Column header
		final IDataProvider columnHeaderDataProvider = new DummyColumnHeaderDataProvider(bodyDataProvider);
		final ILayer columnHeaderLayer = new ColumnHeaderLayer(new DefaultColumnHeaderDataLayer(columnHeaderDataProvider), compositeFreezeLayer, selectionLayer);
		
		// Row header
		final IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider){
			@Override
			public int getColumnCount() {
				return 0;
			}
		};
		final ILayer rowHeaderLayer = new RowHeaderLayer(new DefaultRowHeaderDataLayer(rowHeaderDataProvider), compositeFreezeLayer, selectionLayer);
	    
		// Corner
		final DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider);
		final CornerLayer cornerLayer = new CornerLayer(new DataLayer(cornerDataProvider), rowHeaderLayer, columnHeaderLayer);
		
		
	    // Grid
	    final GridLayer gridLayer = new GridLayer(compositeFreezeLayer, columnHeaderLayer, rowHeaderLayer, cornerLayer);
	    
	    final int DEFAULT_STYLE_OPTIONS = SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED  | SWT.V_SCROLL | SWT.H_SCROLL;
	    natTable = new NatTable(parent,DEFAULT_STYLE_OPTIONS, gridLayer, false){
	    	@Override
	    	public void redraw() {
	    		natTable.getVerticalBar().setVisible(false);
	    		super.redraw();
	    	}
	    	@Override
	    	public void redraw(int x, int y, int width, int height, boolean all) {
	    		natTable.getVerticalBar().setVisible(false);
	    		super.redraw(x, y, width, height, all);
	    	}
	    };

		// Configuration
		
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		natTable.addConfiguration(new HeaderMenuConfiguration(natTable));
		natTable.addConfiguration(new DefaultFreezeGridBindings());
		natTable.configure();

		
		//TODO try scrolling
		//handle Scrolls
//				SelectionListener verticalSelectionListener = new SelectionListener() {
//					
//					@Override
//					public void widgetSelected(SelectionEvent e) {
//						
//						ScrollBar scrollBar =  (ScrollBar) e.widget;
//						int position = scrollBar.getSelection();
//						
////						int row = viewportLayer.getRowPositionByY(position);
//						//EDIT: use selectionLayer to getRowPositionByPosition
//						int row = viewportLayer.getScrollableLayer().getRowPositionByY(position);
//						
//						System.out.println("Position: "+position+" row: "+row);
//						
//						//???? gives 0 at 9 or negative value at 1600+
//						//BUG: sometimes getRowPositionByY gives a negative value
//						if(row>=0){
//							viewportLayer.setOriginRowPosition(row);
//						}
//					}
//					
//					@Override
//					public void widgetDefaultSelected(SelectionEvent e) {
//						//never triggered
//					}
//				};
//		scrollable.getVerticalBar().addSelectionListener(verticalSelectionListener);
		
//		Display.getDefault().addFilter(SWT.KeyDown, new Listener(){
//			int cnt = 0;
//
//			@Override
//			public void handleEvent(Event event) {
//				int y = natTable.getVerticalBar().getSelection();
//				System.out.println("VerticalBar selection "+y);
//				System.out.println("NatTable height: "+ natTable.getHeight());
//
//				natTable.setSize(800, 800);
//				
//				//getting the table row by pixel
//				int row = viewportLayer.getScrollableLayer().getRowPositionByY(cnt+=1000);
//				System.out.println("resulted row: "+row+" by using count "+cnt);
//				
//				viewportLayer.setOriginRowPosition(row);
//				
//				//show row 20
////				viewportLayer.setOriginRowPosition(20);
//				//EDIT: try using setViewportOrigin()
//				
//				
//				//scroll down by page 
////				viewportLayer.scrollVerticallyByAPage(new ScrollSelectionCommand(MoveDirectionEnum.DOWN, false, false));
//				
//				
////				natTable.getVerticalBar().setSelection(y+300);
//			}
//			
//		});
		
		//	visualize table size
		natTable.addListener(SWT.Resize, new Listener(){

			@Override
			public void handleEvent(Event event) {
				tableHeight = natTable.getHeight();
				tableWidth = natTable.getWidth();
			}
		});
		
		
 


		
		//styling
		
	        // get configuration objects
	        cellLabelAccumulator = new ColumnOverrideLabelAccumulator(gridLayer);
	        gridLayer.setConfigLabelAccumulator(cellLabelAccumulator);

	        //labels are after accumulator
	        registerLabelsToColumns(bodyDataProvider);	        
		
        // stretch the table to fill the empty space
        NatGridLayerPainter layerPainter = new NatGridLayerPainter(natTable);
        natTable.setLayerPainter(layerPainter);
        
        

        
        
		//TRIGGER FREEZE
		selectionLayer.setSelectedCell(1, 0);
		natTable.doCommand(new FreezeSelectionCommand());
		
//		freezeLayer.doCommand(new FreezeColumnCommand(freezeLayer, 1, true)); // ?
		
//		freezeLayer.doCommand(new FreezePositionCommand(freezeLayer,1,3 ));
		
		
		
		Display.getDefault().addFilter(SWT.KeyDown, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				System.out.println("Position 1 mapped to "+natTable.getColumnIndexByPosition(2)); 
				
			}
		});
		
		return natTable;
	}
	
	void createOverView(){

	}
	
	
	

	/**
	 * NOTE: The labels for the the custom comparators must go on the columnHeaderDataLayer - since,
	 * 		the SortHeaderLayer will resolve cell labels with respect to its underlying layer i.e columnHeaderDataLayer
	 */
	private IConfiguration getCustomComparatorConfiguration(final AbstractLayer columnHeaderDataLayer) {

		return new AbstractRegistryConfiguration() {

			public void configureRegistry(IConfigRegistry configRegistry) {
				// Add label accumulator
				ColumnOverrideLabelAccumulator labelAccumulator = new ColumnOverrideLabelAccumulator(columnHeaderDataLayer);
				columnHeaderDataLayer.setConfigLabelAccumulator(labelAccumulator);

				// Register labels
				labelAccumulator.registerColumnOverrides(
	               RowDataListFixture.getColumnIndexOfProperty(RowDataListFixture.RATING_PROP_NAME),
	               CUSTOM_COMPARATOR_LABEL);

				labelAccumulator.registerColumnOverrides(
                     RowDataListFixture.getColumnIndexOfProperty(RowDataListFixture.ASK_PRICE_PROP_NAME),
                     NO_SORT_LABEL);

				// Register custom comparator
				configRegistry.registerConfigAttribute(SortConfigAttributes.SORT_COMPARATOR,
									                       getCustomComparator(),
									                       DisplayMode.NORMAL,
									                       CUSTOM_COMPARATOR_LABEL);

				// Register null comparator to disable sort
				configRegistry.registerConfigAttribute(SortConfigAttributes.SORT_COMPARATOR,
				                                       new NullComparator(),
				                                       DisplayMode.NORMAL,
				                                       NO_SORT_LABEL);
			}
		};
	}
	
	private Comparator getCustomComparator() {
		return new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				return ((String) o1).compareToIgnoreCase((String) o2);
			}
		};
	};

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
		for(int i=0; i<3000; i++){
			p = new Person(150, "Hulk"+i, new Date(6000000));
			people.add(p);
		}

		//separate teh id from the label
		propertyToLabels = new HashMap();
		propertyToLabels.put("id", "ID");
		propertyToLabels.put("name", "First Name");
		propertyToLabels.put("birthDate", "DOB");

		propertyNames = new String[] { "id", "name", "birthDate" };
		
		
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
				return 3;
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

	
	//BELOW are methods to create GridLayer composites
	
	public static void main(String[] args) throws Exception {
		StandaloneNatExampleRunner.run(new Table());
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
    
    
    NatTable getNatTable(){
    	return natTable;
    }
    
    ILayer createDependentLayer(ILayer verticallyDependFrom){
    	IUniqueIndexLayer baseLayer = getBaseLayer();
    	
    	CompositeLayer compositeLayer = new CompositeLayer(2, 1);

		DimensionallyDependentLayer testLayer = new DimensionallyDependentLayer(baseLayer, baseLayer, verticallyDependFrom);

    	compositeLayer.setChildLayer("columnHeader", testLayer, 0, 0); //$NON-NLS-1$
    	compositeLayer.setChildLayer("testHeader", verticallyDependFrom, 1, 0);
    	
    	return compositeLayer;
    }
    
    IUniqueIndexLayer getBaseLayer(){
		return new DataLayer(new IDataProvider() {
			@Override
			public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
			}
			@Override
			public int getRowCount() {
				return 0;
			}
			@Override
			public Object getDataValue(int columnIndex, int rowIndex) {
				return null;
			}
			@Override
			public int getColumnCount() {
				return 0;
			}
		});
    }
    
}

