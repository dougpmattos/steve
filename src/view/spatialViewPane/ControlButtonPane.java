package view.spatialViewPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import model.common.SpatialTemporalView;
import model.temporalView.TemporalChain;
import view.common.Language;
import view.stevePane.SteveMenuBar;
import view.temporalViewPane.TemporalChainPane;
import view.temporalViewPane.TemporalViewPane;

public class ControlButtonPane extends BorderPane{
	
	private Button fullScreen;
	private Button play;
	private Button pause;
	private Button run;
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
	private SpatialTemporalView spatialTemporalView;
	private SteveMenuBar steveMenuBar;
	private WebView webView;
	
	public ControlButtonPane(StackPane screen, TemporalViewPane temporalViewPane,SteveMenuBar steveMenuBar, SpatialTemporalView spatialTemporalView){
		
		setId("control-button-pane");
		this.steveMenuBar = steveMenuBar;
		this.screen = screen;
		this.temporalViewPane = temporalViewPane;
		this.spatialTemporalView = spatialTemporalView;
		this.webView = new WebView();
		
	    createButtons();
	  
	    setLeft(fullButtonPane);
		setCenter(centerButtonPane);
		setRight(refreshButtonPane);
	
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

	}
	
	public void createButtons(){
		
		fullScreen = new Button();
		fullScreen.setId("full-button");
		fullScreen.setTooltip(new Tooltip(Language.translate("full.screen")));
		
		run = new Button();
		run.setDisable(true);
		run.setId("run-button");
		run.setTooltip(new Tooltip(Language.translate("run")));
		
		play = new Button();
		play.setDisable(true);
		play.setId("play-button");
		play.setTooltip(new Tooltip(Language.translate("play")));
		
		pause = new Button();
		pause.setDisable(true);
		pause.setId("pause-button");
		pause.setTooltip(new Tooltip(Language.translate("pause")));
		
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
	    //fullButtonPane.getChildren().add(fullScreen);
		
		centerButtonPane = new HBox();
		centerButtonPane.setId("center-button-pane");
		centerButtonPane.getChildren().add(run);
		centerButtonPane.getChildren().add(play);
		centerButtonPane.getChildren().add(pause);
		centerButtonPane.getChildren().add(stop);
		//centerButtonPane.getChildren().add(previousScene);
		//centerButtonPane.getChildren().add(nextScene);
		
		refreshButtonPane = new HBox();
		refreshButtonPane.setId("refresh-pane");
		//refreshButtonPane.getChildren().add(refresh);
		
	}

	public Button getPlayButton(){
		return play;
	}
	public Button getRunButton(){
		return run;
	}
	public Button getPauseButton(){
		return pause;
	}
	public Button getStopButton(){
		return stop;
	}
	public StackPane getScreen(){
		return screen;
	}
	
}
