package de.example.nebula.assembled.nattable.constants;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle.LineStyleEnum;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.VerticalAlignmentEnum;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Shell;

public final class Constants {

    private Constants() {
    }

    private static GC defaultGc = new GC(new Shell());
    private static FontMetrics defaultFontMetric = defaultGc.getFontMetrics();

    public static final int TABLE_PADDING_COLUMNHEADER = 5; // px
    public static final int MIN_BUTTON_WIDTH = Dialog.convertHorizontalDLUsToPixels(defaultFontMetric, 50); // http://msdn.microsoft.com/en-us/library/ms997619.aspx

    public static final int HEADER_LINE_THICKNESS = 1;
    public static final LineStyleEnum HEADER_DEFAULT_LINE_STYLE = LineStyleEnum.SOLID;
    public static final HorizontalAlignmentEnum HEADER_HORIZONTAL_ALIGNMENT = HorizontalAlignmentEnum.CENTER;
    public static final VerticalAlignmentEnum HEADER_VERTICAL_ALIGNMENT = VerticalAlignmentEnum.MIDDLE;
    
    public static final BorderStyle ANCHOR_BORDER_STYLE = new BorderStyle(1, Colors.SELECTION_BORDER_COLOR, LineStyleEnum.SOLID);
    public static final BorderStyle ANCHOR_HEADER_SELECTION_BORDER_STYLE = new BorderStyle(-1, Colors.ANCHOR_HEADER_SELECTED_FOREGROUND_COLOR, LineStyleEnum.SOLID);
}
