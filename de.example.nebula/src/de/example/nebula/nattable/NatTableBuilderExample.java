package de.example.nebula.nattable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDoubleDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.editor.IComboBoxDataProvider;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.fixtures.TableRowFixture;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
import org.eclipse.nebula.widgets.nattable.extension.builder.NatTableBuilder;
import org.eclipse.nebula.widgets.nattable.extension.builder.model.ColumnStyle;
import org.eclipse.nebula.widgets.nattable.extension.builder.model.Editors;
import org.eclipse.nebula.widgets.nattable.extension.builder.model.TableColumn;
import org.eclipse.nebula.widgets.nattable.extension.builder.model.TableModel;
import org.eclipse.nebula.widgets.nattable.extension.builder.util.DisplayConverters;
import org.eclipse.nebula.widgets.nattable.layer.cell.CellOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.test.fixture.data.RowDataListFixture;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import ca.odell.glazedlists.GlazedLists;

public class NatTableBuilderExample extends AbstractNatExample {

	private NatTableBuilder builder;
	private NatTable natTable;

	public static void main(String[] args) throws Exception {
		StandaloneNatExampleRunner.run(800, 600, new NatTableBuilderExample());
	}

	public Control createExampleControl(Composite parent) {
		TableColumn[] columns = new TableColumn[]{
			new TableColumn(0, "securityId", "ISIN").setWidth(200).setEditor(Editors.getTextEditor()).setCategory("C1"),
			new TableColumn(1, "securityDescription", "Name").setStyle(new ColumnStyle()).setGroupName("A1").setCategory("C1"),
			new TableColumn(2, "rating", "Rating").setComparator(getIgnoreCaseComparator()).setGroupName("A1").setCategory("C1"),
			new TableColumn(3, "issueDate", "issueDate").setCategory("C1"),
			new TableColumn(4, "pricingType", "Pricing type")
				.setEditor(Editors.getComboboxEditor(getPricingComboDataProvider()))
				.setFilterRowEditor(Editors.getComboboxEditor(getPricingComboDataProvider()))
				.setCategory("C1"),

			new TableColumn(5, "bidPrice", "Bid price")
				.setEditor(Editors.getTextEditor())
				.setDisplayConverter(new DefaultDoubleDisplayConverter())
				.setGroupName("Pricing")
				.setCategory("C1"),

			new TableColumn(6, "askPrice", "Ask price")
				.setGroupName("Pricing")
				.setDisplayConverter(DisplayConverters.getDoubleDisplayConverter("##.00"))
				.setCategory("C1"),

			new TableColumn(7, "lotSize", "Lot size").setCategory("C1"),
			new TableColumn(8, "publishFlag", "Published").setEditor(Editors.getCheckBoxEditor()).setCategory("C1"),
			new TableColumn(9, "high52Week", "52 Week high").setCategory("C3"),
			new TableColumn(10, "low52Week", "52 week low").setCategory("C3"),
			new TableColumn(11, "eps", "EPS").setCategory("C2"),
			new TableColumn(12, "volume", "Volume").setCategory("C2"),
			new TableColumn(13, "marketCap", "Market cap.").setCategory("C2"),
			new TableColumn(14, "institutionOwned", "Institution owned").setCategory("C2"),
		};
		TableModel table = new TableModel(columns);

		builder = new NatTableBuilder(parent, table, GlazedLists.eventList(TableRowFixture.getList()), TableRowFixture.rowIdAccessor);
		
		// Setup all the layer stacks
		natTable = builder.setupLayerStacks();

		// Since the build() method has not been invoked yet you can
		// tweak layer configuration as you want
		customize();

		return builder.build();
	}

	private void customize() {
		Style style = new Style();
		style.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_RED);

		ListDataProvider bodyDataProvider = builder.getBodyLayerStack().getDataProvider();
		CellOverrideLabelAccumulator myAccumulator = new CellOverrideLabelAccumulator(bodyDataProvider);
		myAccumulator.registerOverride("AAA", 2, "myLabel");

		builder.addCellLabelsToBody(myAccumulator);

		IConfigRegistry configRegistry = natTable.getConfigRegistry();
		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, style, DisplayMode.NORMAL, "myLabel");
	}

	private IComboBoxDataProvider getPricingComboDataProvider() {
		return new IComboBoxDataProvider() {
			public List getValues(int columnIndex, int rowIndex) {
				return Arrays.asList(RowDataListFixture.PRICING_AUTO, RowDataListFixture.PRICING_MANUAL);
			}
		};
	}

	private Comparator getIgnoreCaseComparator() {
		return new Comparator() {
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}

			@Override
			public int compare(Object o1, Object o2) {
				return 0;
			}
		};
	};
}

