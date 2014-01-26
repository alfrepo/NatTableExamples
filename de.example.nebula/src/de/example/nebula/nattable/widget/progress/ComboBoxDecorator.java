package de.example.nebula.nattable.widget.progress;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ImagePainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.CellPainterDecorator;
import org.eclipse.nebula.widgets.nattable.ui.util.CellEdgeEnum;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ComboBoxDecorator extends CellPainterDecorator implements Listener {

    public enum ClickArea {
        DECORATORPAINTER, WRAPPEDPAINTER
    }

    private static final ICellPainter decoratorPainter = new ImagePainter(GUIHelper.getImage("down_2"));
    private static ClickArea lastClickedOn;

    ICellPainter wrappedPainter;
    GC gc;

    /**
     * Create a new {@link ICellPainter} using {@link ICellPainterFactory}
     * 
     * @param comboImage
     *            The image marking the cell as a combo control
     */
    public ComboBoxDecorator(ICellPainter cellPainter) {
        super(cellPainter, CellEdgeEnum.RIGHT, 2, decoratorPainter, true);
        wrappedPainter = cellPainter;
    }

    @Override
    public void handleEvent(Event event) {
        ICellPainter cellPainter = wrappedPainter;

        // redirect all kind of events to the Renderer
        if (cellPainter instanceof Listener && !cellPainter.equals(this)) {
            ((Listener) cellPainter).handleEvent(event);
        }

        // remember the lastly clicked area, to make cell only editable, when decorator was clicked.
        // See "isEditable" rule.
        if (event.type == SWT.MouseDown) {
            if (cellPainter.equals(decoratorPainter)) {
                lastClickedOn = ClickArea.DECORATORPAINTER;
            } else {
                lastClickedOn = ClickArea.WRAPPEDPAINTER;
            }
        }

    }

    /**
     * The rule makes the cells with the current editor only to be editable, when the decorator is
     * clicked!
     */
    public static IEditableRule getICellEditableRule() {
        return new IEditableRule() {
            @Override
            public boolean isEditable(int columnIndex, int rowIndex) {
                return (lastClickedOn != null) && (lastClickedOn == ClickArea.DECORATORPAINTER);
            }

            @Override
            public boolean isEditable(ILayerCell cell, IConfigRegistry configRegistry) {
                return (lastClickedOn != null) && (lastClickedOn == ClickArea.DECORATORPAINTER);
            }
        };
    }

    @Override
    public void paintCell(ILayerCell cell, GC gc, Rectangle adjustedCellBounds, IConfigRegistry configRegistry) {
        super.paintCell(cell, gc, adjustedCellBounds, configRegistry);
        if (this.gc == null) {
            this.gc = gc;
        }
    }

}
