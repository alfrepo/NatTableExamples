package de.example.nebula.assembled.nattable.constants;

import org.eclipse.jface.dialogs.Dialog;
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


}
