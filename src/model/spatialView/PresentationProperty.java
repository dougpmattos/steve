package model.spatialView;

import java.io.Serializable;

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
		
}
