package gui.repositoryPanel;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.NCLSupport.extendedAna.Media;

public class RepositoryMediaItemContainer extends BorderPane{
	
	private static final int TEXT_LABEL_WITDH = 100;
	private static int IMAGE_THUMBNAIL_WIDTH = 70;
	private static int ICON_WIDTH = 40;
	
	private Label label;
	private Media media;
	private MediaListPane mediaListPane;
	private ImageView mediaIcon;

	public RepositoryMediaItemContainer(Media media, MediaListPane mediaListPane){
		
		this.media = media;
		this.mediaListPane = mediaListPane;
		
		try {
			generateMediaIcon();
		} catch (InterruptedException e) {
			Logger.getLogger(RepositoryMediaItemContainer.class.getName()).log(Level.SEVERE, null, e);
		}
		
		setCenter(mediaIcon);
		setId("repo-media-item-container");
		
		label = new Label(media.getName());
		label.setId("label-media-item");
		label.setPrefWidth(TEXT_LABEL_WITDH);
		label.setAlignment(Pos.CENTER);
		setBottom(label);
		
		createListenerEventMediaItem();
		
	}

	private ImageView generateMediaIcon() throws InterruptedException {
		
		switch(media.getImportedMediaType()) { 
	           
	       case IMAGE:
	       	   File imageFile = new File(media.getPath());
	           mediaIcon = new ImageView(new Image(imageFile.toURI().toString()));
	           mediaIcon.setPreserveRatio(true);
	           mediaIcon.setSmooth(true);
	           mediaIcon.setFitWidth(IMAGE_THUMBNAIL_WIDTH);
	           break;
	           
	       case VIDEO:
	    	   //mediaIcon = new ImageView();
	    	   //VideoFrame videoFrame = new VideoFrame(media.getPath(), mediaIcon);
	    	   mediaIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/video.png")));
	    	   break;
	           
	       case AUDIO:
	    	   mediaIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/audio.png")));
	    	   mediaIcon.setPreserveRatio(true);
	    	   mediaIcon.setSmooth(true);
	    	   mediaIcon.setFitWidth(ICON_WIDTH);
	           break; 
	       
	       case TEXT:
	    	   mediaIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/text.png")));
	    	   mediaIcon.setPreserveRatio(true);
	    	   mediaIcon.setSmooth(true);
	    	   mediaIcon.setFitWidth(ICON_WIDTH);
	           break;
	               
	       case OTHER:
	       case PROCEDURAL:
	    	   mediaIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/others.png")));
	    	   mediaIcon.setPreserveRatio(true);
	    	   mediaIcon.setSmooth(true);
	    	   mediaIcon.setFitWidth(ICON_WIDTH);
	           break;                
	       }
	       
	       return mediaIcon;
	                  
	   }
	
	private void createListenerEventMediaItem() {
		
		setOnMouseEntered(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	boolean itemClicked = getStylesheets().contains("gui/styles/mouseClickedMediaItem.css");
	        	if(!itemClicked){
	        		getStylesheets().add("gui/styles/mouseEnteredMediaItem.css");
	        	}
	        	
	        }
	    });
		
		setOnMouseExited(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	getStylesheets().remove("gui/styles/mouseEnteredMediaItem.css");
	        }
	    });
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	RepositoryMediaItemContainer source = (RepositoryMediaItemContainer) e.getSource();
	        	source.getMedia().setSelected(true);
	        	getStylesheets().add("gui/styles/mouseClickedMediaItem.css");
	        	
	        	for(Node media : mediaListPane.getAllTypes()){
	        		
	        		RepositoryMediaItemContainer repoMediaItemContainer = (RepositoryMediaItemContainer) media;
	        		
	        		if(!source.getMedia().equals(repoMediaItemContainer.getMedia())){
	        			repoMediaItemContainer.getMedia().setSelected(false);
	        			repoMediaItemContainer.getStylesheets().remove("gui/styles/mouseClickedMediaItem.css");
	        		}
	        		
	        	}
	        	
	        	for(Node media : mediaListPane.getImagePane()){
	        		
	        		RepositoryMediaItemContainer repoMediaItemContainer = (RepositoryMediaItemContainer) media;
	        		
	        		if(!source.getMedia().equals(repoMediaItemContainer.getMedia())){
	        			repoMediaItemContainer.getStylesheets().remove("gui/styles/mouseClickedMediaItem.css");
	        		}
	        		
	        	}
	        	
	        	for(Node media : mediaListPane.getVideoPane()){
	        		
	        		RepositoryMediaItemContainer repoMediaItemContainer = (RepositoryMediaItemContainer) media;
	        		
	        		if(!source.getMedia().equals(repoMediaItemContainer.getMedia())){
	        			repoMediaItemContainer.getStylesheets().remove("gui/styles/mouseClickedMediaItem.css");
	        		}
	        		
	        	}

				for(Node media : mediaListPane.getAudioPane()){
					
					RepositoryMediaItemContainer repoMediaItemContainer = (RepositoryMediaItemContainer) media;
					
					if(!source.getMedia().equals(repoMediaItemContainer.getMedia())){
	        			repoMediaItemContainer.getStylesheets().remove("gui/styles/mouseClickedMediaItem.css");
	        		}
					
				}

				for(Node media : mediaListPane.getTextPane()){
					
					RepositoryMediaItemContainer repoMediaItemContainer = (RepositoryMediaItemContainer) media;
					
					if(!source.getMedia().equals(repoMediaItemContainer.getMedia())){
	        			repoMediaItemContainer.getStylesheets().remove("gui/styles/mouseClickedMediaItem.css");
	        		}
					
				}
				
				for(Node media : mediaListPane.getOthersPane()){
					
					RepositoryMediaItemContainer repoMediaItemContainer = (RepositoryMediaItemContainer) media;
					
					if(!source.getMedia().equals(repoMediaItemContainer.getMedia())){
	        			repoMediaItemContainer.getStylesheets().remove("gui/styles/mouseClickedMediaItem.css");
	        		}
					
				}
	        	
	        }
	    });
		
	}
	
	
	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

}
