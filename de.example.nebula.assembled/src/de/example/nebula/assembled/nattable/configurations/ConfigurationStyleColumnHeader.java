package de.example.nebula.assembled.nattable.configurations;

import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.config.DefaultColumnHeaderStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.painter.cell.BackgroundPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.GradientBackgroundPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.LineBorderDecorator;
import org.eclipse.nebula.widgets.nattable.sort.painter.SortableHeaderTextPainter;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle.LineStyleEnum;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.style.VerticalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import de.example.nebula.assembled.nattable.constants.Colors;
import de.example.nebula.assembled.nattable.constants.Constants;


/**
 * Extends the default column header style configuration to add custom painters for the column
 * headers.
 * This has to be added to the table using the addConfiguration() method.
 *
 * @see _000_Styled_grid
 */
public class ConfigurationStyleColumnHeader extends AbstractRegistryConfiguration {

	private static final Font FONT = GUIHelper.getFont(new FontData("Verdana", 10, SWT.BOLD));
    
    // bg
    private static final Color DEFAULT_HEADER_BACKGROUND_COLOR = Colors.HEADER_DEFAULT_BACKGROUND_COLOR;
    private static final Color DEFAULT_HEADER_FOREGROUND_COLOR = Colors.HEADER_DEFAULT_FOREGROUND_COLOR;
    private static final Color DEFAULT_HEADER_BACKGROUND_GRADIENT_COLOR = Colors.HEADER_DEFAULT_BACKGROUND_GRADIENT_COLOR;
    private static final Color DEFAULT_HEADER_FOREGROUND_GRADIENT_COLOR = Colors.HEADER_DEFAULT_FOREGROUND_GRADIENT_COLOR;

    private static final Color SELECTION_HEADER_BACKGROUND_COLOR = Colors.HEADER_SELECTION_BACKGROUND_COLOR;
	private static final Color SELECTION_HEADER_FOREGROUND_COLOR = Colors.HEADER_SELECTION_FOREGROUND_COLOR;
	private static final Color SELECTION_HEADER_BACKGROUND_GRADIENT_COLOR = Colors.HEADER_SELECTION_BACKGROUND_GRADIENT_COLOR;
	private static final Color SELECTION_HEADER_FOREGROUND_GRADIENT_COLOR = Colors.HEADER_SELECTION_FOREGROUND_GRADIENT_COLOR;
    
    // border
    private static final Color DEFAULT_HEADER_LINE_COLOR = Colors.HEADER_DEFAULT_LINE_COLOR;
    private static final int DEFAULT_HEADER_LINE_THICKNESS = Constants.HEADER_LINE_THICKNESS; 
    private static final LineStyleEnum DEFAULT_HEADER_LINE_STYLE = Constants.HEADER_DEFAULT_LINE_STYLE;
    private static final HorizontalAlignmentEnum DEFAULT_HORIZONTAL_ALIGNMENT = Constants.HEADER_HORIZONTAL_ALIGNMENT;
	private static final VerticalAlignmentEnum DEFAULT_VERTICAL_ALIGNMENT = Constants.HEADER_VERTICAL_ALIGNMENT;



    		
    public ConfigurationStyleColumnHeader() {
    }

    @Override
    public void configureRegistry(IConfigRegistry configRegistry) {
        addNormalModeStyling(configRegistry);
        addNormalModePainter(configRegistry);
        
        addSelectedModeStyling(configRegistry);
    }

    // CELL_MODE NORMAL 
    private void addNormalModeStyling(IConfigRegistry configRegistry) {
    	Style cellStyle = new Style();
    	
    	// BG
    	cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, DEFAULT_HEADER_BACKGROUND_COLOR);

    	// FOREGROUND_COLOR
    	cellStyle.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, DEFAULT_HEADER_FOREGROUND_COLOR);

    	// GRADIENT BG
    	cellStyle.setAttributeValue(CellStyleAttributes.GRADIENT_BACKGROUND_COLOR, DEFAULT_HEADER_BACKGROUND_GRADIENT_COLOR);
    	
    	// GRADIENT FG
    	cellStyle.setAttributeValue(CellStyleAttributes.GRADIENT_FOREGROUND_COLOR, DEFAULT_HEADER_FOREGROUND_GRADIENT_COLOR);
    	
    	// horizontal alignment
    	cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, DEFAULT_HORIZONTAL_ALIGNMENT);
    	
    	// vertical alignment
    	cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, DEFAULT_VERTICAL_ALIGNMENT);
    	
    	// font
    	cellStyle.setAttributeValue(CellStyleAttributes.FONT, FONT);
    	
    	// Border
    	BorderStyle borderStyle = new BorderStyle();
    	borderStyle.setColor(DEFAULT_HEADER_LINE_COLOR);
    	borderStyle.setThickness(DEFAULT_HEADER_LINE_THICKNESS);
    	borderStyle.setLineStyle(DEFAULT_HEADER_LINE_STYLE);
    	cellStyle.setAttributeValue(CellStyleAttributes.BORDER_STYLE, borderStyle);
    	
        
    	configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, GridRegion.COLUMN_HEADER);
	}
    
    private void addNormalModePainter(IConfigRegistry configRegistry) {
        ICellPainter cellPainter = getStyledHeaderCellPainter( Constants.TABLE_PADDING_COLUMNHEADER);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, cellPainter, DisplayMode.NORMAL, GridRegion.COLUMN_HEADER);
    }
    
    // CELL_MODE SELECTED 
    
    private void addSelectedModeStyling(IConfigRegistry configRegistry) {
    	Style cellStyle = new Style();
        
    	// BG
    	cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, SELECTION_HEADER_BACKGROUND_COLOR);

    	// FOREGROUND_COLOR
    	cellStyle.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, SELECTION_HEADER_FOREGROUND_COLOR);

    	// GRADIENT BG
    	cellStyle.setAttributeValue(CellStyleAttributes.GRADIENT_BACKGROUND_COLOR, SELECTION_HEADER_BACKGROUND_GRADIENT_COLOR);
    	
    	// GRADIENT FG
    	cellStyle.setAttributeValue(CellStyleAttributes.GRADIENT_FOREGROUND_COLOR, SELECTION_HEADER_FOREGROUND_GRADIENT_COLOR);
    	
    	// font
    	cellStyle.setAttributeValue(CellStyleAttributes.FONT, FONT);
    	
    	configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.SELECT, GridRegion.COLUMN_HEADER);
	}


    
    
    // HELPER

	private ICellPainter getStyledHeaderCellPainter(int padding) {
		
		// Text in headers. Padding.
		TextPainter textPainter = new TextPainter(false, false, padding, true, true);
		
		// BG
		ICellPainter bgPainter = new GradientBackgroundPainter(textPainter, true); // use solid color
//		ICellPainter bgPainter = new BackgroundPainter(textPainter); // use gradient
		
        // add the ability to sort
        SortableHeaderTextPainter sortableHeaderTextPainter = new SortableHeaderTextPainter(bgPainter, false, true);
        
        // Line Border
        LineBorderDecorator lineBorderDecorator = new LineBorderDecorator(sortableHeaderTextPainter);
        
        return lineBorderDecorator;
	}
}
