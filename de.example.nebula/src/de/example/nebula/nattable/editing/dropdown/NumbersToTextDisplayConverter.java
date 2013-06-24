package de.example.nebula.nattable.editing.dropdown;

import java.util.HashMap;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.convert.IDisplayConverter;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;

public class NumbersToTextDisplayConverter implements IDisplayConverter{

	HashMap<Integer, String> map1 = new HashMap<>();
	
	public NumbersToTextDisplayConverter() {
		map1.put(1, "Einzelticket");
		map1.put(99, "Rücknahme");
		map1.put(98, "Aufbuchung");
	}
	
	@Override
	public Object canonicalToDisplayValue(Object canonicalValue) {
		
		Object result = canonicalValue;
		Object display = map1.get(canonicalValue);
		if(display != null){
			result = display;
		}
		return String.valueOf(result);
	}

	@Override
	public Object displayToCanonicalValue(Object displayValue) {
		Object result = displayValue;
		
		Object canonical = null;
		for(Integer k: map1.keySet() ){
			if(map1.get(k).equals(displayValue) ){
				canonical = k;
				break;
			}
		}
		
		if(canonical != null){
			result = canonical;
		}
		return result;
	}

	@Override
	public Object canonicalToDisplayValue(ILayerCell cell,
			IConfigRegistry configRegistry, Object canonicalValue) {
		return canonicalToDisplayValue(canonicalValue);
	}

	@Override
	public Object displayToCanonicalValue(ILayerCell cell,
			IConfigRegistry configRegistry, Object displayValue) {
		return displayToCanonicalValue(displayValue);
	}
}
