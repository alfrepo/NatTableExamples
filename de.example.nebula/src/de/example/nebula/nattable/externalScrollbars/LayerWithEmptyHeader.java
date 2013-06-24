package de.example.nebula.nattable.externalScrollbars;

import org.eclipse.nebula.widgets.nattable.config.AggregateConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.layer.DimensionallyDependentLayer;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.IUniqueIndexLayer;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.config.DefaultRowHeaderLayerConfiguration;
import org.eclipse.nebula.widgets.nattable.layer.config.DefaultRowHeaderStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.style.VerticalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * This layer has a header row at it's top
 *
 */
public class LayerWithEmptyHeader extends CompositeLayer {
	
	public Font font = GUIHelper.getFont(new FontData("Verdana", 10, SWT.NORMAL)); //$NON-NLS-1$
	public Color bgColor = GUIHelper.COLOR_WIDGET_BACKGROUND;
	public Color fgColor = GUIHelper.COLOR_WIDGET_FOREGROUND;
	public Color gradientBgColor = GUIHelper.COLOR_WHITE;
	public Color gradientFgColor = GUIHelper.getColor(136, 212, 215);
	public HorizontalAlignmentEnum hAlign = HorizontalAlignmentEnum.CENTER;
	public VerticalAlignmentEnum vAlign = VerticalAlignmentEnum.MIDDLE;
	public BorderStyle borderStyle = null;
	
	
	public static final int CELL_WIDTH = DEFAULTS.CELL_WIDTH; 
	public static final int CELL_HEIGHT = DEFAULTS.CELL_HEIGHT;
	
	private DataLayer headerLayer;

	public LayerWithEmptyHeader(ILayer headlessLayer, ILayer verticallyDependingFromHeaderLayer) {
		super(1, 2);
		
		headerLayer = createHeaderLayer();

		ILayer headerLayerVerticallyDependent;
		headerLayerVerticallyDependent = headerLayer;
		headerLayerVerticallyDependent = createVerticallyDependentAdapterLayer(headerLayer, verticallyDependingFromHeaderLayer);
		
		setChildLayer(GridRegion.ROW_HEADER, headerLayerVerticallyDependent, 0, 0);
		setChildLayer(GridRegion.BODY, headlessLayer, 0, 1);
	}

	
    /**
     * Create the layer, which represents the header row. 
     * @return
     */
    private DataLayer createHeaderLayer(){
		IDataProvider emptyOneCellHeaderDataProvider = new IDataProvider() {
			@Override
			public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
				
			}
			@Override
			public int getRowCount() {
				return 1;
			}
			
			@Override
			public Object getDataValue(int columnIndex, int rowIndex) {
				return "";
			}
			
			@Override
			public int getColumnCount() {
				return 1;
			}
		};
		
		
		DataLayer headerDataLayer = new DataLayer(emptyOneCellHeaderDataProvider, CELL_WIDTH, CELL_HEIGHT);
		
		//WORKS
		ColumnLabelAccumulator overrideLabelAccumulator = new ColumnLabelAccumulator(){
			@Override
			public void accumulateConfigLabels(LabelStack configLabels,
					int columnPosition, int rowPosition) {
				super.accumulateConfigLabels(configLabels, columnPosition, rowPosition);
				configLabels.addLabel(GridRegion.ROW_HEADER); //add GridRegion.ROW_HEADER, so that DefaultRowHeaderLayerConfiguration starts to work
			}
		};
		headerDataLayer.setConfigLabelAccumulator(overrideLabelAccumulator);
		
		//reuse the default configurations. This makes the row header look like a header.
		IConfiguration configurationDefault = new AggregateConfiguration(){
			{
				//add the styling as used in DefaultRowHeaderLayerConfiguration
				addConfiguration(new DefaultRowHeaderStyleConfiguration());
			}
		};
		headerDataLayer.addConfiguration(configurationDefault);
		
    	return headerDataLayer;
    }

    /**
     * Wraps the header into an vertically dependent Adapter, if the given #verticallyDependingFromHeaderLayer is not null. 
     * @param headerLayer
     * @param verticallyDependingFromHeaderLayer
     * @return the vertically dependent layer, if the verticallyDependingFromHeaderLayer is not null and headerLayer is not null.
     */
    private ILayer createVerticallyDependentAdapterLayer(IUniqueIndexLayer headerLayer, ILayer verticallyDependingFromHeaderLayer){
		if(verticallyDependingFromHeaderLayer == null  ||  headerLayer == null){
			return headerLayer;
		}
		
		ILayer verticallyDependentHeaderLayer = new DimensionallyDependentLayer(headerLayer, headerLayer, verticallyDependingFromHeaderLayer);
		return verticallyDependentHeaderLayer;
    }
    
    public DataLayer getHeaderDataLayer() {
		return headerLayer;
	}

}
