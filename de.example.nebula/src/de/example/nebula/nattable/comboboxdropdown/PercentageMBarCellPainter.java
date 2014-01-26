package de.example.nebula.nattable.comboboxdropdown;

import org.eclipse.nebula.widgets.nattable.painter.cell.CellPainterWrapper;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.LineBorderDecorator;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.PaddingDecorator;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle;

public class PercentageMBarCellPainter extends CellPainterWrapper {

	PercentageConverter cellPercentConverter;
	
    public PercentageMBarCellPainter(PercentageConverter cellPercentConverter) {
        this(2, cellPercentConverter);
    }

    public PercentageMBarCellPainter(int outerPadding, PercentageConverter cellPercentConverter) {
        super(new PaddingDecorator(new LineBorderDecorator(new PercentageMBarDecorator(new TextPainter(false, false), cellPercentConverter), new BorderStyle()), outerPadding));
    }
}