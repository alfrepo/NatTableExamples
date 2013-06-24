package de.example.nebula.nattable.editing.date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDateDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.action.MouseEditAction;
import org.eclipse.nebula.widgets.nattable.edit.editor.CheckBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.fixtures.Person;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
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
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.AbstractCellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.BodyCellEditorMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.example.nebula.nattable.editing.validate.DoubleValidatedEditor;
import de.example.nebula.nattable.editing.validate.IntegerValidatedEditor;
import de.example.nebula.nattable.editing.validate.IntegerValidator;

public class Table extends AbstractNatExample {
	
	private static final String CUSTOM_COMPARATOR_LABEL = "customComparatorLabel";
	protected static final String NO_SORT_LABEL = "noSortLabel";

	private IDataProvider bodyDataProvider;
	private String[] propertyNames;
	private BodyLayerStack bodyLayer;
	private Map propertyToLabels;
	
	//ADDED
	IConfigRegistry configRegistry;
	UiBindingRegistry uiBindingRegistry;
	
	ColumnOverrideLabelAccumulator columnOverrideLabelAccumulator;
	NatTable natTable;
	
	ArrayList<Listener> bodyMouseListeners = new ArrayList<Listener>();

	public static void main(String[] args) {
		Table table = new Table();
		table.addMouseListener(new Table_Listener());
		
		StandaloneNatExampleRunner.run(600, 400, table);
		
	}

	public Control createExampleControl(Composite parent) {
		//create a data provider, incl. the labels for the table-columns
		bodyDataProvider = setupBodyDataProvider();
		
		//body with all of it's table layers
		bodyLayer = new BodyLayerStack(bodyDataProvider);
		
		//column names
		DefaultColumnHeaderDataProvider colHeaderDataProvider = new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabels);
		ColumnHeaderLayerStack columnHeaderLayer = new ColumnHeaderLayerStack(colHeaderDataProvider);

		//row names
		DefaultRowHeaderDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider);		
		RowHeaderLayerStack rowHeaderLayer = new RowHeaderLayerStack(rowHeaderDataProvider);
		
		//(left upper)corner
		DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(colHeaderDataProvider, rowHeaderDataProvider);
		CornerLayer cornerLayer = new CornerLayer(new DataLayer(cornerDataProvider), rowHeaderLayer, columnHeaderLayer);

		//upper layer
		GridLayer gridLayer = new GridLayer(bodyLayer, columnHeaderLayer, rowHeaderLayer, cornerLayer);
		natTable = new NatTable(parent, gridLayer, false);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		
		this.configRegistry = natTable.getConfigRegistry();
		this.uiBindingRegistry = natTable.getUiBindingRegistry();
		
		//ATTENTION: do not forget to add the columnOverrideLabelAccumulator to the DataLayer
		this.columnOverrideLabelAccumulator = new ColumnOverrideLabelAccumulator(bodyLayer.getDataLayer());
		bodyLayer.getDataLayer().setConfigLabelAccumulator(columnOverrideLabelAccumulator);
		
		
		
		registerIDisplayConverter();
		
		registerColumnLabels(columnOverrideLabelAccumulator, natTable);

		registerCellRenderers(natTable);
		
		registerEditors(configRegistry);
		
		registerValidators(configRegistry);
		
		createListeners(natTable);
		
		natTable.configure();
		
		return natTable;
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
		final List people = Arrays.asList(
				new MyPerson(100, "Mickey Mouse", new Date(1000000), true), 
				new MyPerson(110, "Batman", new Date(2000000), false), 
				new MyPerson(120, "Bender", new Date(3000000), false), 
				new MyPerson(130, "Cartman", new Date(4000000), false), 
				new MyPerson(140, "Dogbert", new Date(5000000), true));
		

		//separate the id from the label
		propertyToLabels = new HashMap();
		propertyToLabels.put("id", "ID");
		propertyToLabels.put("name", "First Name");
		propertyToLabels.put("birthDate", "DOB");
		propertyToLabels.put("animal", "animal");

		propertyNames = new String[] { "id", "name", "birthDate", "animal" };
		
		return new ListDataProvider(people, new ReflectiveColumnPropertyAccessor(propertyNames)){
			
			@Override
			public void setDataValue(int columnIndex, int rowIndex,
					Object newValue) {
				
				Object object = this.getRowObject(rowIndex);
				System.out.println("Saving : "+newValue + " to "+object);
				
				ILayerCell cell = bodyLayer.getCellByPosition(columnIndex, rowIndex);
				cell.getDataValue();
				
				//TODO how to change the GUI, without changing the Data-Provider?
				
				System.out.println("Value in cell "+cell.getDataValue());
			}
		};
		
	}

	//BELOW are methods to create GridLayer composites
	
	public class BodyLayerStack extends AbstractLayerTransform {

		private SelectionLayer selectionLayer;
		private DataLayer bodyDataLayer;

		public BodyLayerStack(IDataProvider dataProvider) {
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
			setUnderlyingLayer(viewportLayer);
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
			ColumnHeaderLayer colHeaderLayer = new ColumnHeaderLayer(dataLayer, bodyLayer, bodyLayer.getSelectionLayer());
			setUnderlyingLayer(colHeaderLayer);
		}
		
		DataLayer getBodyDataLayer(){
			return dataLayer;
		}
	}

	public class RowHeaderLayerStack extends AbstractLayerTransform {

		public RowHeaderLayerStack(IDataProvider dataProvider) {
			DataLayer dataLayer = new DataLayer(dataProvider, 50, 20);
			RowHeaderLayer rowHeaderLayer = new RowHeaderLayer(dataLayer, bodyLayer, bodyLayer.getSelectionLayer());
			setUnderlyingLayer(rowHeaderLayer);
		}
	}
	
	//PRIVATE
	private void registerColumnLabels(ColumnOverrideLabelAccumulator columnOverrideLabelAccumulator, NatTable natTable){
		columnOverrideLabelAccumulator.registerColumnOverrides(0, "LABEL0");
		columnOverrideLabelAccumulator.registerColumnOverrides(1, "LABEL1");
		columnOverrideLabelAccumulator.registerColumnOverrides(2, "LABEL2");
		columnOverrideLabelAccumulator.registerColumnOverrides(3, "LABEL3", "LABEL_editor");
	}

	private void registerIDisplayConverter(){
		configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new DefaultDateDisplayConverter("d.M.y"), DisplayMode.NORMAL, "LABEL2");
	}
	
	private void registerCellRenderers(NatTable natTable){
		AbstractCellPainter cellPainter = new Table_Renderer();
		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, new CheckBoxPainter(), DisplayMode.NORMAL, "LABEL3");
	}
	
	private void registerEditors(IConfigRegistry configRegistry) {
		
		//1. register the Editor for mouse events
		uiBindingRegistry.registerFirstSingleClickBinding( new BodyCellEditorMouseEventMatcher(DateTimeCellEditor.class), new MouseEditAction());
		
		
		//2. make the cell editable
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE, DisplayMode.EDIT, "LABEL0");
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE, DisplayMode.EDIT, "LABEL1");
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE, DisplayMode.EDIT, "LABEL2");
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE, DisplayMode.EDIT, "LABEL3");
		
		
		//3. register the editors
