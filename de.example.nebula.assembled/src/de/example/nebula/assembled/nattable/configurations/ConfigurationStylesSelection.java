package de.example.nebula.assembled.nattable.configurations;

import org.eclipse.nebula.widgets.nattable.selection.config.DefaultSelectionStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle.LineStyleEnum;

import de.example.nebula.assembled.nattable.constants.Colors;
import de.example.nebula.assembled.nattable.constants.Fonts;

public class ConfigurationStylesSelection extends DefaultSelectionStyleConfiguration {

    ConfigurationStylesSelection() {
        // Setup selection styling
        DefaultSelectionStyleConfiguration selectionStyle = new DefaultSelectionStyleConfiguration();
        selectionStyle.selectionFont = Fonts.SELECTION_FONT;
        selectionStyle.selectionBgColor = Colors.SELECTION_BACKGROUND_COLOR;
        selectionStyle.selectionFgColor = Colors.SELECTION_COLOR;
        selectionStyle.anchorBorderStyle = new BorderStyle(1, Colors.SELECTION_BORDER_COLOR, LineStyleEnum.SOLID);
        selectionStyle.anchorBgColor = Colors.ANCHOR_BACKGROUND_COLOR;
        selectionStyle.selectedHeaderBgColor = Colors.SELECTION_HEADER_BACKGROUND_COLOR;
    }
}
