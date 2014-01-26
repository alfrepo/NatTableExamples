package de.example.nebula10;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.convert.IDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;

public class TableObjectsDebugTrace {

    private static final String PARAMETER_FOR_DEBUG_TRACE_IN_NATTABLE = "-nattabletrace";

    // init
    boolean showCoordinates = true;
    boolean showLabels = true;
    boolean showCellPainter = true;
    boolean showCellEditor = true;
    boolean showDisplayConverter = true;

    private NatTable natTable;
    private IConfigRegistry configRegistry;

    public TableObjectsDebugTrace(NatTable nattable) {
        this.natTable = nattable;
        this.configRegistry = nattable.getConfigRegistry();

        // check for my custom runtime configuration parameter
        List<String> parameters = Arrays.asList(Platform.getCommandLineArgs());

//        if (parameters.contains(PARAMETER_FOR_DEBUG_TRACE_IN_NATTABLE)) {
            nattable.addMouseMoveListener(getDebugTraceMouseListener());
//        }
    }

    private MouseMoveListener getDebugTraceMouseListener() {

        MouseMoveListener listener = new MouseMoveListener() {

            @Override
            public void mouseMove(MouseEvent event) {

                int row = natTable.getRowPositionByY(event.y);
                int col = natTable.getColumnPositionByX(event.x);
                ILayerCell cellTemp = natTable.getCellByPosition(col, row);

                if (cellTemp == null) {
                    // we are pointing to an empty space, ther is no cell
                    return;
                }

                LabelStack labelStack = cellTemp.getConfigLabels();

                if (labelStack == null) {
                    return;
                }
                List<String> listLabels = labelStack.getLabels();

                ICellPainter cellPainter = configRegistry.getConfigAttribute(CellConfigAttributes.CELL_PAINTER,
                        DisplayMode.NORMAL, listLabels);
                ICellEditor cellEditor = configRegistry.getConfigAttribute(EditConfigAttributes.CELL_EDITOR,
                        DisplayMode.EDIT, listLabels);

                IDisplayConverter displayConverterNorm = configRegistry.getConfigAttribute(
                        CellConfigAttributes.DISPLAY_CONVERTER, DisplayMode.NORMAL, listLabels);
                IDisplayConverter displayConverterEdit = configRegistry.getConfigAttribute(
                        CellConfigAttributes.DISPLAY_CONVERTER, DisplayMode.EDIT, listLabels);

                StringBuilder stringBuilder = new StringBuilder();

                if (showCoordinates) {
                    stringBuilder.append("PosRow:" + row + " PosCol:" + col);
                    stringBuilder.append("  ");
                }

                if (showLabels) {
                    stringBuilder.append("Labels:" + listLabels);
                    stringBuilder.append("  ");
                }

                if (showCellPainter) {
                    stringBuilder.append("Painter:" + cellPainter.getClass().getSimpleName());
                    stringBuilder.append("  ");
                }

                if (showCellEditor) {
                    stringBuilder.append("Editor:" + cellEditor.getClass().getSimpleName());
                    stringBuilder.append("  ");
                }

                if (showDisplayConverter) {
                    stringBuilder.append("DisplayConverter::");
                    stringBuilder.append("DMode=NORMAL:" + displayConverterNorm.getClass().getSimpleName());
                    stringBuilder.append(" ");
                    stringBuilder.append("DMode=EDIT:" + displayConverterEdit.getClass().getSimpleName());
                    stringBuilder.append("  ");
                }

                // echo result
                System.out.println(stringBuilder.toString());
            }

        };

        return listener;
    }
}
