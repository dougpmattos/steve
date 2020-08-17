package model.spatialView.media;

import java.io.Serializable;

import javafx.scene.paint.Color;
import model.spatialView.media.enums.FontFamily;
import model.spatialView.media.enums.FontStyle;
import model.spatialView.media.enums.FontWeight;
import model.utility.RGBColor;

public class TextStyleProperty extends StyleProperty implements Serializable{

	private static final long serialVersionUID = 6367190485869850465L;
	
	private FontFamily fontFamily = FontFamily.TIMES_NEW_ROMAN;
	private Double fontSize = 12.0;
	private FontStyle fontStyle = FontStyle.NORMAL;
	private FontWeight fontWeight = FontWeight.NORMAL;
	private RGBColor fontColor = new RGBColor(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue());
	
	public TextStyleProperty(){
		
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
	
	public void setFontColor(RGBColor fontColor){
		this.fontColor = fontColor;
	}
	
	public RGBColor getFontColor(){
		return fontColor;
	}
	
}
