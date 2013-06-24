package de.example.nebula.nattable.externalScrollbars;


import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultBooleanDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.CheckBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.ui.action.IMouseAction;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.IMouseEventMatcher;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

/**
 * This fixture creates a simple, minimal 3x3 table with resizable columns whose
 * cells are of the form 'Row X, Col Y'.
 */
public class TableLeft extends TableVerticallyDependend {
	
	private static final int COLUMN0_WIDTH = DEFAULTS.CELL_WIDTH; 
	private CheckBoxPainter checkBoxPainter=  new CheckBoxPainter();
	
	private ILayer verticallyDependingFromHeaderLayer;
	private LayerWithEmptyHeader compositeWithHeader;

	
	public TableLeft(Scrollable externalScrollable, ITableMainScrollable mainTable) {
		super( externalScrollable, mainTable);
		this.verticallyDependingFromHeaderLayer = mainTable.getHeaderLayer(); 
	}
	

    @Override
    protected void registerDomainSpecificLabels(
    		ColumnOverrideLabelAccumulator columnOverrideLabelAccumulator) {
    	
		//labels to specific columns
        columnOverrideLabelAccumulator.registerColumnOverrides(0, CHECK_BOX_EDITOR_CONFIG_LABEL, CHECK_BOX_CONFIG_LABEL);
        columnOverrideLabelAccumulator.registerColumnOverrides(1, CHECK_BOX_EDITOR_CONFIG_LABEL, CHECK_BOX_CONFIG_LABEL);
    }
    
    @Override
    protected void modifyConfigRegistry(IConfigRegistry configRegistry) {
        
        //use the labels
		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, checkBoxPainter, DisplayMode.NORMAL, CHECK_BOX_CONFIG_LABEL);

		//editor for BOTH modes NORMAL and EDIT
		CheckBoxCellEditor checkBoxCellEditor = new CheckBoxCellEditor();
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, checkBoxCellEditor, DisplayMode.NORMAL, CHECK_BOX_EDITOR_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, checkBoxCellEditor, DisplayMode.EDIT, CHECK_BOX_EDITOR_CONFIG_LABEL);
		
		//when are the cells editable?
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE, DisplayMode.EDIT, CHECK_BOX_CONFIG_LABEL);
		
		configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new DefaultBooleanDisplayConverter(), DisplayMode.NORMAL, CHECK_BOX_CONFIG_LABEL);
	}
    
	@Override
	protected void modifyUiBindingRegistry(UiBindingRegistry uiBindingRegistry) {
		
		IMouseEventMatcher mouseEventMatcher = new IMouseEventMatcher() {
			@Override
			public boolean matches(NatTable natTable, MouseEvent event,
					LabelStack regionLabels) {
				//match all cells, because all cells should react on click.
				//TODO alf: do not match the GRID.HEADER_ROW region?
				return true;
			}
		};
		IMouseAction mouseAction = new IMouseAction() {
			@Override
			public void run(NatTable natTable, MouseEvent event) {
				int columnPosition = natTable.getColumnPositionByX(event.x); // only the columns in the
                int rowPosition = natTable.getRowPositionByY(event.y);
                
                int columnIndex = natTable.getColumnIndexByPosition(columnPosition);
                int rowIndex = natTable.getRowIndexByPosition(rowPosition);
                
                IDataProvider dataProvider = getDataProvider();
                boolean val = (boolean) dataProvider.getDataValue(columnIndex, rowIndex);
                dataProvider.setDataValue(columnIndex, rowIndex, val ? false : true );
			}
		};
		
		uiBindingRegistry.registerFirstSingleClickBinding(
				mouseEventMatcher,
				mouseAction
		);
	}
    

	@Override
	public VerticallyDependentDataProvider getDataProvider() {
		return new VerticallyDependentDataProvider() {
			
			@Override
			public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
				//TODO alf del
				System.out.println("SetDataValue Column: "+columnIndex + " RowIndex "+rowIndex + " new value "+newValue);
			}
			
			@Override
			public Object getDataValue(int columnIndex, int rowIndex) {
				return true;
			}
			
			@Override
			public int getColumnCount() {
				return 1;
			}
		};
	}


	@Override
	public Composite createTableComposite(Composite parent) {
		Composite result = super.createTableComposite(parent);
		return result;
	}
	
	@Override
	public void postTableConstruct() {
		setColumnWidth(0, COLUMN0_WIDTH);
	}

	
	
	
	@Override
	public ILayer modifyLayerStack(ILayer layer) {
		compositeWithHeader = new LayerWithEmptyHeader(layer, verticallyDependingFromHeaderLayer);
		return compositeWithHeader;
	}
	
//	@Override
//	public DataLayer getDataLayer() {
//		return compositeWithHeader.getDataLayer();
//	}
	
}

