package de.example.nebula10;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IColumnAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDateDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.CheckBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.filterrow.ComboBoxFilterRowHeaderComposite;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowHeaderComposite;
import org.eclipse.nebula.widgets.nattable.filterrow.IFilterStrategy;
import org.eclipse.nebula.widgets.nattable.filterrow.combobox.ComboBoxFilterRowConfiguration;
import org.eclipse.nebula.widgets.nattable.freeze.config.DefaultFreezeGridBindings;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.layer.NatGridLayerPainter;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.matchers.CompositeMatcherEditor;


public class Table  {
	
	protected static final String LABEL_FILTERS = "LABEL_FILTERS"; 
	
	private IDataProvider bodyDataProvider;
	private String[] propertyNames;
	private BodyStack bodyStack;
	private Map propertyToLabels;
	private IColumnAccessor columnAccessor;
	private Collection<MyPerson> baseMyPersonsCollection;
	
	//ADDED
	IConfigRegistry configRegistry = new ConfigRegistry();
	UiBindingRegistry uiBindingRegistry;
	
	ColumnOverrideLabelAccumulator columnOverrideLabelAccumulator;
	NatTable natTable;
	
	ArrayList<Listener> bodyMouseListeners = new ArrayList<Listener>();

	public static void main(String[] args) {
		Table table = new Table();
		
		int shellWidth = 600;
		int shellHeight = 400;
		
		// Setup
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		shell.setSize(shellWidth, shellHeight);
		shell.setText("Shell");

		table.createExampleControl(shell);
		

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
	}

	public Control createExampleControl(Composite parent) {
		//create a data provider, incl. the labels for the table-columns
		bodyDataProvider = setupBodyDataProvider();
		
		//body with all of it's table layers
		bodyStack = new BodyStack(bodyDataProvider);
		
		//column names
		DefaultColumnHeaderDataProvider colHeaderDataProvider = new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabels);
		ColumnHeaderLayerStack columnHeaderLayer = new ColumnHeaderLayerStack(colHeaderDataProvider);

		//row names
		DefaultRowHeaderDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider);		
		RowHeaderLayerStack rowHeaderLayer = new RowHeaderLayerStack(rowHeaderDataProvider);
		
		//Filters
		FilterList filterList = new FilterList<>(new BasicEventList<>());
