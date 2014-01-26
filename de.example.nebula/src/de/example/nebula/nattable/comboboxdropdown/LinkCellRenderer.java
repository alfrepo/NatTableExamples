package de.example.nebula.nattable.comboboxdropdown;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;

public class LinkCellRenderer extends TextCellRenderer {


    private static final Color TEXT_COLOR_LINK = GUIHelper.getColor(3, 158, 230);
    private static final Color BG_COLOR_LINK = GUIHelper.COLOR_WHITE;


    public LinkCellRenderer() {
    	styleAsLink();
    }

    @Override
    public void handleEvent(Event event) {
    	System.out.println("LinkCellRenderer.handleEvent. NEW SLIDE");
    }

    @Override
    public void paintCell(ILayerCell cell, GC gc, Rectangle rectangle, IConfigRegistry configRegistry) {
        super.paintCell(cell, gc, rectangle, configRegistry);
    }

    // PRIVATE
    private void styleAsLink() {
        this.setUnderline(true);
        this.setBgColor(BG_COLOR_LINK);
        this.setTextColor(TEXT_COLOR_LINK);
    }

}
