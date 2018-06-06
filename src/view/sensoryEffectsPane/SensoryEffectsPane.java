package view.sensoryEffectsPane;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.repository.RepositoryMediaList;
import view.common.Language;
import view.temporalViewPane.TemporalViewPane;
import controller.Controller;

public class SensoryEffectsPane extends BorderPane{

	private HBox sensoyEffectsChipsPane;
	private Button windEffectChip;
	private Button waterSprayerEffectChip;
	private Button vibrationEffectChip;
	private Button temperatureEffectChip;
	private Button scentEffectChip;
	private Button lightEffectChip;
	private Button fogEffectChip;
	private Button flashlightEffectChip;
	private Button rainstormEffectChip;
	private HBox labelContainer;
	
	public SensoryEffectsPane(Controller controller, TabPane temporalChainTabPane, TemporalViewPane temporalViewPane, RepositoryMediaList repositoryMediaList){
		
		setId("sensory-effects-pane");
	    getStylesheets().add("view/sensoryEffectsPane/styles/sensoryEffectsPane.css");
	    
		sensoyEffectsChipsPane = new HBox();
		sensoyEffectsChipsPane.setId("sensory-effects-chips-pane");
		
		labelContainer = new HBox();
		labelContainer.setId("label-container");
		Label label = new Label(Language.translate("sensory.effects"));
		labelContainer.getChildren().add(label);
		
		createEffectsChips();
	        
		setLeft(labelContainer);
	    setCenter(sensoyEffectsChipsPane);
		
	}
	
	private void createEffectsChips(){
		
		windEffectChip = new Button();
		windEffectChip.setText(Language.translate("wind"));
		windEffectChip.setId("wind-effect-chip");
		windEffectChip.setTooltip(new Tooltip(Language.translate("drag.wind.effect")));
		
		waterSprayerEffectChip = new Button();
		waterSprayerEffectChip.setText(Language.translate("water.sprayer"));
		waterSprayerEffectChip.setId("water-sprayer-effect-chip");
		waterSprayerEffectChip.setTooltip(new Tooltip(Language.translate("drag.water.sprayer.effect")));
		
		vibrationEffectChip = new Button();
		vibrationEffectChip.setText(Language.translate("vibration"));
		vibrationEffectChip.setId("vibration-effect-chip");
		vibrationEffectChip.setTooltip(new Tooltip(Language.translate("drag.vibration.effect")));
		
		temperatureEffectChip = new Button();
		temperatureEffectChip.setText(Language.translate("temperature"));
		temperatureEffectChip.setId("temperature-effect-chip");
		temperatureEffectChip.setTooltip(new Tooltip(Language.translate("drag.temperature.effect")));
		
		scentEffectChip = new Button();
		scentEffectChip.setText(Language.translate("scent"));
		scentEffectChip.setId("scent-effect-chip");
		scentEffectChip.setTooltip(new Tooltip(Language.translate("drag.scent.effect")));
		
		lightEffectChip = new Button();
		lightEffectChip.setText(Language.translate("light"));
		lightEffectChip.setId("light-effect-chip");
		lightEffectChip.setTooltip(new Tooltip(Language.translate("drag.light.effect")));
		
		fogEffectChip = new Button();
		fogEffectChip.setText(Language.translate("fog"));
		fogEffectChip.setId("fog-effect-chip");
		fogEffectChip.setTooltip(new Tooltip(Language.translate("drag.fog.effect")));
		
		flashlightEffectChip = new Button();
		flashlightEffectChip.setText(Language.translate("flashlight"));
		flashlightEffectChip.setId("flashlight-effect-chip");
		flashlightEffectChip.setTooltip(new Tooltip(Language.translate("drag.flashlight.effect")));
		
		rainstormEffectChip = new Button();
		rainstormEffectChip.setText(Language.translate("rainstorm"));
		rainstormEffectChip.setId("rainstorm-effect-chip");
		rainstormEffectChip.setTooltip(new Tooltip(Language.translate("drag.rainstorm.effect")));
		
		sensoyEffectsChipsPane.getChildren().add(windEffectChip);
		sensoyEffectsChipsPane.getChildren().add(waterSprayerEffectChip);
		sensoyEffectsChipsPane.getChildren().add(vibrationEffectChip);
		sensoyEffectsChipsPane.getChildren().add(temperatureEffectChip);
		sensoyEffectsChipsPane.getChildren().add(scentEffectChip);
		sensoyEffectsChipsPane.getChildren().add(lightEffectChip);
		sensoyEffectsChipsPane.getChildren().add(fogEffectChip);
		sensoyEffectsChipsPane.getChildren().add(flashlightEffectChip);
		sensoyEffectsChipsPane.getChildren().add(rainstormEffectChip);
	      
	}
	 
}
