package model.spatialView;

import javafx.scene.paint.Color;

public class TextProperty extends PresentationProperty{

	private static final long serialVersionUID = 6367190485869850465L;
	
	private FontFamily fontFamily;
	private Double fontSize;
	private FontStyle fontStyle;
	private FontWeight fontWeight;
	private String fontColor;
	
	public TextProperty(){
		
		super();
		
		fontFamily = FontFamily.TIMES_NEW_ROMAN;
		fontSize = 12.0;
		fontStyle = FontStyle.NORMAL;
		fontWeight = FontWeight.NORMAL;
		fontColor = Color.WHITE.toString();
		
	}
	
	public void setFontFamily(FontFamily fontFamily){
		this.fontFamily = fontFamily;
	}
	
	public FontFamily getFontFamily(){
		return fontFamily;
	} 
	
	public void setFontSize(Double fontSize){
		this.fontSize = fontSize;
	}
	
	public Double getFontSize(){
		return fontSize;
	} 
	
	public void setFontStyle(FontStyle fontStyle){
		this.fontStyle = fontStyle;
	}
	
	public FontStyle getFontStyle(){
		return fontStyle;
	} 
	
	public void setFontWeight(FontWeight fontWeight){
		this.fontWeight = fontWeight;
	}
	
	public FontWeight getFontWeight(){
		return fontWeight;
	} 
	
	public void setFontColor(String fontColor){
		this.fontColor = fontColor;
	}
	
	public String getFontColor(){
		return fontColor;
	}
	
}
