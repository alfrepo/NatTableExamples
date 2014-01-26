package de.example.nebula.nattable.comboboxdropdown;

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

/**
 * Draws a rectangular bar in cell proportional to the value of the cell.
 */
public class PercentageMBarDecorator extends CellPainterWrapper {

	public static final ConfigAttribute<Color> PERCENTAGE_BAR_COMPLETE_REGION_START_COLOR = new ConfigAttribute<Color>();
	public static final ConfigAttribute<Color> PERCENTAGE_BAR_COMPLETE_REGION_END_COLOR = new ConfigAttribute<Color>();
	public static final ConfigAttribute<Color> PERCENTAGE_BAR_INCOMPLETE_REGION_COLOR = new ConfigAttribute<Color>();
	
	private static final Color DEFAULT_COMPLETE_REGION_START_COLOR = GUIHelper.getColor(new RGB(124, 255, 149));
	private static final Color DEFAULT_COMPLETE_REGION_END_COLOR = GUIHelper.getColor(new RGB(235, 0, 0));
	
	
	private PercentageConverter cellPercentConverter;
	
	public PercentageMBarDecorator(ICellPainter interiorPainter, PercentageConverter converter) {
		super(interiorPainter);
		this.cellPercentConverter = converter;
	}

	@Override
	public void paintCell(ILayerCell cell, GC gc, Rectangle rectangle, IConfigRegistry configRegistry) {
		int percentValueInt;
		Object cellDataValue = cell.getDataValue();
		
		// try to retrieve the percent value for the painter
		if(cellPercentConverter != null){
			percentValueInt = cellPercentConverter.convert(cell, configRegistry, cellDataValue);
			
		} else if(cellDataValue instanceof Double){
			percentValueInt = (int) cellDataValue;
			
		}else{
			throw new IllegalStateException("The cellPercentConverter is null AND the cell value is not double. Can not convert the cell value to double in order to draw the percentage bar.");
		}
		
		
		
		// normalize
		percentValueInt = Math.min(100, (percentValueInt));
		percentValueInt = Math.max(0, percentValueInt);
		
		double percentValueDouble = ((double)percentValueInt)/100.0;
		
		Rectangle bar = new Rectangle(rectangle.x, rectangle.y, (int)(rectangle.width * percentValueDouble), rectangle.height);
		
		Color color1 = CellStyleUtil.getCellStyle(cell, configRegistry).getAttributeValue(PERCENTAGE_BAR_COMPLETE_REGION_START_COLOR);
		Color color2 = CellStyleUtil.getCellStyle(cell, configRegistry).getAttributeValue(PERCENTAGE_BAR_COMPLETE_REGION_END_COLOR);
		if (color1 == null) color1 = DEFAULT_COMPLETE_REGION_START_COLOR;
		if (color2 == null)	color2 = DEFAULT_COMPLETE_REGION_END_COLOR;

		Color barColor = blendColors(percentValueDouble, color1, color2);
		
		gc.setBackground(barColor);
		gc.fillRectangle(bar);
		
		Color incompleteRegionColor = CellStyleUtil.getCellStyle(cell, configRegistry).getAttributeValue(PERCENTAGE_BAR_INCOMPLETE_REGION_COLOR);
		if (incompleteRegionColor != null) {
			Region incompleteRegion = new Region();
			
			incompleteRegion.add(rectangle);
			incompleteRegion.subtract(bar);
			Color originalBackgroundColor = gc.getBackground();
			gc.setBackground(incompleteRegionColor);
			gc.fillRectangle(incompleteRegion.getBounds());
			gc.setBackground(originalBackgroundColor);
			
			incompleteRegion.dispose();
		}
		super.paintCell(cell, gc, rectangle, configRegistry);
	}

	/* percent is in [0,1] */
	Color blendColors(double percent, Color colorStart, Color colorEnd){
		
		AColor bg = new AColor (colorStart);
		bg.A = 1.0-percent;
		
		AColor fg = new AColor (colorEnd);
		fg.A = percent;
		
		
		// The result
		AColor r = new AColor();
		r.A = 1 - (1 - fg.A) * (1 - bg.A); // 0.75
		r.R = fg.R * fg.A / r.A + bg.R * bg.A * (1 - fg.A) / r.A; // 0.67
		r.G = fg.G * fg.A / r.A + bg.G * bg.A * (1 - fg.A) / r.A; // 0.33
		r.B = fg.B * fg.A / r.A + bg.B * bg.A * (1 - fg.A) / r.A; // 0.00
		
		return r.getColor();
	}
	
	class AColor{
		final int MAX_COLOR_VALUE = 255;
		
		double R;
		double G;
		double B;
		double A;
		
		AColor(){};
		
		AColor(double colorR, double colorg, double colorb, double alpha){
			R = colorR;
			G = colorg;
			B = colorb;
			A = alpha;
		}
		
		AColor(Color c){
			R = (double)c.getRed() /255.0;
			G = (double)c.getGreen() /255.0;
			B = (double)c.getBlue() /255.0;
		}
		
		Color getColor(){
			int ri = (int) (R*MAX_COLOR_VALUE);
			int gi = (int) (G*MAX_COLOR_VALUE);
			int bi = (int) (B*MAX_COLOR_VALUE);
			return GUIHelper.getColor(new RGB(ri, gi, bi));
		}
	}
	
}
