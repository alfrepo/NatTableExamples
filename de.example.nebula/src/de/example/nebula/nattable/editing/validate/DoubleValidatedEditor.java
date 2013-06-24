package de.example.nebula.nattable.editing.validate;

import org.eclipse.nebula.widgets.nattable.data.validate.IDataValidator;


public class DoubleValidatedEditor extends ValidatedTextCellEditor {

	@Override
	public String getValidationErrorMessage() {
		return "Expecting a number of form 0.0";
	}

	@Override
	public IDataValidator getValidator() {
		return new DoubleValidator();
	}


}
