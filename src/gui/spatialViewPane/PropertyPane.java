package gui.spatialViewPane;

import java.util.Observable;
import java.util.Observer;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.common.Media;
import model.common.MediaType;
import controller.Controller;

public class PropertyPane extends ScrollPane implements Observer{
	
	private Controller controller;
	private Media media;
	
	private PositionPane positionPane;
	private SizePane sizePane;
	private CropPane cropPane;
	private StylePane stylePane;
	private LevelPane levelPane;
	private VBox container;
	
	public PropertyPane(Controller controller, Media media){
		
		this.controller = controller;
		this.media = media;
		
		setId("property-pane");
		//setFitToHeight(true);
	    setFitToWidth(true);
	    setPadding(new Insets(6, 6, 6, 6));
		
	    container = new VBox();
	    container.setSpacing(6);
	    
	    if(media.getType().equals(MediaType.AUDIO)){
	    	
	    	levelPane = new LevelPane(controller, media);
	    	
	    	container.getChildren().add(levelPane);
	    	
	    }else {
	    	
	    	positionPane = new PositionPane(controller, media);
		    sizePane = new SizePane(controller, media);
		    cropPane = new CropPane(controller, media);
		    stylePane = new StylePane(controller, media);
		    
		    container.getChildren().add(positionPane);
		    container.getChildren().add(sizePane);
		    container.getChildren().add(cropPane);
		    container.getChildren().add(stylePane);
		    
	    }
	    
	    setContent(container);
	    
	}
	
	@Override
	public void update(Observable observable, Object obj) {
	
		
	}

}
