package de.example.nebula.assembled.nattable.configurations;

import org.eclipse.nebula.widgets.nattable.layer.config.DefaultRowHeaderStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;

import de.example.nebula.assembled.nattable.constants.Fonts;

public class ConfigurationStylesRowHeader extends DefaultRowHeaderStyleConfiguration {


    public ConfigurationStylesRowHeader() {
        font = Fonts.HEADER_FONT;

        TextPainter txtPainter = new TextPainter(false, false);
        cellPainter = txtPainter;
    }
}
