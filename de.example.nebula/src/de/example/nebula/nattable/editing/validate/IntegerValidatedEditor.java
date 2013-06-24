package de.example.nebula.nattable.editing.validate;

import org.eclipse.nebula.widgets.nattable.data.validate.IDataValidator;


public class IntegerValidatedEditor extends ValidatedTextCellEditor {

	@Override
	public String getValidationErrorMessage() {
		return "Expecting a number of form 00..0";
	}

	@Override
	public IDataValidator getValidator() {
		return new IntegerValidator();
	}


}
