package view.spatialViewPane;

import java.util.Observable;
import java.util.Observer;

import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.common.Media;
import model.common.enums.MediaType;
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
		
	    container = new VBox();
	    container.setId("property-container");
	    
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
		    //container.getChildren().add(cropPane);
		    container.getChildren().add(stylePane);
		    
	    }
	    
	    setContent(container);
	    
	    createListeners();
		
	}
	
	private void createListeners() {
		
		setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				populatePresentationPropertyJavaBean();
				
			}

			private void populatePresentationPropertyJavaBean() {
				
				if(media.getType().equals(MediaType.AUDIO)){
			    	
			    	levelPane.populateLevelPropertyJavaBean();
			    	
			    }else {
			    	
			    	positionPane.populatePositionPropertyJavaBean();
				    sizePane.populateSizePropertyJavaBean();
				    cropPane.populateCropPropertyJavaBean();
				    stylePane.populateStylePropertyJavaBean();
				    
			    }
				
			}
			
		});
		
	}
	
	@Override
	public void update(Observable observable, Object obj) {
	
		
	}

	public PositionPane getPositionPane() {
		return positionPane;
	}

	public SizePane getSizePane() {
		return sizePane;
	}

	public CropPane getCropPane() {
		return cropPane;
	}

	public StylePane getStylePane() {
		return stylePane;
	}

	public LevelPane getLevelPane() {
		return levelPane;
	}
	
	

}
