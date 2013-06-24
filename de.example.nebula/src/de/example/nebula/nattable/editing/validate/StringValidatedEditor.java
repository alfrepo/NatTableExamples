package de.example.nebula.nattable.editing.validate;

import org.eclipse.nebula.widgets.nattable.data.validate.IDataValidator;


public class StringValidatedEditor extends ValidatedTextCellEditor {

	@Override
	public String getValidationErrorMessage() {
		return "Expecting a word.";
	}

	@Override
	public IDataValidator getValidator() {
		return null;
	}
}
