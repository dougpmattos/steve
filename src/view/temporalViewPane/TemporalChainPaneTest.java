package view.temporalViewPane;

import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalView;
import controller.Controller;

public class TemporalChainPaneTest extends StackPane{

	private TemporalView temporalViewModel;
	private TemporalChain temporalChainModel;
	private TemporalViewPane temporalViewPane;
	
	private VBox container;
	private TimeAxis timeAxis;
	private VBox mediaLineContainer;
	
	public TemporalChainPaneTest(Controller controller, TemporalView temporalViewModel, TemporalChain temporalChainModel, TemporalViewPane temporalViewPane){
		
		this.temporalViewModel = temporalViewModel;
    	this.temporalChainModel = temporalChainModel;
    	this.temporalViewPane = temporalViewPane;
    	
    	container = new VBox();
		timeAxis = new TimeAxis();
		timeAxis.setId("time-axis");
		timeAxis.prefWidthProperty().bind(temporalViewPane.widthProperty());
		mediaLineContainer = new VBox();
		mediaLineContainer.setId("media-line-container");
		mediaLineContainer.prefWidthProperty().bind(temporalViewPane.widthProperty());
		mediaLineContainer.prefHeightProperty().bind(temporalViewPane.heightProperty());
		
		for(int i=0; i<3; i++){
			HBox mediaLine = new HBox();
			mediaLine.setStyle("-fx-border-color: red;");
			mediaLine.setId(Integer.toString(i));
			mediaLine.setMinHeight(85);
			mediaLineContainer.getChildren().add(mediaLine);
		}
		
		container.getChildren().add(timeAxis);
		container.getChildren().add(mediaLineContainer);
		
		getChildren().add(container);
		
	}
	
}
