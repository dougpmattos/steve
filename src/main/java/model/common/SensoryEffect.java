package model.common;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.common.enums.AutoExtractionType;
import model.common.enums.SensoryEffectType;
import model.spatialView.sensoryEffect.EffectPresentationProperty;

public class SensoryEffect extends Node<SensoryEffectType> implements Serializable {

	private static final long serialVersionUID = -2195295637721067870L;
	
	private final int ICON_WIDTH = 40;
	
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

	public ImageView generateEffectIcon() {
		
		switch(type) {
		   
	   		case WIND:
	   			icon = new ImageView(new Image(getClass().getResourceAsStream("/images/sensoryEffectsPane/wind-48.png")));
				icon.setPreserveRatio(true);
	            icon.setSmooth(true);
	            icon.setFitWidth(ICON_WIDTH);
	           break;
	           
			case WATER_SPRAYER:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/images/sensoryEffectsPane/watersprayer-48.png")));
				icon.setPreserveRatio(true);
	            icon.setSmooth(true);
	            icon.setFitWidth(ICON_WIDTH);
				break;
	           
			case FLASH_LIGHT:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/images/sensoryEffectsPane/flashlight-48.png")));
				icon.setPreserveRatio(true);
	            icon.setSmooth(true);
	            icon.setFitWidth(ICON_WIDTH);
				break; 
	       
			case FOG:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/images/sensoryEffectsPane/fog-48.png")));
				icon.setPreserveRatio(true);
	            icon.setSmooth(true);
	            icon.setFitWidth(ICON_WIDTH);
				break;
	               
			case LIGHT:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/images/sensoryEffectsPane/light-48.png")));
				icon.setPreserveRatio(true);
	            icon.setSmooth(true);
	            icon.setFitWidth(ICON_WIDTH);
				break; 
				
			case SCENT:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/images/sensoryEffectsPane/scent-48.png")));
				icon.setPreserveRatio(true);
	            icon.setSmooth(true);
	            icon.setFitWidth(ICON_WIDTH);
				break;
				
			case TEMPERATURE:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/images/sensoryEffectsPane/temperature-48.png")));
				icon.setPreserveRatio(true);
	            icon.setSmooth(true);
	            icon.setFitWidth(ICON_WIDTH);
				break;
				
			case VIBRATION:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/images/sensoryEffectsPane/vibration-48.png")));
				icon.setPreserveRatio(true);
	            icon.setSmooth(true);
	            icon.setFitWidth(ICON_WIDTH);
				break;
				
			case RAINSTORM:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/images/sensoryEffectsPane/rainstorm-48.png")));
				icon.setPreserveRatio(true);
	            icon.setSmooth(true);
	            icon.setFitWidth(ICON_WIDTH);
				break;
		}
   
		return icon;
		
	}

}
