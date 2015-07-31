package de.example.nebula.assembled.nattable;

import org.eclipse.core.runtime.IPath;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
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
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.ButtonCellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ImagePainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.CellPainterDecorator;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.sort.ISortModel;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.ui.action.IMouseAction;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.CellLabelMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.util.CellEdgeEnum;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.example.nebula.assembled.nattable.configurations.ConfigurationAll;

public class Table {
    public static final String CUSTOM_CELL_LABEL = "Cell_LABEL";
 
    public static void main(String[] args) {
        Display display = new Display ();
        Shell shell = new Shell (display);
        shell.setLayout(new GridLayout());
         
        shell.setSize(1000, 600);
         
        new Table(shell);
         
        shell.open ();
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
         
    }
     
    public Table(Shell parent) {
    	
        parent.setLayout(new FillLayout());

        IDataProvider bodyDataProvider = setupBigDataProvider();
         
        //All of the layers, needed for the table
        BodyStack bodyStack = new BodyStack(bodyDataProvider);
         
        //column names
        IDataProvider dataProviderColHeader = getColumnLabelProvider(bodyDataProvider);
        DataLayer dataLayerColHeader = new DataLayer(dataProviderColHeader);
        ColumnHeaderLayer columnHeaderLayer = new ColumnHeaderLayer(dataLayerColHeader, bodyStack, bodyStack.selectionLayer);  //ACHTUNG: use viewportlayer here
         
 
        //row names. 
            //DEMO of encapsulating layers and configs in an "AbstractLayerTransform"
        IDataProvider dataProviderRowHeader = getRowLabelProvider(bodyDataProvider);        
        RowHeaderLayerStack rowHeaderLayer = new RowHeaderLayerStack(dataProviderRowHeader, bodyStack, bodyStack.selectionLayer); //ACHTUNG: use viewportlayer here
                                                                                                //Alternative to creating ColumnHeaderLayer: 
                                                                                                //CUSTOM AbstractLayerTransform, 
                                                                                                //encapsulates row header creation. 
                                                                                                //Can change provide changes to layers beneath.
         
        //(left upper)corner
        DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(dataProviderColHeader, dataProviderRowHeader);
        CornerLayer cornerLayer = new CornerLayer(new DataLayer(cornerDataProvider), rowHeaderLayer, columnHeaderLayer);
 
        //top layer
        GridLayer gridLayer = new GridLayer(bodyStack, columnHeaderLayer, rowHeaderLayer, cornerLayer);
        NatTable natTable = new NatTable(parent, gridLayer, false); //"autoconfigure" I can apply my own configuration
                 
         
         
        IConfigRegistry configRegistry = new ConfigRegistry();
        natTable.setConfigRegistry(configRegistry);
         
 
        // Step 1: Create a label accumulator - adds custom labels to all cells which we
        // wish to render differently. In this case render as a button.
        ColumnOverrideLabelAccumulator cellLabelAccumulator =   new ColumnOverrideLabelAccumulator(bodyStack.bodyDataLayer);

        //accumulator will add Label "CUSTOM_CELL_LABEL" to the column 2
        cellLabelAccumulator.registerColumnOverrides(2, CUSTOM_CELL_LABEL);
 
        // Step 2: Apply the Accumulator to the column
        bodyStack.bodyDataLayer.setConfigLabelAccumulator(cellLabelAccumulator);
 
         
        //Step 3: Apply the Custom style Painter to the cells, annotated by the Label "CUSTOM_CELL_LABEL"
        ConfigurationAll.addCustomConfiguration(natTable, gridLayer, new SortModel(natTable));
 
        
                //3.1 new painter
                final ButtonCellPainter buttonPainter = new ButtonCellPainter(
                            new CellPainterDecorator(
                                    new TextPainter(), CellEdgeEnum.RIGHT, new ImagePainter(GUIHelper.getImage("preferences"))
                            )
                        );
         
                //3.2 make painter responsible for drawing CUSTOM_CELL_LABEL annotated cells 
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
                 
                 
                //3.4 set the style for the CUSTOM_CELL_LABEL annotated cells 
                Style style = new Style();
                style.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_WHITE);   // Set the color of the cell. This is picked up by the button painter to style the button
         
                configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, style, DisplayMode.NORMAL, CUSTOM_CELL_LABEL);
                configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, style, DisplayMode.SELECT, CUSTOM_CELL_LABEL);
                 
             
                //3.5 add Mouse listener to use the Painter on Mouse event
                natTable.addConfiguration(new AbstractUiBindingConfiguration() {
                     
                    @Override
                    public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
                        // Match a mouse event on the body, when the left button is clicked
                        // and the custom cell label is present
                        CellLabelMouseEventMatcher mouseEventMatcher = new CellLabelMouseEventMatcher(
                                                                            GridRegion.BODY,
                                                                            MouseEventMatcher.LEFT_BUTTON,
                                                                            CUSTOM_CELL_LABEL);
                        // Inform the button painter of the click.
                        uiBindingRegistry.registerMouseDownBinding(mouseEventMatcher, buttonPainter);
                    }
                });
                 
        //Step 4 now apply the configuration to the NatTable
            //apply the style, which will draw the data into the Table
        	natTable.addConfiguration(new DefaultNatTableStyleConfiguration());         	// must be done after the setConfigRegistry, because the Configuration is stored in the configRegistry
            natTable.configure();
 
    }
 
     
     

	//HELPER
    private IDataProvider setupBigDataProvider() {
        DummyBodyDataProvider data = new DummyBodyDataProvider(500, 100000000);
        return data;
    }
    
    
    public class BodyStack extends AbstractLayerTransform {
    	
    	private DataLayer bodyDataLayer;
		private ColumnReorderLayer columnReorderLayer;
		private ColumnHideShowLayer columnHideShowLayer;
		private SelectionLayer selectionLayer;
		private ViewportLayer viewportLayer;

		public BodyStack(IDataProvider bodyDataProvider){
            bodyDataLayer = new DataLayer(bodyDataProvider);
            columnReorderLayer = new ColumnReorderLayer(bodyDataLayer);
            columnHideShowLayer = new ColumnHideShowLayer(columnReorderLayer);
            selectionLayer = new SelectionLayer(columnHideShowLayer);
            viewportLayer = new ViewportLayer(selectionLayer);
            
            // make the bottom layer to the layer, which will be used to extend the stack 
            setUnderlyingLayer(viewportLayer);
    	}
    	
    }
     
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
 
        public RowHeaderLayerStack(IDataProvider dataProvider, ILayer bodyDataLayer, SelectionLayer selectionLayer) {
            DataLayer dataLayer = new DataLayer(dataProvider, 50, 20);
            RowHeaderLayer rowHeaderLayer = new RowHeaderLayer(dataLayer, bodyDataLayer, selectionLayer);
            setUnderlyingLayer(rowHeaderLayer);
        }
    }
    
 
}