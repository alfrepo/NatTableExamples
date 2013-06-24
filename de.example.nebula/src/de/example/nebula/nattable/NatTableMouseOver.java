package de.example.nebula.nattable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.examples.examples._102_Configuration.Rendereing_a_cell_as_a_button;
import org.eclipse.nebula.widgets.nattable.extension.builder.layers.ColumnHeaderLayerStack;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayerListener;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.event.ILayerEvent;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionProvider;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.ui.action.IMouseAction;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.CellLabelMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.IMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NatTableMouseOver {
	
	BodyLayerStack bodyLayer;
	
	SelectionLayer selectionLayer;
	
	RowHeaderLayerStack rowHeaderLayer;
	ColumnHeaderLayerStack columnHeaderLayer;
	
	
	public void addListener(final NatTable natTable){
		MouseMoveListener mlistener = new MouseMoveListener() {
			
			@Override
			public void mouseMove(MouseEvent event) {
				int x = event.x;
				int y = event.y;
				System.out.println("x "+x+" y "+y);
				
				int col = natTable.getColumnPositionByX(event.x) - rowHeaderLayer.getColumnCount();
				int row = natTable.getRowPositionByY(event.y) - columnHeaderLayer.getRowCount(); 
				
				//put a focus on it and redraw
				
				if(col>=0 && row >=0){
					System.out.println("col "+col + "row "+row);
				}
			}
		};
		
		natTable.addMouseMoveListener(mlistener);
	}
	
	
	class BodyLayerStack extends AbstractLayerTransform {
		
		private SelectionLayer selectionLayer;

		public BodyLayerStack(IDataProvider dataProvider) {
			DataLayer bodyDataLayer = new DataLayer(dataProvider);
			ColumnReorderLayer columnReorderLayer = 
				new ColumnReorderLayer(bodyDataLayer);
			ColumnHideShowLayer columnHideShowLayer = 
				new ColumnHideShowLayer(columnReorderLayer);
			selectionLayer = new SelectionLayer(columnHideShowLayer);
			NatTableMouseOver.this.selectionLayer = selectionLayer;
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
	
	
	class RowHeaderLayerStack extends AbstractLayerTransform {

		public RowHeaderLayerStack(IDataProvider dataProvider) {
			DataLayer dataLayer = new DataLayer(dataProvider, 50, 20);
			RowHeaderLayer rowHeaderLayer = 
				new RowHeaderLayer(
					dataLayer, bodyLayer, bodyLayer.getSelectionLayer());
			setUnderlyingLayer(rowHeaderLayer);
		}
	}
}
