package de.example.nebula.nattable.externalScrollbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class CompositTwoVerticalTables {
	
	Composite compositeTop;
	Composite compositeBottom;
	
	LayoutCompositVertSash layout;

	public CompositTwoVerticalTables(Composite parent, ScrolledComposite scrolledComposite) {
		
		//LAYOUT
		layout = new LayoutCompositVertSash(parent);
		
		//INIT
		compositeTop = layout.compositeTop;
		compositeBottom = layout.compositeBottom;

		
		
		//CONTENT
//		final TableMain mainTable = new TableMain(scrolledComposite); //pass external scrollable here
//		mainTable.createExampleControl(compositeMiddle);
//		
//		final TableLeft tableLeft = new TableLeft(scrolledComposite, mainTable); //pass external scrollable here
//		tableLeft.createTableComposite(compositeLeft);
//		((FormData)compositeLeft.getLayoutData()).width = tableLeft.getWidth();
		
		
		//STYLE
		compositeTop.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		compositeBottom.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
	}
	
	
	
}
