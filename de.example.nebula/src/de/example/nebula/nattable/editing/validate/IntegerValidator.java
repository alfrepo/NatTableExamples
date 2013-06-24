package de.example.nebula.nattable.editing.validate;



public class IntegerValidator extends PureValueValidator{

	@Override
	public boolean validate(Object newValue) {
		try{
			Integer.parseInt((String) newValue);	
			return true;
		}catch(Exception e){}
		return false;
	}
}
