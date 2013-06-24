package de.example.nebula.nattable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.EditableRule;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultBooleanDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.IDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.validate.DataValidator;
import org.eclipse.nebula.widgets.nattable.data.validate.DefaultNumericDataValidator;
import org.eclipse.nebula.widgets.nattable.data.validate.IDataValidator;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.action.ToggleCheckBoxColumnAction;
import org.eclipse.nebula.widgets.nattable.edit.editor.CheckBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.TextCellEditor;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultGridLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ColumnHeaderCheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ComboBoxPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.BeveledBorderDecorator;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.CellPainterDecorator;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.test.fixture.data.PricingTypeBean;
import org.eclipse.nebula.widgets.nattable.test.fixture.data.RowDataListFixture;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.CellPainterMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.util.CellEdgeEnum;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class EditableGridExampleCheckboxONLY extends AbstractNatExample {

	public static void main(String[] args) {
		StandaloneNatExampleRunner.run(new EditableGridExampleCheckboxONLY());
	}

	@Override
	public String getDescription() {
		return
				"This example demonstrates various edit and styling functionality. All of the functionality in the DefaultGridExample " +
				"is available here. Also note that:\n" +
				"\n" +
				"* CUSTOM ALIGNMENT: All numeric fields are right-aligned, and the Security Description field is left-aligned.\n" +
				"* CUSTOM FORMATTING: The Bid and Ask fields are formatted to 2 decimal places normally, but in edit mode they are " +
				"formatted to 6 decimal places.\n" +
				"* EDITABLE/NON-EDITABLE FIELDS: Spread is a non-editable calculated field that is formatted to 6 decimal places.\n" +
				"* SINGLE AND MULTI-CELL EDIT for text, checkbox, and combo box fields. Note that in order to invoke multi-cell " +
				"edit, all of the selected cells must have the same editor type and be editable.\n" +
				"* VALIDATION: The ISIN field has validation enabled which requires data to be in the form ABC123 (first three alpha " +
				"characters, afterwards numeric). If the validation rule is violated, the characters are highlighted in red and the " +
				"editor will not allow you to enter the value. Also note that if an ISIN field and a different text field are " +
				"selected for multi-cell edit, the multi-cell editor will require a value that is valid for all selected cells.";
	}
	
	public static final String ASK_PRICE_CONFIG_LABEL = "askPriceConfigLabel";
	public static final String SECURITY_ID_CONFIG_LABEL = "SecurityIdConfigLabel";
	public static final String SECURITY_ID_EDITOR = "SecurityIdEditor";
	public static final String BID_PRICE_CONFIG_LABEL = "bidPriceConfigLabel";
	public static final String LOT_SIZE_CONFIG_LABEL = "lotSizeConfigLabel";
	public static final String SPREAD_CONFIG_LABEL = "spreadConfigLabel";

	public static final String FORMAT_DATE_CONFIG_LABEL = "formatDateConfigLabel";
	public static final String FORMAT_DOUBLE_2_PLACES_CONFIG_LABEL = "formatDouble2PlacesConfigLabel";
	public static final String FORMAT_DOUBLE_6_PLACES_CONFIG_LABEL = "formatDouble6PlacesConfigLabel";
	public static final String FORMAT_IN_MILLIONS_CONFIG_LABEL = "formatInMilliosConfigLabel";
	public static final String FORMAT_PRICING_TYPE_CONFIG_LABEL = "formatPricingTypeConfigLabel";

	public static final String ALIGN_CELL_CONTENTS_LEFT_CONFIG_LABEL = "alignCellContentsLeftConfigLabel";
	public static final String ALIGN_CELL_CONTENTS_RIGHT_CONFIG_LABEL = "alignCellContentsRightConfigLabel";

	public static final String CHECK_BOX_CONFIG_LABEL = "checkBox";
	public static final String CHECK_BOX_EDITOR_CONFIG_LABEL = "checkBoxEditor";
	public static final String COMBO_BOX_CONFIG_LABEL = "comboBox";
	public static final String COMBO_BOX_EDITOR_CONFIG_LABEL = "comboBoxEditor";

	public Control createExampleControl(Composite parent) {
		DefaultGridLayer gridLayer = new DefaultGridLayer(RowDataListFixture.getList(), RowDataListFixture.getPropertyNames(), RowDataListFixture.getPropertyToLabelMap());
		
		DataLayer columnHeaderDataLayer = (DataLayer) gridLayer.getColumnHeaderDataLayer();
		columnHeaderDataLayer.setConfigLabelAccumulator(new ColumnLabelAccumulator());
		
		final DataLayer bodyDataLayer = (DataLayer) gridLayer.getBodyDataLayer();
		IDataProvider dataProvider = bodyDataLayer.getDataProvider();

		// NOTE: Register the accumulator on the body data layer.
		// This ensures that the labels are bound to the column index and are unaffected by column order.
		final ColumnOverrideLabelAccumulator columnLabelAccumulator = new ColumnOverrideLabelAccumulator(bodyDataLayer);
		bodyDataLayer.setConfigLabelAccumulator(columnLabelAccumulator);

		NatTable natTable = new NatTable(parent, gridLayer, false);

		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		natTable.addConfiguration(editableGridConfiguration(columnLabelAccumulator, dataProvider));

		natTable.configure();

		return natTable;
	}

	public static AbstractRegistryConfiguration editableGridConfiguration(
						final ColumnOverrideLabelAccumulator columnLabelAccumulator,
						final IDataProvider dataProvider) {

		return new AbstractRegistryConfiguration() {

			public void configureRegistry(IConfigRegistry configRegistry) {
				//	registerConfigLabelsOnColumns
				columnLabelAccumulator.registerColumnOverrides(RowDataListFixture.getColumnIndexOfProperty(RowDataListFixture.PUBLISH_FLAG_PROP_NAME), CHECK_BOX_EDITOR_CONFIG_LABEL, CHECK_BOX_CONFIG_LABEL);

				
				// registerCheckBoxEditor
				configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, new CheckBoxPainter(), DisplayMode.NORMAL, CHECK_BOX_CONFIG_LABEL);
				configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new DefaultBooleanDisplayConverter(), DisplayMode.NORMAL, CHECK_BOX_CONFIG_LABEL);
				configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new CheckBoxCellEditor(), DisplayMode.NORMAL, CHECK_BOX_EDITOR_CONFIG_LABEL);
				

				// registerEditableRules
				configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE, DisplayMode.EDIT, CHECK_BOX_CONFIG_LABEL);
			}

		};
	}


}

