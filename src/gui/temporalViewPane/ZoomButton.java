package gui.temporalViewPane;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.HBox;

public class ZoomButton extends HBox{
	
	private static final int DEFAULT = 100;
	private static final int MAX = 200;
	private static final int MIN = 0;
	
	private HBox scrollBarContainer;
	private ScrollBar scrollBar;
	private Label title;
	private Label minus;
	private Label plus;
	
	public ZoomButton(){
		
		scrollBar = new ScrollBar();
		scrollBar.setMin(MIN);
		scrollBar.setMax(MAX);
		scrollBar.setValue(DEFAULT);
		title = new Label("Zoom");
		minus = new Label("-");
		plus  =new Label("+");
		scrollBarContainer = new HBox();
		
		scrollBarContainer.getChildren().add(minus);
		scrollBarContainer.getChildren().add(scrollBar);
		scrollBarContainer.getChildren().add(plus);
		scrollBarContainer.setId("zoom-button");
		scrollBarContainer.getStylesheets().add("gui/temporalViewPane/styles/temporalViewPane.css");
		scrollBarContainer.setSpacing(3);
		scrollBarContainer.setAlignment(Pos.CENTER);
		
		getChildren().add(title);
		getChildren().add(scrollBarContainer);
		
		setSpacing(5);
		setAlignment(Pos.CENTER);
		
		
		
	}

}
