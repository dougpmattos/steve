
package gui.spatialViewPanel;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


/**
 *
 * @author Douglas
 */
public class SpatialViewPane extends BorderPane{
    
	HBox buttonPane;
	Button fullScreen;
	Button nextFrame;
	Button previousFrame;
	Button play;
	Button stop;
	Button nextScene;
	Button previousScene;
	Button refresh;
	
    public SpatialViewPane() {
  
    	setMinWidth(0);
    	setId("spatial-view-pane");
    	getStylesheets().add("gui/styles/spatialViewPane.css");
    	
    	buttonPane  = new HBox();
    	buttonPane.setId("button-pane");
    	buttonPane.setAlignment(Pos.CENTER);
    	buttonPane.getStylesheets().add("gui/styles/spatialViewPane.css");
        
		fullScreen = new Button();
		fullScreen.setId("full-button");
		fullScreen.setTooltip(new Tooltip("Full screen"));
		fullScreen.setScaleX(0.5);
		fullScreen.setScaleY(0.5);
		HBox fullPane = new HBox();
		fullPane.setId("full-pane");
		fullPane.setAlignment(Pos.CENTER_LEFT);
		fullPane.getChildren().add(fullScreen);
		
		previousFrame = new Button();
		previousFrame.setId("previous-frame-button");
		previousFrame.setTooltip(new Tooltip("Previous frame"));
		previousFrame.setScaleX(0.5);
		previousFrame.setScaleY(0.5);
		nextFrame = new Button();
		nextFrame.setId("next-frame-button");
		nextFrame.setTooltip(new Tooltip("Next frame"));
		nextFrame.setScaleX(0.5);
		nextFrame.setScaleY(0.5);
		play = new Button();
		play.setId("play-button");
		play.setTooltip(new Tooltip("Play"));
		play.setScaleX(0.5);
		play.setScaleY(0.5);
		stop = new Button();
		stop.setDisable(true);
		stop.setId("stop-button");
		stop.setTooltip(new Tooltip("Stop"));
		stop.setScaleX(0.5);
		stop.setScaleY(0.5);
		previousScene = new Button();
		previousScene.setId("previous-scene-button");
		previousScene.setTooltip(new Tooltip("Previous scene"));
		previousScene.setScaleX(0.5);
		previousScene.setScaleY(0.5);
		nextScene = new Button();
		nextScene.setId("next-scene-button");
		nextScene.setTooltip(new Tooltip("Next scene"));
		nextScene.setScaleX(0.5);
		nextScene.setScaleY(0.5);
		HBox centerButtonPane = new HBox();
		centerButtonPane.setId("center-button-pane");
		centerButtonPane.setSpacing(-20);
		centerButtonPane.getChildren().add(previousFrame);
		centerButtonPane.getChildren().add(nextFrame);
		centerButtonPane.getChildren().add(play);
		centerButtonPane.getChildren().add(stop);
		centerButtonPane.getChildren().add(previousScene);
		centerButtonPane.getChildren().add(nextScene);
		
		refresh = new Button();
		refresh.setId("refresh-button");
		refresh.setTooltip(new Tooltip("Refresh spatial view"));
		refresh.setScaleX(0.5);
		refresh.setScaleY(0.5);
		HBox refreshPane = new HBox();
		refreshPane.setId("refresh-pane");
		refreshPane.setAlignment(Pos.CENTER_RIGHT);
		refreshPane.getChildren().add(refresh);
		
		buttonPane.getChildren().add(fullPane);
		buttonPane.getChildren().add(centerButtonPane);
		buttonPane.getChildren().add(refreshPane);
		
		setBottom(buttonPane);
		
    }
    
}
