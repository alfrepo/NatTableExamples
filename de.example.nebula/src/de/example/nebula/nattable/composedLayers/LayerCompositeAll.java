package de.example.nebula.nattable.composedLayers;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.IUniqueIndexLayer;

public class LayerCompositeAll extends CompositeLayer implements IUniqueIndexLayer {

    private LayerNewObjects layerNewObjectsTop;
    private LayerExistingObjects layerExistingObjectsSecond;
	
	public LayerCompositeAll() {
		super(1, 2);
		createLayers();
	}

	/**
	 * ACHTUNG: it is assumed, that the children have the type {@link IDataLayer}.
	 */
	private void createLayers(){
		//CREATE LAYERS
		
		// Layer contains existing domain objects
		this.layerNewObjectsTop = new LayerNewObjects();
		this.layerExistingObjectsSecond = new LayerExistingObjects();
		
		//register the Layer as the first from the top
		setChildLayer(REGIONS.BODY_NEW_OBJECTS, layerNewObjectsTop, 0, 0);
		
		//register the Layer as the second from the top
		setChildLayer(REGIONS.BODY_EXISTING_OBJECTS, layerExistingObjectsSecond, 0, 1);
	}

	@Override 
	public int getColumnPositionByIndex(int columnIndex) {
		return layerExistingObjectsSecond.getColumnPositionByIndex(columnIndex);
	}

	@Override
	public int getRowPositionByIndex(int rowIndex) {
		int rowIndexLocal = compositeRowIndex2LayerRowIndex(rowIndex);
		return getLayerByRowIndex(rowIndex).getRowPositionByY( rowIndexLocal );
	}
	
	/**
	 * Returns the IDataProvider, which considers both encapsulated layers.
	 * @return
	 */
	public IDataProvider getIDataProvider(){
		return new IDataProvider() {
			
			@Override
			public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
				int colLocal = columnIndex;
				int rowLocal = compositeRowIndex2LayerRowIndex(rowIndex);
				IDataLayer dataLayer =  (IDataLayer) getLayerByRowIndex(rowIndex);
				dataLayer.getDataLayer().getDataProvider().setDataValue(colLocal, rowLocal, newValue);
			}
			
			@Override
			public int getRowCount() {
				return layerNewObjectsTop.getRowCount() + layerExistingObjectsSecond.getRowCount() ;
			}
			
			@Override
			public Object getDataValue(int columnIndex, int rowIndex) {
				int colLocal = columnIndex;
				int rowLocal = compositeRowIndex2LayerRowIndex(rowIndex);
				IDataLayer dataLayer =  (IDataLayer) getLayerByRowIndex(rowIndex);
				return dataLayer.getDataLayer().getDataProvider().getDataValue(colLocal, rowLocal);
			}
			
			@Override
			public int getColumnCount() {
				return Math.max( layerNewObjectsTop.getColumnCount(), layerExistingObjectsSecond.getColumnCount() );
			}
		};
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.nebula.widgets.nattable.layer.CompositeLayer#getRowIndexByPosition(int)
	 * 
	 * This method maps the row number to the childLayer's rowNumber on default. 
	 * E.g. if there are two vertically aligned layers with 100 rows and 200 rows -
	 * A request to row 150 will return 50. (Request is automatically mapped onto the Layer with 200 rows)
	 *  
	 */
	@Override
	public int getRowIndexByPosition(int compositeRowPosition) {
		return compositeRowPosition;
	}
	
	//PRIVATE
	private ILayer getLayerByRowIndex(int rowIndex){
		return getUnderlyingLayerByPosition(0, rowIndex);		
	}
	
	private int compositeRowIndex2LayerRowIndex(int compositeRowIndex){
		return localToUnderlyingRowPosition(compositeRowIndex);
	}

}
