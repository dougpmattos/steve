package view.spatialViewPane;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import view.temporalViewPane.TemporalViewPane;

public class DisplayPane extends BorderPane {

	private ControlButtonPane controlButtonPane;
	private StackPane screen;

	public DisplayPane(TemporalViewPane temporalViewPane){
		
		setId("display-pane");
	
		screen = new StackPane();
		screen.setId("screen-pane");
		
		controlButtonPane = new ControlButtonPane(screen, temporalViewPane);
		
		setCenter(screen);
		setBottom(controlButtonPane);
		
	}

}
