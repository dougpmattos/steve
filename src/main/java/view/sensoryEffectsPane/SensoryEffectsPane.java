package view.sensoryEffectsPane;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.common.enums.SensoryEffectType;
import model.repository.RepositoryMediaList;
import view.common.Language;
import view.temporalViewPane.TemporalViewPane;
import controller.ApplicationController;

public class SensoryEffectsPane extends BorderPane{

	private static final DataFormat dataFormat = new DataFormat("String");
	
	private HBox sensoyEffectsChipsPane;
	private Button windEffectChip;
	private Button waterSprayerEffectChip;
	private Button vibrationEffectChip;
	private Button coldEffectChip;
	private Button hotEffectChip;
	private Button scentEffectChip;
	private Button lightEffectChip;
	private Button fogEffectChip;
	private Button flashlightEffectChip;
	private Button rainstormEffectChip;
	private HBox labelContainer;
	
	public SensoryEffectsPane(ApplicationController applicationController, TabPane temporalChainTabPane, TemporalViewPane temporalViewPane, RepositoryMediaList repositoryMediaList){
		
		setId("sensory-effects-pane");
	    getStylesheets().add("styles/sensoryEffectsPane/sensoryEffectsPane.css");
	    
		sensoyEffectsChipsPane = new HBox();
		sensoyEffectsChipsPane.setId("sensory-effects-chips-pane");
		
		labelContainer = new HBox();
		labelContainer.setId("label-container");
		Label label = new Label(Language.translate("sensory.effects"));
		labelContainer.getChildren().add(label);
		
		createEffectsChips();
	        
		setLeft(labelContainer);
	    setCenter(sensoyEffectsChipsPane);
	    
	    createDragDropEffect();
		
	}
	
	private void createDragDropEffect() {
		
		windEffectChip.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
			
		        Dragboard dragBoard = startDragAndDrop(TransferMode.COPY);
		        ClipboardContent content = new ClipboardContent();

		        content.put(dataFormat, SensoryEffectType.WIND);
		        
		        dragBoard.setContent(content);
		        
		        mouseEvent.consume();
				
			}
			
       });
		lightEffectChip.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
			
		        Dragboard dragBoard = startDragAndDrop(TransferMode.COPY);
		        ClipboardContent content = new ClipboardContent();

		        content.put(dataFormat, SensoryEffectType.LIGHT);
		        
		        dragBoard.setContent(content);
		        
		        mouseEvent.consume();
				
			}
			
       });
		flashlightEffectChip.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
			
		        Dragboard dragBoard = startDragAndDrop(TransferMode.COPY);
		        ClipboardContent content = new ClipboardContent();

		        content.put(dataFormat, SensoryEffectType.FLASH_LIGHT);
		        
		        dragBoard.setContent(content);
		        
		        mouseEvent.consume();
				
			}
			
       });
		scentEffectChip.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
			
		        Dragboard dragBoard = startDragAndDrop(TransferMode.COPY);
		        ClipboardContent content = new ClipboardContent();

		        content.put(dataFormat, SensoryEffectType.SCENT);
		        
		        dragBoard.setContent(content);
		        
		        mouseEvent.consume();
				
			}
			
       });
		vibrationEffectChip.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
			
		        Dragboard dragBoard = startDragAndDrop(TransferMode.COPY);
		        ClipboardContent content = new ClipboardContent();

		        content.put(dataFormat, SensoryEffectType.VIBRATION);
		        
		        dragBoard.setContent(content);
		        
		        mouseEvent.consume();
				
			}
			
       });
		fogEffectChip.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
			
		        Dragboard dragBoard = startDragAndDrop(TransferMode.COPY);
		        ClipboardContent content = new ClipboardContent();

		        content.put(dataFormat, SensoryEffectType.FOG);
		        
		        dragBoard.setContent(content);
		        
		        mouseEvent.consume();
				
			}
			
       });
		rainstormEffectChip.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
			
		        Dragboard dragBoard = startDragAndDrop(TransferMode.COPY);
		        ClipboardContent content = new ClipboardContent();

		        content.put(dataFormat, SensoryEffectType.RAINSTORM);
		        
		        dragBoard.setContent(content);
		        
		        mouseEvent.consume();
				
			}
			
       });
		coldEffectChip.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
			
		        Dragboard dragBoard = startDragAndDrop(TransferMode.COPY);
		        ClipboardContent content = new ClipboardContent();

		        content.put(dataFormat, SensoryEffectType.COLD);
		        
		        dragBoard.setContent(content);
		        
		        mouseEvent.consume();
				
			}
			
       });
		hotEffectChip.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {

				Dragboard dragBoard = startDragAndDrop(TransferMode.COPY);
				ClipboardContent content = new ClipboardContent();

				content.put(dataFormat, SensoryEffectType.HOT);

				dragBoard.setContent(content);

				mouseEvent.consume();

			}

		});
		waterSprayerEffectChip.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
			
		        Dragboard dragBoard = startDragAndDrop(TransferMode.COPY);
		        ClipboardContent content = new ClipboardContent();

		        content.put(dataFormat, SensoryEffectType.WATER_SPRAYER);
		        
		        dragBoard.setContent(content);
		        
		        mouseEvent.consume();
				
			}
			
       });
				
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
		
		coldEffectChip = new Button();
		coldEffectChip.setText(Language.translate("cold"));
		coldEffectChip.setId("cold-effect-chip");
		coldEffectChip.setTooltip(new Tooltip(Language.translate("drag.cold.effect")));

		hotEffectChip = new Button();
		hotEffectChip.setText(Language.translate("hot"));
		hotEffectChip.setId("hot-effect-chip");
		hotEffectChip.setTooltip(new Tooltip(Language.translate("drag.hot.effect")));
		
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
		sensoyEffectsChipsPane.getChildren().add(coldEffectChip);
		sensoyEffectsChipsPane.getChildren().add(hotEffectChip);
		sensoyEffectsChipsPane.getChildren().add(scentEffectChip);
		sensoyEffectsChipsPane.getChildren().add(lightEffectChip);
		sensoyEffectsChipsPane.getChildren().add(fogEffectChip);
		sensoyEffectsChipsPane.getChildren().add(flashlightEffectChip);
		sensoyEffectsChipsPane.getChildren().add(rainstormEffectChip);
	      
	}
	 
}
