package view.sensoryEffectsPane;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.common.enums.SensoryEffectType;
import model.repository.RepositoryMediaList;
import view.common.Language;
import view.temporalViewPane.TemporalViewPane;
import controller.ApplicationController;

public class SensoryEffectsPane extends BorderPane {
	
	private HBox sensoryEffectsChipsPane;

	private ButtonChip windEffectChip;
	private ButtonChip waterSprayerEffectChip;
	private ButtonChip flashlightEffectChip;
	private ButtonChip fogEffectChip;
	private ButtonChip lightEffectChip;
	private ButtonChip scentEffectChip;
	private ButtonChip coldEffectChip;
	private ButtonChip hotEffectChip;
	private ButtonChip vibrationEffectChip;
	private ButtonChip bubbleEffectChip;
	private ButtonChip rainstormEffectChip;
	private ButtonChip snowEffectChip;

	private HBox labelContainer;
	
	public  SensoryEffectsPane(ApplicationController applicationController, TabPane temporalChainTabPane, TemporalViewPane temporalViewPane, RepositoryMediaList repositoryMediaList){
		
		setId("sensory-effects-pane");
	    getStylesheets().add("styles/sensoryEffectsPane/sensoryEffectsPane.css");

		sensoryEffectsChipsPane = new HBox();
		sensoryEffectsChipsPane.setId("sensory-effects-chips-pane");
		sensoryEffectsChipsPane.setAlignment(Pos.CENTER);
		sensoryEffectsChipsPane.setSpacing(300);
		
		labelContainer = new HBox();
		labelContainer.setId("label-container");
		Label label = new Label(Language.translate("sensory.effects"));
		labelContainer.getChildren().add(label);

		createEffectsChips();

		setLeft(labelContainer);
		setCenter(sensoryEffectsChipsPane);
		
	}

	private void createEffectsChips(){
		
		windEffectChip = new ButtonChip(SensoryEffectType.WIND);
		windEffectChip.setText(Language.translate("wind"));
		windEffectChip.setId("wind-effect-chip");
		windEffectChip.setTooltip(new Tooltip(Language.translate("drag.wind.effect")));
		
		waterSprayerEffectChip = new ButtonChip(SensoryEffectType.WATER_SPRAYER);
		waterSprayerEffectChip.setText(Language.translate("water.sprayer"));
		waterSprayerEffectChip.setId("water-sprayer-effect-chip");
		waterSprayerEffectChip.setTooltip(new Tooltip(Language.translate("drag.water.sprayer.effect")));
		
		vibrationEffectChip = new ButtonChip(SensoryEffectType.VIBRATION);
		vibrationEffectChip.setText(Language.translate("vibration"));
		vibrationEffectChip.setId("vibration-effect-chip");
		vibrationEffectChip.setTooltip(new Tooltip(Language.translate("drag.vibration.effect")));
		
		coldEffectChip = new ButtonChip(SensoryEffectType.COLD);
		coldEffectChip.setText(Language.translate("cold"));
		coldEffectChip.setId("cold-effect-chip");
		coldEffectChip.setTooltip(new Tooltip(Language.translate("drag.cold.effect")));

		hotEffectChip = new ButtonChip(SensoryEffectType.HOT);
		hotEffectChip.setText(Language.translate("hot"));
		hotEffectChip.setId("hot-effect-chip");
		hotEffectChip.setTooltip(new Tooltip(Language.translate("drag.hot.effect")));
		
		scentEffectChip = new ButtonChip(SensoryEffectType.SCENT);
		scentEffectChip.setText(Language.translate("scent"));
		scentEffectChip.setId("scent-effect-chip");
		scentEffectChip.setTooltip(new Tooltip(Language.translate("drag.scent.effect")));
		
		lightEffectChip = new ButtonChip(SensoryEffectType.LIGHT);
		lightEffectChip.setText(Language.translate("light"));
		lightEffectChip.setId("light-effect-chip");
		lightEffectChip.setTooltip(new Tooltip(Language.translate("drag.light.effect")));
		
		fogEffectChip = new ButtonChip(SensoryEffectType.FOG);
		fogEffectChip.setText(Language.translate("fog"));
		fogEffectChip.setId("fog-effect-chip");
		fogEffectChip.setTooltip(new Tooltip(Language.translate("drag.fog.effect")));
		
		flashlightEffectChip = new ButtonChip(SensoryEffectType.FLASH);
		flashlightEffectChip.setText(Language.translate("flash"));
		flashlightEffectChip.setId("flashlight-effect-chip");
		flashlightEffectChip.setTooltip(new Tooltip(Language.translate("drag.flash.effect")));
		
		rainstormEffectChip = new ButtonChip(SensoryEffectType.RAINSTORM);
		rainstormEffectChip.setText(Language.translate("rainstorm"));
		rainstormEffectChip.setId("rainstorm-effect-chip");
		rainstormEffectChip.setTooltip(new Tooltip(Language.translate("drag.rainstorm.effect")));

//		bubbleEffectChip = new ButtonChip();
//		bubbleEffectChip.setText(Language.translate("bubble"));
//		bubbleEffectChip.setId("bubble-effect-chip");
//		bubbleEffectChip.setTooltip(new Tooltip(Language.translate("drag.bubble.effect")));
//
//		snowEffectChip  =new ButtonChip();
//		snowEffectChip.setText(Language.translate("snow"));
//		snowEffectChip.setId("snow-effect-chip");
//		snowEffectChip.setTooltip(new Tooltip(Language.translate("drag.snow.effect")));

		sensoryEffectsChipsPane.getChildren().add(windEffectChip);
		sensoryEffectsChipsPane.getChildren().add(waterSprayerEffectChip);
		sensoryEffectsChipsPane.getChildren().add(vibrationEffectChip);
		sensoryEffectsChipsPane.getChildren().add(coldEffectChip);
		sensoryEffectsChipsPane.getChildren().add(hotEffectChip);
		sensoryEffectsChipsPane.getChildren().add(scentEffectChip);
		sensoryEffectsChipsPane.getChildren().add(lightEffectChip);
		sensoryEffectsChipsPane.getChildren().add(flashlightEffectChip);
		sensoryEffectsChipsPane.getChildren().add(rainstormEffectChip);
		sensoryEffectsChipsPane.getChildren().add(fogEffectChip);
		//sensoryEffectsChipsPane.getChildren().add(snowEffectChip);
		//sensoryEffectsChipsPane.getChildren().add(bubbleEffectChip);

	}

	public ButtonChip getButtonChipByType(SensoryEffectType sensoryEffectType) {

		switch (sensoryEffectType) {

			case WIND:
				return windEffectChip;
			case WATER_SPRAYER:
				return waterSprayerEffectChip;
			case FOG:
				return fogEffectChip;
			case FLASH:
				return flashlightEffectChip;
			case SCENT:
				return scentEffectChip;
			case COLD:
				return coldEffectChip;
			case HOT:
				return hotEffectChip;
			case VIBRATION:
				return vibrationEffectChip;
			case LIGHT:
				return lightEffectChip;
			case RAINSTORM:
				return rainstormEffectChip;
			default:
				return null;

		}
	}
	 
}
