package de.example.nebula.nattable.externalScrollbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class LayoutCompositVertSash {

	Composite compositeTop;
	Composite compositeBottom;
	
	LayoutCompositVertSash(Composite parent){
		
		parent.setLayout(new FillLayout());
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		
		//CREATE
		compositeTop = new Composite(sashForm, SWT.NONE);
		compositeBottom = new Composite(sashForm, SWT.NONE);
		
		//LAYOUT
		//let the composites fill the parent
		sashForm.setWeights(new int[] {1, 1});
	}
	
	public Composite getCompositeTop() {
		return compositeTop;
	}
	
	public Composite getCompositeBottom() {
		return compositeBottom;
	}

	
}
