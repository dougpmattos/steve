package view.spatialViewPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import model.temporalView.TemporalChain;
import view.common.Language;
import view.temporalViewPane.TemporalChainPane;
import view.temporalViewPane.TemporalViewPane;

public class ControlButtonPane extends BorderPane{
	
	private Button fullScreen;
	private Button previousFrame;
	private Button nextFrame;
	private Button play;
	private Button stop;
	private Button previousScene;
	private Button nextScene;
	private Button refresh;
	private HBox fullButtonPane;
	private HBox centerButtonPane;
	private HBox refreshButtonPane;
	private StackPane screen;
	private TemporalChain temporalChainModel;
	private TemporalChainPane selectedTemporalChainPane;
	private TemporalViewPane temporalViewPane;
	
	public ControlButtonPane(StackPane screen, TemporalViewPane temporalViewPane){
		
		setId("control-button-pane");
		
		this.screen = screen;
		this.temporalViewPane = temporalViewPane;
		
	    createButtons();
	  
	    setLeft(fullButtonPane);
		setCenter(centerButtonPane);
		setRight(refreshButtonPane);
		
		createButtonActions();
		
		createListeners();

	}
	
	private void createListeners(){
		
		temporalViewPane.getTemporalChainTabPane().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				
				TemporalChainPane temporalChainPane = (TemporalChainPane) newValue.getContent();
				selectedTemporalChainPane = temporalChainPane;
				temporalChainModel = selectedTemporalChainPane.getTemporalChainModel();
				
			}
			
		});
		
		//TODO listener para a linha de play quando ela mudar de posicao. Para cada mudanca de posicao 
		//pegar as midias que estao sobre a linha e exibir na tel conforme suas propriedades
//		@Override
//		public void handle(ActionEvent event) {
//			
//			for(Media media : temporalChainModel.getMediaUnderThePlayLineList()){
//				
//				ImageView imageMedia = media.generateMediaIcon();
//				imageMedia.setFitWidth(300);
//				screen.getChildren().add(imageMedia);
//				
//			}
//
//		}
		
	}
	
	public void createButtons(){
		
		fullScreen = new Button();
		fullScreen.setId("full-button");
		fullScreen.setTooltip(new Tooltip(Language.translate("full.screen")));
		
		previousFrame = new Button();
		previousFrame.setId("previous-frame-button");
		previousFrame.setTooltip(new Tooltip(Language.translate("previous.frame")));
		
		nextFrame = new Button();
		nextFrame.setId("next-frame-button");
		nextFrame.setTooltip(new Tooltip(Language.translate("next.frame")));
		
		play = new Button();
		play.setId("play-button");
		play.setTooltip(new Tooltip(Language.translate("play")));

		stop = new Button();
		stop.setDisable(true);
		stop.setId("stop-button");
		stop.setTooltip(new Tooltip(Language.translate("stop")));
		
		previousScene = new Button();
		previousScene.setId("previous-scene-button");
		previousScene.setTooltip(new Tooltip(Language.translate("previous.scene")));
		
		nextScene = new Button();
		nextScene.setId("next-scene-button");
		nextScene.setTooltip(new Tooltip(Language.translate("next.scene")));
		
		refresh = new Button();
		refresh.setId("refresh-button");
		refresh.setTooltip(new Tooltip(Language.translate("refresh")));
		
		fullButtonPane = new HBox();
	    fullButtonPane.setId("full-pane");
	    fullButtonPane.getChildren().add(fullScreen);
		
		centerButtonPane = new HBox();
		centerButtonPane.setId("center-button-pane");
		centerButtonPane.getChildren().add(previousFrame);
		centerButtonPane.getChildren().add(nextFrame);
		centerButtonPane.getChildren().add(play);
		centerButtonPane.getChildren().add(stop);
		centerButtonPane.getChildren().add(previousScene);
		centerButtonPane.getChildren().add(nextScene);
		
		refreshButtonPane = new HBox();
		refreshButtonPane.setId("refresh-pane");
		refreshButtonPane.getChildren().add(refresh);
		
	}
	
	public void createButtonActions(){
		
		play.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				//TODO iniciar o movimento de translateX da linha play
				//com o listner para a linha definido acimaa screen ira responder
				
			}
			
		});
		
		
		
	}
	
}
