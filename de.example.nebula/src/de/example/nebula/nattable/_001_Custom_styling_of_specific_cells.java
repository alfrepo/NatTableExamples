package de.example.nebula.nattable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.fixtures.Person;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
import org.eclipse.nebula.widgets.nattable.grid.data.DummyColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultGridLayer;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.IConfigLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.stack.DefaultBodyLayerStack;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class _001_Custom_styling_of_specific_cells extends AbstractNatExample {
	
	public static void main(String[] args) throws Exception {
		StandaloneNatExampleRunner.run(800, 600, new _001_Custom_styling_of_specific_cells());
	}
	
	@Override
	public String getDescription() {
		return
				"NatTable has a flexible mechanism for customizing styles for specific cells.\n" +
				"First an IConfigLabelAccumulator is used to tag the cells you want to customize with a custom label.\n" +
				"Then a new style is registered in the config registry for the custom label.\n" +
				"\n" +
				"This example shows a trivial example that simply changes the background color for the cell at column, row index (1, 5).\n" +
				"You can change the IConfigLabelAccumulator to target arbitrary other cells, and you can also modify any other style\n" +
				"attributes you wish. You can also register custom display converters, editable rules, etc. in the same way.";
	}
	
	private static final String FOO_LABEL = "FOO";
	
	public Control createExampleControl(Composite parent) {
		List myList = new ArrayList();
		for (int i = 0; i < 100; i++) {
			myList.add(new Person(i, "Joe" + i, new Date()));
		}
		
		String[] propertyNames = {
				"id",
				"name",
				"birthDate"
		};
		
		IColumnPropertyAccessor columnPropertyAccessor = new ReflectiveColumnPropertyAccessor(propertyNames);
		ListDataProvider listDataProvider = new ListDataProvider(myList, columnPropertyAccessor);
		DefaultGridLayer gridLayer = new DefaultGridLayer(listDataProvider, new DummyColumnHeaderDataProvider(listDataProvider));
		final DefaultBodyLayerStack bodyLayer = gridLayer.getBodyLayer();
		
		// Custom label "FOO" for cell at column, row index (1, 5)
		IConfigLabelAccumulator cellLabelAccumulator = new IConfigLabelAccumulator() {
			public void accumulateConfigLabels(LabelStack configLabels, int columnPosition, int rowPosition) {
				int columnIndex = bodyLayer.getColumnIndexByPosition(columnPosition);
				int rowIndex = bodyLayer.getRowIndexByPosition(rowPosition);
				if (columnIndex == 1 && rowIndex == 5) {
					configLabels.addLabel(FOO_LABEL);
				}
			}
		};
		bodyLayer.setConfigLabelAccumulator(cellLabelAccumulator);
		
		NatTable natTable = new NatTable(parent, gridLayer, false);
		
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		// Custom style for label "FOO"
		natTable.addConfiguration(new AbstractRegistryConfiguration() {
			public void configureRegistry(IConfigRegistry configRegistry) {
				Style cellStyle = new Style();
				cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_GREEN);
				configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, FOO_LABEL);
			}
		});
		natTable.configure();
		
		return natTable;
	}
	
}

