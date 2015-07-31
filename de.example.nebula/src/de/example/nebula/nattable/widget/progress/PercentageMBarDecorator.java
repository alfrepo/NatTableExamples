package de.example.nebula.nattable.widget.progress;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.CellPainterWrapper;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.ConfigAttribute;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Draws a rectangular bar in cell proportional to the value of the cell.
 * Is able to use {@link IPercentageConverter} objects in order to convert cell values to percent
 * values.
 * Passes mouse events to embedded {@link ICellPainter}.
 */
public class PercentageMBarDecorator extends CellPainterWrapper implements Listener {

    public static final ConfigAttribute<Color> PERCENTAGE_BAR_COMPLETE_REGION_START_COLOR = new ConfigAttribute<Color>();
    public static final ConfigAttribute<Color> PERCENTAGE_BAR_COMPLETE_REGION_END_COLOR = new ConfigAttribute<Color>();
    public static final ConfigAttribute<Color> PERCENTAGE_BAR_INCOMPLETE_REGION_COLOR = new ConfigAttribute<Color>();

    private static final Color DEFAULT_COMPLETE_REGION_START_COLOR = GUIHelper.getColor(new RGB(124, 255, 149));
    private static final Color DEFAULT_COMPLETE_REGION_END_COLOR = GUIHelper.getColor(new RGB(235, 0, 0));

    private IPercentageConverter cellPercentConverter;

    public PercentageMBarDecorator(ICellPainter interiorPainter, IPercentageConverter converter) {
        super(interiorPainter);
        this.cellPercentConverter = converter;
    }

    @Override
    public void paintCell(ILayerCell cell, GC gc, Rectangle rectangle, IConfigRegistry configRegistry) {
        int percentValueInt;
        Object cellDataValue = cell.getDataValue();

        // try to retrieve the percent value for the painter
        if (cellPercentConverter != null) {
            percentValueInt = cellPercentConverter.convert(cell, configRegistry, cellDataValue);

        } else if (cellDataValue instanceof Integer) {
            percentValueInt = (int) cellDataValue;

        } else {
            throw new IllegalStateException(
                    "The cellPercentConverter is null AND the cell value is not Integer. Can not convert the cell value to double in order to draw the percentage bar.");
        }

        // normalize
        percentValueInt = Math.min(100, (percentValueInt));
        percentValueInt = Math.max(0, percentValueInt);

        double percentValueDouble = (percentValueInt) / 100.0;
        Rectangle bar = new Rectangle(rectangle.x, rectangle.y, (int) (rectangle.width * percentValueDouble), rectangle.height);

//        Color color1 = CellStyleUtil.getCellStyle(cell, configRegistry).getAttributeValue( PERCENTAGE_BAR_COMPLETE_REGION_START_COLOR);
//        Color color2 = CellStyleUtil.getCellStyle(cell, configRegistry).getAttributeValue( PERCENTAGE_BAR_COMPLETE_REGION_END_COLOR);

        Color barColor = getColor(percentValueDouble);

        gc.setBackground(barColor);
        gc.fillRectangle(bar);

        super.paintCell(cell, gc, rectangle, configRegistry);
    }
    
    private Color getColor(double percentage){
    	Color green = GUIHelper.getColor(new RGB(189, 255, 197)); 
    	Color yellow = GUIHelper.getColor(new RGB(255, 237, 189)); 
    	Color red = GUIHelper.getColor( new RGB(255, 184, 184));
    	if(percentage<0.7){
    		return green;
    	} else if(percentage< 0.80){
    		return yellow;
    	}else{
    		return red;
    	}
    }


}
