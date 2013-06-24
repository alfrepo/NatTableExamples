package de.example.nebula.nattable.externalScrollbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;

public class CompositHorScrollbar {
	
	ScrolledComposite scrolledComposite;
	Composite clientArea;
	
	LayoutCompositHorScrollbar layoutHorScroll;

	public CompositHorScrollbar(Composite parent) {
		
		//LAYOUT
		layoutHorScroll = new LayoutCompositHorScrollbar(parent);
		
		
		//INIT
		clientArea = layoutHorScroll.clientArea;
		scrolledComposite = layoutHorScroll.scrolledComposite;
		

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

		//CONTENT
		new CompositTwoVerticalTables(clientArea, scrolledComposite);
	}
	
	
	
	
}
