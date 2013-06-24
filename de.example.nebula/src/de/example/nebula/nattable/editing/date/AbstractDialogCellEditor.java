package de.example.nebula.nattable.editing.date;

import org.eclipse.nebula.widgets.nattable.data.convert.IDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.editor.TextCellEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import de.example.nebula.nattable.editing.validate.ValidatedTextCellEditor;

/**
 * A base dialog editor, which will be used to construct every other dialog in the system. 
 * On default it uses a Text widget to display the cell in Edit-mode.
 * It It can be switched to any other control by overriding the {@link TextCellEditor#activateCell(Composite, Object, Character, org.eclipse.nebula.widgets.nattable.widget.EditModeEnum, org.eclipse.nebula.widgets.nattable.edit.ICellEditHandler, org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell, org.eclipse.nebula.widgets.nattable.config.IConfigRegistry)} 
 * method.
 *
 * This method provides a function, for dialog creation, in order for the dialog to be used as an input widget.
 * 
 * @author alf
 *
 */
public abstract class AbstractDialogCellEditor extends ValidatedTextCellEditor {
	
	private PopDropdown popDropdown;
	
	
	/**
	 * Generates a control, which will be used inside the cell, when the cell is set to edit-mode.
	 * Every time, when this method is triggered - a new control is generated.
	 * Hook in here, to spawn a Dialog, which will depend on the cell input. 
	 * 
	 */
	@Override
	protected Control activateCell(Composite parent,
			Object originalCanonicalValue, Character initialEditValue) {
		
		final Text text = (Text) super.activateCell(parent, originalCanonicalValue, initialEditValue);
		final Object originalCanonicalValueFinal = originalCanonicalValue;
		
		//open the Dropdown, which will be Display the Input widgets 
		Point leftBottomCellPosition = getLeftBottomCellPosition(parent);
		popDropdown = new PopDropdown(parent.getShell(), "Title"){
			@Override
			protected Composite createContentArea(Composite parentOfDialog) {
				return createDialogClientArea(parentOfDialog, text, originalCanonicalValueFinal);
			}
		};
		popDropdown.setStyle(PopDropdown.CLOSE_ON_PARENT_MOVE | PopDropdown.CLOSE_ON_ESC );
		popDropdown.open(leftBottomCellPosition.x, leftBottomCellPosition.y);
		
		
		return text;
	}
	
	@Override
	public void close() {
		
		if( Display.getCurrent().getActiveShell() == popDropdown.getShell() ){
			/*
			 * don't close the editor, because the dropdown has got the focus,
			 * and so the editing goes on
			 */
			
		}else{
			super.close();
			popDropdown.close();
		}
	}
	
	
	//PRIVATE
	private Point getLeftBottomCellPosition(Composite parent){
		//Relative position of the Cell
		Rectangle bounds = this.layerCell.getBounds();
		
		//absolute position of the cell
		Point absolutePos = parent.toDisplay(bounds.x, bounds.y+bounds.height);
		
		return absolutePos;
	}
	
	//ABSTRACT
	/**
	 * Will create a dialog with widgets, which will help to edit the cell usable 
	 * @param parent - the parent, for constructing SWT widgets.
	 * @param cellText - the widget, which is used inside the cell, for editing the text
	 * @param originalCanonicalValue - the value of the cell, before the conversion with {@link IDisplayConverter} was applied to it.
	 * @return - a composite, which will be used as a client ares for the dropdown dialog
	 */
	public abstract Composite createDialogClientArea(Composite parent, Text cellText, Object originalCanonicalValue);
	
	
}
