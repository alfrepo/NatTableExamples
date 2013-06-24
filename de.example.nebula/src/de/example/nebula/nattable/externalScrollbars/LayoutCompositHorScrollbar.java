package de.example.nebula.nattable.externalScrollbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public class LayoutCompositHorScrollbar {

	
	ScrolledComposite scrolledComposite;
	Composite clientArea;
	
	LayoutCompositHorScrollbar(Composite parent){
		//INIT
		scrolledComposite = new ScrolledComposite(parent, SWT.NONE);
		clientArea = new Composite(scrolledComposite, SWT.NONE);
		
		scrolledComposite.setContent(clientArea);
	    scrolledComposite.setExpandVertical(true);
	    scrolledComposite.setExpandHorizontal(true);
	    scrolledComposite.setAlwaysShowScrollBars(true);
	    scrolledComposite.getVerticalBar().setVisible(false);
	    scrolledComposite.getVerticalBar().setEnabled(false);
	    scrolledComposite.getHorizontalBar().setVisible(true);
	    scrolledComposite.getHorizontalBar().setEnabled(true);
	    
	    Point size = scrolledComposite.getSize();
		clientArea.setSize(size);
	}
	
	public Composite getClientArea() {
		return clientArea;
	}
	
	public ScrolledComposite getScrolledComposite() {
		return scrolledComposite;
	}

}
