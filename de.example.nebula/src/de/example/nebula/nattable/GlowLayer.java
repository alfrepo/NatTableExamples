package de.example.nebula.nattable;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.nebula.widgets.nattable.blink.IBlinkingCellResolver;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.layer.event.RowUpdateEvent;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * Do not forget to do {@link #registerStyles(IConfigRegistry)} after the NatTabel is created.
 * @author alf
 *
 */
public class GlowLayer extends AbstractLayerTransform {

    private final Set<Integer> glowingRows = new HashSet<>();
    
    private final String GLOW_LABEL_PREFIX = "BLINK_LABEL_INTENSITY1";
    private String glowLabelIntensity1 = "BLINK_LABEL_INTENSITY1";
    private String glowLabelIntensity2 = "BLINK_LABEL_INTENSITY2";
    private String glowLabelIntensity3 = "BLINK_LABEL_INTENSITY3";
    private String glowLabelIntensity4 = "BLINK_LABEL_INTENSITY4";
    private String glowLabelIntensity5 = "BLINK_LABEL_INTENSITY5";
    
    private GlowRequestGenerator glowRequestGenerator;
    
//	private final IUniqueIndexLayer dataLayer;
//	private final IRowDataProvider rowDataProvider;
//	private final IConfigRegistry configRegistry;
//	private final IRowIdAccessor rowIdAccessor;
//	private final IColumnPropertyResolver columnPropertyResolver;
//	private final ScheduledExecutorService scheduler;

    ILayer underlyingLayer;
    DataLayer dataLayer;
    
    IConfigRegistry configRegistry;
    
    String[] labels;
    private final int gradation;
    private final int frequency;
    private final Color glowColor;
    private final Color bgColor;
    
    private final int MAX_ALPHA = 70; //maximum alpha value, which will be used to blend te background color, when glowing.
    
    private int currentLabelPointer=0;
    
    /**
     * 
     * @param underlyingLayer
     * @param gradation - how many steps should be done, when glowing
     * @param frequencyMiliSec - how many milliseconds should be passed between steps
     */
    public GlowLayer(ILayer underlyingLayer,  DataLayer dataLayer,  int gradation, int frequencyMiliSec, Color bgColor, Color glowColor) {
		super(underlyingLayer);
		
		this.underlyingLayer = underlyingLayer;
		this.dataLayer = dataLayer;
		this.configRegistry = configRegistry;
		
		this.gradation = gradation;
		this.frequency = frequencyMiliSec;
		this.glowColor = glowColor;
		this.bgColor = bgColor;
		
		generateLabels();
	}
    
    private void generateLabels(){
    	labels = new String[gradation];
    	for(int i=0; i<gradation; i++){
    		labels[i] = GLOW_LABEL_PREFIX+i;
    	}
    }
    
    public void registerStyles(IConfigRegistry configRegistry){
    	
    	int alpha = 0;
    	int alphaStep = (int) ((double)MAX_ALPHA / (double)gradation);
    	
    	for(String label: labels){
    		Color blendetColor = blend(bgColor, glowColor, alpha, Display.getCurrent());
    		
    		Style cellStyle = new Style();
    		cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, blendetColor);
    		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, label );
    		
    		alpha = Math.min(MAX_ALPHA, alpha+alphaStep);
    	}
    }
    
    
