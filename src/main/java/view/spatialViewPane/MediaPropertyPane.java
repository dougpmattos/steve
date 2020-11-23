package view.spatialViewPane;

import java.util.Observable;
import java.util.Observer;

import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.common.MediaNode;
import model.common.enums.MediaType;
import controller.ApplicationController;

public class MediaPropertyPane extends ScrollPane implements Observer{
	
	private ApplicationController applicationController;
	private MediaNode mediaNode;
	
	private PositionPane positionPane;
	private SizePane sizePane;
	private CropPane cropPane;
	private StylePane stylePane;
	private LevelPane levelPane;
	private VBox container;
	
	public MediaPropertyPane(ApplicationController applicationController, MediaNode mediaNode){
		
		this.applicationController = applicationController;
		this.mediaNode = mediaNode;
		
		setId("property-pane");
		
	    container = new VBox();
	    container.setId("property-container");
	    
	    if(mediaNode.getType().equals(MediaType.AUDIO)){
	    	
	    	levelPane = new LevelPane(applicationController, mediaNode);
	    	
	    	container.getChildren().add(levelPane);
	    	
	    }else {
	    	
	    	positionPane = new PositionPane(applicationController, mediaNode);
		    sizePane = new SizePane(applicationController, mediaNode);
		    cropPane = new CropPane(applicationController, mediaNode);
		    stylePane = new StylePane(applicationController, mediaNode);
		    
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
				
				if(mediaNode.getType().equals(MediaType.AUDIO)){
			    	
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
