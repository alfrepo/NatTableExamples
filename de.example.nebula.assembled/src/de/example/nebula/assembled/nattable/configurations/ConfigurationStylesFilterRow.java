package de.example.nebula.assembled.nattable.configurations;

import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfiguration;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;

import de.example.nebula.assembled.nattable.constants.Colors;
import de.example.nebula.assembled.nattable.constants.IdsNattable;

public class ConfigurationStylesFilterRow implements IConfiguration {

    @Override
    public void configureLayer(ILayer layer) {

    }

    @Override
    public void configureRegistry(IConfigRegistry configRegistry) {
        // FILTER ROW - Shade the row to be slightly darker than the blue background.
        final Style rowStyle = new Style();
        rowStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, Colors.FILTER_ROW_COLOR);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, rowStyle, DisplayMode.NORMAL, GridRegion.FILTER_ROW);

    }

    @Override
    public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {

    }

}
