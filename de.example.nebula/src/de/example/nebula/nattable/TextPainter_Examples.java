package de.example.nebula.nattable;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DummyColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.config.DefaultColumnHeaderLayerConfiguration;
import org.eclipse.nebula.widgets.nattable.layer.config.DefaultColumnHeaderStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.painter.cell.GradientBackgroundPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.VerticalTextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.BeveledBorderDecorator;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.LineBorderDecorator;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.PaddingDecorator;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.VerticalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TextPainter_Examples extends AbstractNatExample {
	
	public static void main(String[] args) throws Exception {
		StandaloneNatExampleRunner.run(new TextPainter_Examples());
	}
	
	@Override
	public String getDescription() {
		return
				"The way content is rendered as text into cells is defined via TextPainter.\n" +
				"The TextPainter can be configured to wrap text and/or calculate the cell borders. " +
				"In the first row are examples showing the normal TextPainter with various configurations," +
				"the second row shows various configurations of the VerticalTextPainter."+
				"Following configurations are used for the tables by column:\n" +
				"1: no wrap, no calculate (default) - the content is cutted and showed with ...\n" +
				"2: wrap, no calculate - the content is modified with new lines but showed with ... if it still doesn't fit\n" +
				"3: no wrap, calculate - the content is showed completely as the cell will grow with its content (one line).\n" +
				"4: wrap, calculate - the content is showed completely as the cell will grow with its content by wrapping the text.\n" +
				"5: wrap, calculate, spacing used - the content is showed completely as the cell will grow with its content by wrapping the text.\n" +
				"6: wrap, calculate, spacing used, wrapped by PaddingDecorator - the content is showed completely as the cell will grow with its content by wrapping the text.";
	}
	
	public Control createExampleControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(parent);
		
		Composite tableContainer = new Composite(parent, SWT.NONE);
		
		tableContainer.setLayout(new GridLayout(6, true));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tableContainer);
		
		createNatTable(tableContainer, new GradientBackgroundPainter(new TextPainter(false, false, false), true));
		createNatTable(tableContainer, new TextPainter(true, true, false));
		createNatTable(tableContainer, new TextPainter(false, true, true));
		createNatTable(tableContainer, new TextPainter(true, true, true));
		createNatTable(tableContainer, new TextPainter(true, true, 5, true));
		createNatTable(tableContainer, new PaddingDecorator(new TextPainter(true, true, 5, true), 5));
		
		createVerticalHeaderNatTable(tableContainer, new VerticalTextPainter(false, true, false));
		createVerticalHeaderNatTable(tableContainer, new VerticalTextPainter(true, true, false));
		createVerticalHeaderNatTable(tableContainer, new GradientBackgroundPainter(new VerticalTextPainter(false, false, true)));
		createVerticalHeaderNatTable(tableContainer, new VerticalTextPainter(true, true, true));
		createVerticalHeaderNatTable(tableContainer, new VerticalTextPainter(true, true, 5, true));
		createVerticalHeaderNatTable(tableContainer, new PaddingDecorator(new VerticalTextPainter(true, true, 5, true), 5));
		
		TextPainter underlineTextPainer = new TextPainter();
		underlineTextPainer.setUnderline(true);
		createNatTable2(tableContainer, underlineTextPainer);
		TextPainter strikethroughTextPainer = new TextPainter();
		strikethroughTextPainer.setStrikethrough(true);
		createNatTable2(tableContainer, strikethroughTextPainer);
		TextPainter underlineStrikethroughTextPainer = new TextPainter();
		underlineStrikethroughTextPainer.setUnderline(true);
		underlineStrikethroughTextPainer.setStrikethrough(true);
		createNatTable2(tableContainer, underlineStrikethroughTextPainer);
		
		VerticalTextPainter vunderlineTextPainer = new VerticalTextPainter(true, true, true);
		vunderlineTextPainer.setUnderline(true);
		createVerticalHeaderNatTable(tableContainer, vunderlineTextPainer);
		VerticalTextPainter vstrikethroughTextPainer = new VerticalTextPainter(true, true, true);
		vstrikethroughTextPainer.setStrikethrough(true);
		createVerticalHeaderNatTable(tableContainer, vstrikethroughTextPainer);
		VerticalTextPainter vunderlineStrikethroughTextPainer = new VerticalTextPainter(true, true, true);
		vunderlineStrikethroughTextPainer.setUnderline(true);
		vunderlineStrikethroughTextPainer.setStrikethrough(true);
		createVerticalHeaderNatTable(tableContainer, vunderlineStrikethroughTextPainer);
		
		return tableContainer;
	}
	
	
	private void createNatTable(Composite parent, final ICellPainter painter) {
		IDataProvider bodyDataProvider = new ExampleTextBodyDataProvider();
		DataLayer dataLayer = new DataLayer(bodyDataProvider);
		dataLayer.setRowHeightByPosition(0, 32);
		SelectionLayer selectionLayer = new SelectionLayer(new ColumnReorderLayer(dataLayer));
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		ILayer columnHeaderLayer = new ColumnHeaderLayer(
			new DataLayer(new DummyColumnHeaderDataProvider(bodyDataProvider)),
			viewportLayer, 
			selectionLayer);
		
		CompositeLayer compositeLayer = new CompositeLayer(1, 2);
		compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER, columnHeaderLayer, 0, 0);
		compositeLayer.setChildLayer(GridRegion.BODY, viewportLayer, 0, 1);
		
		NatTable natTable = new NatTable(parent, compositeLayer, false);
		
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration() {
			{
				vAlign = VerticalAlignmentEnum.TOP;
				hAlign = HorizontalAlignmentEnum.LEFT;
				cellPainter = new LineBorderDecorator(painter);
			}
		});
		
		natTable.configure();
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
	private void createNatTable2(Composite parent, final ICellPainter painter) {
		IDataProvider bodyDataProvider = new ExampleTextBodyDataProvider();
		DataLayer dataLayer = new DataLayer(bodyDataProvider);
		dataLayer.setRowHeightByPosition(0, 32);
		SelectionLayer selectionLayer = new SelectionLayer(new ColumnReorderLayer(dataLayer));
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		ColumnHeaderLayer columnHeaderLayer = new ColumnHeaderLayer(
			new DataLayer(new ExampleHeaderDataProvider()),
			viewportLayer, 
			selectionLayer,
			false);
		columnHeaderLayer.addConfiguration(new DefaultColumnHeaderLayerConfiguration() {
			protected void addColumnHeaderStyleConfig() {
				addConfiguration(new DefaultColumnHeaderStyleConfiguration() {
					{
						cellPainter = new BeveledBorderDecorator(painter);
					}
				});
			}
		});
		
		CompositeLayer compositeLayer = new CompositeLayer(1, 2);
		compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER, columnHeaderLayer, 0, 0);
		compositeLayer.setChildLayer(GridRegion.BODY, viewportLayer, 0, 1);
		
		NatTable natTable = new NatTable(parent, compositeLayer, false);
		
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration() {
			{
				vAlign = VerticalAlignmentEnum.TOP;
				hAlign = HorizontalAlignmentEnum.LEFT;
				cellPainter = new LineBorderDecorator(painter);
			}
		});
		
		natTable.configure();
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
	private void createVerticalHeaderNatTable(Composite parent, final ICellPainter painter) {
		IDataProvider bodyDataProvider = new ExampleHeaderDataProvider();
		SelectionLayer selectionLayer = new SelectionLayer(new ColumnReorderLayer(new DataLayer(bodyDataProvider)));
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		ColumnHeaderLayer columnHeaderLayer = new ColumnHeaderLayer(
			new DataLayer(new ExampleHeaderDataProvider()),
			viewportLayer, 
			selectionLayer, false);
		columnHeaderLayer.addConfiguration(new DefaultColumnHeaderLayerConfiguration() {
			protected void addColumnHeaderStyleConfig() {
				addConfiguration(new DefaultColumnHeaderStyleConfiguration() {
					{
						cellPainter = new BeveledBorderDecorator(painter);
					}
				});
			}
		});
		
		CompositeLayer compositeLayer = new CompositeLayer(1, 2);
		compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER, columnHeaderLayer, 0, 0);
		compositeLayer.setChildLayer(GridRegion.BODY, viewportLayer, 0, 1);
		
		NatTable natTable = new NatTable(parent, compositeLayer);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
	
	class ExampleTextBodyDataProvider implements IDataProvider {

		public Object getDataValue(int columnIndex, int rowIndex) {
			return "Lorem\nipsum dolor sit amet, consetetur sadipscing elitr, " +
					"sed diam nonumy.";
//			return "Lorem ipsum dolor sit amet, consetetur sadipscing elitr,\n" + 
//			"sed diam nonumy eirmod tempor invidunt ut labore et dolore\n" + 
//			"magna aliquyam erat, sed diam voluptua. At vero eos et accusam\n" + 
//			"et justo duo dolores et ea rebum. Stet clita kasd gubergren,\n" + 
//			"no sea takimata sanctus est Lorem ipsum dolor sit amet.\n" + 
//			"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam\n" + 
//			"nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat,\n" + 
//			"sed diam voluptua.\n\n " + 
//			"At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd\n" + 
//			"gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		}

		public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
			throw new UnsupportedOperationException();
		}

		public int getColumnCount() {
			return 1;
		}

		public int getRowCount() {
			return 1;
		}
		
	}
	
	class ExampleHeaderDataProvider implements IDataProvider {

		public Object getDataValue(int columnIndex, int rowIndex) {
			return "Lorem ipsum.";
		}

		public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
			throw new UnsupportedOperationException();
		}

		public int getColumnCount() {
			return 1;
		}

		public int getRowCount() {
			return 1;
		}
		
	}
}

