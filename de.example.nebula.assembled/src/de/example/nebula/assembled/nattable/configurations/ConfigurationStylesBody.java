package de.example.nebula.assembled.nattable.configurations;

import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.PaddingDecorator;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.VerticalAlignmentEnum;

import de.example.nebula.assembled.nattable.constants.Colors;

/**
 * objects encapsulates the style configurations of table.
 * 
 * @author alf
 * 
 */
public class ConfigurationStylesBody extends DefaultNatTableStyleConfiguration {

    public static final int CELL_PADDING = 3;
    public static final int CELL_PADDING_LEFT = 3;
    public static final int CELL_PADDING_RIGHT = 3;
    public static final int CELL_PADDING_TOP = 0;
    public static final int CELL_PADDING_BOTTOM = 0;

    public ConfigurationStylesBody() {

        this.vAlign = VerticalAlignmentEnum.MIDDLE;
        this.hAlign = HorizontalAlignmentEnum.LEFT;

        this.bgColor = Colors.TABLE_BODY_BG_COLOR;
        this.fgColor = Colors.TABLE_BODY_FG_COLOR;

        // A custom painter can be plugged in to paint the cells differently
        this.cellPainter = new PaddingDecorator(new TextPainter(), CELL_PADDING);

    }

}
