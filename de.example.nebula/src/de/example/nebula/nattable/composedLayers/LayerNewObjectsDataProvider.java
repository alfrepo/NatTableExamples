package de.example.nebula.nattable.composedLayers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.examples.fixtures.Person;

public class LayerNewObjectsDataProvider implements IDataProvider {

	private List<Person> people;

		
	public LayerNewObjectsDataProvider() {
		people = new ArrayList<Person>();
		Person p;
		for(int i=0; i<=100; i++){
			p = new Person(150, "Hulk"+i, new Date(6000000));
			people.add(p);
		}
	}

			
	@Override
	public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
		
	}
	
	@Override
	public int getRowCount() {
		return people.size();
	}
	
	@Override
	public Object getDataValue(int columnIndex, int rowIndex) {
		Person p = people.get(rowIndex);
		if(columnIndex == 0){
			return p.getId();
		}else if(columnIndex == 1){
			return p.getName();
		}else{
			return p.getBirthDate();
		}
	}
	
	@Override
	public int getColumnCount() {
		return 14;
	};

}

