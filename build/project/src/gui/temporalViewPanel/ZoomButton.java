package gui.temporalViewPanel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.HBox;

public class ZoomButton extends HBox{
	
	HBox scrollBarContainer;
	ScrollBar scrollBar;
	Label title;
	Label minus;
	Label plus;
	
	public ZoomButton(){
		
		scrollBar = new ScrollBar();
		scrollBar.setMin(0);
		scrollBar.setMax(200);
		scrollBar.setValue(100);
		title = new Label("Zoom");
		minus = new Label("-");
		plus  =new Label("+");
		scrollBarContainer = new HBox();
		
		scrollBarContainer.getChildren().add(minus);
		scrollBarContainer.getChildren().add(scrollBar);
		scrollBarContainer.getChildren().add(plus);
		scrollBarContainer.setId("zoom-button");
		scrollBarContainer.getStylesheets().add("gui/styles/temporalViewPane.css");
		scrollBarContainer.setSpacing(3);
		scrollBarContainer.setAlignment(Pos.CENTER);
		
		getChildren().add(title);
		getChildren().add(scrollBarContainer);
		
		setSpacing(5);
		setAlignment(Pos.CENTER);
		
		
		
	}

}
