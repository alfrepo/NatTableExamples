package de.example.nebula.nattable;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.examples._102_Configuration.ButtonClickConfiguration;
import org.eclipse.nebula.widgets.nattable.examples.examples._102_Configuration.Rendereing_a_cell_as_a_button;
import org.eclipse.nebula.widgets.nattable.examples.fixtures.SelectionExampleGridLayer;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DummyBodyDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.ButtonCellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ImagePainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.CellPainterDecorator;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.ui.NatEventData;
import org.eclipse.nebula.widgets.nattable.ui.action.IMouseAction;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.CellLabelMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.menu.DebugMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.util.CellEdgeEnum;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NatTableAdvanced {
	public static final String CUSTOM_CELL_LABEL = "Cell_LABEL";

	private ButtonCellPainter buttonPainter;
	private SelectionExampleGridLayer gridLayer;
	
	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout(new GridLayout());
		
		shell.setSize(1000, 600);
		
		createNatTable(shell);
		
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
		
	}
	
	private static void createNatTable(Composite parent){
		parent.setLayout(new FillLayout());
		IDataProvider bodyDataProvider =  new DummyBodyDataProvider(500, 100000000);
		new NatTableAdvanced(parent,bodyDataProvider);
	}
	
	public NatTableAdvanced(Composite parent, final IDataProvider bodyDataProvider) {
				//All of the layers, needed for the table
					//1. Use the dataProvider, which is the DataSource
					DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
					//2. 
					ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(bodyDataLayer);
					//3. disables some columns
					ColumnHideShowLayer columnHideShowLayer = new ColumnHideShowLayer(columnReorderLayer);
					//4. can handle selection
					SelectionLayer selectionLayer = new SelectionLayer(columnHideShowLayer);
					//5. draw Scrollbars
					ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
	
				
				//column names
				IDataProvider dataProviderColHeader = getColumnLabelProvider(bodyDataProvider);
				DataLayer dataLayerColHeader = new DataLayer(dataProviderColHeader);
				ColumnHeaderLayer columnHeaderLayer = new ColumnHeaderLayer(dataLayerColHeader, bodyDataLayer, selectionLayer);
				
		
				//row names. 
					//DEMO of encapsulating layers and configs in an "AbstractLayerTransform"
				IDataProvider dataProviderRowHeader = getRowLabelProvider(bodyDataProvider);		
				RowHeaderLayerStack rowHeaderLayer = new RowHeaderLayerStack(dataProviderRowHeader, bodyDataLayer, selectionLayer); 	
																										//Alternative to creating ColumnHeaderLayer: 
																										//CUSTOM AbstractLayerTransform, 
																										//encapsulates row header creation. 
																										//Can change provide changes to layers beneath.
				
				//(left upper)corner
				DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(dataProviderColHeader, dataProviderRowHeader);
				CornerLayer cornerLayer = new CornerLayer(new DataLayer(cornerDataProvider), rowHeaderLayer, columnHeaderLayer);
		
				//top layer
				GridLayer gridLayer = new GridLayer(viewportLayer, columnHeaderLayer, rowHeaderLayer, cornerLayer);
				NatTable natTable = new NatTable(parent, gridLayer, false); //"autoconfigure" I can apply my own configuration
				
		
		
		IConfigRegistry configRegistry = new ConfigRegistry();
		

		// Step 1: Create a label accumulator - adds custom labels to all cells which we
		// wish to render differently. In this case render as a button.
		ColumnOverrideLabelAccumulator cellLabelAccumulator =	new ColumnOverrideLabelAccumulator(bodyDataLayer);
		//accumulator will add Label "CUSTOM_CELL_LABEL" to the column 2
		cellLabelAccumulator.registerColumnOverrides(2, CUSTOM_CELL_LABEL);

		// Step 2: Apply the Accumulator to the column
		bodyDataLayer.setConfigLabelAccumulator(cellLabelAccumulator);

		
		//Step 3: Apply the Custom style Painter to the cells, annotated by the Label "CUSTOM_CELL_CONFIG_LABEL"

				//3.1 new painter
				final ButtonCellPainter buttonPainter = new ButtonCellPainter(
							new CellPainterDecorator(
									new TextPainter(), CellEdgeEnum.RIGHT, new ImagePainter(GUIHelper.getImage("preferences"))
							)
						);
		
				//3.2 make painter responsible for drawing CUSTOM_CELL_CONFIG_LABEL annotated cells 
				configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER,
						buttonPainter,
						DisplayMode.NORMAL,
						CUSTOM_CELL_LABEL);
		
				
				//3.3 Add the listener to the button
				buttonPainter.addClickListener(new IMouseAction() {
					
					@Override
					public void run(NatTable natTable, MouseEvent event) {
						System.out.println("MouseClick");
					}
				});
				
				
				//3.4 set the style for the CUSTOM_CELL_CONFIG_LABEL annotated cells 
				Style style = new Style();
				style.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_WHITE); 	// Set the color of the cell. This is picked up by the button painter to style the button
		
				configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE,	style, DisplayMode.NORMAL, CUSTOM_CELL_LABEL);
				configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE,	style, DisplayMode.SELECT, CUSTOM_CELL_LABEL);
				
			
				//3.5 add Mouse listener to use the Painter on Mouse event
				natTable.addConfiguration(new AbstractUiBindingConfiguration() {
					
					@Override
					public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
						// Match a mouse event on the body, when the left button is clicked
						// and the custom cell label is present
						CellLabelMouseEventMatcher mouseEventMatcher = new CellLabelMouseEventMatcher(
																			GridRegion.BODY,
																			MouseEventMatcher.LEFT_BUTTON,
																			Rendereing_a_cell_as_a_button.CUSTOM_CELL_LABEL);
						// Inform the button painter of the click.
						uiBindingRegistry.registerMouseDownBinding(mouseEventMatcher, buttonPainter);
					}
				});
				
				//3.6 now apply the configuration to the NatTable
					//apply the style, which will draw the data into the Table
					natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
					natTable.setConfigRegistry(configRegistry);
					natTable.configure();

	}

	
	
	
	
	//HELPER
	
	private IDataProvider getRowLabelProvider(final IDataProvider bodyDataProvider){
		return new IDataProvider() {
			
			@Override
			public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
				//nothing
			}
			
			@Override
			public int getRowCount() {
				return bodyDataProvider.getRowCount();
			}
			
			@Override
			public Object getDataValue(int columnIndex, int rowIndex) {
				//here the name of the row is set
				return rowIndex;
			}
			
			@Override
			public int getColumnCount() {
				return 1;
			}
		};
	}
	private IDataProvider getColumnLabelProvider(final IDataProvider bodyDataProvider){
		return new IDataProvider() {
			
			@Override
			public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
				//nothing
			}
			
			@Override
			public int getRowCount() {
				return 1;
			}
			
			@Override
			public Object getDataValue(int columnIndex, int rowIndex) {
				//here the name of the row is set
				return columnIndex;
			}
			
			@Override
			public int getColumnCount() {
				return bodyDataProvider.getColumnCount();
			}
		};
	}
	
	private class RowHeaderLayerStack extends AbstractLayerTransform {

		public RowHeaderLayerStack(IDataProvider dataProvider, DataLayer bodyDataLayer, SelectionLayer selectionLayer) {
			DataLayer dataLayer = new DataLayer(dataProvider, 50, 20);
			RowHeaderLayer rowHeaderLayer = new RowHeaderLayer(dataLayer, bodyDataLayer, selectionLayer);
			setUnderlyingLayer(rowHeaderLayer);
		}
	}

}
