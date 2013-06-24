package de.example.nebula.nattable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfiguration;
import org.eclipse.nebula.widgets.nattable.config.NullComparator;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.fixtures.Person;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultSelectionStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.sort.SortConfigAttributes;
import org.eclipse.nebula.widgets.nattable.sort.SortHeaderLayer;
import org.eclipse.nebula.widgets.nattable.sort.config.SingleClickSortConfiguration;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.test.fixture.data.RowDataListFixture;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class Sorting_Getting_Started extends AbstractNatExample {
	
	private static final String CUSTOM_COMPARATOR_LABEL = "customComparatorLabel";
	protected static final String NO_SORT_LABEL = "noSortLabel";

	private IDataProvider bodyDataProvider;
	private String[] propertyNames;
	private BodyLayerStack bodyLayer;
	private Map propertyToLabels;

	public static void main(String[] args) {
		StandaloneNatExampleRunner.run(600, 400, new Sorting_Getting_Started());
	}

	public Control createExampleControl(Composite parent) {
		//create a data provider, incl. the labels for the table-columns
		bodyDataProvider = setupBodyDataProvider();
		
		//body with all of it's table layers
		bodyLayer = new BodyLayerStack(bodyDataProvider);
		
		//column names
		DefaultColumnHeaderDataProvider colHeaderDataProvider = new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabels);
		ColumnHeaderLayerStack columnHeaderLayer = new ColumnHeaderLayerStack(colHeaderDataProvider);

		//row names
		DefaultRowHeaderDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider);		
		RowHeaderLayerStack rowHeaderLayer = new RowHeaderLayerStack(rowHeaderDataProvider);
		
		//(left upper)corner
		DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(colHeaderDataProvider, rowHeaderDataProvider);
		CornerLayer cornerLayer = new CornerLayer(new DataLayer(cornerDataProvider), rowHeaderLayer, columnHeaderLayer);

		//upper layer
		GridLayer gridLayer = new GridLayer(bodyLayer, columnHeaderLayer, rowHeaderLayer, cornerLayer);
		NatTable natTable = new NatTable(parent, gridLayer, false);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		
		//SORTING
		// Change the default sort key bindings. Note that 'auto configure' was turned off
		// for the SortHeaderLayer (setup in the GlazedListsGridLayer)
//		natTable.addConfiguration(new SingleClickSortConfiguration());
//		natTable.addConfiguration(getCustomComparatorConfiguration(columnHeaderLayer));
//		natTable.addConfiguration(new DefaultSelectionStyleConfiguration());
		
		natTable.configure();
		
		return natTable;
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
	               RowDataListFixture.getColumnIndexOfProperty(RowDataListFixture.RATING_PROP_NAME),
	               CUSTOM_COMPARATOR_LABEL);

				labelAccumulator.registerColumnOverrides(
                     RowDataListFixture.getColumnIndexOfProperty(RowDataListFixture.ASK_PRICE_PROP_NAME),
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

	/**
	 * creates the content
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private IDataProvider setupBodyDataProvider() {
		final List people = Arrays.asList(
				new Person(100, "Mickey Mouse", new Date(1000000)), 
				new Person(110, "Batman", new Date(2000000)), 
				new Person(120, "Bender", new Date(3000000)), 
				new Person(130, "Cartman", new Date(4000000)), 
				new Person(140, "Dogbert", new Date(5000000)));
		

		//separate teh id from the label
		propertyToLabels = new HashMap();
		propertyToLabels.put("id", "ID");
		propertyToLabels.put("name", "First Name");
		propertyToLabels.put("birthDate", "DOB");

		propertyNames = new String[] { "id", "name", "birthDate" };
		
		return new ListDataProvider(people, new ReflectiveColumnPropertyAccessor(propertyNames));
	}

	//BELOW are methods to create GridLayer composites
	
	public class BodyLayerStack extends AbstractLayerTransform {

		private SelectionLayer selectionLayer;

		public BodyLayerStack(IDataProvider dataProvider) {
			//1. Use the dataProvider, which is the DataSource
			DataLayer bodyDataLayer = new DataLayer(dataProvider);
			//2. 
			ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(bodyDataLayer);
			//3. disables some columns
			ColumnHideShowLayer columnHideShowLayer = new ColumnHideShowLayer(columnReorderLayer);
			//4. can handle selection
			selectionLayer = new SelectionLayer(columnHideShowLayer);
			//5. draw Scrollbars
			ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
			setUnderlyingLayer(viewportLayer);
		}

		public SelectionLayer getSelectionLayer() {
			return selectionLayer;
		}
	}

	public class ColumnHeaderLayerStack extends AbstractLayerTransform {

		public ColumnHeaderLayerStack(IDataProvider dataProvider) {
			DataLayer dataLayer = new DataLayer(dataProvider);
			ColumnHeaderLayer colHeaderLayer = new ColumnHeaderLayer(dataLayer, bodyLayer, bodyLayer.getSelectionLayer());
			setUnderlyingLayer(colHeaderLayer);
		}
	}

	public class RowHeaderLayerStack extends AbstractLayerTransform {

		public RowHeaderLayerStack(IDataProvider dataProvider) {
			DataLayer dataLayer = new DataLayer(dataProvider, 50, 20);
			RowHeaderLayer rowHeaderLayer = new RowHeaderLayer(dataLayer, bodyLayer, bodyLayer.getSelectionLayer());
			setUnderlyingLayer(rowHeaderLayer);
		}
	}
}
