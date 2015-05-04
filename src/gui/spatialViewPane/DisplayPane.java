package gui.spatialViewPane;

import javafx.scene.layout.BorderPane;

public class DisplayPane extends BorderPane {

	private ControlButtonPane controlButtonPane;
	
	public DisplayPane(){
		
		setId("display-pane");
		
		controlButtonPane = new ControlButtonPane();
		
		setBottom(controlButtonPane);
		
	}
}
