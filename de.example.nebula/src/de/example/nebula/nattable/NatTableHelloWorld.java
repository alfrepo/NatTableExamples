package de.example.nebula.nattable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfiguration;
import org.eclipse.nebula.widgets.nattable.config.NullComparator;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
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
import org.eclipse.nebula.widgets.nattable.sort.config.SingleClickSortConfiguration;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.test.fixture.data.RowDataListFixture;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NatTableHelloWorld {
	
	BodyLayerStack bodyLayer;
	
	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout(new GridLayout());
		
//		shell.setSize(1000, 600);
		
		new NatTableHelloWorld(shell);
		
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
		
	}
	
	
	public NatTableHelloWorld(Shell parent) {
			//set the layout
			parent.setLayout(new FillLayout());
			
			List<Person> people = new ArrayList<>();
			people.add(new Person(1, "George", new Date()));
			people.add(new Person(2, "Mike", new Date()));
			people.add(new Person(3, "Fox", new Date()));
			people.add(new Person(4, "Rikki", new Date()));
			
			String[] propertyNames = new String[] { "id", "name", "birthDate" };
			IDataProvider dataProvider = new ListDataProvider<Person>(people, new ReflectiveColumnPropertyAccessor(propertyNames));
			
			
			this.bodyLayer = new BodyLayerStack(dataProvider);
			ColumnHeaderLayerStack columnHeaderLayer = new ColumnHeaderLayerStack(dataProvider);
			RowHeaderLayerStack2 rowHeaderLayer = new RowHeaderLayerStack2(dataProvider);
			
			DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(dataProvider, dataProvider);
			CornerLayer cornerLayer =  new CornerLayer( new DataLayer(cornerDataProvider), rowHeaderLayer, columnHeaderLayer);
			
			GridLayer gridLayer = 
					new GridLayer(bodyLayer, columnHeaderLayer, rowHeaderLayer, cornerLayer);
			
			NatTable natTable = new NatTable(parent, gridLayer, false);
			natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
			
			
			
			//SORTING
			// Change the default sort key bindings. Note that 'auto configure' was turned off
			// for the SortHeaderLayer (setup in the GlazedListsGridLayer)
			natTable.addConfiguration(new SingleClickSortConfiguration());
			natTable.addConfiguration(getCustomComparatorConfiguration(columnHeaderLayer));
			natTable.addConfiguration(new DefaultSelectionStyleConfiguration());
			
			natTable.configure();
	}
	
	

	private static final String CUSTOM_COMPARATOR_LABEL = "customComparatorLabel";
	protected static final String NO_SORT_LABEL = "noSortLabel";
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
                labelAccumulator.registerColumnOverrides(1, CUSTOM_COMPARATOR_LABEL);
                labelAccumulator.registerColumnOverrides(2, NO_SORT_LABEL);

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
	
	
	
	
	class BodyLayerStack extends AbstractLayerTransform {
		
		private SelectionLayer selectionLayer;

		public BodyLayerStack(IDataProvider dataProvider) {
			DataLayer bodyDataLayer = new DataLayer(dataProvider);
			ColumnReorderLayer columnReorderLayer = 
				new ColumnReorderLayer(bodyDataLayer);
			ColumnHideShowLayer columnHideShowLayer = 
				new ColumnHideShowLayer(columnReorderLayer);
			selectionLayer = new SelectionLayer(columnHideShowLayer);
			ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
			setUnderlyingLayer(viewportLayer);
		}

		public SelectionLayer getSelectionLayer() {
			return selectionLayer;
		}
	}
	
	class ColumnHeaderLayerStack extends AbstractLayerTransform {

		public ColumnHeaderLayerStack(IDataProvider dataProvider) {
			DataLayer dataLayer = new DataLayer(dataProvider);
			ColumnHeaderLayer colHeaderLayer = 
				new ColumnHeaderLayer(
					dataLayer, bodyLayer, bodyLayer.getSelectionLayer());
			setUnderlyingLayer(colHeaderLayer);
		}
	}
	
	class RowHeaderLayerStack2 extends AbstractLayerTransform {

		public RowHeaderLayerStack2(IDataProvider dataProvider) {
			DataLayer dataLayer = new DataLayer(dataProvider, 50, 20);
			RowHeaderLayer rowHeaderLayer = 
				new RowHeaderLayer(
					dataLayer, bodyLayer, bodyLayer.getSelectionLayer());
			setUnderlyingLayer(rowHeaderLayer);
		}
	}
}
