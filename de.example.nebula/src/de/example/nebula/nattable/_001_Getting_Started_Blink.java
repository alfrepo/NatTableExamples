package de.example.nebula.nattable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.blink.BlinkConfigAttributes;
import org.eclipse.nebula.widgets.nattable.blink.BlinkingCellResolver;
import org.eclipse.nebula.widgets.nattable.blink.IBlinkingCellResolver;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
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
import org.eclipse.nebula.widgets.nattable.sort.SortConfigAttributes;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.test.fixture.data.RowDataListFixture;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class _001_Getting_Started_Blink extends AbstractNatExample {
	
	private static final String CUSTOM_COMPARATOR_LABEL = "customComparatorLabel";
	protected static final String NO_SORT_LABEL = "noSortLabel";

	private IDataProvider bodyDataProvider;
	private String[] propertyNames;
	private BodyLayerStack bodyLayer;
	private Map propertyToLabels;
	
	private GlowLayer glowLayer;

	public static void main(String[] args) {
		StandaloneNatExampleRunner.run(600, 400, new _001_Getting_Started_Blink());
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
		
		
		//BLINK
		
		IConfigRegistry configRegistry = natTable.getConfigRegistry();
		
		
		// Add label accumulator
//		ColumnOverrideLabelAccumulator labelAccumulator = new ColumnOverrideLabelAccumulator(bodyLayer);
//		bodyLayer.setConfigLabelAccumulator(labelAccumulator);
//		registerLabels(configRegistry, labelAccumulator);
		
//		1. add GlowLayer to stack
//		2. register the styles to labels
		bodyLayer.getGlowLayer().registerStyles(configRegistry);
//		3. add some layers to make glowing
		bodyLayer.getGlowLayer().startGlowing(1);
		
		
		natTable.configure();
		
		return natTable;
	}
	
	
	
	
//	public void registerStyles(IConfigRegistry configRegistry){
//		GlowLayer glowLayer = bodyLayer.getGlowLayer();
//		
//		Style cellStyle = new Style();
//		cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_RED);
//		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, glowLayer.getG );
//	}
	
	
	
	

	/**
	 * creates the content
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private IDataProvider setupBodyDataProvider() {
//		List people = Arrays.asList(
//				new Person(100, "Mickey Mouse", new Date(1000000)), 
//				new Person(110, "Batman", new Date(2000000)), 
//				new Person(120, "Bender", new Date(3000000)), 
//				new Person(130, "Cartman", new Date(4000000)), 
//				new Person(140, "Dogbert", new Date(5000000)));
		
		//TODO create many entries
		final List<Person> people = new ArrayList<Person>();
		Person p;
		for(int i=0; i<3000; i++){
			p = new Person(150, "Hulk", new Date(6000000));
			people.add(p);
		}

		//separate teh id from the label
		propertyToLabels = new HashMap();
		propertyToLabels.put("id", "ID");
		propertyToLabels.put("name", "First Name");
		propertyToLabels.put("birthDate", "DOB");

		propertyNames = new String[] { "id", "name", "birthDate" };
		
		
		return new IDataProvider() {
			
			@Override
			public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
				
			}
			
			@Override
			public int getRowCount() {
				System.out.println("RowCount");
				return people.size();
			}
			
			@Override
			public Object getDataValue(int columnIndex, int rowIndex) {
				System.out.println("getDataValue : row "+rowIndex +" column "+columnIndex);
				Person p = people.get(rowIndex);
				if(columnIndex == 0){
					return p.getId();
				}else if(columnIndex == 1){
					return p.getName();
				}else{
					return p.getBirthDate();
				}
			}
			
			@Override
			public int getColumnCount() {
				System.out.println("getColumnCount");
				return 3;
			}
		};
	}

	
	//BELOW are methods to create GridLayer composites
	
	public class BodyLayerStack extends AbstractLayerTransform {

		private SelectionLayer selectionLayer;
		private ViewportLayer viewportLayer;

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
			viewportLayer = new ViewportLayer(selectionLayer);
			//6. Glow
			glowLayer = new GlowLayer(viewportLayer, bodyDataLayer, 5, 100, GUIHelper.COLOR_WHITE, GUIHelper.COLOR_YELLOW);
			setUnderlyingLayer(glowLayer);
		}

		public SelectionLayer getSelectionLayer() {
			return selectionLayer;
		}
		public GlowLayer getGlowLayer() {
			return glowLayer;
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
	
	
	//BLINK
	
	private static final String BLINK_UP_CONFIG_LABEL = "blinkUpConfigLabel";
	private static final String BLINK_DOWN_CONFIG_LABEL = "blinkDownConfigLabel";
	
	private void registerBlinkingConfigCells(ConfigRegistry configRegistry) {
		configRegistry.registerConfigAttribute(BlinkConfigAttributes.BLINK_RESOLVER, getBlinkResolver(), DisplayMode.NORMAL);

		// Bg color styles to be used for blinking cells
		Style cellStyle = new Style();
		cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_GREEN);
		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, BLINK_UP_CONFIG_LABEL);

		cellStyle = new Style();
		cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_RED);
		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, BLINK_DOWN_CONFIG_LABEL);
	}
	
	/**
	 * The blinking resolver decides how the cell should blink
	 * i.e what styles should be applied depending on the update.
	 * This one returns a green color label when the value goee up, a red one otherwise.
	 */
	private IBlinkingCellResolver getBlinkResolver() {
		return new BlinkingCellResolver() {
			private String[] configLabels = new String[1];

			public String[] resolve(Object oldValue, Object newValue) {
				double old = ((Double) oldValue).doubleValue();
				double latest = ((Double) newValue).doubleValue();
				configLabels[0] = (latest > old ? BLINK_UP_CONFIG_LABEL : BLINK_DOWN_CONFIG_LABEL);
				return configLabels;
			};
		};
	}
	
	

}
