package gui.spatialViewPane;

import gui.common.Language;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

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
	
	public ControlButtonPane(){
		
		setId("control-button-pane");
	    
	    createButtons();
	  
	    setLeft(fullButtonPane);
		setCenter(centerButtonPane);
		setRight(refreshButtonPane);
		
		createButtonActions();

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
		
	}
	
}
