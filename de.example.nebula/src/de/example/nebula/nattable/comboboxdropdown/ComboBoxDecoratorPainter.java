package de.example.nebula.nattable.comboboxdropdown;

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

public class ComboBoxDecoratorPainter extends CellPainterDecorator implements Listener {

	public enum ClickArea { DECORATORPAINTER, WRAPPEDPAINTER}
	
	private static ClickArea lastClickedOn;
	
	ICellPainter wrappedPainter;
	GC gc;
	
	
	/**
	 * Create a new {@link ICellPainter} using {@link ICellPainterFactory}} 
	 * 
	 * @param comboImage
	 *            The image marking the cell as a combo control
	 */
	public ComboBoxDecoratorPainter(ICellPainter cellPainter) {
		super(cellPainter, CellEdgeEnum.RIGHT, 2 , new ImagePainter(GUIHelper.getImage("down_2")), true);
		wrappedPainter = cellPainter;
	}
	

	@Override
	public void handleEvent(Event event) {
		if(event.type != SWT.MouseDown){
			return;
		}

		Table.EventData data = (Table.EventData) event.data;

		// redirect clicks to the Renderer
		ICellPainter cellPainter = getCellPainterAt(event.x, event.y, data.layerCell, gc, data.layerCell.getBounds(), data.configRegistry);
		if(cellPainter instanceof Listener && !cellPainter.equals(this)){
			((Listener)cellPainter).handleEvent(event);
		}
		
		// remember the last are clicked, to reuse inside of "isEditable" rule
		if(cellPainter.equals(wrappedPainter)){
			lastClickedOn = ClickArea.WRAPPEDPAINTER;
		}else{
			lastClickedOn = ClickArea.DECORATORPAINTER;
		}
	}
	
	/** The rule makes the cells with the current editor only to be editable, when the decorator is clicked! */
	public static IEditableRule getICellEditableRule(final ClickArea editableClickInArea){
		return new IEditableRule() {
			@Override
			public boolean isEditable(int columnIndex, int rowIndex) {
				return (lastClickedOn!=null) && (lastClickedOn==editableClickInArea);
			}
			@Override
			public boolean isEditable(ILayerCell cell, IConfigRegistry configRegistry) {
				return (lastClickedOn!=null) && (lastClickedOn==editableClickInArea);
			}
		};
	}
	
	@Override
	public void paintCell(ILayerCell cell, GC gc, Rectangle adjustedCellBounds,
			IConfigRegistry configRegistry) {
		super.paintCell(cell, gc, adjustedCellBounds, configRegistry);
		if(this.gc == null){
			this.gc = gc;
		}
	}
	
}
