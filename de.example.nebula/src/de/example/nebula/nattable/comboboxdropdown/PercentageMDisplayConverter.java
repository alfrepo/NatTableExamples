package de.example.nebula.nattable.comboboxdropdown;

import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;

public class PercentageMDisplayConverter extends DisplayConverter {

	public Object canonicalToDisplayValue(Object canonicalValue) {
		if (canonicalValue != null) {
			double percentageValue = ((Integer) canonicalValue).intValue();
			double displayInt = percentageValue / 200 * 100;
			return String.valueOf(displayInt) + "%"; //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}

	public Object displayToCanonicalValue(Object displayValue) {
		String displayString = (String) displayValue;
		displayString = displayString.trim();
		if (displayString.endsWith("%")) { //$NON-NLS-1$
			displayString = displayString.substring(0, displayString.length() - 1);
		}
		displayString = displayString.trim();
		int displayInt = Integer.valueOf(displayString).intValue();
		double percentageValue = (double) displayInt * 200 / 100;
		return Double.valueOf(percentageValue);
	}
}