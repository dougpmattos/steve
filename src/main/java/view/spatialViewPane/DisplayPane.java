package view.spatialViewPane;

import model.common.SpatialTemporalApplication;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import view.stevePane.SteveMenuBar;
import view.temporalViewPane.TemporalViewPane;

public class DisplayPane extends BorderPane {

	private ControlButtonPane controlButtonPane;
	private StackPane screen;
	private SpatialTemporalApplication spatialTemporalApplication;

	public DisplayPane(TemporalViewPane temporalViewPane, SteveMenuBar steveMenuBar, SpatialTemporalApplication spatialTemporalApplication){
		
		setId("display-pane");
	
		screen = new StackPane();
		screen.setId("screen-pane");
		
		controlButtonPane = new ControlButtonPane(screen, temporalViewPane, steveMenuBar, spatialTemporalApplication);
		
		setCenter(screen);
		setBottom(controlButtonPane);
		
	}
	
	public StackPane getScreen(){
		return screen;
	}
	
	public ControlButtonPane getControlButtonPane(){
		return controlButtonPane;
	}

}
