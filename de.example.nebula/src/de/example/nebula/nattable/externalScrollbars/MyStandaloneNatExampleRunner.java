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
		
		
		//TODO hide scrolledComposite
//		scrolledComposite.setContent(clientArea);
//	    scrolledComposite.setExpandVertical(true);
//	    scrolledComposite.setExpandHorizontal(true);
//	    scrolledComposite.setAlwaysShowScrollBars(true);
//	    scrolledComposite.getVerticalBar().setVisible(true);
//	    scrolledComposite.getHorizontalBar().setVisible(true);
//	    
//	    Point size = scrolledComposite.getSize();
//		clientArea.setSize(size);
		
	    
	    
	    //EDIT: do it by passing the scrollBars to the ViewportLayer
	    //Sync scrollBar and NatTable 
	    	// set the right height of content in ScrollCommposite
	    	// catch the scroll events from ScrollComposite
	    	// pass the scroll events to content in ScrollComposite 
	    	// pass the scroll events to NatTable, control the NatTable' scroll remotely
	    
	    
		
	    	
	    	
	    
//		clientArea.setLayout(new FormLayout());
//		
//		
//		
//		FormData fd_composite_middle = new FormData();
//		fd_composite_middle.top = new FormAttachment(0);
//		fd_composite_middle.left = new FormAttachment(composite_left, 0);
//		fd_composite_middle.right = new FormAttachment(composite_right, 0);
////		fd_table_composite_left.right = new FormAttachment(100, 0);
//		fd_composite_middle.bottom = new FormAttachment(100);
//		composite_middle.setLayoutData(fd_composite_middle);
//		
//		
//		
//		FormData fd_composite_right = new FormData();
//		fd_composite_right.width = 60;
//		fd_composite_right.top = new FormAttachment(0);
//		fd_composite_right.right = new FormAttachment(100, 0);
//		fd_composite_right.bottom = new FormAttachment(100);
//		composite_right.setLayoutData(fd_composite_right);
//		
//		FormData fd_composite_left = new FormData();
//		fd_composite_left.width = 100;
//		fd_composite_left.top = new FormAttachment(0);
//		fd_composite_left.left = new FormAttachment(0, 0);
//		fd_composite_left.bottom = new FormAttachment(100);
//		composite_left.setLayoutData(fd_composite_left);
//		
//		
//		
//		composite_left.setLayout(new FillLayout());
//		composite_middle.setLayout(new FillLayout());
//		composite_right.setLayout(new FillLayout());
//		
//		
//		//style
//		composite_left.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
//		composite_right.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
//		
//		
//		//Fill Content
//		final Table table = new Table(scrolledComposite); //pass external scrollable here
//		table.createExampleControl(composite_middle);
//		
////		final VerticallyDependendNatTable tableLeft = new VerticallyDependendNatTable(scrolledComposite, table.getBodyLayer() ); //pass external scrollable here
//		final TableVerticallyDependend tableLeft = new TableVerticallyDependend(scrolledComposite, table.getBodyLayer() ); //pass external scrollable here
//		tableLeft.createExampleControl(composite_left);
		
		
		
		//
		//TODO hide table
//		TableComposedThreeScrollableTables composedThreeScrollableTables = new TableComposedThreeScrollableTables(clientArea, scrolledComposite);
		
		
//		new CompositHorScrollbar(shell);
	}

	
    //TODO del
	public static void main(String[] args) throws Exception {
		new MyStandaloneNatExampleRunner().run();
	}
}