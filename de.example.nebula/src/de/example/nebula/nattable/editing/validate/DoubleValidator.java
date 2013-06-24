package de.example.nebula.nattable.editing.validate;



public class DoubleValidator extends PureValueValidator{

	@Override
	public boolean validate(Object newValue) {
		try{
			Double result = Double.parseDouble((String) newValue);	
			return true;
		}catch(Exception e){}
		return false;
	}
}
