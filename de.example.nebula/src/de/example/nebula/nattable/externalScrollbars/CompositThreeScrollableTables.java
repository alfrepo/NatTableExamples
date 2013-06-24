package de.example.nebula.nattable.externalScrollbars;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;

public class CompositThreeScrollableTables {
	
	Composite compositeMiddle;
	Composite compositeRight;
	Composite compositeLeft;
	
	LayoutCompositThreeTables layout;

	public CompositThreeScrollableTables(Composite parent, ScrolledComposite scrolledComposite) {
		
		//LAYOUT
		layout = new LayoutCompositThreeTables(parent);
		compositeLeft = layout.compositeLeft;
		compositeMiddle = layout.compositeMiddle;
		compositeRight = layout.compositeRight;

		
		
		//CONTENT
		final TableMain mainTable = new TableMain(scrolledComposite); //pass external scrollable here
		mainTable.createExampleControl(compositeMiddle);
		
		final TableLeft tableLeft = new TableLeft(scrolledComposite, mainTable); //pass external scrollable here
		tableLeft.createTableComposite(compositeLeft);
		((FormData)compositeLeft.getLayoutData()).width = tableLeft.getWidth();
	}
	
	
	
}
