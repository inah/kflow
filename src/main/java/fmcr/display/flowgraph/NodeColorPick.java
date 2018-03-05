package fmcr.display.flowgraph;

import java.util.Random;

public class NodeColorPick {
	private static Random abnormalKeyReset = new Random();
	
	final static String ORANGE = "#F1C40F";
	final static String BLACK	= "#000000";
	final static String SILVER	= "#C0C0C0";
	final static String GREEN2 = "#16A085";
	final static String RED		= "#FF0000";
	final static String GRAY	= "#808080";
	final static String MAROON = "#800000";
	final static String YELLOW = "#FFFF00";
	final static String OLIVE  = "#808000";
	final static String LIME   = "#00FF00";
	final static String GREEN	= "#008000";
	final static String AQUA	= "#00FFFF";
	final static String TEAL	= "#008080";
	final static String BLUE	= "#0000FF";
	final static String BLUE2  = "#2471A3";
	final static String NAVY	= "#000080";
	final static String FUCHSIA	= "#FF00FF";
	final static String PURPLE	= "#800080";
	final static String RED2   = "#B03A2E";
	
	public static String getColor(int keyIndex){
		String color;
		
		if(keyIndex >17) {
			keyIndex = abnormalKeyReset.nextInt((17 - 0) + 1) + 0;
		}
		switch(keyIndex) {
		case 0:
			color= ORANGE;
			break;
		case 1:
			color= GREEN2;
			break;
		case 2:
			color= RED;
			break;
		case 3:
			color= SILVER;
			break;
		case 4:
			color= MAROON;
			break;
		case 5:
			color= GRAY;
			break;
		case 6:
			color= YELLOW;
			break;
		case 7:
			color= OLIVE;
			break;
		case 8:
			color= LIME;
			break;
		case 9:
			color= GREEN;
			break;
		case 10:
			color= AQUA;
			break;
		case 11:
			color= TEAL;
			break;
		case 12:
			color= BLUE;
			break;
		case 13:
			color= BLUE2;
			break;
		case 14:
			color= NAVY;
			break;
		case 15:
			color= FUCHSIA;
			break;
		case 16:
			color= PURPLE;
			break;
		case 17:
			color= RED2;
			break;
		default:
			color= BLACK;

		}
		return color;
	}
}
