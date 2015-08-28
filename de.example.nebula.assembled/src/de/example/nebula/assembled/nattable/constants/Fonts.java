package de.example.nebula.assembled.nattable.constants;

import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

public final class Fonts {

    private Fonts() {
    }

    private static final int HEADER_FONT_HEIGHT = 8;
    public static final Font HEADER_FONT = GUIHelper.getFont(new FontData("Verdana", HEADER_FONT_HEIGHT, SWT.NORMAL));
    
    public static final Font HEADER_SELECTION_FONT = HEADER_FONT;

}
