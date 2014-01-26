package de.example.nebula.nattable.widget.progress;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;

/** Used to map Cell's value to the scala from 0 till 1 */
public interface IPercentageConverter {

    /** Converts the value of the cell to map it to the interval from 0 till 100 */
    int convert(ILayerCell layerCell, IConfigRegistry configRegistry, Object cellDataValue);
}
