package de.example.nebula.assembled.nattable.configurations;

import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.config.DefaultColumnHeaderStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.TableCellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.sort.painter.SortableHeaderTextPainter;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import de.example.nebula.assembled.nattable.constants.Constants;


/**
 * Extends the default column header style configuration to add custom painters for the column
 * headers.
 * This has to be added to the table using the addConfiguration() method.
 *
 * @see _000_Styled_grid
 */
public class ConfigurationStyledColumnHeader extends DefaultColumnHeaderStyleConfiguration {

    private static final Font FONT = GUIHelper.getFont(new FontData("Verdana", 10, SWT.BOLD));
    private static final Color BG_COLOR = GUIHelper.getColor(192, 192, 192);

    public ConfigurationStyledColumnHeader() {
        font = FONT;
    }

    @Override
    public void configureRegistry(IConfigRegistry configRegistry) {
        super.configureRegistry(configRegistry);
        addNormalModeStyling(configRegistry);
        addSelectedModeStyling(configRegistry);
    }

    private void addSelectedModeStyling(IConfigRegistry configRegistry) {

        TextPainter txtPainter = new TextPainter(false, false);
        ICellPainter selectedCellPainter = new TableCellPainter(txtPainter, BG_COLOR, BG_COLOR, 20, true);
        
        // If sorting is enables we still want the sort icon to be drawn.
        SortableHeaderTextPainter selectedHeaderPainter = new SortableHeaderTextPainter(selectedCellPainter, false,
                true);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, selectedHeaderPainter,
                DisplayMode.SELECT, GridRegion.COLUMN_HEADER);
    }

    private void addNormalModeStyling(IConfigRegistry configRegistry) {

        // Painter of headers
        // + Padding
        // + make cell as high/wide as content
        TextPainter txtPainter = new TextPainter(false, false, Constants.TABLE_PADDING_COLUMNHEADER, false, true);

        // Header BG
        ICellPainter bgImagePainter = new TableCellPainter(txtPainter, BG_COLOR, BG_COLOR, 20, true);

        SortableHeaderTextPainter sortableHeaderTextPainter = new SortableHeaderTextPainter(bgImagePainter, false, true);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, sortableHeaderTextPainter,
                DisplayMode.NORMAL, GridRegion.COLUMN_HEADER);

        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, txtPainter,
                DisplayMode.NORMAL, GridRegion.CORNER);

    }
}
