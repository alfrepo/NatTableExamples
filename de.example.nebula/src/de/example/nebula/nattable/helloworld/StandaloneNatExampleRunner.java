package de.example.nebula.nattable.helloworld;

import org.eclipse.nebula.widgets.nattable.examples.INatExample;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class StandaloneNatExampleRunner {
	
	Composite composite_left;
	Composite composite_right;

	public void run(INatExample example) {
		run(800, 800, example);
	}

	public void run(int shellWidth, int shellHeight, INatExample example) {
		// Setup
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setSize(shellWidth, shellHeight);
		shell.setText(example.getName());

		createContents(example, shell);
		
		// Start
		example.onStart();

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		// Stop
		example.onStop();

		composite_left.dispose();
		composite_right.dispose();

		shell.dispose();
		display.dispose();
	}
	
	protected void createContents(INatExample leftControl, Shell shell) {
		
		shell.setLayout(new FormLayout());
		
		composite_left = (Composite) ( leftControl).createExampleControl(shell);
//		composite_right = new Composite(shell, SWT.NONE);
		
		FormData fd_composite_left = new FormData();
		fd_composite_left.top = new FormAttachment(0);
		fd_composite_left.left = new FormAttachment(0, 0);
//		fd_composite_left.right = new FormAttachment(composite_right, 0);
		fd_composite_left.right = new FormAttachment(100, 0);
		fd_composite_left.bottom = new FormAttachment(100);
		composite_left.setLayoutData(fd_composite_left);
		
//		FormData fd_composite_right = new FormData();
//		fd_composite_right.width = 60;
//		fd_composite_right.top = new FormAttachment(0);
//		fd_composite_right.right = new FormAttachment(100, 0);
//		fd_composite_right.bottom = new FormAttachment(100);
//		composite_right.setLayoutData(fd_composite_right);
	}

}