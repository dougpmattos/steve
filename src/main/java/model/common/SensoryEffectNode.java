package model.common;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.common.enums.SensoryEffectType;
import model.spatialView.sensoryEffect.*;
import model.spatialView.sensoryEffect.commonProperties.IntensityRange;
import model.spatialView.sensoryEffect.commonProperties.IntensityValue;
import model.spatialView.sensoryEffect.LightPresentationProperty;
import model.spatialView.sensoryEffect.temperature.ColdPresentationProperty;
import model.spatialView.sensoryEffect.temperature.HotPresentationProperty;
import view.spatialViewPane.*;

public class SensoryEffectNode extends Node<SensoryEffectType> implements Serializable {

	private static final long serialVersionUID = -2195295637721067870L;
	
	private final int ICON_WIDTH = 40;
	
	private EffectPresentationProperty presentationProperty;

	public SensoryEffectNode(SensoryEffectType sensoryEffectType) {
		setType(sensoryEffectType);
		setPresentationProperty();
	}

	private void setPresentationProperty(){

		switch(type) {

//			case ACTIVE_KINESTHETIC:
//				presentationProperty = new ActiveKinestheticPresentationProperty();
//				break;
//
//			case RIGID_BODY_MOTION:
//				presentationProperty = new RigidBodyMotionPresentationProperty();
//				break;
//
//			case PASSIVE_KINESTHETIC_MOTION:
//				presentationProperty = new PassiveKinestheticPresentationProperty();
//				break;

			case WIND:
				presentationProperty = new WindPresentationProperty();
				break;

			case LIGHT:
				presentationProperty = new LightPresentationProperty();
				break;

			case FLASH:
				presentationProperty = new FlashPresentationProperty();
				break;

			case SCENT:
				presentationProperty = new ScentPresentationProperty();
				break;

			case WATER_SPRAYER:
				presentationProperty = new WaterSprayerPresentationProperty();
				break;

			case FOG:
				presentationProperty = new FogPresentationProperty();
				break;

//			case BUBBLE:
//				presentationProperty = new BubblePresentationProperty();
//				break;

//			case SNOW:
//				presentationProperty = new SnowPresentationProperty();
//				break;

			case VIBRATION:
				presentationProperty = new VibrationPresentationProperty();
				break;

//			case COLOR_CORRECTION:
//				presentationProperty = new ColorCorrectionPresentationProperty();
//				break;
//
//			case TACTILE:
//				presentationProperty = new TactilePresentationProperty();
//				break;

			case COLD:
				presentationProperty = new ColdPresentationProperty();
				break;

			case HOT:
				presentationProperty = new HotPresentationProperty();
				break;

//			case PASSIVE_KINESTHETIC_FORCE:
//				presentationProperty = new PassivePresentationProperty();
//				break;

			case RAINSTORM:
				presentationProperty = new RainstormPresentationProperty();
				break;

		}

	}
	
	public EffectPresentationProperty getPresentationProperty() {
		return presentationProperty;
	}
	
	public void setPresentationProperty(
			EffectPresentationProperty presentationProperty) {
		this.presentationProperty = presentationProperty;
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
	           
			case FLASH:
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
				
			case COLD:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/images/sensoryEffectsPane/cold-48.png")));
				icon.setPreserveRatio(true);
	            icon.setSmooth(true);
	            icon.setFitWidth(ICON_WIDTH);
				break;

			case HOT:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/images/sensoryEffectsPane/hot-48.png")));
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

//			case SNOW:
//				icon = new ImageView(new Image(getClass().getResourceAsStream("/images/sensoryEffectsPane/snow-48.png")));
//				icon.setPreserveRatio(true);
//				icon.setSmooth(true);
//				icon.setFitWidth(ICON_WIDTH);
//				break;

//			case BUBBLE:
//				icon = new ImageView(new Image(getClass().getResourceAsStream("/images/sensoryEffectsPane/bubble-48.png")));
//				icon.setPreserveRatio(true);
//				icon.setSmooth(true);
//				icon.setFitWidth(ICON_WIDTH);
//				break;
		}
   
