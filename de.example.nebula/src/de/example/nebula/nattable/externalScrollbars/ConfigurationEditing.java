package de.example.nebula.nattable.externalScrollbars;

import org.eclipse.nebula.widgets.nattable.config.AggregateConfiguration;
import org.eclipse.nebula.widgets.nattable.edit.action.CellEditDragMode;
import org.eclipse.nebula.widgets.nattable.edit.action.KeyEditAction;
import org.eclipse.nebula.widgets.nattable.edit.action.MouseEditAction;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditConfiguration;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.TextCellEditor;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.BodyCellEditorMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.CellPainterMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.KeyEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.LetterOrDigitKeyEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.swt.SWT;

/**
 * This Object is responsible for table configuration, like editing binding (double-click or
 * single-click)
 * 
 * @author alf
 * 
 */
public class ConfigurationEditing extends AggregateConfiguration {

    public ConfigurationEditing() {
        addEditingHandlerConfig();
        addEditingUIConfig();
    }

    protected void addEditingUIConfig() {
        addConfiguration(new CustomEditBindings());
    }

    protected void addEditingHandlerConfig() {
        addConfiguration(new DefaultEditConfiguration());
    }

    /**
     * Used DefaultEditBindings.class as template. Choose the bindings for Edit actions inside Table
     * Cells.
     * 
     * @author alf
     * 
     */
    private class CustomEditBindings extends AggregateConfiguration {
        @Override
        public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
            uiBindingRegistry.registerKeyBinding(new KeyEventMatcher(SWT.NONE, SWT.F2), new KeyEditAction());
            uiBindingRegistry.registerKeyBinding(new LetterOrDigitKeyEventMatcher(), new KeyEditAction());

            uiBindingRegistry.registerFirstDoubleClickBinding(
                    new BodyCellEditorMouseEventMatcher(TextCellEditor.class), new MouseEditAction());

            uiBindingRegistry.registerFirstMouseDragMode(new BodyCellEditorMouseEventMatcher(TextCellEditor.class),
                    new CellEditDragMode());

            uiBindingRegistry.registerFirstDoubleClickBinding(new CellPainterMouseEventMatcher(GridRegion.BODY,
                    MouseEventMatcher.LEFT_BUTTON, CheckBoxPainter.class), new MouseEditAction());

            uiBindingRegistry.registerFirstMouseDragMode(new CellPainterMouseEventMatcher(GridRegion.BODY,
                    MouseEventMatcher.LEFT_BUTTON, CheckBoxPainter.class), new CellEditDragMode());

            uiBindingRegistry.registerFirstDoubleClickBinding(new BodyCellEditorMouseEventMatcher(
                    ComboBoxCellEditor.class), new MouseEditAction());

            uiBindingRegistry.registerFirstMouseDragMode(new BodyCellEditorMouseEventMatcher(ComboBoxCellEditor.class),
                    new CellEditDragMode());
        }

    }
}