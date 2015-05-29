package model.spatialView;

import gui.spatialViewPane.CropPane;
import gui.spatialViewPane.LevelPane;
import gui.spatialViewPane.PositionPane;
import gui.spatialViewPane.SizePane;
import gui.spatialViewPane.StylePane;

import java.io.Serializable;

import model.utility.RGBColor;

public class PresentationProperty implements Serializable{

	private static final long serialVersionUID = 7275587760424836463L;
	
	private PositionProperty positionProperty = new PositionProperty();
	private SizeProperty sizeProperty = new SizeProperty();
	private CropProperty cropProperty = new CropProperty();
	private StyleProperty styleProperty = new StyleProperty();
	private TextStyleProperty textStyleProperty = new TextStyleProperty();
	private LevelProperty levelProperty = new LevelProperty();
	
	public PresentationProperty(){

	}

	public PositionProperty getPositionProperty() {
		return positionProperty;
	}

	public void setPositionProperty(PositionProperty positionProperty) {
		this.positionProperty = positionProperty;
	}

	public SizeProperty getSizeProperty() {
		return sizeProperty;
	}

	public void setSizeProperty(SizeProperty sizeProperty) {
		this.sizeProperty = sizeProperty;
	}

	public CropProperty getCropProperty() {
		return cropProperty;
	}

	public void setCropProperty(CropProperty cropProperty) {
		this.cropProperty = cropProperty;
	}

	public StyleProperty getStyleProperty() {
		return styleProperty;
	}

	public void setStyleProperty(StyleProperty styleProperty) {
		this.styleProperty = styleProperty;
	}

	public TextStyleProperty getTextStyleProperty() {
		return textStyleProperty;
	}

	public void setTextStyleProperty(TextStyleProperty textStyleProperty) {
		this.textStyleProperty = textStyleProperty;
	}

	public LevelProperty getLevelProperty() {
		return levelProperty;
	}

	public void setLevelProperty(LevelProperty levelProperty) {
		this.levelProperty = levelProperty;
	}
	
	public void populatePositionPropertyJavaBean(PositionPane positionPane) {
		
		PositionProperty positionProperty = getPositionProperty();
		
		positionProperty.setLeft(positionPane.getLeftValue());
		positionProperty.setRight(positionPane.getRightValue());
		positionProperty.setTop(positionPane.getTopValue());
		positionProperty.setBottom(positionPane.getBottomValue());
		positionProperty.setRotation(positionPane.getRotationValue());
		positionProperty.setOrderZ(Integer.parseInt(positionPane.getZOrderValue()));
	
	}

	public void populateSizePropertyJavaBean(SizePane sizePane) {
		
		SizeProperty sizeProperty = getSizeProperty();
	   
		sizeProperty.setWidth(sizePane.getWidthValue());
		sizeProperty.setHeight(sizePane.getHeightValue());
		sizeProperty.setAspectRatio(sizePane.getAspectRatioValue());
		
	}

	public void populateCropPropertyJavaBean(CropPane cropPane) {
		   
		CropProperty cropProperty = getCropProperty();
		   
		cropProperty.setLeft(cropPane.getLeftValue());
		cropProperty.setRight(cropPane.getRightValue());
		cropProperty.setTop(cropPane.getTopValue());
		cropProperty.setBottom(cropPane.getBottomValue());
		
	}

	public void populateStylePropertyJavaBean(StylePane stylePane) {
		
		StyleProperty styleProperty = getStyleProperty();
		   
		styleProperty.setTransparency(stylePane.getTransparency());
		
	}

	public void populateTextStylePropertyJavaBean(StylePane stylePane) {
		   
	   TextStyleProperty textStyleProperty = getTextStyleProperty();
		   
	   textStyleProperty.setTransparency(stylePane.getTransparency());
	   textStyleProperty.setFontColor(new RGBColor(stylePane.getFontColorValue().getRed(), stylePane.getFontColorValue().getGreen(), stylePane.getFontColorValue().getBlue()));
	   textStyleProperty.setFontFamily(stylePane.getFontFamilyValue());
	   textStyleProperty.setFontSize(Double.parseDouble(stylePane.getFontSizeValue()));
	   textStyleProperty.setFontStyle(stylePane.getFontStyleValue());
	   textStyleProperty.setFontWeight(stylePane.getFontWeightValue());
		
	}

	public void populateLevelPropertyJavaBean(LevelPane levelPane) {
		
		LevelProperty levelProperty = getLevelProperty();
		   
		levelProperty.setVolume(levelPane.getVolume());
		levelProperty.setBalance(levelPane.getBalance());
		levelProperty.setTreble(levelPane.getTreble());
		levelProperty.setBass(levelPane.getBass());
		
	}
		
}
