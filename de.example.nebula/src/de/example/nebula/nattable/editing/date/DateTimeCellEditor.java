package de.example.nebula.nattable.editing.date;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.IDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.validate.IDataValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;

public class DateTimeCellEditor extends AbstractDialogCellEditor{
	
	private String simpleDateFormat;
	private boolean inputFieldsListenersLocked = false;
	
	/**
	 * Optionally the simpleDataFormat should be passed in here, which was used {@link DisplayConverter} too. 
	 * @param simpleDateFormat - used for the feedback message. Is optional.
	 */
	public DateTimeCellEditor(String simpleDateFormat) {
		super();
		this.simpleDateFormat = simpleDateFormat;
	}
	
	public DateTimeCellEditor(){
		this(null);
	}
	
	
	@Override
	public Composite createDialogClientArea(Composite parent, final Text cellText, final Object originalCanonicalVal) {
		
		//cell data
//		this.configRegistry;
//		this.getDataTypeConverter() //IDisplayConverter is DefaultDateDisplayConverter here
//		this.getDataValidator();
//		this.conversionEditErrorHandler;
//		this.getCellStyle();
//		getDecorationProvider(); //can display the drop-down arrow?
//		
//		//useful objects
//		this.layerCell;
		
		

		
		Composite clientArea = new Composite(parent, SWT.NONE);
		clientArea.setLayout(new FillLayout());
		
		final DateTime calendarWidget = new DateTime(clientArea, SWT.CALENDAR);
		
		//Synchronize: Cell -> Calendar
		cellText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				
				//org.eclipse.nebula.widgets.nattable.data.validate.DefaultDataValidator.validate is broken.
				//validate manually, by trying to convert the value
				Object canonicalValue = null;
				Date canonicalDate = null;
				boolean isValid = true;
				try{
					IDisplayConverter defaultDateDisplayConverter = getDataTypeConverter();
					canonicalValue = defaultDateDisplayConverter.displayToCanonicalValue(layerCell, configRegistry, cellText.getText()); //Date.class
					canonicalDate = (Date) canonicalValue;
				}catch(Throwable exception){
					isValid = false;
				}
				
				//now use the valid value
				if(isValid && canonicalDate != null ){
					
					try{
						inputFieldsListenersLocked = true;
						
						//converter, which hopefully will convert the String to a Date object
						System.out.println(canonicalValue);
						Calendar dateConverter = Calendar.getInstance();
						dateConverter.setTime(canonicalDate);
						
						calendarWidget.setDay(dateConverter.get(Calendar.DAY_OF_MONTH));
						calendarWidget.setMonth(dateConverter.get(Calendar.MONTH));
						calendarWidget.setYear(dateConverter.get(Calendar.YEAR));
						
					}catch(SWTException se){
						//Widget disposed
					}finally{
						inputFieldsListenersLocked = false;
					}
					
				}
			}
		});
		
		//Synchronize: Cell <- Calendar
		System.out.println("originalCanonicalVal: "+originalCanonicalVal); //Date.class
		calendarWidget.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(inputFieldsListenersLocked == false){
				
					try{
						inputFieldsListenersLocked = true;
						
						//Calendar replaces the deprecated Date.class constructors
						Calendar converterCalendar;
						converterCalendar = Calendar.getInstance(); 
						converterCalendar.set(calendarWidget.getYear(), calendarWidget.getMonth(), calendarWidget.getDay());
						
						//Date Object, which's value must be copied to the table cell
						Date canonicalValue = converterCalendar.getTime();			
						
						//converter, which hopefully will convert the Date to String, with the right format 
						IDisplayConverter defaultDateDisplayConverter = getDataTypeConverter();
						Object displayValue = defaultDateDisplayConverter.canonicalToDisplayValue(layerCell, configRegistry, canonicalValue);
						
						cellText.setText( displayValue.toString() );
						
						//hide the cell-error caused by wrong input 
						hideErors();
						
					}finally{
						inputFieldsListenersLocked = false;
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		return clientArea;
	}

	@Override
	public String getValidationErrorMessage() {
		String message;
		message = "Expecting a Date";
		
		if(simpleDateFormat != null){
			message += " formatted as "+simpleDateFormat;	
		}
		
		return message;
	}

	@Override
	public IDataValidator getValidator() {
		return null;
	}


}
