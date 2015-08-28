package de.example.nebula.assembled.nattable.configurations;

import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.SelectionStyleLabels;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import de.example.nebula.assembled.nattable.constants.Colors;
import de.example.nebula.assembled.nattable.constants.Constants;
import de.example.nebula.assembled.nattable.constants.Fonts;

public class ConfigurationStylesSelection extends AbstractRegistryConfiguration {

    ConfigurationStylesSelection() {
    }

    // Selection style
    public Font selectionFont = Fonts.HEADER_SELECTION_FONT; //$NON-NLS-1$
    public Color selectionBgColor = Colors.SELECTION_BACKGROUND_COLOR;
    public Color selectionFgColor = Colors.SELECTION_COLOR;

    // Anchor style
    public Color anchorBorderColor = Colors.ANCHOR_BORDER_COLOR;
    public BorderStyle anchorBorderStyle = Constants.ANCHOR_BORDER_STYLE;
    public Color anchorBgColor = Colors.ANCHOR_BACKGROUND_COLOR;
    public Color anchorFgColor = Colors.ANCHOR_FOREGROUND_COLOR;

    // Selected headers style
    public Color selectedHeaderBgColor = Colors.ANCHOR_HEADER_SELECTED_BACKGROUND_COLOR;
    public Color selectedHeaderFgColor = Colors.ANCHOR_HEADER_SELECTED_FOREGROUND_COLOR;
    public Font selectedHeaderFont = Fonts.HEADER_SELECTION_FONT;
    public BorderStyle selectedHeaderBorderStyle = Constants.ANCHOR_HEADER_SELECTION_BORDER_STYLE;

    public Color fullySelectedHeaderBgColor = Colors.ANCHOR_SELECTED_FULLY_BACKGROUND_COLOR;
    

    // Anchor grid line style
    public Color anchorGridBorderColor = Colors.ANCHOR_GRID_BORDER_COLOR;
    public BorderStyle anchorGridBorderStyle = Constants.ANCHOR_BORDER_STYLE;
    

    @Override
    public void configureRegistry(IConfigRegistry configRegistry) {
        configureSelectionStyle(configRegistry);
        configureSelectionAnchorStyle(configRegistry);
        configureSelectionAnchorGridLineStyle(configRegistry);
        configureHeaderHasSelectionStyle(configRegistry);
        configureHeaderFullySelectedStyle(configRegistry);
    }

    protected void configureSelectionStyle(IConfigRegistry configRegistry) {
        Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.FONT, this.selectionFont);
        cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, this.selectionBgColor);
        cellStyle.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, this.selectionFgColor);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.SELECT);
    }

    protected void configureSelectionAnchorStyle(IConfigRegistry configRegistry) {
        // Selection anchor style for normal display mode
        Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.BORDER_STYLE, this.anchorBorderStyle);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, SelectionStyleLabels.SELECTION_ANCHOR_STYLE);

        // Selection anchor style for select display mode
        cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR,  this.anchorBgColor);
        cellStyle.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, this.anchorFgColor);
        cellStyle.setAttributeValue(CellStyleAttributes.BORDER_STYLE, this.anchorBorderStyle);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE,  cellStyle, DisplayMode.SELECT,  SelectionStyleLabels.SELECTION_ANCHOR_STYLE);
    }

    protected void configureSelectionAnchorGridLineStyle(
            IConfigRegistry configRegistry) {
        Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.BORDER_STYLE, this.anchorGridBorderStyle);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.SELECT, SelectionStyleLabels.SELECTION_ANCHOR_GRID_LINE_STYLE);
    }

    protected void configureHeaderHasSelectionStyle(
            IConfigRegistry configRegistry) {
        Style cellStyle = new Style();

        cellStyle.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, this.selectedHeaderFgColor);
        cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR,  this.selectedHeaderBgColor);
        cellStyle.setAttributeValue(CellStyleAttributes.FONT, this.selectedHeaderFont);
        cellStyle.setAttributeValue(CellStyleAttributes.BORDER_STYLE, this.selectedHeaderBorderStyle);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.SELECT, GridRegion.CORNER);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.SELECT, GridRegion.ROW_HEADER);
    }

    protected void configureHeaderFullySelectedStyle(
            IConfigRegistry configRegistry) {
        // Header fully selected
        Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, this.fullySelectedHeaderBgColor);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.SELECT, SelectionStyleLabels.COLUMN_FULLY_SELECTED_STYLE);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE,cellStyle, DisplayMode.SELECT, SelectionStyleLabels.ROW_FULLY_SELECTED_STYLE);
    }
    
    
    
}
