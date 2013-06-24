package de.example.nebula.nattable.editing.validate;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.validate.IDataValidator;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;

public abstract class PureValueValidator implements IDataValidator {

	@Override
	public boolean validate(int columnIndex, int rowIndex, Object newValue) {
		return validate(newValue);
	}

	@Override
	public boolean validate(ILayerCell cell, IConfigRegistry configRegistry,
			Object newValue) {
		return validate(newValue);
	}
	
	public abstract boolean validate(Object newValue);


}
