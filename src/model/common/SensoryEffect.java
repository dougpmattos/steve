package model.common;

import model.common.enums.AutoExtractionType;
import model.common.enums.SensoryEffectType;
import model.spatialView.sensoryEffect.EffectPresentationProperty;

public class SensoryEffect extends Node<SensoryEffectType> {

	private static final long serialVersionUID = -2195295637721067870L;
	
	private EffectPresentationProperty presentationProperty = new EffectPresentationProperty();
	private AutoExtractionType autoExtraction;
	private String alt;
	private Integer priority;
	
	public SensoryEffect(){
		
	}
	
	public EffectPresentationProperty getPresentationProperty() {
		return presentationProperty;
	}
	
	public void setPresentationProperty(
			EffectPresentationProperty presentationProperty) {
		this.presentationProperty = presentationProperty;
	}
	
	public AutoExtractionType getAutoExtraction() {
		return autoExtraction;
	}
	
	public void setAutoExtraction(AutoExtractionType autoExtraction) {
		this.autoExtraction = autoExtraction;
	}
	
	public String getAlt() {
		return alt;
	}
	
	public void setAlt(String alt) {
		this.alt = alt;
	}
	
	public Integer getPriority() {
		return priority;
	}
	
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

}
