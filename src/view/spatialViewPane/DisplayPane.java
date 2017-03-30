package view.spatialViewPane;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import view.temporalViewPane.TemporalViewPane;

public class DisplayPane extends BorderPane {

	private ControlButtonPane controlButtonPane;
	private StackPane screen;

	public DisplayPane(TemporalViewPane temporalViewPane){
		HBox root = new HBox();
		
		setId("display-pane");
	
		screen = new StackPane();
		screen.setId("screen-pane");

		Image hi = new Image("hello-world.gif");
		
		Rectangle rectangle = new Rectangle(200,200);
		
		rectangle.setFill(new ImagePattern(hi, 0, 0, 1, 1, true));

		//rectangle.setFill(new ImagePattern(image));
		
		
		
		DragResizeMod.makeResizable(rectangle);
		
		screen.getChildren().add(rectangle);
		
		controlButtonPane = new ControlButtonPane(screen, temporalViewPane);
		
		root.getChildren().add(screen);
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