//		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new Table_CellEditor_ICellEditor(), DisplayMode.EDIT, "LABEL1");
//		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new Table_CellEditor(), DisplayMode.EDIT, "LABEL1");
//		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new Table_CellEditor2(), DisplayMode.EDIT, "LABEL1");
//		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new TextCellEditor(), DisplayMode.EDIT, "LABEL1");

		
		new DoubleValidatedEditor().register(configRegistry, "LABEL0");
//		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new DoubleValidatedEditor(), DisplayMode.EDIT, "LABEL0");
		
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new DateTimeCellEditor("day.month.year"), DisplayMode.EDIT, "LABEL2");

		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new CheckBoxCellEditor(), DisplayMode.NORMAL, "LABEL3");
	}
	
	private void registerValidators(IConfigRegistry configRegistry){
//		configRegistry.registerConfigAttribute(EditConfigAttributes.DATA_VALIDATOR,new IntegerValidator(), DisplayMode.EDIT,"LABEL0");
	}
	

	
	private void createListeners(final NatTable natTable){
		Listener listener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				// only the columns in the viewport are considered
				int row = natTable.getRowPositionByY(event.y);
				int col = natTable.getColumnPositionByX(event.x); 
				
				// getting the cell from Mouse coordinates
				ILayerCell cell = natTable.getCellByPosition(col, row);
				ICellPainter painter = natTable.getCellPainter(col, row, cell, configRegistry);
				
				if (col >= 0 && row >= 0) {
					for(Listener listener : bodyMouseListeners){
						listener.handleEvent(event);
					}
					
					
					//abs column number. starts with the column 0, which contains the row header.
					//Considers all columns, not just those in the viewport.
					int absCol = cell.getColumnIndex();
					int absRow = cell.getRowIndex();
					
					if(painter instanceof Listener){
						((Listener)painter).handleEvent(event);
					}
					
				}
			}
		};
		
		natTable.addListener(SWT.MouseDown, listener);
		natTable.addListener(SWT.MouseUp, listener);
		natTable.addListener(SWT.MouseMove, listener);
	}
	
	
	public class MyPerson extends Person{
		
		public boolean animal;

		private MyPerson(int id, String name, Date birthDate) {
			super(id, name, birthDate);
		}
		
		private MyPerson(int id, String name, Date birthDate, boolean isAnimal) {
			super(id, name, birthDate);
			this.setAnimal(isAnimal);
		}

		public boolean isAnimal() {
			return animal;
		}

		public void setAnimal(boolean animal) {
			this.animal = animal;
		}
		
	}
	
	public void addMouseListener(Listener listener){
		this.bodyMouseListeners.add(listener);
	}
	
}
