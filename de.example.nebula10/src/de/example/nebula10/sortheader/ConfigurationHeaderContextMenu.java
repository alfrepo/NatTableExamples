package de.example.nebula10.sortheader;

import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.KeySequenceText;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.bindings.keys.formatting.IKeyFormatter;
import org.eclipse.jface.bindings.keys.formatting.KeyFormatterFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.command.ColumnHideCommand;
import org.eclipse.nebula.widgets.nattable.hideshow.command.ColumnHideCommandHandler;
import org.eclipse.nebula.widgets.nattable.sort.ISortModel;
import org.eclipse.nebula.widgets.nattable.sort.SortDirectionEnum;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.menu.AbstractHeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.IMenuItemProvider;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuAction;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Creates a context menu for table columns.
 * Associates the standard commands with the menu.
 * The handlers of the commands are usually located in the Layers.
 * E.g. {@link ColumnHideCommand} is handled by {@link ColumnHideCommandHandler} in
 * {@link ColumnHideShowLayer}
 *
 * @see {@link HeaderMenuConfiguration} {@link AbstractHeaderMenuConfiguration}
 *
 * @author alf
 *
 */
public class ConfigurationHeaderContextMenu extends AbstractUiBindingConfiguration {

    private Menu colHeaderMenu;
    private Menu rowHeaderMenu;
    private Menu cornerMenu;

    String label;

    public ConfigurationHeaderContextMenu(NatTable natTable) {

        this.colHeaderMenu = createColumnHeaderMenu(natTable).build();
        this.rowHeaderMenu = createRowHeaderMenu(natTable).build();
        this.cornerMenu = createCornerMenu(natTable).build();

        // ensure that the menus will be disposed when the NatTable is disposed
        natTable.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                if (colHeaderMenu != null) {
                    colHeaderMenu.dispose();
                }

                if (rowHeaderMenu != null) {
                    rowHeaderMenu.dispose();
                }

                if (cornerMenu != null) {
                    cornerMenu.dispose();
                }
            }

        });

    }

    /**
     * Creates the {@link PopupMenuBuilder} for the column header menu with the menu
     * items that should be added to the menu.
     *
     * @param natTable
     *            The NatTable where the menu should be attached.
     * @return The {@link PopupMenuBuilder} that is used to build the column
     *         header menu.
     */
    protected PopupMenuBuilder createColumnHeaderMenu(NatTable natTable) {
        return new PopupMenuBuilder(natTable).withMenuItemProvider(getMenuItemSort());
    }

    private IMenuItemProvider getMenuItemSort() {
        return new IMenuItemProvider() {

            @Override
            public void addMenuItem(final NatTable natTable, Menu popupMenu) {

                /*
                 * TODO alf
                 * at this place we do not receive the concrete column - so we only can introduce a
                 * general menu for all columns.
                 * 
                 * That means we can not display sorting state of the current column,
                 * we can not disable the menu, if sorting is not allowed on this column.
                 * 
                 * Introduces a mechanism which would allow to react on the state current sorting
                 * state.
                 */

                // TODO alf translate

                Image iconUp = GUIHelper.getImage("up_0");
                Image iconDown = GUIHelper.getImage("down_0");

//                // group
//                MenuItem sortMenuGroupItem = new MenuItem(popupMenu, SWT.CASCADE);
//                sortMenuGroupItem.setText("Sortierung");
//                sortMenuGroupItem.setImage(iconDown);
//
//                sortMenuGroupItem.setEnabled(true);
//                Menu sortMenuGroup = new Menu(sortMenuGroupItem);
//                sortMenuGroupItem.setMenu(sortMenuGroup);
                
                Menu sortMenuGroup = popupMenu;

                // Item sort none
                MenuItem sortMenuItemNo = new MenuItem(sortMenuGroup, SWT.PUSH);
                
                IKeyFormatter formatter = SWTKeySupport.getKeyFormatterForPlatform();
                formatter.format(SWT.CTRL); // Ctrl
                
                
                sortMenuItemNo.setText("Search\t "+formatter.format(SWT.CTRL));
                sortMenuItemNo.setAccelerator(SWT.MOD2 | 'T');
                
                sortMenuItemNo.addSelectionListener(getSelectionAdapterSorting(SortDirectionEnum.NONE, 
                        natTable));

                // item sort asc
                MenuItem sortMenuItemAsc = new MenuItem(sortMenuGroup, SWT.PUSH);
                sortMenuItemAsc.setText("Aufsteigend");
                sortMenuItemAsc.setImage(iconUp);
                sortMenuItemAsc.setAccelerator(SWT.ALT + 'A');
                sortMenuItemAsc.addSelectionListener(getSelectionAdapterSorting(SortDirectionEnum.ASC, 
                        natTable));

                // Item sort desc
                MenuItem sortMenuItemDesc = new MenuItem(sortMenuGroup, SWT.PUSH);
                sortMenuItemDesc.setText("Absteigend");
                sortMenuItemDesc.setImage(iconDown);
                sortMenuItemDesc.setAccelerator(SWT.CTRL + 'A');
                sortMenuItemDesc.addSelectionListener(getSelectionAdapterSorting(SortDirectionEnum.DESC, 
                        natTable));

            }
        };
    }

    /**
     * Creates the {@link PopupMenuBuilder} for the row header menu with the menu
     * items that should be added to the menu.
     *
     * @param natTable
     *            The NatTable where the menu should be attached.
     * @return The {@link PopupMenuBuilder} that is used to build the row
     *         header menu.
     */
    protected PopupMenuBuilder createRowHeaderMenu(NatTable natTable) {
        return new PopupMenuBuilder(natTable);
    }

    /**
     * Creates the {@link PopupMenuBuilder} for the corner menu with the menu
     * items that should be added to the menu.
     *
     * @param natTable
     *            The NatTable where the menu should be attached.
     * @return The {@link PopupMenuBuilder} that is used to build the corner menu.
     */
    protected PopupMenuBuilder createCornerMenu(NatTable natTable) {
        return new PopupMenuBuilder(natTable);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.nebula.widgets.nattable.config.IConfiguration#configureUiBindings(org.eclipse
     * .nebula.widgets.nattable.ui.binding.UiBindingRegistry)
     */
    @Override
    public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
        if (this.colHeaderMenu != null) {
            uiBindingRegistry.registerMouseDownBinding(new MouseEventMatcher(SWT.NONE, GridRegion.COLUMN_HEADER,
                    MouseEventMatcher.RIGHT_BUTTON), new PopupMenuAction(this.colHeaderMenu));
        }

        if (this.rowHeaderMenu != null) {
            uiBindingRegistry.registerMouseDownBinding(new MouseEventMatcher(SWT.NONE, GridRegion.ROW_HEADER,
                    MouseEventMatcher.RIGHT_BUTTON), new PopupMenuAction(this.rowHeaderMenu));
        }

        if (this.cornerMenu != null) {
            uiBindingRegistry.registerMouseDownBinding(new MouseEventMatcher(SWT.NONE, GridRegion.CORNER,
                    MouseEventMatcher.RIGHT_BUTTON), new PopupMenuAction(this.cornerMenu));
        }
    }

    // HELPER

    private SelectionAdapter getSelectionAdapterSorting(final SortDirectionEnum sortDirectionEnum, final NatTable natTable) {
        return new SelectionAdapter() {

            @SuppressWarnings("unused")
            @Override
            public void widgetSelected(SelectionEvent e) {
            	System.out.println("Slected");
            }

        };
    }

}
