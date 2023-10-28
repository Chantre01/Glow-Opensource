package cn.font;

public class FontManager {
	/*
	* jelloLight
	*/
	public static TTFFontRenderer jelloLight = getSizedFont("jellolight", 18);
	public static TTFFontRenderer jelloMedium = getSizedFont("jellomedium", 18);
	public static TTFFontRenderer jelloRegular = getSizedFont("jelloregular", 18);
	public static TTFFontRenderer jelloRegular16 = getSizedFont("jelloregular", 16);
	public static TTFFontRenderer jelloRegular25 = getSizedFont("jelloregular", 25);

	public static TTFFontRenderer jelloRegular13 = getSizedFont("jelloregular", 13);
	public static TTFFontRenderer wqy13 = getSizedFont("wqy", 13);
	public static TTFFontRenderer wqy16 = getSizedFont("wqy", 16);
	public static TTFFontRenderer wqy18 = getSizedFont("wqy", 18);
	public static TTFFontRenderer wqy25 = getSizedFont("wqy", 25);
	public static TTFFontRenderer wqy = getSizedFont("wqy", 18);


	public static TTFFontRenderer micon25 = getSizedFont("micon", 25);
	public static TTFFontRenderer micon15 = getSizedFont("micon", 15);

	public static TTFFontRenderer micon30 = getSizedFont("micon", 30);


	public static TTFFontRenderer jellofr = getSizedFont("jello", 18);
	public static TTFFontRenderer sigmaJello = getFont("jellolight2");
	public static TTFFontRenderer sfUi = getSizedFont("sfui", 18);
	public static TTFFontRenderer sf20 = getSizedFont("sf", 20);
	public static TTFFontRenderer sfBold20 = getSizedFont("sfbold", 20);

	public static TTFFontRenderer jelloFontBoldSmall = getSizedFont("jelloregular", 19);
	public static TTFFontRenderer jelloFontMarker = getSizedFont("jellolight", 19);
	
	public static TTFFontRenderer getFont(String name) {
		return new TTFFontRenderer(name, 0, 18, 0, 1);
	}
	
	public static TTFFontRenderer getSizedFont(String name, int size) {
		return new TTFFontRenderer(name, 0, size, 0, 1);
	}
	
	public static TTFFontRenderer getFontQuality(String name, float quality) {
		return new TTFFontRenderer(name, 0, 18, 0, quality);

	} 
	
}