//			ComboBoxFilterRowHeaderComposite<MyPerson> filterRowHeaderComposite = getComboBoxFilterRowHeaderComposite(filterList,columnHeaderLayer, colHeaderDataProvider);
			FilterRowHeaderComposite<MyPerson> filterRowHeaderComposite =  getFilterRowHeaderComposite(columnHeaderLayer, colHeaderDataProvider, MyPerson.class);
		
		
		//(left upper)corner
		DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(colHeaderDataProvider, rowHeaderDataProvider);
		CornerLayer cornerLayer = new CornerLayer(new DataLayer(cornerDataProvider), rowHeaderLayer, filterRowHeaderComposite);

		//upper layer
		GridLayer gridLayer = new GridLayer(bodyStack, filterRowHeaderComposite, rowHeaderLayer, cornerLayer);
		
	    final int DEFAULT_STYLE_OPTIONS = SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED  | SWT.V_SCROLL | SWT.H_SCROLL;
	    natTable = new NatTable(parent,DEFAULT_STYLE_OPTIONS, gridLayer, false);
	        
		// Configuration
			natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
			natTable.addConfiguration(new HeaderMenuConfiguration(natTable));
			natTable.addConfiguration(new DefaultFreezeGridBindings());

		// retrieve the configuration storages
			natTable.setConfigRegistry(configRegistry);
			this.uiBindingRegistry = natTable.getUiBindingRegistry();
			
        // stretch the table to fill the empty space
	        NatGridLayerPainter layerPainter = new NatGridLayerPainter(natTable);
	        natTable.setLayerPainter(layerPainter);
	        
		
		//ATTENTION: do not forget to add the columnOverrideLabelAccumulator to the DataLayer
		this.columnOverrideLabelAccumulator = new ColumnOverrideLabelAccumulator(bodyStack.getDataLayer());
		bodyStack.getDataLayer().setConfigLabelAccumulator(columnOverrideLabelAccumulator);
		
		
		
		registerIDisplayConverter();
		registerColumnLabels(columnOverrideLabelAccumulator, natTable);
		registerEditors(configRegistry);
		
		natTable.configure();
		new TableObjectsDebugTrace(natTable);
		
		return natTable;
	}
	
	private <T> FilterRowHeaderComposite<T> getFilterRowHeaderComposite(ILayer columnHeaderLayer, IDataProvider columnHeaderDataProvider, Class<T> clazz) {
		
		IFilterStrategy<T> filterStrategy = new IFilterStrategy<T>() {
			@Override
			public void applyFilter(Map<Integer, Object> filterIndexToObjectMap) {
				//nix
			}
		}; 
		
		FilterRowHeaderComposite<T> f = new FilterRowHeaderComposite<T>(filterStrategy, columnHeaderLayer, columnHeaderDataProvider, configRegistry);
		
		return f;
	}
	
	private <T> ComboBoxFilterRowHeaderComposite<T> getComboBoxFilterRowHeaderComposite(FilterList<T> filterListP, ILayer columnHeaderLayerP, IDataProvider columnHeaderLayerDataProvider){
			
			CompositeMatcherEditor compositeMatcherEditor = new CompositeMatcherEditor<T>();
		
			//The FilterList that will be used for filtering.
			FilterList<T> filterList=filterListP;

			//A layer in the body region. Usually the DataLayer or a layer that is responsible for list event handling. Needed for creation of the FilterRowComboBoxDataProvider.
			ILayer bodyLayer=bodyStack.filterListLayer;
			
			//The base collection that is used to fill the body. Needed to determine the values to show in the filter comboboxes and initially pre-select them.
//			Collection baseCollection=new ArrayList<>();
			Collection baseCollection=baseMyPersonsCollection;
			
			//The IColumnAccessor that is needed by the IFilterStrategy to perform filtering.
			IColumnAccessor<T> bodyDataColumnAccessor=Table.this.columnAccessor;
			
			//The columnheader layer the filter row layer is related to. Needed for building this CompositeLayer, dimensionally connect the filter row to and retrieve information and perform actions related to filtering.
			ILayer columnHeaderLayer=columnHeaderLayerP;
			
			//The IDataProvider of the column header needed to retrieve the real column count of the column header and not a transformed one.
			IDataProvider columnHeaderDataProvider=columnHeaderLayerDataProvider;
			
			//The IConfigRegistry needed to retrieve various configurations.
			IConfigRegistry configRegistry=Table.this.configRegistry;
			
			//Tell whether the default configuration should be used or not. If not you need to ensure to add a configuration that adds at least the needed configuration specified in ComboBoxFilterRowConfiguration
			boolean useDefaultConfiguration=true;
//			bodyStack.bodyDataLayer.addConfiguration(new ConfigurationFilterRow());
//			bodyStack.bodyDataLayer.addConfiguration(new ComboBoxFilterRowConfiguration());
		
			ComboBoxFilterRowHeaderComposite<T> c = new ComboBoxFilterRowHeaderComposite<T>(
					filterList,
					bodyLayer,
					baseCollection, 
					bodyDataColumnAccessor,
					columnHeaderLayer,  
					columnHeaderDataProvider,
					configRegistry,
					useDefaultConfiguration);
			
			c.addConfiguration(new ComboBoxFilterRowConfiguration(c.getComboBoxDataProvider()));
//			c.addConfiguration(new ConfigurationFilterRowComboBox(c.getComboBoxDataProvider()));
		
		return c;
	}

	/**
	 * creates the content
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private IDataProvider setupBodyDataProvider() {
		final List people = Arrays.asList(
				new MyPerson(100, "Mickey Mouse", new Date(1000000), true, 22313), 
				new MyPerson(110, "Batman", new Date(2000000), false, 22313), 
				new MyPerson(120, "Bender", new Date(3000000), false, 22313), 
				new MyPerson(130, "Cartman", new Date(4000000), false, 22313), 
				new MyPerson(140, "Dogbert", new Date(5000000), true, 22313));
		
		baseMyPersonsCollection = people;
		propertyNames = new String[] { "id", "name", "birthDate", "animal", "distance" };
		
		
		columnAccessor = new IColumnAccessor<MyPerson>() {
			@Override
			public Object getDataValue(MyPerson rowObject, int columnIndex) {
				switch(columnIndex){
				case 0:
					return rowObject.id;
				case 1:
					return rowObject.name;
				case 2:
					return rowObject.birthDate;
				case 3:
					return rowObject.animal;
				case 4:
					return rowObject.distance;
				}
				return null;
			}

			@Override
			public void setDataValue(MyPerson rowObject, int columnIndex,
					Object newValue) {
				//nix
			}

			@Override
			public int getColumnCount() {
				return 5;
			}

		};
		
		return new ListDataProvider(people,columnAccessor) ;
	}

	//BodyLayerStack creates all kinds of layers
	public class BodyStack extends AbstractLayerTransform {
		public SelectionLayer selectionLayer;
		public DataLayer bodyDataLayer;
		public FilterListLayer filterListLayer;

		public BodyStack(IDataProvider dataProvider) {
			//1. Use the dataProvider, which is the DataSource
			bodyDataLayer = new DataLayer(dataProvider);
			//2. 
			ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(bodyDataLayer);
			//3. disables some columns
			ColumnHideShowLayer columnHideShowLayer = new ColumnHideShowLayer(columnReorderLayer);
			//4. can handle selection
			selectionLayer = new SelectionLayer(columnHideShowLayer);
			//5. draw Scrollbars
			ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
			//6. FilterListLayer for handline Filter events
			filterListLayer = new FilterListLayer(viewportLayer);

			setUnderlyingLayer(filterListLayer);
		}

		public SelectionLayer getSelectionLayer() {
			return selectionLayer;
		}
		
		public DataLayer getDataLayer() {
			return bodyDataLayer;
		}
	}

	public class ColumnHeaderLayerStack extends AbstractLayerTransform {
		DataLayer dataLayer;
		
		public ColumnHeaderLayerStack(IDataProvider dataProvider) {
			 dataLayer = new DataLayer(dataProvider);
			ColumnHeaderLayer colHeaderLayer = new ColumnHeaderLayer(dataLayer, bodyStack, bodyStack.getSelectionLayer());
			setUnderlyingLayer(colHeaderLayer);
		}
		
		DataLayer getBodyDataLayer(){
			return dataLayer;
		}
	}

	public class RowHeaderLayerStack extends AbstractLayerTransform {
		public RowHeaderLayerStack(IDataProvider dataProvider) {
			DataLayer dataLayer = new DataLayer(dataProvider, 50, 20);
			RowHeaderLayer rowHeaderLayer = new RowHeaderLayer(dataLayer, bodyStack, bodyStack.getSelectionLayer());
			setUnderlyingLayer(rowHeaderLayer);
		}
	}
	
	//PRIVATE
	private void registerColumnLabels(ColumnOverrideLabelAccumulator columnOverrideLabelAccumulator, NatTable natTable){
		columnOverrideLabelAccumulator.registerColumnOverrides(0, "LABEL0");
		columnOverrideLabelAccumulator.registerColumnOverrides(1, "LABEL1");
		columnOverrideLabelAccumulator.registerColumnOverrides(2, "LABEL2");
		columnOverrideLabelAccumulator.registerColumnOverrides(3, "LABEL3", "LABEL_editor");
		columnOverrideLabelAccumulator.registerColumnOverrides(4, "LABEL4");
	}

	private void registerIDisplayConverter(){
		configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new DefaultDateDisplayConverter("d.M.y"), DisplayMode.NORMAL, "LABEL2");
	}
	
	
	private void registerEditors(IConfigRegistry configRegistry) {
		
		//1. register the Editor for mouse events
		
		
		//2. make the cell editable
//		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE, DisplayMode.EDIT, "LABEL0");
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE, DisplayMode.EDIT, "LABEL1");
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE, DisplayMode.EDIT, "LABEL2");
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE, DisplayMode.EDIT, "LABEL3");
		
		
		//3. register the editors
//		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new Table_CellEditor_ICellEditor(), DisplayMode.EDIT, "LABEL1");
//		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new Table_CellEditor(), DisplayMode.EDIT, "LABEL1");
//		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new Table_CellEditor2(), DisplayMode.EDIT, "LABEL1");
//		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new TextCellEditor(), DisplayMode.EDIT, "LABEL1");

		
//		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new DoubleValidatedEditor(), DisplayMode.EDIT, "LABEL0");
		
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new CheckBoxCellEditor(), DisplayMode.NORMAL, "LABEL3");
		
	}
	

	
}
