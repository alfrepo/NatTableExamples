package de.example.nebula.assembled.nattable.configurations;

import org.eclipse.nebula.widgets.nattable.config.AggregateConfiguration;
import org.eclipse.nebula.widgets.nattable.edit.action.KeyEditAction;
import org.eclipse.nebula.widgets.nattable.edit.action.MouseEditAction;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditConfiguration;
import org.eclipse.nebula.widgets.nattable.edit.editor.TextCellEditor;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.BodyCellEditorMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.CellEditorMouseEventMatcher;
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

    protected final void addEditingUIConfig() {
        addConfiguration(new CustomEditBindings());
    }

    protected final void addEditingHandlerConfig() {
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

            // every default NatTable cell Editor implements ICellEditor.class as do all the self
            // implemented classes in the system
            uiBindingRegistry.registerFirstDoubleClickBinding(
                    new BodyCellEditorMouseEventMatcher(TextCellEditor.class), new MouseEditAction());

            // trigger cell editing, on single-click, when ComboBoxEditorDisplayConverted is
            // registered for the cell. Register abstract classes here, from which concrete classes
            // will inherit
            uiBindingRegistry.registerFirstSingleClickBinding(new CellEditorMouseEventMatcher(null, MouseEventMatcher.LEFT_BUTTON), new MouseEditAction());

//            uiBindingRegistry.registerFirstSingleClickBinding(new BodyCellEditorMouseEventMatcher(
//                    CheckboxEditorDisplayConverted.class), new MouseEditAction());

        }
    }
}
