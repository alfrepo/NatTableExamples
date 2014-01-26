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

        Rectangle bar = new Rectangle(rectangle.x, rectangle.y, (int) (rectangle.width * percentValueDouble),
                rectangle.height);

        Color color1 = CellStyleUtil.getCellStyle(cell, configRegistry).getAttributeValue(
                PERCENTAGE_BAR_COMPLETE_REGION_START_COLOR);
        Color color2 = CellStyleUtil.getCellStyle(cell, configRegistry).getAttributeValue(
                PERCENTAGE_BAR_COMPLETE_REGION_END_COLOR);
        if (color1 == null) {
            color1 = DEFAULT_COMPLETE_REGION_START_COLOR;
        }
        if (color2 == null) {
            color2 = DEFAULT_COMPLETE_REGION_END_COLOR;
        }

        Color barColor = blendColors(percentValueDouble, color1, color2);

        gc.setBackground(barColor);
        gc.fillRectangle(bar);

        Color incompleteRegionColor = CellStyleUtil.getCellStyle(cell, configRegistry).getAttributeValue(
                PERCENTAGE_BAR_INCOMPLETE_REGION_COLOR);
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
    Color blendColors(double percent, Color colorStart, Color colorEnd) {

        AColor bg = new AColor(colorStart);
        bg.a = 1.0 - percent;

        AColor fg = new AColor(colorEnd);
        fg.a = percent;

        // The result
        AColor r = new AColor();
        r.a = 1 - (1 - fg.a) * (1 - bg.a); // 0.75
        r.r = fg.r * fg.a / r.a + bg.r * bg.a * (1 - fg.a) / r.a; // 0.67
        r.g = fg.g * fg.a / r.a + bg.g * bg.a * (1 - fg.a) / r.a; // 0.33
        r.b = fg.b * fg.a / r.a + bg.b * bg.a * (1 - fg.a) / r.a; // 0.00

        return r.getColor();
    }

    class AColor {
        final int maxRGBColorValue = 255;

        double r;
        double g;
        double b;
        double a;

        AColor() {
        };

        AColor(double colorR, double colorg, double colorb, double alpha) {
            r = colorR;
            g = colorg;
            b = colorb;
            a = alpha;
        }

        AColor(Color c) {
            r = c.getRed() / 255.0;
            g = c.getGreen() / 255.0;
            b = c.getBlue() / 255.0;
        }

        Color getColor() {
            int ri = (int) (r * maxRGBColorValue);
            int gi = (int) (g * maxRGBColorValue);
            int bi = (int) (b * maxRGBColorValue);
            return GUIHelper.getColor(new RGB(ri, gi, bi));
        }
    }

    @Override
    public void handleEvent(Event event) {
        ICellPainter painter = getWrappedPainter();
        if (painter instanceof Listener) {
            ((Listener) painter).handleEvent(event);
        }
    }

}
