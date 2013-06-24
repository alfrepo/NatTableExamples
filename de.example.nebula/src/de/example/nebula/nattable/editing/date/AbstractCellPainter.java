package de.example.nebula.nattable.editing.date;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public abstract class AbstractCellPainter implements ICellPainter {
	
	public enum Status{NORMAL, MOUSEOVER};
	Status status = Status.NORMAL;
	

	abstract void paintCellNormal(ILayerCell cell, GC gc, Rectangle bounds,
			IConfigRegistry configRegistry);
	
	abstract void paintCellMouseOver(ILayerCell cell, GC gc, Rectangle bounds,
			IConfigRegistry configRegistry);
	
	
	@Override
	public void paintCell(ILayerCell cell, GC gc, Rectangle bounds,
			IConfigRegistry configRegistry) {
		switch (status) {
		case MOUSEOVER:
			paintCellMouseOver(cell, gc, bounds, configRegistry);
			break;

		default:
			paintCellNormal(cell, gc, bounds, configRegistry);
			break;
		}
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Status getStatus() {
		return status;
	}
}
