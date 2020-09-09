package model.spatialView.sensoryEffect;

import java.io.Serializable;

import model.spatialView.PresentationProperty;
import model.spatialView.sensoryEffect.enums.ScentType;

public class EffectPresentationProperty extends PresentationProperty<EffectPositionProperty> implements Serializable {

	private static final long serialVersionUID = -4925711654365986227L;
	
	private Double intensityValue;
	private String intensityRange;
	private String rgbColorProperty;
	private Integer frequency;
	private Integer priority;
	private ScentType scent;
	private ColorCorrectionProperty colorCorrectionProperty;
	
	public EffectPresentationProperty(){
		
		this.positionProperty = new EffectPositionProperty();
		
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Double getIntensityValue() {
		return intensityValue;
	}

	public void setIntensityValue(Double intensityValue) {
		this.intensityValue = intensityValue;
	}

	public String getIntensityRange() {
		return intensityRange;
	}

	public void setIntensityRange(String intensityRange) {
		this.intensityRange = intensityRange;
	}

	public String getRgbColorProperty() {
		return rgbColorProperty;
	}

	public void setRgbColorProperty(String rgbColorProperty) {
		this.rgbColorProperty = rgbColorProperty;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public ScentType getScent() {
		return scent;
	}

	public void setScent(ScentType scent) {
		this.scent = scent;
	}

	public ColorCorrectionProperty getColorCorrectionProperty() {
		return colorCorrectionProperty;
	}

	public void setColorCorrectionProperty(
			ColorCorrectionProperty colorCorrectionProperty) {
		this.colorCorrectionProperty = colorCorrectionProperty;
	}
	
}
