package de.example.nebula.assembled.nattable;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
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
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
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