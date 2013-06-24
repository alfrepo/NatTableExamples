package de.example.nebula.nattable.composedLayers;

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


public class MyStandaloneNatExampleRunner {
	
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
		clientArea.setLayout(new FillLayout());
		
		
		
//	    Composite c = new Composite(clientArea, SWT.NONE);
//	    c.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		
//		TableComposedThreeScrollableTables composedThreeScrollableTables = new TableComposedThreeScrollableTables(clientArea, scrolledComposite);
	    
		new TableAllLayers(clientArea, scrolledComposite);
	}
	
	public static void main(String[] args) throws Exception {
		new MyStandaloneNatExampleRunner().run();
	}

}