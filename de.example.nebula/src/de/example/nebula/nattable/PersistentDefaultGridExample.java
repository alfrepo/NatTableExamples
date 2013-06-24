package de.example.nebula.nattable;

import org.eclipse.nebula.widgets.nattable.examples.PersistentNatExampleWrapper;
import org.eclipse.nebula.widgets.nattable.examples.examples._000_Default_NatTable;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;

public class PersistentDefaultGridExample extends PersistentNatExampleWrapper {
	
	public static void main(String[] args) throws Exception {
		StandaloneNatExampleRunner.run(new PersistentDefaultGridExample());
	}
	
	public PersistentDefaultGridExample() {
		super(new _000_Default_NatTable());
	}
	
}

