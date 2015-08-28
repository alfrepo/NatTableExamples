package de.example.nebula.assembled.nattable.constants;

import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import de.example.nebula.assembled.nattable.utils.Utils;

public final class Colors {
	
	
	// define a color palette. Reuse the colors from the color palette below
	public static final Color BLACK1 = hexToColor("#000000");
	
	public static final Color WHITE1 = hexToColor("#FFFFFF");
	
	public static final Color GRAY1 = hexToColor("#E0E0E0");
	public static final Color GRAY2 = hexToColor("#CCCCCC");
	public static final Color GRAY3 = hexToColor("#C0C0C0");
	public static final Color GRAY4 = hexToColor("#ACACAC");
	
	public static final Color GREEN1 = hexToColor("#7CFF95");
	public static final Color GREEN2 = hexToColor("#9CD167");
	public static final Color GREEN3 = hexToColor("#88BD53");
	public static final Color GREEN4 = hexToColor("#1E4C13");
	
    public static final Color ORANGE1 = hexToColor("#FFE59F");
    public static final Color ORANGE2 = hexToColor("#F5D869");
    public static final Color ORANGE3 = hexToColor("#FFAA00");
    
    public static final Color RED1 = hexToColor("#FFB8B8");
    public static final Color RED2 = hexToColor("#FF5D00");
    public static final Color RED3 = hexToColor("#FF0000");
    
    public static final Color BEIGE1 = hexToColor("#FFF9EF");
    
    public static final Color BLUE1 = hexToColor("#D9E8FB");
    public static final Color BLUE2 = hexToColor("#C5D4E7");
    public static final Color BLUE3 = hexToColor("#88D4D7");
    
    

    private Colors() {

    }
    
    private static final Color hexToColor(String hexValue){
    	return Utils.hexToColor(hexValue);
    }

    public static final int MAX_BASE_COLOR_VALUE = 256;

    public static final Color HEADER_DEFAULT_BACKGROUND_COLOR = GRAY3;
    public static final Color HEADER_DEFAULT_FOREGROUND_COLOR = BLACK1;
    public static final Color HEADER_DEFAULT_FOREGROUND_GRADIENT_COLOR = GRAY3; // top gradient
    public static final Color HEADER_DEFAULT_BACKGROUND_GRADIENT_COLOR = GRAY4; // bottom gradient
    
    public static final Color HEADER_DEFAULT_LINE_COLOR = BLACK1;

    public static final Color HEADER_SELECTION_BACKGROUND_COLOR = GREEN2;
    public static final Color HEADER_SELECTION_FOREGROUND_COLOR = BLACK1;
    public static final Color HEADER_SELECTION_FOREGROUND_GRADIENT_COLOR = GREEN2; // top gradient
    public static final Color HEADER_SELECTION_BACKGROUND_GRADIENT_COLOR = GREEN3; // bottom gradient
    
    
    
    public static final Color ANCHOR_BACKGROUND_COLOR = GUIHelper.COLOR_WIDGET_NORMAL_SHADOW;
    public static final Color ANCHOR_BORDER_COLOR = GUIHelper.COLOR_DARK_GRAY;
    public static final Color ANCHOR_FOREGROUND_COLOR = BLACK1;
    
    
    public static final Color ANCHOR_HEADER_SELECTED_BACKGROUND_COLOR = GREEN2;
    public static final Color ANCHOR_HEADER_SELECTED_FOREGROUND_COLOR = BLACK1;
    public static final Color ANCHOR_HEADER_SELECTED_BORDER_COLOR =  BLACK1;
    
    
    public static final Color ANCHOR_SELECTED_FULLY_BACKGROUND_COLOR = GUIHelper.COLOR_WIDGET_NORMAL_SHADOW;
    public static final Color ANCHOR_GRID_BORDER_COLOR = BLACK1;
    

    
    public static final Color SELECTION_BACKGROUND_COLOR = BLUE1;

    public static final Color SELECTION_COLOR = BLACK1;

    public static final Color SELECTION_BORDER_COLOR = GUIHelper.COLOR_DARK_GRAY;

    public static final Color DIRTY_ROW_COLOR = ORANGE1;

    public static final Color DIRTY_CELL_COLOR = ORANGE3;

    public static final Color REQUIRED_CELL_BORDER_COLOR = RED2; 

    public static final Color CONFLICT_ROW_COLOR = RED1; 

    public static final Color DELETED_ROW_FG_COLOR = RED3;

    public static final Color DELETED_ROW_BG_COLOR = GRAY1; 

    public static final Color INVALID_ROW_FG_COLOR = GRAY1;

    public static final Color INVALID_ROW_BG_COLOR = GRAY1;

    public static final Color ODD_ROW_COLOR = BEIGE1;

    public static final Color EVEN_ROW_COLOR = WHITE1;

    public static final Color ODD_ROW_BG_COLOR_UNASSOCIATED = GRAY2;

    public static final Color EVEN_ROW_BG_COLOR_UNASSOCIATED = GRAY1;

    public static final Color TABLE_BODY_BG_COLOR = ORANGE3;

    public static final Color TABLE_BODY_FG_COLOR = GREEN4;

    public static final Color FILTER_ROW_COLOR = BLUE2;

    public static final Color DARK_SHADOW = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);

    public static final Color SHADOW_GRADIENT_COLOR = BLUE3;

    public static final Color PercentageMBarDecorator_START_COLOR = GREEN1;

    public static final Color PercentageMBarDecorator_MID_COLOR = ORANGE2;

    public static final Color PercentageMBarDecorator_END_COLOR = RED1;
}
