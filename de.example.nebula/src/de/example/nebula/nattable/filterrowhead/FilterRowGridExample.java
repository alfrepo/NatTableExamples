package de.example.nebula.nattable.filterrowhead;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDoubleDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.PersistentNatExampleWrapper;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowDataLayer;
import org.eclipse.nebula.widgets.nattable.filterrow.TextMatchingMode;
import org.eclipse.nebula.widgets.nattable.filterrow.config.FilterRowConfigAttributes;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.test.fixture.data.PricingTypeBean;
import org.eclipse.nebula.widgets.nattable.ui.menu.DebugMenuConfiguration;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FilterRowGridExample extends AbstractNatExample {
	
	public static void main(String[] args) {
		StandaloneNatExampleRunner.run(new PersistentNatExampleWrapper(new FilterRowGridExample()));
	}

	public Control createExampleControl(Composite parent) {
		IConfigRegistry configRegistry = new ConfigRegistry();
		ILayer underlyingLayer = new FilterRowExampleGridLayer(configRegistry);

		NatTable natTable = new NatTable(parent, underlyingLayer, false);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
//		natTable.addConfiguration(new HeaderMenuConfiguration(natTable));
		
		natTable.addConfiguration(new DebugMenuConfiguration(natTable));
//		natTable.addConfiguration(new FilterRowCustomConfiguration() {
//			@Override
//			public void configureRegistry(IConfigRegistry configRegistry) {
//				super.configureRegistry(configRegistry);
//				
//				// Shade the row to be slightly darker than the blue background.
//				final Style rowStyle = new Style();
//				rowStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.getColor(197, 212, 231));
//				configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, rowStyle, DisplayMode.NORMAL, GridRegion.FILTER_ROW);
//			}
//		});

		natTable.setConfigRegistry(configRegistry);
		natTable.configure();

		return natTable;
	}

	public static class FilterRowCustomConfiguration extends AbstractRegistryConfiguration {

		final DefaultDoubleDisplayConverter doubleDisplayConverter = new DefaultDoubleDisplayConverter();

		public void configureRegistry(IConfigRegistry configRegistry) {
			// Configure custom comparator on the rating column
			configRegistry.registerConfigAttribute(FilterRowConfigAttributes.FILTER_COMPARATOR,
					getIngnorecaseComparator(),
					DisplayMode.NORMAL,
					FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 2);

			// If threshold comparison is used we have to convert the string entered by the
			// user to the correct underlying type (double), so that it can be compared

			// Configure Bid column
			configRegistry.registerConfigAttribute(FilterRowConfigAttributes.FILTER_DISPLAY_CONVERTER,
					doubleDisplayConverter,
					DisplayMode.NORMAL,
					FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 5);
			configRegistry.registerConfigAttribute(FilterRowConfigAttributes.TEXT_MATCHING_MODE,
					TextMatchingMode.REGULAR_EXPRESSION,
					DisplayMode.NORMAL,
					FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 5);
			
			// Configure Ask column
			configRegistry.registerConfigAttribute(FilterRowConfigAttributes.FILTER_DISPLAY_CONVERTER,
					doubleDisplayConverter,
					DisplayMode.NORMAL,
					FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 6);
			configRegistry.registerConfigAttribute(FilterRowConfigAttributes.TEXT_MATCHING_MODE,
					TextMatchingMode.REGULAR_EXPRESSION,
					DisplayMode.NORMAL,
					FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 6);
		
			// Configure a combo box on the pricing type column
			
			// Register a combo box editor to be displayed in the filter row cell 
			//    when a value is selected from the combo, the object is converted to a string
			//    using the converter (registered below)
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, 
					new ComboBoxCellEditor(Arrays.asList(new PricingTypeBean("MN"), new PricingTypeBean("AT"))), 
					DisplayMode.NORMAL, 
					FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 4);
			
			// The pricing bean object in column is converted to using this display converter
			// A 'text' match is then performed against the value from the combo box 
			configRegistry.registerConfigAttribute(FilterRowConfigAttributes.FILTER_DISPLAY_CONVERTER,
					PricingTypeBean.getDisplayConverter(),
					DisplayMode.NORMAL,
					FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 4);
			
			configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER,
					PricingTypeBean.getDisplayConverter(),
					DisplayMode.NORMAL,
					FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 4);
			
			configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, PricingTypeBean.getDisplayConverter(), DisplayMode.NORMAL, "PRICING_TYPE_PROP_NAME");
		}
	}
	
	private static Comparator getIngnorecaseComparator() {
		return new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				return ((String) o1).compareToIgnoreCase((String)o2);
			}
		};
	};

}

