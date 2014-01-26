package de.example.nebula.nattable.comboboxdropdown;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.nebula.widgets.nattable.ui.action.IMouseAction;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Class, intended to be implemented by Bundles, in order to contribute cell renderers for custom
 * domain objects.
 * 
 * Because the renderer is contributed by a Bundle through an ExtensionPoint it can use Injections.
 * 
 * <p>
 * ACHTUNG: the renderer is capable to handle mouse events. To handle mouse events correctly - set
 * the {@link IEclipseContext} by calling {@link #setEclipseContext(IEclipseContext)}
 * </p>
 * 
 * @author alf
 * 
 */
public abstract class TextCellRenderer extends TextPainter implements Listener, ICellRenderer {

    private String id;

    private Color textColor;
    private Color bgColor;

    protected final List<IMouseAction> clickLiseners = new ArrayList<IMouseAction>();

    public TextCellRenderer() {
        super(false, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.ivu.fare.rcp.spi.swt.widgets.table.ITextCellRenderer#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.ivu.fare.rcp.spi.swt.widgets.table.ITextCellRenderer#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.ivu.fare.rcp.spi.swt.widgets.table.ITextCellRenderer#getPreferredWidth(org.eclipse.nebula
     * .widgets.nattable.layer.cell.ILayerCell, org.eclipse.swt.graphics.GC,
     * org.eclipse.nebula.widgets.nattable.config.IConfigRegistry)
     */
    @Override
    public int getPreferredWidth(ILayerCell cell, GC gc, IConfigRegistry configRegistry) {
        setupGCFromConfig(gc, CellStyleUtil.getCellStyle(cell, configRegistry));
        return getLengthFromCache(gc, convertDataType(cell, configRegistry)) + (spacing * 2) + 1;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }
    

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public Color getBgColor() {
        return bgColor;
    }


    @Override
    public void setupGCFromConfig(GC gc, IStyle cellStyle) {
        super.setupGCFromConfig(gc, cellStyle);
        // override own colors
        if (textColor != null) {
            gc.setForeground(textColor);
        }
        if (bgColor != null) {
            gc.setBackground(bgColor);
        }
    }

    @Override
    public void paintCell(ILayerCell cell, GC gc, Rectangle rectangle, IConfigRegistry configRegistry) {
        super.paintCell(cell, gc, rectangle, configRegistry);
    }

    public void addClickListener(IMouseAction mouseAction) {
        clickLiseners.add(mouseAction);
    }

    public void removeClickListener(IMouseAction mouseAction) {
        clickLiseners.remove(mouseAction);
    }

    @Override
    public abstract void handleEvent(Event event);

    @Override
    public void register(IConfigRegistry configRegistry, String configLabel) {
        configRegistry
                .registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, this, DisplayMode.NORMAL, configLabel);
    }

    @Override
    public void unregister(IConfigRegistry configRegistry, String configLabel) {
        configRegistry.unregisterConfigAttribute(CellConfigAttributes.CELL_PAINTER, DisplayMode.NORMAL, configLabel);
    }

}
