package de.example.nebula.nattable.externalScrollbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;


public class MyStandaloneNatExampleRunner2 {
	
	public ScrollBar vbar;

	public void run() {
		run(800, 800);
	}

	public void run(int shellWidth, int shellHeight ) {
		// Setup
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setSize(shellWidth, shellHeight);

		createContents(shell);
		

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		shell.dispose();
		display.dispose();
	}
	
	
	protected void createContents( Shell shell) {
		
		//CREATE
		final ScrolledComposite scrolledComposite = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		//TODO remove existing listener. Will implement my own later, inside of the table
				for( Listener l : scrolledComposite.getVerticalBar().getListeners(SWT.Selection)){
		    		scrolledComposite.getVerticalBar().removeListener(SWT.Selection, l);
		    	}
				for( Listener l : scrolledComposite.getHorizontalBar().getListeners(SWT.Selection)){
		    		scrolledComposite.getHorizontalBar().removeListener(SWT.Selection, l);
		    	}
		final Composite clientArea = new Composite(scrolledComposite, SWT.NONE);
		
		
		
		//LAYOUT
		shell.setLayout(new FillLayout());
		
		
		scrolledComposite.setContent(clientArea);
	    scrolledComposite.setExpandVertical(true);
	    scrolledComposite.setExpandHorizontal(true);
	    scrolledComposite.setAlwaysShowScrollBars(true);
	    scrolledComposite.getVerticalBar().setVisible(true);
	    scrolledComposite.getHorizontalBar().setVisible(true);
	    
	    Point size = scrolledComposite.getSize();
		clientArea.setSize(size);
		
	    
	    
	    //EDIT: do it by passing the scrollBars to the ViewportLayer
	    //Sync scrollBar and NatTable 
	    	// set the right height of content in ScrollCommposite
	    	// catch the scroll events from ScrollComposite
	    	// pass the scroll events to content in ScrollComposite 
	    	// pass the scroll events to NatTable, control the NatTable' scroll remotely
	    
	    
		
		CompositThreeScrollableTables composedThreeScrollableTables = new CompositThreeScrollableTables(clientArea, scrolledComposite);
		
	}
	
    //TODO del
	public static void main(String[] args) throws Exception {
		new MyStandaloneNatExampleRunner2().run();
	}

}