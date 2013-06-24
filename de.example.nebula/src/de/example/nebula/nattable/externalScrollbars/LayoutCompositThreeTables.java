package de.example.nebula.nattable.externalScrollbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Layout, which represents three Containers in a horizontal row:  [][   ][]
 * @author alf
 *
 */
public class LayoutCompositThreeTables extends Layout{

	
	public static int SHADOW_WDITH = 5; //px
	
	Composite compositeLeft;
	Composite compositeMiddle;
	Composite compositeRight;
	
	
    protected Color colorBgcolor;
    protected Color colorForeground;
    protected Color colorBorder;
    protected Color colorDarkShadow;
    protected Color colorHighlightShadow;
    protected Color colorLightShadow;
    protected Color colorNormalShadow;
    protected Color colorTextMouseover;
    protected Color colorTextMouseout;
    protected Color colorText;
	
	LayoutCompositThreeTables(Composite parent) {
		super(parent);
		
        colorBgcolor = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
        colorForeground = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
        colorBorder = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BORDER);
        colorDarkShadow = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);
        colorHighlightShadow = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
        colorLightShadow = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
        colorNormalShadow = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
        colorTextMouseout = colorDarkShadow;
        colorTextMouseover = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
		
		
		
		//CREATE
		compositeRight = new Composite(parent, SWT.NONE);
		compositeMiddle = new Composite(parent, SWT.NONE);
		compositeLeft = new Composite(parent, SWT.NONE);
		
		Canvas canvasLeftShadow = new Canvas(parent, SWT.NONE);
		Canvas canvasRightShadow = new Canvas(parent, SWT.NONE);
		
		
		
		//LAYOUT LEVEL 1
		parent.setLayout(new FormLayout());
		
		
		
		FormData fdCompositeLeft = new FormData();
		fdCompositeLeft.width = 100;
		fdCompositeLeft.top = new FormAttachment(0);
		fdCompositeLeft.left = new FormAttachment(0, 0);
		fdCompositeLeft.bottom = new FormAttachment(100);
		compositeLeft.setLayoutData(fdCompositeLeft);
		
		
		FormData fdCompositeLeftShadow = new FormData();
		fdCompositeLeftShadow.width = SHADOW_WDITH;
		fdCompositeLeftShadow.top = new FormAttachment(0);
		fdCompositeLeftShadow.left = new FormAttachment(compositeLeft, 0);
		fdCompositeLeftShadow.bottom = new FormAttachment(100);
		canvasLeftShadow.setLayoutData(fdCompositeLeftShadow);
		
		
		
		FormData fdCompositeMiddle = new FormData();
		fdCompositeMiddle.top = new FormAttachment(0);
		fdCompositeMiddle.left = new FormAttachment(canvasLeftShadow, 0);
		fdCompositeMiddle.right = new FormAttachment(canvasRightShadow, 0);
		fdCompositeMiddle.bottom = new FormAttachment(100);
		compositeMiddle.setLayoutData(fdCompositeMiddle);
		
		
		
		FormData fdCompositeRightShadow = new FormData();
		fdCompositeRightShadow.width = SHADOW_WDITH;
		fdCompositeRightShadow.top = new FormAttachment(0);
		fdCompositeRightShadow.right = new FormAttachment(compositeRight, 0);
		fdCompositeRightShadow.bottom = new FormAttachment(100);
		canvasRightShadow.setLayoutData(fdCompositeRightShadow);
		
		
		
		FormData fdCompositeRight = new FormData();
		fdCompositeRight.width = 60;
		fdCompositeRight.top = new FormAttachment(0);
		fdCompositeRight.right = new FormAttachment(100, 0);
		fdCompositeRight.bottom = new FormAttachment(100);
		compositeRight.setLayoutData(fdCompositeRight);
		
		

		
		
		
		//LAYOUT LEVEL 2
		compositeLeft.setLayout(new FillLayout());
		compositeMiddle.setLayout(new FillLayout());
		compositeRight.setLayout(new FillLayout());
		
		canvasLeftShadow.setLayout(new FillLayout());
		canvasRightShadow.setLayout(new FillLayout());
		
		
		
		
		//STYLE
		compositeLeft.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		compositeRight.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		
		//draw shadows
		drawShadows(canvasLeftShadow, canvasRightShadow);
	}


	public Composite getCompositeMiddle() {
		return compositeMiddle;
	}


	public Composite getCompositeRight() {
		return compositeRight;
	}


	public Composite getCompositeLeft() {
		return compositeLeft;
	}
	
	
	
	
	//DRAWING
	
	private void drawShadows(final Canvas left, final Canvas right){
        drawShadow(left,colorLightShadow, colorHighlightShadow );
        drawShadow(right,colorLightShadow, colorHighlightShadow );
	}
	
	private void drawShadow(final Canvas canvas, final Color left, final Color right){
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				Rectangle rectLeft = canvas.getClientArea();
				e.gc.setForeground(left);
                e.gc.setBackground(right);
                e.gc.fillGradientRectangle(rectLeft.x, rectLeft.y, rectLeft.width, rectLeft.height, false);
			}
		});
	}
}

