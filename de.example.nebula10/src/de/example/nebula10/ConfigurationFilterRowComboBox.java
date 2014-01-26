package de.example.nebula10;

import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultComparator;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.action.MouseEditAction;
import org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.IComboBoxDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.editor.TextCellEditor;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowPainter;
import org.eclipse.nebula.widgets.nattable.filterrow.TextMatchingMode;
import org.eclipse.nebula.widgets.nattable.filterrow.action.ClearFilterAction;
import org.eclipse.nebula.widgets.nattable.filterrow.action.ToggleFilterRowAction;
import org.eclipse.nebula.widgets.nattable.filterrow.combobox.ComboBoxFilterIconPainter;
import org.eclipse.nebula.widgets.nattable.filterrow.combobox.FilterRowComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.filterrow.config.FilterRowConfigAttributes;
import org.eclipse.nebula.widgets.nattable.filterrow.event.ClearFilterIconMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.filterrow.event.FilterRowMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ImagePainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.CellPainterDecorator;
import org.eclipse.nebula.widgets.nattable.sort.painter.SortableHeaderTextPainter;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.KeyEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.util.CellEdgeEnum;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import static org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes.CELL_PAINTER;
import static org.eclipse.nebula.widgets.nattable.config.IEditableRule.ALWAYS_EDITABLE;
import static org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes.CELL_EDITABLE_RULE;
import static org.eclipse.nebula.widgets.nattable.filterrow.config.FilterRowConfigAttributes.FILTER_COMPARATOR;
import static org.eclipse.nebula.widgets.nattable.filterrow.config.FilterRowConfigAttributes.FILTER_DISPLAY_CONVERTER;
import static org.eclipse.nebula.widgets.nattable.filterrow.config.FilterRowConfigAttributes.TEXT_MATCHING_MODE;
import static org.eclipse.nebula.widgets.nattable.style.DisplayMode.NORMAL;

public class ConfigurationFilterRowComboBox extends
		AbstractRegistryConfiguration {

	private String LABEL_FILTERS;

	/**
	 * The ICellEditor that should be used for the filter cells. Usually it
	 * should be the FilterRowComboBoxCellEditor.
	 */
	protected ICellEditor cellEditor;

	/**
	 * The ImagePainter that will be registered as the painter of the combobox
	 * cells in the filter row.
	 */
	protected ImagePainter filterIconPainter;

	/**
	 * The empty default constructor needed for specialising.
	 * <p>
	 * <b>Note: On using this constructor you need to ensure that the local
	 * member variables for <i>cellEditor</i> and <i>filterIconPainter</i> need
	 * to be set manually. Otherwise this configuration will not work
	 * correctly!</b>
	 */
	public ConfigurationFilterRowComboBox() {
	}

	/**
	 * Create a ConfigurationFilterRow that uses the default
	 * FilterRowComboBoxCellEditor showing the maximum number of 10 items at
	 * once and the ComboBoxFilterIconPainter with the default filter icon.
	 * 
	 * @param comboBoxDataProvider
	 *            The IComboBoxDataProvider that is used to fill the filter row
	 *            comboboxes.
	 */
	public ConfigurationFilterRowComboBox(
			IComboBoxDataProvider comboBoxDataProvider) {
		// this.cellEditor = new
		// FilterRowComboBoxCellEditor(comboBoxDataProvider, 10);
		this.cellEditor = new TextCellEditor();
		// this.filterIconPainter = new
		// ComboBoxFilterIconPainter(comboBoxDataProvider);

		// LABEL_FILTERS = Table.LABEL_FILTERS;
		LABEL_FILTERS = GridRegion.FILTER_ROW;
	}

	@Override
	public void configureRegistry(IConfigRegistry configRegistry) {
		// configureCopamboBoxFilterRowHeaderComposite(configRegistry);
		// configureInputFields(configRegistry);
	}

	@Override
	public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
		// configureUiBindingsInputFields(uiBindingRegistry);
	}

	// PRIVATE

	private void configureComboBoxFilterRowHeaderComposite(
			IConfigRegistry configRegistry) {
		configRegistry.registerConfigAttribute(
				EditConfigAttributes.CELL_EDITOR, this.cellEditor,
				DisplayMode.NORMAL, LABEL_FILTERS);

		configRegistry.registerConfigAttribute(
				FilterRowConfigAttributes.TEXT_MATCHING_MODE,
				TextMatchingMode.REGULAR_EXPRESSION);

		// ICellPainter cellPainter = new CellPainterDecorator(
		// new TextPainter() {
		// {
		// this.paintFg = false;
		// }
		// },
		// CellEdgeEnum.RIGHT,
		// this.filterIconPainter);

		// ICellPainter cellPainter = new TextPainter() {
		// {
		// this.paintFg = true;
		// }
		// };
		ICellPainter cellPainter = new SortableHeaderTextPainter();

		configRegistry.registerConfigAttribute(
				CellConfigAttributes.CELL_PAINTER, cellPainter,
				DisplayMode.NORMAL, LABEL_FILTERS);
	}

	// INPUT FIELDS

	public FilterRowPainter cellPainter = new FilterRowPainter();
	public TextMatchingMode textMatchingMode = TextMatchingMode.CONTAINS;
	public int showHideKeyConstant = SWT.F3;

	// private void configureInputFields(IConfigRegistry configRegistry) {
	// // Plug in custom painter
	// configRegistry.registerConfigAttribute(CELL_PAINTER, cellPainter, NORMAL,
	// LABEL_FILTERS);
	//
	// // Make cells editable
	// configRegistry.registerConfigAttribute(CELL_EDITABLE_RULE,
	// ALWAYS_EDITABLE, NORMAL, LABEL_FILTERS);
	//
	// // Default text matching mode
	// configRegistry.registerConfigAttribute(TEXT_MATCHING_MODE,
	// textMatchingMode);
	//
	// // Default display converter. Used to convert the values typed into the
	// text boxes into String objects.
	// configRegistry.registerConfigAttribute(FILTER_DISPLAY_CONVERTER, new
	// DefaultDisplayConverter());
	//
	// // Default comparator. Used to compare objects in the column during
	// threshold matching.
	// configRegistry.registerConfigAttribute(FILTER_COMPARATOR,
	// DefaultComparator.getInstance());
	// }
	//
	// private void configureUiBindingsInputFields(UiBindingRegistry
	// uiBindingRegistry) {
	// uiBindingRegistry.registerFirstSingleClickBinding(new
	// FilterRowMouseEventMatcher(), new MouseEditAction());
	// uiBindingRegistry.registerFirstSingleClickBinding(new
	// ClearFilterIconMouseEventMatcher(cellPainter), new ClearFilterAction());
	// uiBindingRegistry.registerKeyBinding(new
	// KeyEventMatcher(showHideKeyConstant), new ToggleFilterRowAction());
	// }

}