//    public GlowLayer(IUniqueIndexLayer dataLayer,
//            IRowDataProvider listDataProvider,
//            IRowIdAccessor rowIdAccessor,
//            IColumnPropertyResolver columnPropertyResolver,
//            IConfigRegistry configRegistry,
//            boolean triggerBlinkOnRowUpdate,
//            ScheduledExecutorService scheduler) {
//		super(dataLayer);
////		this.dataLayer = dataLayer;
////		this.rowDataProvider = listDataProvider;
////		this.rowIdAccessor = rowIdAccessor;
////		this.columnPropertyResolver = columnPropertyResolver;
////		this.configRegistry = configRegistry;
////		this.scheduler = scheduler;
//		
//		 // TODO del
//		glowingRows.add(1);
//    }
    
    @Override
    public LabelStack getConfigLabelsByPosition(int columnPosition,
    		int rowPosition) {
    	if(!this.glowingRows.contains(rowPosition)){
    		return getUnderlyingLayer().getConfigLabelsByPosition(columnPosition, rowPosition);	
    	}
    	
    	return getBlinkLabels();
    	
//		ILayerCell cell = underlyingLayer.getCellByPosition(columnPosition, rowPosition);
//
//		int columnIndex = getUnderlyingLayer().getColumnIndexByPosition(columnPosition);
//		String columnProperty = columnPropertyResolver.getColumnProperty(columnIndex);
//
//		int rowIndex = getUnderlyingLayer().getRowIndexByPosition(rowPosition);
//		String rowId = rowIdAccessor.getRowId(rowDataProvider.getRowObject(rowIndex)).toString();
//		
//		
//		return getUnderlyingLayer().getConfigLabelsByPosition(columnPosition, rowPosition);
    }
    
    
	/**
	 * Find the {@link IBlinkingCellResolver} from the {@link ConfigRegistry}.
	 * Use the above to find the config types associated with a blinking cell.
	 * @param indexCoordinate 
	 */
	public LabelStack resolveConfigTypes(ILayerCell cell, Object oldValue, Object newValue) {
		// Acquire default config types for the coordinate. Use these to search for the associated resolver.
		LabelStack underlyingLabelStack = underlyingLayer.getConfigLabelsByPosition(cell.getColumnIndex(), cell.getRowIndex());

//		String[] blinkConfigTypes = null;
//		IBlinkingCellResolver resolver = getBlinkingCellResolver(underlyingLabelStack.getLabels());
//		if (resolver != null) {
//		    blinkConfigTypes = resolver.resolve(cell, configRegistry, oldValue, newValue);
//		}
//		if (!ArrayUtils.isEmpty(blinkConfigTypes)) { //blinkConfigTypes != null && blinkConfigTypes.length > 0
//			return new LabelStack(blinkConfigTypes);
//		}
		return underlyingLabelStack;
	}
	
	public void startGlowing(int row){
		this.glowingRows.add(row);
		this.glowRequestGenerator = new GlowRequestGenerator();
		new Thread(this.glowRequestGenerator).start();
	}
	
	public void stopGlowing(int row){
		this.glowingRows.remove(row);
		if(glowingRows.isEmpty()){
			this.glowRequestGenerator.stop();
		}
	}
	
	//PRIVATE
	
	private LabelStack getBlinkLabels(){
		String[] labels = new String[]{"BLINK"};
		return new LabelStack(labels);
	}

	private Color blend(Color bg,  Color blendColor, int alpha, Display display){
		  
		  final Image image = new Image(display, 2, 2);
		  final Rectangle rect = image.getBounds();
		  
		  GC gc = new GC(image);

	      gc.setBackground(bg);
		  gc.fillRectangle(rect);
	      
		  
	      gc.setBackground(blendColor);
	      gc.setAlpha(alpha);
	      gc.fillRectangle(rect);
	      
	      ImageData imageData = image.getImageData();
	      PaletteData palette = imageData.palette;
	      RGB rgb;
	      if(palette.isDirect)
	      {
	         // DirectColorModel
	         rgb = imageData.palette.getRGB(imageData.getPixel(1, 1));
	      }
	      else
	      {
	         // IndexColorModel
	         RGB rgbs[] = palette.getRGBs();
	         rgb = rgbs[imageData.getPixel(1, 1)];
	      }
	      return new Color(display, rgb);
	  }

	//CLASSES

	//responsible for triggering and redrawing the layer. Generated the time requests.
	private class GlowRequestGenerator implements Runnable{
		boolean stopped = false;

		@Override
		public void run() {
			while(!stopped){
				try {
					/*
					 * repaint the rows.
					 * on repaint the GlowingLayer will be asked to draw a label on rows, which are marked as glowingrows.
					 * the GlowingLayer will append the label to the glowing rows.
					 * then the pointer is inceremented, so that the next time an other pointer is drawn.
					 */
					for(int rowIndex:glowingRows){
						dataLayer.fireLayerEvent(new RowUpdateEvent(dataLayer, rowIndex));	
					}
					incrementLabelPointer();
					Thread.sleep(frequency);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		void incrementLabelPointer(){
			currentLabelPointer = (currentLabelPointer ++) % gradation;
		}
		
		void stop(){
			this.stopped = true;
		}
		
	}
	
	
	
	
	
}
