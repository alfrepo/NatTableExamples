package de.example.nebula.assembled.nattable.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.cell.AlternatingRowConfigLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.config.DefaultRowStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle.LineStyleEnum;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.nebula.widgets.nattable.style.Style;

import de.example.nebula.assembled.nattable.constants.Colors;

public class ConfigurationStylesRowAndCell extends DefaultRowStyleConfiguration {

    public static final String COLUMN_LABELPREFIX = "COLUMN_LABEL_";
    public static final String DIRTY_CELL_LABEL = "DIRTY_CELL";
    public static final String DIRTY_CELL_INCOLUMN_LABELPREFIX = "DIRTY_CELL_INCOLUMN_";
    public static final String CELLOVERRIDER_LABELPREFIX = "CELLOVERRIDER_";
    public static final String REQUIRED_CELL_LABEL = "REQUIRED_CELL";

    // This is used as a Prefix for setting the label, which is an internal identifier for different
    // Table columns.
    public static final String DIRTY_ROW_LABEL = "DIRTY_ROW";
    public static final String CONFLICT_ROW_LABEL = "CONFLICT_ROW";
    public static final String DELETED_ROW_LABEL = "DELETED_ROW";
    public static final String INVALID_ROW_LABEL = "INVALID_ROW";
    public static final String UNASSOCIATED_ROW_LABEL_EVEN = "UNASSOCIATED_ROW_EVEN";
    public static final String UNASSOCIATED_ROW_LABEL_ODD = "UNASSOCIATED_ROW_ODD";

    /**
     * The priority in which the labels are added to the cells is important.
     * In NatTable system objects (Editor/Rederer/..) are associated with cell-labels. One cell may
     * have multiple
     * labels.
     * When looking for cell's Editor/Rederer/.. all cells labels are iterated.
     * For every label the system checks, whether an object of the right type is associated with the
     * label.
     * The labels-List is iterated from 0 to list.length.
     * The first system-object which is found - is accepted.
     * 
     * <p>
     * This means, that the ordering of labels in the list is important. The current list defines
     * the priority of the labels prefixes. The labels which have a prefix with a lower index - have
     * more priority.
     * </p>
     */
    public static final List<String> LABEL_PRIORITY = Arrays.asList(new String[] { CELLOVERRIDER_LABELPREFIX,
            DIRTY_CELL_INCOLUMN_LABELPREFIX });

    /**
     * Sorts labels according to {@link #LABEL_PRIORITY}
     * 
     * @param labels
     */
    public static final List<String> sort(List<String> labels) {
        List<String> result = new ArrayList<>();
        List<String> givenLabels = new ArrayList<>(labels);

        // first get the high prio labels
        for (String prioLabelPrefix : LABEL_PRIORITY) {
            Iterator<String> iter = givenLabels.iterator();
            while (iter.hasNext()) {
                String label = iter.next();
                if (label.startsWith(prioLabelPrefix)) {
                    result.add(label); // found a high prio label
                    iter.remove(); // remove from labels list
                }
            }
        }

        // now copy the rest of the labels, which are not listed in the priority list
        result.addAll(givenLabels);
        return result;
    }

    private static final BorderStyle REQUIRED_CELL_BORDER_STYLE = new BorderStyle(3, Colors.REQUIRED_CELL_BORDER_COLOR,
            LineStyleEnum.SOLID);

    public ConfigurationStylesRowAndCell(GridLayer gridLayer) {
        this.oddRowBgColor = Colors.ODD_ROW_COLOR;
        this.evenRowBgColor = Colors.EVEN_ROW_COLOR;

        // alternating row color accumulator
        gridLayer.setConfigLabelAccumulatorForRegion(GridRegion.BODY, new AlternatingRowConfigLabelAccumulator());
    }

    @Override
    public void configureRegistry(IConfigRegistry configRegistry) {
        super.configureRegistry(configRegistry);
        registerMyRowLabels(configRegistry);

    }

    // private
    private void registerMyRowLabels(IConfigRegistry configRegistry) {
        IStyle cellstyle = new Style();
        cellstyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.CENTER);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellstyle);

        Style dirtyRowStyle = new Style();
        dirtyRowStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, Colors.DIRTY_ROW_COLOR);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, dirtyRowStyle, DisplayMode.NORMAL,
                DIRTY_ROW_LABEL);

        Style conflictRowStyle = new Style();
        conflictRowStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, Colors.CONFLICT_ROW_COLOR);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, conflictRowStyle, DisplayMode.NORMAL,
                CONFLICT_ROW_LABEL);

        // TODO ALF optimal w?re hier durchgestrichener text oder zeile
        Style deletedRowStyle = new Style();
        deletedRowStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, Colors.CONFLICT_ROW_COLOR);
        deletedRowStyle.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, Colors.DELETED_ROW_FG_COLOR);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, deletedRowStyle, DisplayMode.NORMAL,
                DELETED_ROW_LABEL);

        Style invalidRowStyle = new Style();
        invalidRowStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, Colors.INVALID_ROW_BG_COLOR);
        invalidRowStyle.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, Colors.INVALID_ROW_FG_COLOR);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, invalidRowStyle, DisplayMode.NORMAL,
                INVALID_ROW_LABEL);

        Style dirtyCellStyle = new Style();
        dirtyCellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, Colors.DIRTY_CELL_COLOR);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, dirtyCellStyle, DisplayMode.NORMAL,
                DIRTY_CELL_LABEL);

        Style requiredCellStyle = new Style();
        requiredCellStyle.setAttributeValue(CellStyleAttributes.BORDER_STYLE, REQUIRED_CELL_BORDER_STYLE);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, requiredCellStyle, DisplayMode.NORMAL,
                REQUIRED_CELL_LABEL);

        Style unsassociatedRowEvenStyle = new Style();
        unsassociatedRowEvenStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR,
                Colors.EVEN_ROW_BG_COLOR_UNASSOCIATED);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, unsassociatedRowEvenStyle,
                DisplayMode.NORMAL, UNASSOCIATED_ROW_LABEL_EVEN);

        Style unsassociatedOddEvenStyle = new Style();
        unsassociatedOddEvenStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR,
                Colors.ODD_ROW_BG_COLOR_UNASSOCIATED);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, unsassociatedOddEvenStyle,
                DisplayMode.NORMAL, UNASSOCIATED_ROW_LABEL_ODD);
    }
}
