package de.example.nebula.nattable;


import java.util.Comparator;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfiguration;
import org.eclipse.nebula.widgets.nattable.config.NullComparator;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DummyBodyDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DummyColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultSelectionStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.sort.SortConfigAttributes;
import org.eclipse.nebula.widgets.nattable.sort.config.SingleClickSortConfiguration;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ColumnHeaderSelectionDataLayerExample extends AbstractNatExample {
	

	private static final String CUSTOM_COMPARATOR_LABEL = "customComparatorLabel";
	protected static final String NO_SORT_LABEL = "noSortLabel";
	
	public static void main(String[] args) throws Exception {
		StandaloneNatExampleRunner.run(new ColumnHeaderSelectionDataLayerExample());
	}

	public Control createExampleControl(Composite parent) {
		DummyBodyDataProvider bodyDataProvider = new DummyBodyDataProvider(1000000, 1000000);
		SelectionLayer selectionLayer = new SelectionLayer(new DataLayer(bodyDataProvider));
		DataLayer colDataLayer = new DataLayer(new DummyColumnHeaderDataProvider(bodyDataProvider));
		ILayer columnHeaderLayer = new ColumnHeaderLayer(colDataLayer, selectionLayer, selectionLayer);
		
		CompositeLayer compositeLayer = new CompositeLayer(1, 2);
		compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER, columnHeaderLayer, 0, 0);
		compositeLayer.setChildLayer(GridRegion.BODY, selectionLayer, 0, 1);
		
		NatTable nattable =  new NatTable(parent, compositeLayer, false);
		nattable.addConfiguration(new DefaultNatTableStyleConfiguration());

		
		// Change the default sort key bindings. Note that 'auto configure' was turned off
		nattable.addConfiguration(new SingleClickSortConfiguration());
		nattable.addConfiguration(getCustomComparatorConfiguration( colDataLayer ));
		nattable.addConfiguration(new DefaultSelectionStyleConfiguration());
		
		nattable.configure();
		return nattable;
	}
	
	
	/**
	 * NOTE: The labels for the the custom comparators must go on the columnHeaderDataLayer - since,
	 * 		the SortHeaderLayer will resolve cell labels with respect to its underlying layer i.e columnHeaderDataLayer
	 */
	private IConfiguration getCustomComparatorConfiguration(final AbstractLayer columnHeaderDataLayer) {

		return new AbstractRegistryConfiguration() {

			public void configureRegistry(IConfigRegistry configRegistry) {
				// Add label accumulator
				ColumnOverrideLabelAccumulator labelAccumulator = new ColumnOverrideLabelAccumulator(columnHeaderDataLayer);
				columnHeaderDataLayer.setConfigLabelAccumulator(labelAccumulator);

				// Register labels
				labelAccumulator.registerColumnOverrides(
	              1, CUSTOM_COMPARATOR_LABEL);

				labelAccumulator.registerColumnOverrides(
                     2,
                     NO_SORT_LABEL);

				// Register custom comparator
				configRegistry.registerConfigAttribute(SortConfigAttributes.SORT_COMPARATOR,
									                       getCustomComparator(),
									                       DisplayMode.NORMAL,
									                       CUSTOM_COMPARATOR_LABEL);

				// Register null comparator to disable sort
				configRegistry.registerConfigAttribute(SortConfigAttributes.SORT_COMPARATOR,
				                                       new NullComparator(),
				                                       DisplayMode.NORMAL,
				                                       NO_SORT_LABEL);
			}
		};
	}

	private Comparator getCustomComparator() {
		return new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				return ((String) o1).compareToIgnoreCase((String) o2);
			}
		};
	};
	
}