		return icon;
		
	}

	public void updateProperties(SensoryEffectPropertyPane sensoryEffectPropertyPane) {

		IntensityValue intensityValue;
		IntensityRange intensityRange;

		switch(type) {

			case WIND:

				WindPresentationPropertyPane windPresentationPropertyPane
						= (WindPresentationPropertyPane) sensoryEffectPropertyPane;
				WindPresentationProperty windPresentationProperty
						= (WindPresentationProperty) presentationProperty;

				intensityValue = new IntensityValue();
				intensityValue.setValue(Float.valueOf(windPresentationPropertyPane.getIntensityValueTextField().getText()));
				windPresentationProperty.setIntensityValue(intensityValue);

				intensityRange = new IntensityRange();
				intensityRange.setFromValue(Float.valueOf(windPresentationPropertyPane.getRangeFromTextField().getText()));
				intensityRange.setToValue(Float.valueOf(windPresentationPropertyPane.getRangeToTextField().getText()));
				windPresentationProperty.setIntensityRange(intensityRange);

				windPresentationProperty.setUnit(windPresentationPropertyPane.getIntensityUnit().getValue());

				break;

			case WATER_SPRAYER:

				WaterSprayerPresentationPropertyPane waterSprayerPresentationPropertyPane
						= (WaterSprayerPresentationPropertyPane) sensoryEffectPropertyPane;
				WaterSprayerPresentationProperty waterSprayerPresentationProperty
						= (WaterSprayerPresentationProperty) presentationProperty;

				intensityValue = new IntensityValue();
				intensityValue.setValue(Float.valueOf(waterSprayerPresentationPropertyPane.getIntensityValueTextField().getText()));
				waterSprayerPresentationProperty.setIntensityValue(intensityValue);

				intensityRange = new IntensityRange();
				intensityRange.setFromValue(Float.valueOf(waterSprayerPresentationPropertyPane.getRangeFromTextField().getText()));
				intensityRange.setToValue(Float.valueOf(waterSprayerPresentationPropertyPane.getRangeToTextField().getText()));
				waterSprayerPresentationProperty.setIntensityRange(intensityRange);

				waterSprayerPresentationProperty.setSprayingType(waterSprayerPresentationPropertyPane.getSprayingTypeChoiceBox().getValue());

				break;

			case FLASH:

				FlashPresentationPropertyPane flashLightPresentationPropertyPane
						= (FlashPresentationPropertyPane) sensoryEffectPropertyPane;
				FlashPresentationProperty flashLightPresentationProperty
						= (FlashPresentationProperty) presentationProperty;

				intensityValue = new IntensityValue();
				intensityValue.setValue(Float.valueOf(flashLightPresentationPropertyPane.getIntensityValueTextField().getText()));
				flashLightPresentationProperty.setIntensityValue(intensityValue);

				intensityRange = new IntensityRange();
				intensityRange.setFromValue(Float.valueOf(flashLightPresentationPropertyPane.getRangeFromTextField().getText()));
				intensityRange.setToValue(Float.valueOf(flashLightPresentationPropertyPane.getRangeToTextField().getText()));
				flashLightPresentationProperty.setIntensityRange(intensityRange);

				flashLightPresentationProperty.setColor(flashLightPresentationPropertyPane.getColorPicker().getValue());
				flashLightPresentationProperty.setFrequency(Integer.valueOf(flashLightPresentationPropertyPane.getFrequencyTextField().getText()));

				break;

			case FOG:

				FogPresentationPropertyPane fogPresentationPropertyPane
						= (FogPresentationPropertyPane) sensoryEffectPropertyPane;
				FogPresentationProperty fogPresentationProperty
						= (FogPresentationProperty) presentationProperty;

				intensityValue = new IntensityValue();
				intensityValue.setValue(Float.valueOf(fogPresentationPropertyPane.getIntensityValueTextField().getText()));
				fogPresentationProperty.setIntensityValue(intensityValue);

				intensityRange = new IntensityRange();
				intensityRange.setFromValue(Float.valueOf(fogPresentationPropertyPane.getRangeFromTextField().getText()));
				intensityRange.setToValue(Float.valueOf(fogPresentationPropertyPane.getRangeToTextField().getText()));
				fogPresentationProperty.setIntensityRange(intensityRange);

				break;

			case LIGHT:

				LightPresentationPropertyPane lightPresentationPropertyPane
						= (LightPresentationPropertyPane) sensoryEffectPropertyPane;
				LightPresentationProperty lightPresentationProperty
						= (LightPresentationProperty) presentationProperty;

				intensityValue = new IntensityValue();
				intensityValue.setValue(Float.valueOf(lightPresentationPropertyPane.getIntensityValueTextField().getText()));
				lightPresentationProperty.setIntensityValue(intensityValue);

				intensityRange = new IntensityRange();
				intensityRange.setFromValue(Float.valueOf(lightPresentationPropertyPane.getRangeFromTextField().getText()));
				intensityRange.setToValue(Float.valueOf(lightPresentationPropertyPane.getRangeToTextField().getText()));
				lightPresentationProperty.setIntensityRange(intensityRange);

				lightPresentationProperty.setColor(lightPresentationPropertyPane.getColorPicker().getValue());

				break;

			case SCENT:

				ScentPresentationPropertyPane scentPresentationPropertyPane
						= (ScentPresentationPropertyPane) sensoryEffectPropertyPane;
				ScentPresentationProperty scentPresentationProperty
						= (ScentPresentationProperty) presentationProperty;

				intensityValue = new IntensityValue();
				intensityValue.setValue(Float.valueOf(scentPresentationPropertyPane.getIntensityValueTextField().getText()));
				scentPresentationProperty.setIntensityValue(intensityValue);

				intensityRange = new IntensityRange();
				intensityRange.setFromValue(Float.valueOf(scentPresentationPropertyPane.getRangeFromTextField().getText()));
				intensityRange.setToValue(Float.valueOf(scentPresentationPropertyPane.getRangeToTextField().getText()));
				scentPresentationProperty.setIntensityRange(intensityRange);

				scentPresentationProperty.setScentType(scentPresentationPropertyPane.getScentTypeChoiceBox().getValue());

				break;

			case COLD:

				ColdPresentationPropertyPane coldPresentationPropertyPane
						= (ColdPresentationPropertyPane) sensoryEffectPropertyPane;
				ColdPresentationProperty coldPresentationProperty
						= (ColdPresentationProperty) presentationProperty;

				intensityValue = new IntensityValue();
				intensityValue.setValue(Float.valueOf(coldPresentationPropertyPane.getIntensityValueTextField().getText()));
				coldPresentationProperty.setIntensityValue(intensityValue);

				intensityRange = new IntensityRange();
				intensityRange.setFromValue(Float.valueOf(coldPresentationPropertyPane.getRangeFromTextField().getText()));
				intensityRange.setToValue(Float.valueOf(coldPresentationPropertyPane.getRangeToTextField().getText()));
				coldPresentationProperty.setIntensityRange(intensityRange);

				break;

			case HOT:

				HotPresentationPropertyPane hotPresentationPropertyPane
						= (HotPresentationPropertyPane) sensoryEffectPropertyPane;
				HotPresentationProperty hotPresentationProperty
						= (HotPresentationProperty) presentationProperty;

				intensityValue = new IntensityValue();
				intensityValue.setValue(Float.valueOf(hotPresentationPropertyPane.getIntensityValueTextField().getText()));
				hotPresentationProperty.setIntensityValue(intensityValue);

				intensityRange = new IntensityRange();
				intensityRange.setFromValue(Float.valueOf(hotPresentationPropertyPane.getRangeFromTextField().getText()));
				intensityRange.setToValue(Float.valueOf(hotPresentationPropertyPane.getRangeToTextField().getText()));
				hotPresentationProperty.setIntensityRange(intensityRange);

				break;

			case VIBRATION:

				VibrationPresentationPropertyPane vibrationPresentationPropertyPane
						= (VibrationPresentationPropertyPane) sensoryEffectPropertyPane;
				VibrationPresentationProperty vibrationPresentationProperty
						= (VibrationPresentationProperty) presentationProperty;

				intensityValue = new IntensityValue();
				intensityValue.setValue(Float.valueOf(vibrationPresentationPropertyPane.getIntensityValueTextField().getText()));
				vibrationPresentationProperty.setIntensityValue(intensityValue);

				intensityRange = new IntensityRange();
				intensityRange.setFromValue(Float.valueOf(vibrationPresentationPropertyPane.getRangeFromTextField().getText()));
				intensityRange.setToValue(Float.valueOf(vibrationPresentationPropertyPane.getRangeToTextField().getText()));
				vibrationPresentationProperty.setIntensityRange(intensityRange);

				break;

//			case BUBBLE:
//
//				BubblePresentationPropertyPane bubblePresentationPropertyPane
//						= (BubblePresentationPropertyPane) sensoryEffectPropertyPane;
//				BubblePresentationProperty bubblePresentationProperty
//						= (BubblePresentationProperty) presentationProperty;
//
//				intensityValue = new IntensityValue();
//				intensityValue.setValue(Float.valueOf(bubblePresentationPropertyPane.getIntensityValueTextField().getText()));
//				bubblePresentationProperty.setIntensityValue(intensityValue);
//
//				intensityRange = new IntensityRange();
//				intensityRange.setFromValue(Float.valueOf(bubblePresentationPropertyPane.getRangeFromTextField().getText()));
//				intensityRange.setToValue(Float.valueOf(bubblePresentationPropertyPane.getRangeToTextField().getText()));
//				bubblePresentationProperty.setIntensityRange(intensityRange);
//
//				break;

			case RAINSTORM:

				RainstormPresentationPropertyPane rainstormPresentationPropertyPane
						= (RainstormPresentationPropertyPane) sensoryEffectPropertyPane;
				RainstormPresentationProperty rainstormPresentationProperty
						= (RainstormPresentationProperty) presentationProperty;

				intensityValue = new IntensityValue();
				intensityValue.setValue(Float.valueOf(rainstormPresentationPropertyPane.getIntensityValueTextField().getText()));
				rainstormPresentationProperty.getWaterSprayerPresentationProperty().setIntensityValue(intensityValue);

				intensityRange = new IntensityRange();
				intensityRange.setFromValue(Float.valueOf(rainstormPresentationPropertyPane.getRangeFromTextField().getText()));
				intensityRange.setToValue(Float.valueOf(rainstormPresentationPropertyPane.getRangeToTextField().getText()));
				rainstormPresentationProperty.getWaterSprayerPresentationProperty().setIntensityRange(intensityRange);
				rainstormPresentationProperty.getWaterSprayerPresentationProperty().setSprayingType(rainstormPresentationPropertyPane.getSprayingTypeChoiceBox().getValue());

				intensityValue = new IntensityValue();
				intensityValue.setValue(Float.valueOf(rainstormPresentationPropertyPane.getFlashIntensityValueTextField().getText()));
				rainstormPresentationProperty.getFlashPresentationProperty().setIntensityValue(intensityValue);

				intensityRange = new IntensityRange();
				intensityRange.setFromValue(Float.valueOf(rainstormPresentationPropertyPane.getFlashRangeFromTextField().getText()));
				intensityRange.setToValue(Float.valueOf(rainstormPresentationPropertyPane.getFlashRangeToTextField().getText()));
				rainstormPresentationProperty.getFlashPresentationProperty().setIntensityRange(intensityRange);

				rainstormPresentationProperty.getFlashPresentationProperty().setColor(rainstormPresentationPropertyPane.getColorPicker().getValue());
				rainstormPresentationProperty.getFlashPresentationProperty().setFrequency(Integer.valueOf(rainstormPresentationPropertyPane.getFrequencyTextField().getText()));

				break;

//			case SNOW:
//
//				SnowPresentationPropertyPane snowPresentationPropertyPane
//						= (SnowPresentationPropertyPane) sensoryEffectPropertyPane;
//				SnowPresentationProperty snowPresentationProperty
//						= (SnowPresentationProperty) presentationProperty;
//
//				intensityValue = new IntensityValue();
//				intensityValue.setValue(Float.valueOf(snowPresentationPropertyPane.getIntensityValueTextField().getText()));
//				snowPresentationProperty.setIntensityValue(intensityValue);
//
//				intensityRange = new IntensityRange();
//				intensityRange.setFromValue(Float.valueOf(snowPresentationPropertyPane.getRangeFromTextField().getText()));
//				intensityRange.setToValue(Float.valueOf(snowPresentationPropertyPane.getRangeToTextField().getText()));
//				snowPresentationProperty.setIntensityRange(intensityRange);
//
//				break;

		}

	}

	public void updateSensoryEffectPositions(EffectPositionPane effectPositionPane) {

		EffectPositionProperty effectPositionPropertyOfCurrentNode = getPresentationProperty().getPositionProperty();
		effectPositionPropertyOfCurrentNode.setxPosition(effectPositionPane.getxPositionChoiceBox().getValue());
		effectPositionPropertyOfCurrentNode.setyPosition(effectPositionPane.getyPositionChoiceBox().getValue());
		effectPositionPropertyOfCurrentNode.setzPosition(effectPositionPane.getzPositionChoiceBox().getValue());

	}

	@Override
	public String toString(){
		return name;
	}

}
