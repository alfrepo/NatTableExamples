package de.example.nebula.nattable.composedLayers;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.IUniqueIndexLayer;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;

public class LayerNewObjects extends AbstractLayerTransform implements IUniqueIndexLayer, IDataLayer {

	private DataLayer bodyDataLayer;
	
	public LayerNewObjects(){
		
		IDataProvider newObjectsDataprovider = new LayerNewObjectsDataProvider();
		
		//stack
		bodyDataLayer = new DataLayer(newObjectsDataprovider);
		ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(bodyDataLayer);
		ColumnHideShowLayer columnHideShowLayer = new ColumnHideShowLayer(columnReorderLayer);
		SelectionLayer selectionLayer = new SelectionLayer(columnHideShowLayer);
		setUnderlyingLayer(selectionLayer);
	}
    
	@Override
	public int getColumnPositionByIndex(int columnIndex) {
		return bodyDataLayer.getColumnIndexByPosition(columnIndex);
	}

	@Override
	public int getRowPositionByIndex(int rowIndex) {
		return bodyDataLayer.getRowPositionByIndex(rowIndex);
	}

	@Override
	public DataLayer getDataLayer() {
		return bodyDataLayer;
	}

}
