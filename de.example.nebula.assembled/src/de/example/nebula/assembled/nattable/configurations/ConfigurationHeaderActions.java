package de.example.nebula.assembled.nattable.configurations;

import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.sort.action.SortColumnAction;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.swt.SWT;

/**
 * Defines the user actions (Mouse clicks, HotKeys) on the table header
 *
 * @author alf
 *
 */
public class ConfigurationHeaderActions extends AbstractUiBindingConfiguration {

    public ConfigurationHeaderActions() {
    }

    @Override
    public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
        /*
         * on mouse-click with ctrl send a SortColumnAction which will issue a SortCommand.
         * a SortCommand switches the column into the next sort state
         */
        uiBindingRegistry.registerMouseDownBinding(new MouseEventMatcher(SWT.CTRL, GridRegion.COLUMN_HEADER,
                MouseEventMatcher.RIGHT_BUTTON), new SortColumnAction(true));
    }

}
