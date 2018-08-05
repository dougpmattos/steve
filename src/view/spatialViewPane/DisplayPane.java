package view.spatialViewPane;

import model.common.SpatialTemporalView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import view.stevePane.SteveMenuBar;
import view.temporalViewPane.TemporalViewPane;

public class DisplayPane extends BorderPane {

	private ControlButtonPane controlButtonPane;
	private StackPane screen;
	private SpatialTemporalView spatialTemporalView;

	public DisplayPane(TemporalViewPane temporalViewPane, SteveMenuBar steveMenuBar, SpatialTemporalView spatialTemporalView){
		
		setId("display-pane");
	
		screen = new StackPane();
		screen.setId("screen-pane");
		
		controlButtonPane = new ControlButtonPane(screen, temporalViewPane, steveMenuBar, spatialTemporalView);

		this.minHeightProperty().set(390);
		this.maxHeightProperty().set(390);
		this.minWidthProperty().set(640);
		this.maxWidthProperty().set(640);

		setCenter(screen);
//		getCenter().maxWidth(640);
//		getCenter().minWidth(640);
//		getCenter().minHeight(360);
//		getCenter().maxHeight(360);
		setBottom(controlButtonPane);

	}
	
	public StackPane getScreen(){
		return screen;
	}
	
	public ControlButtonPane getControlButtonPane(){
		return controlButtonPane;
	}



}
