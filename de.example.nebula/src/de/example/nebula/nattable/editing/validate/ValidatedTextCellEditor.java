package de.example.nebula.nattable.editing.validate;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.validate.IDataValidator;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.TextCellEditor;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 
 * @author alf
 * 
 * ACHTUNG: use the {@link #register(IConfigRegistry, String)} method, instead, to make sure that both - the validator and the editor are both registered.  
 * 
 *
 */
public abstract class ValidatedTextCellEditor extends TextCellEditor {

	/**
	 * Registers the editor and a validator to the cell.
	 * @param configRegistry - the table registry
	 * @param configLabel - the cell / column label
	 */
	public ICellEditor register(IConfigRegistry configRegistry,  String configLabel){
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, this, DisplayMode.NORMAL, configLabel);
		configRegistry.registerConfigAttribute(EditConfigAttributes.DATA_VALIDATOR, this.getValidator(), DisplayMode.EDIT, configLabel);
		return this;
	}
	

	
	@Override
	protected Control activateCell(Composite parent,
			Object originalCanonicalValue, Character initialEditValue) {
		
		if(getValidationErrorMessage() != null){
			this.setErrorDecorationEnabled(true);
			this.setErrorDecorationText(getValidationErrorMessage());
			this.setDecorationPositionOverride(SWT.LEFT | SWT.TOP);	
		}
		
		return super.activateCell(parent, originalCanonicalValue, initialEditValue);
	}

	/**
	 * Returns the Validator, set in the current Editor, if it exists.
	 * Otherwise it drops to the default Validator, which is defined through the Registry.
	 */
	@Override
	protected IDataValidator getDataValidator() {
		IDataValidator dataValidator = getValidator();
		if(dataValidator != null){
			return dataValidator;
		}
		return super.getDataValidator();
	}
	
	//ABSTRACT
	
	/**
	 * Used to tell the user, which input is inspected for the current input box 
	 * @return - the hint message, about the right information format for the current text box
	 */
	public abstract String getValidationErrorMessage();
	
	/**
	 * Used to validate the cell input. On error it will display the error message, returned by the {@link #getValidationErrorMessage()}
	 * @return - the Object, responsible for Input validation. May return null, then the default validator will be used, if exists. 
	 */
	public abstract IDataValidator getValidator();
	
	
	
	//PROTECTED
	protected void hideErors(){
		decorationProvider.hideDecoration(); //ControlDecorationProvider, hides the red x icon near the editor
		validationEditErrorHandler.removeError(this);
		conversionEditErrorHandler.removeError(this);
		
		//reset the text color
		IStyle cellStyle = this.getCellStyle();
		Color textColor = cellStyle.getAttributeValue(CellStyleAttributes.FOREGROUND_COLOR);
		this.getTextControl().setForeground(textColor);
	}
	
	

}
