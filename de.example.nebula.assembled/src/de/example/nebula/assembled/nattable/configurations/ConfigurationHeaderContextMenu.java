package de.example.nebula.assembled.nattable.configurations;

import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.bindings.keys.formatting.IKeyFormatter;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.command.ColumnHideCommand;
import org.eclipse.nebula.widgets.nattable.hideshow.command.ColumnHideCommandHandler;
import org.eclipse.nebula.widgets.nattable.sort.ISortModel;
import org.eclipse.nebula.widgets.nattable.sort.SortDirectionEnum;
import org.eclipse.nebula.widgets.nattable.sort.command.SortColumnCommand;
import org.eclipse.nebula.widgets.nattable.ui.NatEventData;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.menu.AbstractHeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.IMenuItemProvider;
import org.eclipse.nebula.widgets.nattable.ui.menu.MenuItemProviders;
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

	public static final String LABEL_SORTMENU = "Sort"; //$NON-NLS-1$
	public static final String LABEL_ASC = "Asc"; //$NON-NLS-1$
	public static final String LABEL_DESC = "Desc"; //$NON-NLS-1$
	public static final String LABEL_NONE = "None"; //$NON-NLS-1$
	public static final String LABEL_HIDE_COL = "Hide column"; //$NON-NLS-1$
	public static final String LABEL_CLEARALLFILTERS = "Clean all filters"; //$NON-NLS-1$
	public static final String LABEL_SHOWALLCOLS = "Show all columns"; //$NON-NLS-1$
	public static final String LABEL_AUTORESIZE = "Auto resize columns"; //$NON-NLS-1$
	public static final String LABEL_HOTKEY_SEARCH = "Right mouse click";
	
	
    IKeyFormatter formatter = SWTKeySupport.getKeyFormatterForPlatform();
    String s = formatter.format(SWT.CTRL); // Ctrl

    public static final String MENU_TAB_CHAR = "\t"; //$NON-NLS-1$
    public static final String PLUS_CHAR = "+"; //$NON-NLS-1$
    public static final String HOTKEY_CTRL = SWTKeySupport.getKeyFormatterForPlatform().format(SWT.CTRL);
    

    private Menu colHeaderMenu;
    private Menu rowHeaderMenu;
    private Menu cornerMenu;

    String label;
    private final ISortModel sortModel;

    public ConfigurationHeaderContextMenu(NatTable natTable, ISortModel sortModel) {
        this.sortModel = sortModel;

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
        return new PopupMenuBuilder(natTable).withHideColumnMenuItem(LABEL_HIDE_COL)
                .withShowAllColumnsMenuItem(LABEL_HIDE_COL)
                .withAutoResizeSelectedColumnsMenuItem(LABEL_AUTORESIZE)
                .withClearAllFilters(LABEL_CLEARALLFILTERS)
                .withMenuItemProvider(getMenuItemSort());
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

                Image iconUp = GUIHelper.getImage("up_0"); //$NON-NLS-1$
                Image iconDown = GUIHelper.getImage("down_0"); //$NON-NLS-1$

                // group
                MenuItem sortMenuGroupItem = new MenuItem(popupMenu, SWT.CASCADE);
                /** the hotkey is defined in ConfigurationHeaderActions */
                sortMenuGroupItem.setText(LABEL_SORTMENU + MENU_TAB_CHAR + HOTKEY_CTRL
                        + PLUS_CHAR + LABEL_HOTKEY_SEARCH);
                sortMenuGroupItem.setImage(iconDown);

                sortMenuGroupItem.setEnabled(true);
                Menu sortMenuGroup = new Menu(sortMenuGroupItem);
                sortMenuGroupItem.setMenu(sortMenuGroup);

                // Item sort none
                MenuItem sortMenuItemNo = new MenuItem(sortMenuGroup, SWT.PUSH);
                sortMenuItemNo.setText(LABEL_NONE);
                sortMenuItemNo
                        .addSelectionListener(getSelectionAdapterSorting(SortDirectionEnum.NONE, sortModel, natTable));

                // item sort asc
                MenuItem sortMenuItemAsc = new MenuItem(sortMenuGroup, SWT.PUSH);
                sortMenuItemAsc.setText(LABEL_ASC);
                sortMenuItemAsc.setImage(iconUp);
                sortMenuItemAsc
                        .addSelectionListener(getSelectionAdapterSorting(SortDirectionEnum.ASC, sortModel, natTable));

                // Item sort desc
                MenuItem sortMenuItemDesc = new MenuItem(sortMenuGroup, SWT.PUSH);
                sortMenuItemDesc.setText(LABEL_DESC);
                sortMenuItemDesc.setImage(iconDown);
                sortMenuItemDesc
                        .addSelectionListener(getSelectionAdapterSorting(SortDirectionEnum.DESC, sortModel, natTable));

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
            uiBindingRegistry.registerMouseDownBinding(
                    new MouseEventMatcher(SWT.NONE, GridRegion.COLUMN_HEADER, MouseEventMatcher.RIGHT_BUTTON),
                    new PopupMenuAction(this.colHeaderMenu));
        }

        if (this.rowHeaderMenu != null) {
            uiBindingRegistry.registerMouseDownBinding(
                    new MouseEventMatcher(SWT.NONE, GridRegion.ROW_HEADER, MouseEventMatcher.RIGHT_BUTTON),
                    new PopupMenuAction(this.rowHeaderMenu));
        }

        if (this.cornerMenu != null) {
            uiBindingRegistry.registerMouseDownBinding(
                    new MouseEventMatcher(SWT.NONE, GridRegion.CORNER, MouseEventMatcher.RIGHT_BUTTON),
                    new PopupMenuAction(this.cornerMenu));
        }
    }

    // HELPER

    private SelectionAdapter getSelectionAdapterSorting(final SortDirectionEnum sortDirectionEnum,
            final ISortModel sortModel, final NatTable natTable) {
        return new SelectionAdapter() {

            @SuppressWarnings("unused")
            @Override
            public void widgetSelected(SelectionEvent e) {
                NatEventData natEventData = MenuItemProviders.getNatEventData(e);
                int columnPosition = natEventData.getColumnPosition();
                int columnIndex = natTable.getColumnIndexByPosition(columnPosition);

                // switch model into the predescessor state. SortColumnCommand will siwtch it into
                // the next state
                sortModel.sort(columnIndex, SortDirectionEnum.ASC, false);

                // send a command
                // the command will switch the model into the next sorting-state
                natTable.doCommand(new SortColumnCommand(natTable, columnPosition, false));
            }

        };
    }

}
