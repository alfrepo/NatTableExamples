package de.example.nebula.nattable;

import java.util.Comparator;


import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfiguration;
import org.eclipse.nebula.widgets.nattable.config.NullComparator;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.PersistentNatExampleWrapper;
import org.eclipse.nebula.widgets.nattable.examples.fixtures.GlazedListsGridLayer;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.stack.DummyGridLayerStack;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultSelectionStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.sort.SortConfigAttributes;
import org.eclipse.nebula.widgets.nattable.sort.SortHeaderLayer;
import org.eclipse.nebula.widgets.nattable.sort.config.SingleClickSortConfiguration;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.test.fixture.data.RowDataFixture;
import org.eclipse.nebula.widgets.nattable.test.fixture.data.RowDataListFixture;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TransformedList;

/**
 * Example to demonstrate sorting of the columns.
 */
public class SortableGridExample extends AbstractNatExample {

	private static final String CUSTOM_COMPARATOR_LABEL = "customComparatorLabel";
	protected static final String NO_SORT_LABEL = "noSortLabel";
	
	public static void main(String[] args) {
		StandaloneNatExampleRunner.run(700, 400, new PersistentNatExampleWrapper(new SortableGridExample()));
	}
	
	@Override
	public String getDescription() {
		return
				"Grid demonstrates sorting moving data.\n" +
				"\n" +
				"Features:\n" +
				"The contents of the grid are kept in sorted order as the rows are added/removed.\n" +
				"Custom comparators can be applied to each column.\n" +
				"Ignorecase comparator applied to the 'Rating' column.\n" +
				"Sorting can be turned off on the selective columns.\n" +
				"'Ask price' field is not sortable.\n" +
				"\n" +
				"Key bindings:\n" +
				"Sort by left clicking on the column header.\n" +
				"Add columns to the existing sort by (Alt. + left click) on the column header\n" +
				"\n" +
				"Technical information:\n" +
				"The default implementation uses GlazedLists to sort the backing data source.";
	}

	private TransformedList rowObjectsGlazedList;
	private NatTable nattable;

	/**
	 * @see GlazedListsGridLayer to see the required stack setup.
	 * 	Basically the {@link SortHeaderLayer} needs to be a part of the Column header layer stack.
	 */
	public Control createExampleControl(Composite parent) {
		EventList eventList = GlazedLists.eventList(RowDataListFixture.getList());
		rowObjectsGlazedList = GlazedLists.threadSafeList(eventList);

		ConfigRegistry configRegistry = new ConfigRegistry();
		GlazedListsGridLayer glazedListsGridLayer = new GlazedListsGridLayer(
				rowObjectsGlazedList,
				RowDataListFixture.getPropertyNames(),
				RowDataListFixture.getPropertyToLabelMap(),
				configRegistry);
		

		// Change the default sort key bindings. Note that 'auto configure' was turned off
		nattable.addConfiguration(new SingleClickSortConfiguration());
		nattable.addConfiguration(getCustomComparatorConfiguration(glazedListsGridLayer.getColumnHeaderLayerStack().getDataLayer()));
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

